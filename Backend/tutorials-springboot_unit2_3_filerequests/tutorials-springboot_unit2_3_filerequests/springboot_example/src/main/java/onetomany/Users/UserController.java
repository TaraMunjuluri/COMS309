
package onetomany.Users;

import java.io.IOException;
import java.net.InetAddress;
import java.sql.Blob;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;

import javax.sql.rowset.serial.SerialBlob;

import com.fasterxml.jackson.databind.ObjectMapper;

import onetomany.Groups.UserGroup;
import onetomany.Groups.GroupRepository;
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

//package onetomany.Users;

import java.io.IOException;
import java.sql.Blob;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;

import javax.sql.rowset.serial.SerialBlob;



@RestController
public class UserController {

    @Autowired
    UserRepository userRepository;

    @Autowired
    LaptopRepository laptopRepository;

    @Autowired
    private GroupRepository groupRepository;



    private String success = "{\"message\":\"success\"}";
    private String failure = "{\"message\":\"failure\"}";

//    demo 3
    @PostMapping("/groups")
    public ResponseEntity<String> createGroup(@RequestParam String name) {
        UserGroup group = new UserGroup();
        group.setName(name);
        groupRepository.save(group);
        return ResponseEntity.status(HttpStatus.CREATED).body("Group created successfully");
    }

    @PostMapping("/groups/{groupId}/users/{userId}")
    public ResponseEntity<String> addUserToGroup(@PathVariable Long groupId, @PathVariable Long userId) {
        UserGroup group = groupRepository.findById(groupId).orElse(null);
        User user = userRepository.findById(userId).orElse(null);
        if (group == null || user == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Group or User not found");
        }
        group.getMembers().add(user);
        user.getGroups().add(group);
        groupRepository.save(group);
        userRepository.save(user);
        return ResponseEntity.ok("User added to group successfully");
    }

//    demo 3

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
    String deleteUser(@PathVariable int id){
        userRepository.deleteById(id);
        return success;
    }

    @GetMapping(path = "/users/search")
    List<User> searchUsers(@RequestParam("username") String username) {
        return userRepository.findByUsernameContainingIgnoreCase(username);
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
