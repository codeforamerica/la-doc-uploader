package org.ladocuploader.app;

import com.smartystreets.api.exceptions.SmartyException;
import formflow.library.FormFlowController;
import formflow.library.ValidationService;
import formflow.library.address_validation.AddressValidationService;
import formflow.library.address_validation.ValidatedAddress;
import formflow.library.config.*;
import formflow.library.data.FormSubmission;
import formflow.library.data.Submission;
import formflow.library.data.SubmissionRepositoryService;
import formflow.library.inputs.UnvalidatedField;
import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.Nullable;
import org.joda.time.DateTime;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.view.RedirectView;

import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.*;
import java.util.Map.Entry;

/**
 * A controller to render any screen in flows, including subflows.
 */
@Controller
@EnableAutoConfiguration
@Slf4j
@RequestMapping("/flow")
public class ScreenController extends FormFlowController {

  private final List<FlowConfiguration> flowConfigurations;
  private final ValidationService validationService;

  private final AddressValidationService addressValidationService;

  private final ConditionManager conditionManager;
  private final ActionManager actionManager;

  public ScreenController(
      List<FlowConfiguration> flowConfigurations,
      SubmissionRepositoryService submissionRepositoryService,
      ValidationService validationService,
      AddressValidationService addressValidationService,
      ConditionManager conditionManager,
      ActionManager actionManager) {

    super(submissionRepositoryService);
    this.flowConfigurations = flowConfigurations;
    this.validationService = validationService;
    this.addressValidationService = addressValidationService;
    this.conditionManager = conditionManager;
    this.actionManager = actionManager;
    log.info("Screen Controller Created!");
  }

  /**
   * Chooses which screen template and model data to render.
   *
   * @param flow        The current flow name, not null
   * @param screen      The current screen name in the flow, not null
   * @param uuid        The uuid of a subflow entry, can be null
   * @param httpSession The current httpSession, not null
   * @return the screen template with model data
   */
  @GetMapping("{flow}/{screen}")
  ModelAndView getScreen(
      @PathVariable String flow,
      @PathVariable String screen,
      @RequestParam(value = "uuid", required = false) String uuid,
      HttpSession httpSession
  ) {
    log.info(String.format("/flow/%s/%s 🚀", flow, screen));
    log.info("getScreen: flow: " + flow + ", screen: " + screen);
    var currentScreen = getScreenConfig(flow, screen);
    Submission submission = submissionRepositoryService.findOrCreate(httpSession);
    if (currentScreen == null) {
      return new ModelAndView("redirect:/error");
    }

    if (uuid != null) {
      actionManager.handleBeforeDisplayAction(currentScreen, submission, uuid);
    } else {
      actionManager.handleBeforeDisplayAction(currentScreen, submission);
    }

    Map<String, Object> model = createModel(flow, screen, httpSession, submission);

    String formAction = createFormActionString(flow, screen);
    model.put("formAction", formAction);
    if (isDeleteConfirmationScreen(flow, screen)) {
      ModelAndView nothingToDeleteModelAndView = handleDeleteBackBehavior(flow, screen, uuid, submission);
      if (nothingToDeleteModelAndView != null) {
        return nothingToDeleteModelAndView;
      }
    }

    log.info("getScreen: flow: " + flow + ", screen: " + screen);
    return new ModelAndView("%s/%s".formatted(flow, screen), model);
  }

