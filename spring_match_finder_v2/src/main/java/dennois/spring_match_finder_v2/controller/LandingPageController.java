package dennois.spring_match_finder_v2.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class LandingPageController {

    public static final String HOME_URL = "/home";

    @GetMapping("/home")
    public String showLandingPage(Model model) {
        // Add any necessary model attributes here
        return "index"; // The name of your Thymeleaf template without the .html extension
    }
}
