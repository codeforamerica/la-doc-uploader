package org.formflowstartertemplate.app;

import java.util.HashMap;
import javax.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.info.GitProperties;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
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
  ModelAndView getIndex(HttpSession httpSession) {
    // For dev, reset session if you visit home
    httpSession.invalidate();

    return new ModelAndView("index");
  }

  /**
   * Renders the website faq page.
   *
   * @return the static page template
   */
  @GetMapping("/faq")
  String getFaq() { return "faq"; }

  @GetMapping("/privacy")
  String getPrivacy() { return "privacy"; }
}