  /**
   * Processes input data from current screen.
   *
   * <p>
   * If validation of input data passes this will redirect to move the client to the next screen.
   * </p>
   * <p>
   * If validation of input data fails this will redirect the client to the same screen so they can fix the data.
   * </p>
   *
   * @param formData    The input data from current screen, can be null
   * @param flow        The current flow name, not null
   * @param screen      The current screen name in the flow, not null
   * @param httpSession The HTTP session if it exists, can be null
   * @return a redirect to endpoint that gets the next screen in the flow
   */
  @PostMapping("{flow}/{screen}")
  ModelAndView postScreen(
      @RequestParam(required = false) MultiValueMap<String, String> formData,
      @RequestParam(required = false) String id,
      @PathVariable String flow,
      @PathVariable String screen,
      HttpSession httpSession
  ) throws SmartyException, IOException, InterruptedException {
    log.info("postScreen: flow: " + flow + ", screen: " + screen);
    Submission submission = submissionRepositoryService.findOrCreate(httpSession);
    FormSubmission formSubmission = new FormSubmission(formData);
    var currentScreen = getScreenConfig(flow, screen);

    actionManager.handleOnPostAction(currentScreen, formSubmission);

    // Field validation
    var errorMessages = validationService.validate(currentScreen, flow, formSubmission);
    handleErrors(httpSession, errorMessages, formSubmission);

    if (errorMessages.size() > 0) {
      return new ModelAndView(String.format("redirect:/flow/%s/%s", flow, screen));
    }

    // Address validation
    List<String> addressValidationFields = formSubmission.getAddressValidationFields();
    if (!addressValidationFields.isEmpty()) {
      Map<String, ValidatedAddress> validatedAddresses = addressValidationService.validate(formSubmission);
      formSubmission.setValidatedAddress(validatedAddresses);
      // clear lingering address(es) from the submission stored in the database.
      cleanAddressesInSubmission(submission, addressValidationFields);
    }

    log.info("submission = " + submission);
    // if there's already a session
    if (submission.getId() != null) {
      submission.mergeFormDataWithSubmissionData(formSubmission);
    } else {
      submission.setFlow(flow);
      submission.setInputData(formSubmission.getFormData());
    }

    actionManager.handleBeforeSaveAction(currentScreen, submission);
    saveToRepository(submission);
    httpSession.setAttribute("id", submission.getId());

    return new ModelAndView(String.format("redirect:/flow/%s/%s/navigation", flow, screen));
  }

  /**
   * Processes input data from the first page of a subflow screen.
   *
   * <p>
   * If validation of input data passes this will redirect to move the client to the next screen.
   * </p>
   * <p>
   * If validation of input data fails this will redirect the client to the same subflow screen so they can fix the data.
   * </p>
   * <p>
   * A newly created UUID will be stored with the entire subflow data as unique id to reference the data by.
   * </p>
   *
   * @param formData    The input data from current screen, can be null
   * @param flow        The current flow name, not null
   * @param screen      The current screen name in the flow, not null
   * @param httpSession The HTTP session if it exists, can be null
   * @return a redirect to next screen
   */
  @PostMapping("{flow}/{screen}/new")
  ModelAndView postNewSubflow(
      @RequestParam(required = false) MultiValueMap<String, String> formData,
      @PathVariable String flow,
      @PathVariable String screen,
      HttpSession httpSession
  ) {
//    Copy from OG /post request
    log.info("postScreen new subflow: flow: " + flow + ", screen: " + screen);
    FormSubmission formSubmission = new FormSubmission(formData);
    Submission submission = submissionRepositoryService.findOrCreate(httpSession);
    var currentScreen = getScreenConfig(flow, screen);
    HashMap<String, SubflowConfiguration> subflows = getFlowConfigurationByName(flow).getSubflows();
    String subflowName = subflows.entrySet().stream().filter(subflow ->
            subflow.getValue().getIterationStartScreen().equals(screen))
        .map(Entry::getKey).findFirst().orElse(null);

    actionManager.handleOnPostAction(currentScreen, formSubmission);

    var errorMessages = validationService.validate(currentScreen, flow, formSubmission);

    handleErrors(httpSession, errorMessages, formSubmission);
    if (errorMessages.size() > 0) {
      return new ModelAndView(String.format("redirect:/flow/%s/%s", flow, screen));
    }

    UUID uuid = UUID.randomUUID();
    formSubmission.getFormData().put("uuid", uuid);

    // if there's already a session
    if (submission.getId() != null) {
      if (!submission.getInputData().containsKey(subflowName)) {
        submission.getInputData().put(subflowName, new ArrayList<Map<String, Object>>());
      }
      ArrayList<Map<String, Object>> subflow = (ArrayList<Map<String, Object>>) submission.getInputData().get(subflowName);
      Boolean iterationIsComplete = !isNextScreenInSubflow(flow, httpSession, currentScreen);
      formSubmission.getFormData().put("iterationIsComplete", iterationIsComplete);
      subflow.add(formSubmission.getFormData());
      saveToRepository(submission, subflowName);
    } else {
      submission.setFlow(flow);
      // TODO: create the subflow here and add formSubmission to that
      submission.setInputData(formSubmission.getFormData());
      saveToRepository(submission, subflowName);
      httpSession.setAttribute("id", submission.getId());
    }
    String nextScreen = getNextScreenName(flow, httpSession, currentScreen);
    String viewString = isNextScreenInSubflow(flow, httpSession, currentScreen) ?
        String.format("redirect:/flow/%s/%s/%s", flow, nextScreen, uuid)
        : String.format("redirect:/flow/%s/%s", flow, nextScreen);
    return new ModelAndView(viewString);
  }

