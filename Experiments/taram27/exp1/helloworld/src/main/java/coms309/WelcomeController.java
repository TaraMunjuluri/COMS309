package coms309;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.PathVariable;

@RestController
class WelcomeController {

    @GetMapping("/")
    public String welcome() {
        return "This is the welcome page";
    }
    @GetMapping("/Home")
    public String Home() {
        return "Hello, this is home";
    }
    @GetMapping("/Contact")
    public String Contact() {
        return "Hello, this is the contact page";
    }
    @GetMapping("/{name}")
    public String welcome(@PathVariable String name) {
        return "Hello and welcome:Q " + name;
    }
}
