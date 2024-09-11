package coms309;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.PathVariable;

@RestController
class WelcomeController {

    @GetMapping("/")
    public String welcome() {
        return "Hello and welcome from Aelia's laptop";
    }

    @GetMapping("/{name}")
    public String welcome(@PathVariable String name) {
        return "Hello and welcome from the world of Windows: " + name;
    }
    @GetMapping("/{name}")
    public String bye(@PathVariable String name) {
        return "bye: " + name;
    }

}