  /**
   * Chooses which screen template and model data to render in a subflow.
   *
   * @param flow        The current flow name, not null
   * @param screen      The current screen name in the subflow, not null
   * @param uuid        The uuid of a subflow entry, not null
   * @param httpSession The current httpSession, not null
   * @return the screen template with model data
   */
  // 😭 If we could use a method: <string>.replaceFirst("\\{([^}]*)}", "flow:(?!assets).*")
  // We have to put the regex inline because spring boot must have a compile-time available string
  @GetMapping("{flow:(?!assets).*}/{screen}/{uuid}")
  ModelAndView getSubflowScreen(
      @PathVariable String flow,
      @PathVariable String screen,
      @PathVariable String uuid,
      HttpSession httpSession
  ) {
    Submission submission = submissionRepositoryService.findOrCreate(httpSession);
    var currentScreen = getScreenConfig(flow, screen);
    actionManager.handleBeforeDisplayAction(currentScreen, submission, uuid);
    Map<String, Object> model = createModel(flow, screen, httpSession, submission);
    model.put("formAction", String.format("/flow/%s/%s/%s", flow, screen, uuid));
    return new ModelAndView(String.format("%s/%s", flow, screen), model);
  }

  /**
   * Processes input data from a page of a subflow screen that is not the first page of the subflow. The data from the first page
   * of a subflow is processed by {@link #postNewSubflow}, every subsequent page of the subflow is processed by this method.
   *
   * <p>
   * If validation of input data passes this will redirect to move the client to the next screen.
   * </p>
   * <p>
   * If validation of input data fails this will redirect the client to the same subflow screen so they can fix the data.
   * </p>
   *
   * @param formData    The input data from current screen, can be null
   * @param flow        The current flow name, not null
   * @param screen      The current screen name in the flow, not null
   * @param uuid        Unique id associated with the subflow's data, not null
   * @param httpSession The HTTP session if it exists, not null
   * @return a redirect to next screen
   */
  @PostMapping("{flow:(?!assets).*}/{screen}/{uuid}")
  ModelAndView addToIteration(
      @RequestParam(required = false) MultiValueMap<String, String> formData,
      @PathVariable String flow,
      @PathVariable String screen,
      @PathVariable String uuid,
      HttpSession httpSession
  ) {
    log.info("addToIteration: flow: " + flow + ", screen: " + screen + ", uuid: " + uuid);
    UUID id = (UUID) httpSession.getAttribute("id");
    Optional<Submission> submissionOptional = submissionRepositoryService.findById(id);
    FormSubmission formSubmission = new FormSubmission(formData);
    ScreenNavigationConfiguration currentScreen = getScreenConfig(flow, screen);
    String subflowName = currentScreen.getSubflow();

    actionManager.handleOnPostAction(currentScreen, formSubmission, uuid);

    var errorMessages = validationService.validate(currentScreen, flow, formSubmission);
    handleErrors(httpSession, errorMessages, formSubmission);
    if (errorMessages.size() > 0) {
      return new ModelAndView(String.format("redirect:/flow/%s/%s/%s", flow, screen, uuid));
    }

    if (submissionOptional.isPresent()) {
      Submission submission = submissionOptional.get();
      var iterationToEdit = submission.getSubflowEntryByUuid(subflowName, uuid);
      if (iterationToEdit != null) {
        Boolean iterationIsComplete = !isNextScreenInSubflow(flow, httpSession, currentScreen);
        formSubmission.getFormData().put("iterationIsComplete", iterationIsComplete);
        submission.mergeFormDataWithSubflowIterationData(subflowName, iterationToEdit, formSubmission.getFormData());
        submission.removeIncompleteIterations(subflowName, uuid);
        actionManager.handleBeforeSaveAction(currentScreen, submission, uuid);
        saveToRepository(submission, subflowName);
      }
    } else {
      return new ModelAndView("error", HttpStatus.BAD_REQUEST);
    }
    String nextScreen = getNextScreenName(flow, httpSession, currentScreen);
    String viewString = isNextScreenInSubflow(flow, httpSession, currentScreen) ?
        String.format("redirect:/flow/%s/%s/%s", flow, nextScreen, uuid)
        : String.format("redirect:/flow/%s/%s", flow, nextScreen);
    return new ModelAndView(viewString);
  }

