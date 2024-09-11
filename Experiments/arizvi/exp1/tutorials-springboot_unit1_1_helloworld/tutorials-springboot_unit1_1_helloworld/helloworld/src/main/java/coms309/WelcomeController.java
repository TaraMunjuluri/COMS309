package coms309;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.PathVariable;

@RestController
class WelcomeController {

    @GetMapping("/")
    public String welcome() {
        return "Please specify if you want a hello or a bye, followed by your name";
    }

    @GetMapping("/hello/{name}")
    public String welcome(@PathVariable String name) {
        return "Hello and welcome: " + name;
    }

    @GetMapping("/bye/{name}")
    public String bye(@PathVariable String name) {
        return "Okay bye: " + name;
    }
}
