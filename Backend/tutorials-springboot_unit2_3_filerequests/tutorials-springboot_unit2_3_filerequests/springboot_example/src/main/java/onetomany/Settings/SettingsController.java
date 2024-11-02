//package onetomany.Settings;
//
//import onetomany.Users.User;
//import onetomany.Users.UserRepository;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.web.bind.annotation.*;
//
//@RestController
//@RequestMapping("/settings")
//@CrossOrigin(origins = "*")
//public class SettingsController {
//
//    @Autowired
//    private UserRepository userRepository;
//
//    @GetMapping("/test")
//    public String test() {
//        return "Settings controller is working";
//    }
//    // Endpoint to change app mode
//    @PutMapping("/mode")
//    public String updateAppMode(@RequestParam Long id, @RequestParam String mode) {
//        User user = userRepository.findById(id).orElseThrow(() -> new RuntimeException("User not found"));
//        user.setAppMode(mode);
//        userRepository.save(user);
//        return "App mode updated to " + mode;
//    }
//
//    // Endpoint to change password
//    @PutMapping("/password")
//    public String updatePassword(@RequestParam Long id, @RequestParam String newPassword) {
//        User user = userRepository.findById(id).orElseThrow(() -> new RuntimeException("User not found"));
//        user.setPassword(newPassword); // Ensure proper hashing of the password in a real app
//        userRepository.save(user);
//        return "Password updated successfully.";
//    }
//
//    // Endpoint to delete user account by username
//    @DeleteMapping("/delete")
//    public String deleteUser(@RequestParam String username) {
//        User user = userRepository.findByUsername(username);
//        if (user != null) {
//            userRepository.delete(user);
//            return "User account deleted successfully.";
//        } else {
//            return "User not found.";
//        }
//    }
//}
