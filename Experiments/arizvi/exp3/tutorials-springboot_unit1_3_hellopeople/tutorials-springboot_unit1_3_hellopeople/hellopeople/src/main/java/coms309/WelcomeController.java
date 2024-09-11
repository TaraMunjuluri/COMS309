package coms309;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;


/**
 * Simple Hello World Controller to display the string returned
 *
 * @author Vivek Bengre
 */

@RestController
class WelcomeController {

    @GetMapping("/")
    public String welcome() {
        return "Hey what's up, it's Aelia coming at you";
    }

    @GetMapping("/greeting")
    public String greeting() {return "Greetings from Aelia!"; }

    @GetMapping("/bye")
    public String bye() {return "Good Bye, Good Morrow! Have a wonderful day!";}

}
