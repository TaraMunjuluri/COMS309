
package onetomany.Users;

import java.io.IOException;
import java.net.InetAddress;
import java.sql.Blob;
import java.sql.SQLException;
import java.util.*;

import javax.sql.rowset.serial.SerialBlob;

import com.fasterxml.jackson.databind.ObjectMapper;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import onetomany.Laptops.Laptop;
import onetomany.Laptops.LaptopRepository;

import org.springframework.http.HttpStatus;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import java.net.UnknownHostException;
//
//@RestController
//public class UserController {
//
//    @Autowired
//    UserRepository userRepository;
//
//    @Autowired
//    LaptopRepository laptopRepository;
//
//    @Autowired
//    BCryptPasswordEncoder passwordEncoder;
//
//    private String success = "{\"message\":\"success\"}";
//    private String failure = "{\"message\":\"failure\"}";
//
//    @GetMapping(path = "/users")
//    List<User> getAllUsers(){
//        return userRepository.findAll();
//    }
//
//    @GetMapping(path = "/users/{id}")
//    User getUserById(@PathVariable int id){
//        return userRepository.findById(id);
//    }
//
//    @PostMapping(path = "/users")
//    String createUser(@RequestParam("avatar") MultipartFile avatar, @RequestParam("user") String userString) throws Exception {
//        if (userString == null)
//            return failure;
//
//        ObjectMapper objectMapper = new ObjectMapper();
//        User user = objectMapper.readValue(userString, User.class);
//
//        user.setExtension(avatar.getOriginalFilename());
//        userRepository.save(user);
//
//        if(avatar != null) {
//            byte[] file = avatar.getBytes();
//            SerialBlob blob = new SerialBlob(file);
//            Blob image = blob;
//            user.setAvatar(image);
//            userRepository.save(user);
//        }
//        return success;
//    }
//
//    @PostMapping("/signup")
//    public ResponseEntity<String> signup(@RequestBody User user) {
//        if (userRepository.findByEmailId(user.getEmailId()) != null) {
//            return new ResponseEntity<>("Email is already registered", HttpStatus.BAD_REQUEST);
//        }
//
//        user.setPassword(passwordEncoder.encode(user.getPassword()));
//        userRepository.save(user);
//
//        return new ResponseEntity<>("User registered successfully", HttpStatus.CREATED);
//    }
//
//    @PostMapping("/login")
//    public ResponseEntity<String> login(@RequestBody User user) {
//        User existingUser = userRepository.findByEmailId(user.getEmailId());
//        if (existingUser == null || !passwordEncoder.matches(user.getPassword(), existingUser.getPassword())) {
//            return new ResponseEntity<>("Invalid credentials", HttpStatus.UNAUTHORIZED);
//        }
//
//        return new ResponseEntity<>("Login successful", HttpStatus.OK);
//    }
//
//
//    @PostMapping("/auth/login")
//    public ResponseEntity<String> login(@RequestBody User user, HttpServletRequest request) {
//        User existingUser = userRepository.findByEmailId(user.getEmailId());
//        if (existingUser == null || !passwordEncoder.matches(user.getPassword(), existingUser.getPassword())) {
//            return new ResponseEntity<>(failure, HttpStatus.UNAUTHORIZED);
//        }
//
//        // Create a session for the logged-in user
//        HttpSession session = request.getSession();
//        session.setAttribute("user", existingUser);
//
//        return new ResponseEntity<>(success, HttpStatus.OK);
//    }
//
//    // Logout endpoint to invalidate the session
//    @PostMapping("/auth/logout")
//    public ResponseEntity<String> logout(HttpServletRequest request) {
//        HttpSession session = request.getSession(false);
//        if (session != null) {
//            session.invalidate();
//        }
//        return new ResponseEntity<>("{\"message\":\"Logout successful\"}", HttpStatus.OK);
//    }
//
//    @PutMapping("/users/{id}")
//    User updateUser(@PathVariable int id, @RequestBody User request){
//        User user = userRepository.findById(id);
//        if(user == null)
//            return null;
//        userRepository.save(request);
//        return userRepository.findById(id);
//    }
//
//    @PutMapping("/users/{userId}/laptops/{laptopId}")
//    String assignLaptopToUser(@PathVariable int userId,@PathVariable int laptopId){
//        User user = userRepository.findById(userId);
//        Laptop laptop = laptopRepository.findById(laptopId);
//        if(user == null || laptop == null)
//            return failure;
//        laptop.setUser(user);
//        user.setLaptop(laptop);
//        userRepository.save(user);
//        return success;
//    }
//
//    @DeleteMapping(path = "/users/{id}")
//    String deleteUser(@PathVariable int id){
//        userRepository.deleteById(id);
//        return success;
//    }
//
//    @GetMapping(path = "/users/search")
//    List<User> searchUsers(@RequestParam("username") String username) {
//        return userRepository.findByUsernameContainingIgnoreCase(username);
//    }
//
//    @GetMapping(path = "/users/{id}/avatar")
//    ResponseEntity<Resource> getUserAvatar(@PathVariable int id) throws IOException, SQLException{
//        User user = userRepository.findById(id);
//
//        if(user == null || user.getAvatar() == null){
//            return null;
//        }
//
//        HttpHeaders header = new HttpHeaders();
//        header.add(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename="+user.getExtension());
//        header.add("Cache-Control", "no-cache, no-store, must-revalidate");
//        header.add("Pragma", "no-cache");
//        header.add("Expires", "0");
//
//        int blobLength = (int)user.getAvatar().length();
//        byte[] byteArray = user.getAvatar().getBytes(1, blobLength);
//        ByteArrayResource data = new ByteArrayResource(byteArray);
//
//        return ResponseEntity.ok()
//                .headers(header)
//                .contentLength(blobLength)
//                .contentType(MediaType.parseMediaType("application/octet-stream"))
//                .body(data);
//    }
//}