  /**
   * Returns a redirect to delete confirmation screen.
   *
   * @param flow        The current flow name, not null
   * @param subflow     The current subflow name, not null
   * @param uuid        Unique id associated with the subflow's data, not null
   * @param httpSession The HTTP session if it exists, not null
   * @return a redirect to delete confirmation screen for a particular uuid's data
   */
  @GetMapping("{flow}/{subflow}/{uuid}/deleteConfirmation")
  RedirectView deleteConfirmation(
      @PathVariable String flow,
      @PathVariable String subflow,
      @PathVariable String uuid,
      HttpSession httpSession
  ) {
    String deleteConfirmationScreen = getFlowConfigurationByName(flow)
        .getSubflows().get(subflow).getDeleteConfirmationScreen();
    UUID id = (UUID) httpSession.getAttribute("id");
    Optional<Submission> submissionOptional = submissionRepositoryService.findById(id);

    if (submissionOptional.isPresent()) {
      Submission submission = submissionOptional.get();
      var existingInputData = submission.getInputData();
      var subflowArr = (ArrayList<Map<String, Object>>) existingInputData.get(subflow);
      var entryToDelete = subflowArr.stream().filter(entry -> entry.get("uuid").equals(uuid)).findFirst();
      entryToDelete.ifPresent(entry -> httpSession.setAttribute("entryToDelete", entry));
    }

    return new RedirectView(String.format("/flow/%s/" + deleteConfirmationScreen + "?uuid=" + uuid, flow));
  }

