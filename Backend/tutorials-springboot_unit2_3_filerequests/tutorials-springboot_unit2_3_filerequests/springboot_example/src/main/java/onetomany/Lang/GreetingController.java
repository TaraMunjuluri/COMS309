package onetomany.Lang;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Locale;

@RestController
public class GreetingController {

    @Autowired
    private MessageSource messageSource;

    @GetMapping("/greeting")
    public String getGreeting(@RequestParam(defaultValue = "en") String lang) {
        Locale locale = new Locale(lang); // Convert 'lang' parameter to Locale
        return messageSource.getMessage("greeting", null, locale); // Fetch message from properties
    }
}