//package onetomany.Users;

import java.io.IOException;
import java.sql.Blob;
import java.sql.SQLException;
import java.util.List;

import javax.sql.rowset.serial.SerialBlob;

import com.fasterxml.jackson.databind.ObjectMapper;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.http.HttpStatus;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import onetomany.Laptops.Laptop;
import onetomany.Laptops.LaptopRepository;


@RestController
public class UserController {

    @Autowired
    UserRepository userRepository;

    @Autowired
    LaptopRepository laptopRepository;

    private String success = "{\"message\":\"success\"}";
    private String failure = "{\"message\":\"failure\"}";

    @GetMapping(path = "/users")
    List<User> getAllUsers(){
        return userRepository.findAll();
    }

    @GetMapping(path = "/users/{id}")
    User getUserById(@PathVariable int id){
        return userRepository.findById(id);
    }

    //code section for ip address to be used by frontend
//    @GetMapping("/ip-address")
//    public ResponseEntity<String> getIpAddress() {
//        try {
//            InetAddress ip = InetAddress.getLocalHost();
//            return new ResponseEntity<>(ip.getHostAddress(), HttpStatus.OK);
//        } catch (UnknownHostException e) {
//            return new ResponseEntity<>("Could not determine IP address", HttpStatus.INTERNAL_SERVER_ERROR);
//        }
//    }
    @PostMapping(path = "/users")
    String createUser(@RequestParam("avatar") MultipartFile avatar, @RequestParam("user") String userString) throws Exception {
        if (userString == null)
            return failure;

        ObjectMapper objectMapper = new ObjectMapper();
        User user = objectMapper.readValue(userString, User.class);

        user.setExtension(avatar.getOriginalFilename());
        userRepository.save(user);

        if(avatar != null) {
            byte[] file = avatar.getBytes();
            SerialBlob blob = new SerialBlob(file);
            Blob image = blob;
            user.setAvatar(image);
            userRepository.save(user);
        }
        return success;
    }

//    @PostMapping("/signup")
//    public ResponseEntity<String> signup(@RequestBody User user) {
//        if (userRepository.findByEmailId(user.getEmailId()) != null) {
//            return new ResponseEntity<>("Email is already registered", HttpStatus.BAD_REQUEST);
//        }
//
//        // Store the password as plain text (not recommended for production)
//        userRepository.save(user);
//
//        return new ResponseEntity<>("User registered successfully", HttpStatus.CREATED);
//    }
//
//    @PostMapping("/login")
//    public ResponseEntity<String> login(@RequestBody User user) {
//        User existingUser = userRepository.findByEmailId(user.getEmailId());
//        if (existingUser == null || !user.getPassword().equals(existingUser.getPassword())) { // Compare raw passwords
//            return new ResponseEntity<>("Invalid credentials", HttpStatus.UNAUTHORIZED);
//        }
//
//        return new ResponseEntity<>("Login successful", HttpStatus.OK);
//    }

