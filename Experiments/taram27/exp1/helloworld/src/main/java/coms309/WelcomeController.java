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
        return "Hello and welcome:" + name;
    }
    @GetMapping("/multiply/{num1}/{num2}")
    public String multiply(@PathVariable double num1, @PathVariable double num2) {
        double result = num1 * num2;
        return "Calculation of multiplying " + num1 + " and " + num2 + " is: " + result;
    }

    @GetMapping("/subtract/{num1}/{num2}")
    public String subtract(@PathVariable double num1, @PathVariable double num2) {
        double result = num1 - num2;
        return "Calculation of subtracting " + num2 + " from " + num1 + " is: " + result;
    }
}
