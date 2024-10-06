package coms309;

        import org.springframework.web.bind.annotation.*;
        import java.util.HashMap;

@RestController
class WelcomeController {

    // A simple in-memory data store
    HashMap<String, String> nameMessages = new HashMap<>();

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

    // 1. Create Operation using @RequestBody (Create a new message for a person)
    @PostMapping("/createMessage")
    public String createMessage(@RequestBody HashMap<String, String> personData) {
        String name = personData.get("name");
        String message = personData.get("message");
        nameMessages.put(name, message);
        return "Message for " + name + " created successfully!";
    }

    // 2. Read Operation using @RequestParam (Retrieve a message by person's name)
    @GetMapping("/getMessage")
    public String getMessage(@RequestParam String name) {
        if (nameMessages.containsKey(name)) {
            return "Message for " + name + ": " + nameMessages.get(name);
        } else {
            return "No message found for " + name;
        }
    }

    // 3. Update Operation using @PathVariable and @RequestBody (Update message for a specific person)
    public String updateMessage(@PathVariable String name, @RequestBody HashMap<String, String> messageData) {
        if (nameMessages.containsKey(name)) {
            String newMessage = messageData.get("message");
            nameMessages.put(name, newMessage);
            return "Message for " + name + " updated successfully!";
        } else {
            return "No message found for " + name;
        }
    }

    // 4. Delete Operation using @PathVariable (Delete message for a specific person)
    @DeleteMapping("/deleteMessage/{name}")
    public String deleteMessage(@PathVariable String name) {
        if (nameMessages.containsKey(name)) {
            nameMessages.remove(name);
            return "Message for " + name + " deleted successfully!";
        } else {
            return "No message found for " + name;
        }
    }

    // Optionally, you could add a List operation to retrieve all stored messages.
    @GetMapping("/listMessages")
    public HashMap<String, String> listMessages() {
        return nameMessages;
    }
}