  /**
   * Deletes a subflow's input data set, based on that set's uuid.
   *
   * @param flow        The current flow name, not null
   * @param subflow     The current subflow name, not null
   * @param uuid        Unique id associated with the subflow's data, not null
   * @param httpSession The HTTP session if it exists, not null
   * @return A screen template and model to redirect to either the entry or review page for a flow, depending on if there is more
   * submission data for a subflow.  If no submission data is found an error template is returned.
   */
  @PostMapping("{flow}/{subflow}/{uuid}/delete")
  ModelAndView deleteSubflowIteration(
      @PathVariable String flow,
      @PathVariable String subflow,
      @PathVariable String uuid,
      HttpSession httpSession
  ) {
    UUID id = (UUID) httpSession.getAttribute("id");
    Optional<Submission> submissionOptional = submissionRepositoryService.findById(id);
    String subflowEntryScreen = getFlowConfigurationByName(flow).getSubflows().get(subflow)
        .getEntryScreen();
    if (submissionOptional.isPresent()) {
      Submission submission = submissionOptional.get();
      var existingInputData = submission.getInputData();
      if (existingInputData.containsKey(subflow)) {
        var subflowArr = (ArrayList<Map<String, Object>>) existingInputData.get(subflow);
        Optional<Map<String, Object>> entryToDelete = subflowArr.stream()
            .filter(entry -> entry.get("uuid").equals(uuid)).findFirst();
        entryToDelete.ifPresent(subflowArr::remove);
        if (!subflowArr.isEmpty()) {
          existingInputData.put(subflow, subflowArr);
          submission.setInputData(existingInputData);
          saveToRepository(submission, subflow);
        } else {
          existingInputData.remove(subflow);
          submission.setInputData(existingInputData);
          saveToRepository(submission, subflow);
          return new ModelAndView("redirect:/flow/%s/%s".formatted(flow, subflowEntryScreen));
        }
      } else {
        return new ModelAndView("redirect:/flow/%s/%s".formatted(flow, subflowEntryScreen));
      }
    } else {
      return new ModelAndView("error", HttpStatus.BAD_REQUEST);
    }
    String reviewScreen = getFlowConfigurationByName(flow).getSubflows().get(subflow)
        .getReviewScreen();
    return new ModelAndView(String.format("redirect:/flow/%s/" + reviewScreen, flow));
  }

  /**
   * Returns the template and model for a subflow's screen, filled in with previously supplied values.
   *
   * @param flow        The current flow name, not null
   * @param screen      The current screen name in the flow, not null
   * @param uuid        Unique id associated with the subflow's data, not null
   * @param httpSession The HTTP session if it exists, not null
   * @return the screen template with model data, or error page if data not found
   */
  @GetMapping("{flow}/{screen}/{uuid}/edit")
  ModelAndView getEditScreen(
      @PathVariable String flow,
      @PathVariable String screen,
      @PathVariable String uuid,
      HttpSession httpSession
  ) {
    ScreenNavigationConfiguration currentScreenConfig = getScreenConfig(flow, screen);
    String subflow = currentScreenConfig.getSubflow();
    UUID id = (UUID) httpSession.getAttribute("id");
    Map<String, Object> model;

    Optional<Submission> submissionOptional = submissionRepositoryService.findById(id);
    if (submissionOptional.isPresent()) {
      Submission submission = submissionOptional.get();
      var currentScreen = getScreenConfig(flow, screen);

      actionManager.handleBeforeDisplayAction(currentScreen, submission, uuid);
      model = createModel(flow, screen, httpSession, submission);

      var existingInputData = submission.getInputData();
      var subflowArr = (ArrayList<Map<String, Object>>) existingInputData.get(subflow);
      var entryToEdit = subflowArr.stream()
          .filter(entry -> entry.get("uuid").equals(uuid)).findFirst();
      entryToEdit.ifPresent(stringObjectMap -> model.put("inputData", stringObjectMap));
      model.put("formAction", String.format("/flow/%s/%s/%s/edit", flow, screen, uuid));
    } else {
      return new ModelAndView("error", HttpStatus.BAD_REQUEST);
    }

    return new ModelAndView(String.format("%s/%s", flow, screen), model);
  }

