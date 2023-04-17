package org.ladocuploader.app;

import javax.servlet.http.HttpSession;
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
   * @param httpSession The current HTTP session, not null
   * @return the static page template
   */
  @GetMapping("/")
  ModelAndView getIndex(HttpSession httpSession,
                        @RequestParam(required = false) String ref_id) {
    // For dev, reset session if you visit home
    httpSession.invalidate();
    httpSession.setAttribute("ref_id", ref_id);

    return new ModelAndView("index");
  }

  @GetMapping("/privacy")
  String getPrivacy() {
    return "privacy";
  }
}
