
package onetomany.Users;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;


    @Controller
    public class LoginController {

        @Autowired
        private User userService;

        @GetMapping("/login")
        public String login(Model model) {
            return "login";  // Return the login view
        }

        @RequestMapping("/")
        public String home() {
            return "home";  // Return the home view after login
        }

        @GetMapping("/user")
        public String userPage(Model model) {
//            Authentication auth = SecurityContextHolder.getContext().getAuthentication();
//            String emailId = auth.getName(); // Get logged-in user's email
//            User user = userService.findByEmailId(emailId).orElse(null);
//            model.addAttribute("user", user); // Add user data to the model
            return "user"; // User-specific page
        }
    }