  /**
   * Processes input data from a page of a subflow screen.
   *
   * <p>
   * If validation of input data passes this will redirect to move the client to the next screen.
   * </p>
   * <p>
   * If validation of input data fails this will redirect the client to the same subflow screen so they can fix the data.
   * </p>
   *
   * @param formData    The input data from current screen, can be null
   * @param flow        The current flow name, not null
   * @param screen      The current screen name in the flow, not null
   * @param uuid        Unique id associated with the subflow's data, not null
   * @param httpSession The HTTP session if it exists, not null
   * @return a redirect to next screen
   */
  @PostMapping("{flow}/{screen}/{uuid}/edit")
  ModelAndView edit(
      @RequestParam(required = false) MultiValueMap<String, String> formData,
      @PathVariable String flow,
      @PathVariable String screen,
      @PathVariable String uuid,
      HttpSession httpSession
  ) {
    ScreenNavigationConfiguration currentScreen = getScreenConfig(flow, screen);
    String subflowName = currentScreen.getSubflow();
    UUID id = (UUID) httpSession.getAttribute("id");
    Optional<Submission> submissionOptional = submissionRepositoryService.findById(id);
    FormSubmission formSubmission = new FormSubmission(formData);

    actionManager.handleOnPostAction(currentScreen, formSubmission, uuid);
    var errorMessages = validationService.validate(currentScreen, flow, formSubmission);
    handleErrors(httpSession, errorMessages, formSubmission);
    if (errorMessages.size() > 0) {
      return new ModelAndView(String.format("redirect:/flow/%s/%s/%s/edit", flow, screen, uuid));
    }

    if (submissionOptional.isPresent()) {
      Submission submission = submissionOptional.get();
      var iterationToEdit = submission.getSubflowEntryByUuid(subflowName, uuid);
      if (iterationToEdit != null) {
        submission.mergeFormDataWithSubflowIterationData(subflowName, iterationToEdit, formSubmission.getFormData());
        actionManager.handleBeforeSaveAction(currentScreen, submission, uuid);
        saveToRepository(submission, subflowName);
      }
    } else {
      return new ModelAndView("error", HttpStatus.BAD_REQUEST);
    }
    String nextScreen = getNextScreenName(flow, httpSession, currentScreen);
    String viewString = isNextScreenInSubflow(flow, httpSession, currentScreen) ?
        String.format("redirect:/flow/%s/%s/%s/edit", flow, nextScreen, uuid)
        : String.format("redirect:/flow/%s/%s", flow, nextScreen);
    return new ModelAndView(viewString);
  }

  /**
   * This is the final submission endpoint where the submission is marked complete and the application is considered submitted to
   * the system.
   *
   * @param formData
   * @param flow
   * @param screen
   * @param httpSession
   * @return
   */
  @PostMapping("{flow}/{screen}/submit")
  ModelAndView submit(
      @RequestParam(required = false) MultiValueMap<String, String> formData,
      @PathVariable String flow,
      @PathVariable String screen,
      HttpSession httpSession
  ) {
    log.info("submit postScreen: flow: " + flow + ", screen: " + screen);
    ScreenNavigationConfiguration currentScreen = getScreenConfig(flow, screen);
    FormSubmission formSubmission = new FormSubmission(formData);
    Submission submission = submissionRepositoryService.findOrCreate(httpSession);

    actionManager.handleOnPostAction(currentScreen, formSubmission);

    var errorMessages = validationService.validate(currentScreen, flow, formSubmission);
    handleErrors(httpSession, errorMessages, formSubmission);

    if (errorMessages.size() > 0) {
      return new ModelAndView(String.format("redirect:/flow/%s/%s", flow, screen));
    }
    submission.setSubmittedAt(DateTime.now().toDate());

    // if there's already a session
    if (submission.getId() != null) {
      submission.mergeFormDataWithSubmissionData(formSubmission);
      saveToRepository(submission);
    } else {
      submission.setFlow(flow);
      submission.setInputData(formSubmission.getFormData());
      saveToRepository(submission);
      httpSession.setAttribute("id", submission.getId());
    }

    return new ModelAndView(String.format("redirect:/flow/%s/%s/navigation", flow, screen));
  }

  /**
   * Chooses the next screen template and model to render based on what is next in the flow.
   *
   * @param flow        The current flow name, not null
   * @param screen      The current screen name in the flow, not null
   * @param httpSession The current httpSession, not null
   * @return the screen template with model data, returns error page on error
   */
  @GetMapping("{flow}/{screen}/navigation")
  RedirectView navigation(
      @PathVariable String flow,
      @PathVariable String screen,
      HttpSession httpSession
  ) {
    var currentScreen = getScreenConfig(flow, screen);
    log.info("navigation: flow: " + flow + ", screen: " + screen);
    if (currentScreen == null) {
      return new RedirectView("/error");
    }
    String nextScreen = getNextScreenName(flow, httpSession, currentScreen);

    log.info("navigation: flow: " + flow + ", nextScreen: " + nextScreen);
    return new RedirectView("/flow/%s/%s".formatted(flow, nextScreen));
  }

