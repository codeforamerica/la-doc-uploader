package org.ladocuploader.app;

import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;
import javax.servlet.http.HttpServletRequest;

/**
 * A controller to render static pages that are not in any flow.
 */
@Controller
public class StaticPageController {

  /**
   * Renders the website index page.
   *
   * @param request The current request, not null
   * @param response The current request response
   * @return the static page template
   */
  @GetMapping("/")
  ModelAndView getIndex(HttpServletRequest request,
                        HttpServletResponse response,
                        @RequestParam(required = false) String ref_id) {
    // For dev, reset session if you visit home
    HttpSession httpSession = request.getSession();
    httpSession.invalidate();
    httpSession = request.getSession(true);

    // start new session
    httpSession.setAttribute("ref_id", ref_id);

    return new ModelAndView("index");
  }

  @GetMapping("/privacy")
  String getPrivacy() {
    return "privacy";
  }
}
