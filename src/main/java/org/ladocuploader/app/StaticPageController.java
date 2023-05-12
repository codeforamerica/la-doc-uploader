package org.ladocuploader.app;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import java.util.HashMap;
import java.util.Map;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

/**
 * A controller to render static pages that are not in any flow.
 */
@Controller
public class StaticPageController {

  /**
   * Renders the website index page.
   *
   * @param request  The current request, not null
   * @return the static page template
   */
  @GetMapping("/")
  ModelAndView getIndex(HttpServletRequest request,
      @RequestParam(required = false) String ref_id) {
    // For dev, reset session if you visit home
    HttpSession httpSession = request.getSession();
    httpSession.invalidate();
    httpSession = request.getSession(true);

    // start new session
    httpSession.setAttribute("ref_id", ref_id);

    Map<String, Object> model = new HashMap();
    model.put("screen", "/");

    return new ModelAndView("index", model);
  }

  @GetMapping("/privacy")
  ModelAndView getPrivacy() {
    Map<String, Object> model = new HashMap();
    model.put("screen", "privacy");

    return new ModelAndView("privacy", model);
  }
}