  private String getNextScreenName(String flow, HttpSession httpSession,
      ScreenNavigationConfiguration currentScreen) {
    NextScreen nextScreen;

    List<NextScreen> nextScreens = getConditionalNextScreen(flow, currentScreen, httpSession);

    if (isConditionalNavigation(currentScreen) && nextScreens.size() > 0) {
      nextScreen = nextScreens.get(0);
    } else {
      // TODO this needs to throw an error if there are more than 1 next screen that don't have a condition or more than one evaluate to true
      nextScreen = getNonConditionalNextScreen(currentScreen);
    }

    log.info("getNextScreenName: currentScreen:" + currentScreen + ", nextScreen: " + nextScreen.getName());
    // TODO throw a better error if the next screen doesn't exist (incorrect name / name is not in flow config)
    return nextScreen.getName();
  }

  /**
   * Fetches the navigation configuration for a particular screen in a particular flow.
   *
   * @param flow   the flow containing the screen
   * @param screen the screen that configuration is wanted for
   * @return navigation configuration for the screen
   */
  private ScreenNavigationConfiguration getScreenConfig(String flow, String screen) {
    FlowConfiguration currentFlowConfiguration = getFlowConfigurationByName(flow);
    return currentFlowConfiguration.getScreenNavigation(screen);
  }

  private FlowConfiguration getFlowConfigurationByName(String flow) {
    return flowConfigurations.stream().filter(
        flowConfiguration -> flowConfiguration.getName().equals(flow)
    ).toList().get(0);
  }

  private Boolean isConditionalNavigation(ScreenNavigationConfiguration currentScreen) {
    return currentScreen.getNextScreens().stream()
        .anyMatch(nextScreen -> nextScreen.getCondition() != null);
  }

  /**
   * Returns a list of possible next screens, the ones whose conditions pass
   *
   * @param currentScreen screen you're on
   * @param httpSession   session
   * @return List<NextScreen> list of next screens
   */
  private List<NextScreen> getConditionalNextScreen(String flow, ScreenNavigationConfiguration currentScreen,
      HttpSession httpSession) {
    return currentScreen.getNextScreens().stream()
        .filter(nextScreen -> conditionManager.conditionExists(nextScreen.getCondition()))
        .filter(nextScreen -> conditionManager.runCondition(nextScreen.getCondition(),
            submissionRepositoryService.findOrCreate(httpSession)))
        .toList();
  }

  private NextScreen getNonConditionalNextScreen(ScreenNavigationConfiguration currentScreen) {
    return currentScreen.getNextScreens().stream()
        .filter(nxtScreen -> nxtScreen.getCondition() == null).toList().get(0);
  }

  private Boolean isIterationStartScreen(String flow, String screen) {
    HashMap<String, SubflowConfiguration> subflows = getFlowConfigurationByName(flow).getSubflows();
    if (subflows == null) {
      return false;
    }
    return subflows.entrySet().stream().anyMatch(subflowConfig ->
        subflowConfig.getValue().getIterationStartScreen().equals(screen));
  }

  private Boolean isNextScreenInSubflow(String flow, HttpSession session, ScreenNavigationConfiguration currentScreen) {
    String nextScreenName = getNextScreenName(flow, session, currentScreen);
    return getScreenConfig(flow, nextScreenName).getSubflow() != null;
  }

  private String createFormActionString(String flow, String screen) {
    return isIterationStartScreen(flow, screen) ?
        "/flow/%s/%s/new".formatted(flow, screen) : "/flow/%s/%s".formatted(flow, screen);
  }

