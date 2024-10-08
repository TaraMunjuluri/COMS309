
package onetomany.Users;

import java.io.IOException;
import java.net.InetAddress;
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
import org.springframework.web.multipart.MultipartFile;

import onetomany.Laptops.Laptop;
import onetomany.Laptops.LaptopRepository;

import org.springframework.http.HttpStatus;

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
//        if (existingUser == null || !user.getPassword().equals(existingUser.getPassword())) { // Compare raw passwords
//            return new ResponseEntity<>("Invalid credentials", HttpStatus.UNAUTHORIZED);
//        }
//
//        return new ResponseEntity<>("Login successful", HttpStatus.OK);
//    }

    @PostMapping("/signup")
    public ResponseEntity<String> signup(@RequestBody User user) {
        if (userRepository.findByEmailId(user.getEmailId()) != null) {
            return new ResponseEntity<>("Email is already registered", HttpStatus.BAD_REQUEST);
        }

        // Save the user with the plain text password
        userRepository.save(user);

        return new ResponseEntity<>("User registered successfully", HttpStatus.CREATED);
    }

    @PostMapping("/login")
    public ResponseEntity<String> login(@RequestBody User user) {
        User existingUser = userRepository.findByEmailId(user.getEmailId());
        if (existingUser == null || !user.getPassword().equals(existingUser.getPassword())) {
            return new ResponseEntity<>("Invalid credentials", HttpStatus.UNAUTHORIZED);
        }

        return new ResponseEntity<>("Login successful", HttpStatus.OK);
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
    public ResponseEntity<String> logout(HttpServletRequest request) {
        HttpSession session = request.getSession(false);
        if (session != null) {
            session.invalidate();
        }
        return new ResponseEntity<>("{\"message\":\"Logout successful\"}", HttpStatus.OK);
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