    //latest signup
//    @PostMapping("/signup")
//    public ResponseEntity<Map<String, Object>> signup(@RequestBody User user) {
//        Map<String, Object> response = new HashMap<>();
//
//        if (userRepository.findByEmailId(user.getEmailId()) != null) {
////            return new ResponseEntity<>("Email is already registered", HttpStatus.BAD_REQUEST);
//            response.put("message", "Email is already registered");
//            return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
//        }
//        if (userRepository.findByUsername(user.getUsername()) != null) {
////            return new ResponseEntity<>("Username is already registered", HttpStatus.BAD_REQUEST);
//            response.put("message", "Username is already registered");
//            return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
//        }
//
//        // Save the user with the plain text password
//        userRepository.save(user);
//        response.put("message", "User registered successfully");
//        response.put("userId", user.getId());
//
////        return new ResponseEntity<>("User registered successfully", HttpStatus.CREATED);
//        return new ResponseEntity<>(response, HttpStatus.CREATED);
//    }

    @PostMapping("/signup")
    public ResponseEntity<Map<String, Object>> signup(@RequestBody User user) {
        if (userRepository.findByEmailId(user.getEmailId()) != null) {
            return new ResponseEntity<>(failureResponse("Email is already registered"), HttpStatus.BAD_REQUEST);
        }
        if (userRepository.findByUsername(user.getUsername()) != null) {
            return new ResponseEntity<>(failureResponse("Username is already registered"), HttpStatus.BAD_REQUEST);
        }

        // Save the user with the plain text password
        userRepository.save(user);

        // Return a JSON response instead of a plain string
        Map<String, Object> response = new HashMap<>();
        response.put("message", "Signup successful");
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    // Helper method for failure responses
    private Map<String, Object> failureResponse(String message) {
        Map<String, Object> response = new HashMap<>();
        response.put("message", message);
        return response;
    }
    @PostMapping("/login")
    public ResponseEntity<Map<String, Object>> login(@RequestBody User user,HttpServletRequest request) {
        Map<String, Object> response = new HashMap<>();
        User existingUser = userRepository.findByEmailIdOrUsername(user.getUsername());
        if (existingUser == null || !user.getPassword().equals(existingUser.getPassword())) {
//            return new ResponseEntity<>("Invalid credentials", HttpStatus.UNAUTHORIZED);
            response.put("message", "Invalid credentials");
            return new ResponseEntity<>(response, HttpStatus.UNAUTHORIZED);
        }
        HttpSession session = request.getSession();
        session.setAttribute("loggedInUser", existingUser);

//        return new ResponseEntity<>("Login successful", HttpStatus.OK);
        response.put("message", "Login successful");
        response.put("user", existingUser);  // Optionally include user details in response
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @PostMapping("/auth/login")
    public ResponseEntity<String> loginWithSession(@RequestBody User user, HttpServletRequest request) {
        User existingUser = userRepository.findByEmailId(user.getEmailId());
        if (existingUser == null || !user.getPassword().equals(existingUser.getPassword())) { // Compare raw passwords
            return new ResponseEntity<>(failure, HttpStatus.UNAUTHORIZED);
        }

        // Create a session for the logged-in user
        HttpSession session = request.getSession();
        session.setAttribute("user", existingUser);

        return new ResponseEntity<>(success, HttpStatus.OK);
    }

    // Logout endpoint to invalidate the session
    @PostMapping("/auth/logout")
    public ResponseEntity<Map<String, Object>> logout(HttpServletRequest request) {
        Map<String, Object> response = new HashMap<>();
        HttpSession session = request.getSession(false);
        if (session != null) {
            session.invalidate();
            response.put("message", "Logout successful");
        }else{
            response.put("message", "No active session found");
        }
//        return new ResponseEntity<>("{\"message\":\"Logout successful\"}", HttpStatus.OK);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }


    @PutMapping("/users/{id}")
    User updateUser(@PathVariable int id, @RequestBody User request){
        User user = userRepository.findById(id);
        if(user == null)
            return null;
        userRepository.save(request);
        return userRepository.findById(id);
    }

    @PutMapping("/users/{userId}/laptops/{laptopId}")
    String assignLaptopToUser(@PathVariable int userId, @PathVariable int laptopId){
        User user = userRepository.findById(userId);
        Laptop laptop = laptopRepository.findById(laptopId);
        if(user == null || laptop == null)
            return failure;
        laptop.setUser(user);
        user.setLaptop(laptop);
        userRepository.save(user);
        return success;
    }

    @DeleteMapping(path = "/users/{id}")
    String deleteUser(@PathVariable long id){
        userRepository.deleteById(id);
        return success;
    }

    @GetMapping(path = "/users/search")
    List<User> searchUsers(@RequestParam("username") String username) {
        return userRepository.findByUsernameContainingIgnoreCase(username);
    }

//    @PutMapping("/mode")
//    public String updateAppMode(@RequestParam Long id, @RequestParam String mode) {
//        User user = userRepository.findById(id).orElseThrow(() -> new RuntimeException("User not found"));
//        user.setAppMode(mode);
//        userRepository.save(user);
//        return "App mode updated to " + mode;
//    }
//@PutMapping("/mode")
//public String updateAppMode(@RequestBody Map<String, Object> request) {
//    Long id = Long.valueOf(request.get("id").toString());
//    String mode = request.get("mode").toString();
//
//    User user = userRepository.findById(id).orElseThrow(() -> new RuntimeException("User not found"));
//    user.setAppMode(mode);
//    userRepository.save(user);
//
//    return "App mode updated to " + mode;
//}
@PutMapping("/users/{id}/theme")
public ResponseEntity<Map<String, Object>> toggleThemeMode(
        @PathVariable int id,
        @RequestBody Map<String, String> themeRequest
) {
    User user = userRepository.findById(id);
    if (user == null) {
        return new ResponseEntity<>(failureResponse("User not found"), HttpStatus.NOT_FOUND);
    }

    String themeMode = themeRequest.get("themeMode");
    if (themeMode == null || (!themeMode.equalsIgnoreCase("dark") && !themeMode.equalsIgnoreCase("light"))) {
        return new ResponseEntity<>(failureResponse("Invalid theme mode"), HttpStatus.BAD_REQUEST);
    }

    user.setTheme(themeMode);
    userRepository.save(user);

    Map<String, Object> response = new HashMap<>();
    response.put("message", "Theme updated successfully");
    response.put("themeMode", themeMode);
    return new ResponseEntity<>(response, HttpStatus.OK);
}


    // Endpoint to change password
    @PutMapping("/updatePassword/{username}")
    public ResponseEntity<String> updatePassword(@PathVariable("username") String username, @RequestBody Map<String, String> passwordMap) {
        // Fetch the new password from the request body
        String newPassword = passwordMap.get("newPassword");
        if (newPassword == null || newPassword.isEmpty()) {
            return ResponseEntity.badRequest().body("Password cannot be empty");
        }

        // Find the user by username
        User existingUser = userRepository.findByUsername(username);
        if (existingUser == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User not found");
        }

        // Update the password
        existingUser.setPassword(newPassword);
        userRepository.save(existingUser);

        return ResponseEntity.ok("Password updated successfully");
    }


    // Endpoint to delete user account by username
    @DeleteMapping("/delete")
    public String deleteUser(@RequestParam String username) {
        User user = userRepository.findByUsername(username);
        if (user != null) {
            userRepository.delete(user);
            return "User account deleted successfully.";
        } else {
            return "User not found.";
        }
    }

    @GetMapping(path = "/users/{id}/avatar")
    ResponseEntity<Resource> getUserAvatar(@PathVariable int id) throws IOException, SQLException {
        User user = userRepository.findById(id);

        if(user == null || user.getAvatar() == null) {
            return null;
        }

        HttpHeaders header = new HttpHeaders();
        header.add(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=" + user.getExtension());
        header.add("Cache-Control", "no-cache, no-store, must-revalidate");
        header.add("Pragma", "no-cache");
        header.add("Expires", "0");

        int blobLength = (int) user.getAvatar().length();
        byte[] byteArray = user.getAvatar().getBytes(1, blobLength);
        ByteArrayResource data = new ByteArrayResource(byteArray);

        return ResponseEntity.ok()
                .headers(header)
                .contentLength(blobLength)
                .contentType(MediaType.parseMediaType("application/octet-stream"))
                .body(data);
    }
}