  private Map<String, Object> createModel(String flow, String screen, HttpSession httpSession, Submission submission) {
    Map<String, Object> model = new HashMap<>();
    FlowConfiguration flowConfig = getFlowConfigurationByName(flow);
    model.put("flow", flow);
    model.put("screen", screen);
    model.put("conditionManager", conditionManager);

    if (flowConfig.getFlow().get(screen).getSubflow() != null) {
      model.put("subflow", flowConfig.getFlow().get(screen).getSubflow());
    }

    // Put subflow on model if on subflow delete confirmation screen
    HashMap<String, SubflowConfiguration> subflows = getFlowConfigurationByName(flow).getSubflows();
    if (subflows != null) {
      List<String> subflowFromDeleteConfirmationConfig = subflows
          .entrySet().stream().filter(entry ->
              entry.getValue().getDeleteConfirmationScreen().equals(screen)).map(Entry::getKey).toList();

      // Add the iteration start page to the model if we are on the review page for a subflow so we have it for the edit button
      subflows.forEach((key, value) -> {
        if (value.getReviewScreen().equals(screen)) {
          model.put("iterationStartScreen", value.getIterationStartScreen());
        }
      });

      if (!subflowFromDeleteConfirmationConfig.isEmpty()) {
        model.put("subflow", subflowFromDeleteConfirmationConfig.get(0));
      }
    }

    // If there are errors, merge form data that was submitted, with already existing inputData
    if (httpSession.getAttribute("formDataSubmission") != null) {
      submission.mergeFormDataWithSubmissionData(
          new FormSubmission((Map<String, Object>) httpSession.getAttribute("formDataSubmission")));
    }

    model.put("submission", submission);
    model.put("inputData", submission.getInputData());

    model.put("errorMessages", httpSession.getAttribute("errorMessages"));
    return model;
  }

  private void handleErrors(HttpSession httpSession, Map<String, List<String>> errorMessages,
      FormSubmission formSubmission) {
    if (errorMessages.size() > 0) {
      httpSession.setAttribute("errorMessages", errorMessages);
      httpSession.setAttribute("formDataSubmission", formSubmission.getFormData());
    } else {
      httpSession.removeAttribute("errorMessages");
      httpSession.removeAttribute("formDataSubmission");
    }
  }

  private Boolean isDeleteConfirmationScreen(String flow, String screen) {
    HashMap<String, SubflowConfiguration> subflows = getFlowConfigurationByName(flow).getSubflows();
    if (subflows == null) {
      return false;
    }
    return subflows.entrySet().stream()
        .anyMatch(subflow -> subflow.getValue().getDeleteConfirmationScreen().equals(screen));
  }

  @Nullable
  private ModelAndView handleDeleteBackBehavior(String flow, String screen, String uuid,
      Submission submission) {
    ModelMap model = new ModelMap();
    String subflowName = getFlowConfigurationByName(
        flow).getSubflows().entrySet().stream()
        .filter(entry -> entry.getValue().getDeleteConfirmationScreen().equals(screen))
        .toList().get(0).getKey();
    ArrayList<Map<String, Object>> subflow = (ArrayList<Map<String, Object>>) submission.getInputData().get(subflowName);
    if (subflow == null || subflow.stream().noneMatch(entry -> entry.get("uuid").equals(uuid))) {
      model.put("noEntryToDelete", true);
      model.put("reviewScreen", getFlowConfigurationByName(flow).getSubflows().get(subflowName).getReviewScreen());
      if (subflow == null) {
        model.put("subflowIsEmpty", true);
        model.put("entryScreen", getFlowConfigurationByName(flow).getSubflows().get(subflowName).getEntryScreen());
      }
      return new ModelAndView("%s/%s".formatted(flow, screen), model);
    }
    return null;
  }

  /**
   * Clears out the Address Fields related to the list of input field names passed in. An example field name looks like this:
   * "_validateresidentialAddress"
   *
   * @param submission              Submission data from the database
   * @param addressValidationFields List of strings indicating which addresses are requesting validation
   */
  private void cleanAddressesInSubmission(Submission submission, List<String> addressValidationFields) {
    addressValidationFields.forEach(item -> {
      String inputName = item.replace(UnvalidatedField.VALIDATE_ADDRESS, "");
      submission.clearAddressFields(inputName);
    });
  }

}
