////package onetomany.Users;
////
////import java.io.IOException;
////import java.sql.Blob;
////import java.sql.SQLException;
////import java.util.List;
////
////import javax.sql.rowset.serial.SerialBlob;
////
////import com.fasterxml.jackson.databind.ObjectMapper;
////
////import org.springframework.beans.factory.annotation.Autowired;
////import org.springframework.core.io.ByteArrayResource;
////import org.springframework.core.io.Resource;
////import org.springframework.http.HttpHeaders;
////import org.springframework.http.MediaType;
////import org.springframework.http.ResponseEntity;
////import org.springframework.web.bind.annotation.DeleteMapping;
////import org.springframework.web.bind.annotation.GetMapping;
////import org.springframework.web.bind.annotation.PathVariable;
////import org.springframework.web.bind.annotation.PostMapping;
////import org.springframework.web.bind.annotation.PutMapping;
////import org.springframework.web.bind.annotation.RequestBody;
////import org.springframework.web.bind.annotation.RequestParam;
////import org.springframework.web.bind.annotation.RestController;
////import org.springframework.web.multipart.MultipartFile;
////
////import onetomany.Laptops.Laptop;
////import onetomany.Laptops.LaptopRepository;
////
//////new import statements
////import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
////import java.util.Optional;
////import org.springframework.http.HttpStatus;
////
////
/////**
//// *
//// * @author Vivek Bengre
//// *
//// */
////
////@RestController
////public class UserController {
////
////    @Autowired
////    UserRepository userRepository;
////
////    @Autowired
////    LaptopRepository laptopRepository;
////
////    //demo 2 passwordEncoder
////    @Autowired
////    BCryptPasswordEncoder passwordEncoder;
////
////
////    private String success = "{\"message\":\"success\"}";
////    private String failure = "{\"message\":\"failure\"}";
////
////    @GetMapping(path = "/users")
////    List<User> getAllUsers(){
////        return userRepository.findAll();
////    }
////
////    @GetMapping(path = "/users/{id}")
////    User getUserById( @PathVariable int id){
////        return userRepository.findById(id);
////    }
////
////    /**
////     * This is where the userdata and the image should be uploaded, you can observe that there is no @RequestBody,
////     * this is because a file and data cannot be uploaded together with different formats with @RequestBody.
////     * @RequestParam allows for 2 different types of data i.e. file and text. The text is entered as a JSON and then
////     * converted into an Object by the Object mapper and then saved into the database. Note: use form data as body for postman testing
////     * first key will be avatar with assciated file, and the second key will be user with input as a json
////     */
////    @PostMapping(path = "/users")
////    String createUser(@RequestParam("avatar") MultipartFile avatar, @RequestParam("user") String userString) throws Exception {
////        // check if user input is null
////        if (userString == null)
////            return failure;
////
////        // convert the string into an object which is requied, this is what happens by default when
////        // you use @RequestBody
////        ObjectMapper objectMapper = new ObjectMapper();
////        User user = objectMapper.readValue(userString, User.class);
////
////        // This indicates the file name and the extenstion of the image i.e. png jpg etc and is required when
////        // the image has to be sent to the front end
////        user.setExtenstion(avatar.getOriginalFilename());
////
////        // user is first saved as the image can be null
////        userRepository.save(user);
////
////        // check if the image is not null update the image for the entity
////        if(avatar != null) {
////            byte[] file = avatar.getBytes();
////            SerialBlob blob = new SerialBlob(file);
////            Blob image = blob;
////            user.setAvtar(image);
////            userRepository.save(user);
////        }
////        return success;
////    }
////
////// demo 2 login page logic (start)
////    @PostMapping("/signup")
////    public ResponseEntity<String> signup(@RequestBody User user) {
////        // Check if the email is already registered
////        if (userRepository.findByEmailId(user.getEmailId()) != null) {
////            return new ResponseEntity<>("Email is already registered", HttpStatus.BAD_REQUEST);
////        }
////
////        // Encrypt the password before saving
////        user.setPassword(passwordEncoder.encode(user.getPassword()));
////        userRepository.save(user);
////
////        return new ResponseEntity<>("User registered successfully", HttpStatus.CREATED);
////    }
////
////    //==================== Login Endpoint ===============================//
////
////    @PostMapping("/login")
////    public ResponseEntity<String> login(@RequestBody User user) {
////        User existingUser = userRepository.findByEmailId(user.getEmailId());
////        if (existingUser == null || !passwordEncoder.matches(user.getPassword(), existingUser.getPassword())) {
////            return new ResponseEntity<>("Invalid credentials", HttpStatus.UNAUTHORIZED);
////        }
////
////        // Handle session management or JWT token generation here
////        return new ResponseEntity<>("Login successful", HttpStatus.OK);
////    }
////
//////    demo 2 login page logic (end)
////
////    @PutMapping("/users/{id}")
////    User updateUser(@PathVariable int id, @RequestBody User request){
////        User user = userRepository.findById(id);
////        if(user == null)
////            return null;
////        userRepository.save(request);
////        return userRepository.findById(id);
////    }
////
////    @PutMapping("/users/{userId}/laptops/{laptopId}")
////    String assignLaptopToUser(@PathVariable int userId,@PathVariable int laptopId){
////        User user = userRepository.findById(userId);
////        Laptop laptop = laptopRepository.findById(laptopId);
////        if(user == null || laptop == null)
////            return failure;
////        laptop.setUser(user);
////        user.setLaptop(laptop);
////        userRepository.save(user);
////        return success;
////    }
////
////    @DeleteMapping(path = "/users/{id}")
////    String deleteLaptop(@PathVariable int id){
////        userRepository.deleteById(id);
////        return success;
////    }
////
////
////    @GetMapping(path = "/users/search")
////    List<User> searchUsers(@RequestParam("username") String username) {
////        return userRepository.findByUsernameContainingIgnoreCase(username);
////    }
////
////    /**
////     * The Response Entity type is set as <Resource>, which can handle files and images very well
////     * additional header have to be set to tell the front end what type of conent is being sent from the
////     * backend, thus in this example headers are set.
////    */
////    @GetMapping(path = "/users/{id}/avatar")
////    ResponseEntity<Resource> getLaptopInvoice(@PathVariable int id) throws IOException, SQLException{
////
////        User user = userRepository.findById(id);
////
////        // if user id not found it cannot have an avatar associated with it
////        if(user == null || user.getAvtar() == null){
////            return null;
////        }
////
////        // add headers to state that a file is being downloaded
////        HttpHeaders header = new HttpHeaders();
////        header.add(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename="+user.getExtenstion());
////        header.add("Cache-Control", "no-cache, no-store, must-revalidate");
////        header.add("Pragma", "no-cache");
////        header.add("Expires", "0");
////
////        // convert blob to bytearray and set it as response
////        int blobLength = (int)user.getAvtar().length();
////        byte[] byteArray = user.getAvtar().getBytes(1, blobLength);
////        ByteArrayResource data = new ByteArrayResource(byteArray);
////
////        // send the response entity back to the front end with the file
////        return ResponseEntity.ok()
////                .headers(header)
////                .contentLength(blobLength)
////                .contentType(MediaType.parseMediaType("application/octet-stream"))
////                .body(data);
////    }
////}
//
//package onetomany.Users;
//
//import java.io.IOException;
//import java.sql.Blob;
//import java.sql.SQLException;
//import java.util.List;
//
//import javax.sql.rowset.serial.SerialBlob;
//
//import com.fasterxml.jackson.databind.ObjectMapper;
//
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.core.io.ByteArrayResource;
//import org.springframework.core.io.Resource;
//import org.springframework.http.HttpHeaders;
//import org.springframework.http.MediaType;
//import org.springframework.http.ResponseEntity;
//import org.springframework.web.bind.annotation.DeleteMapping;
//import org.springframework.web.bind.annotation.GetMapping;
//import org.springframework.web.bind.annotation.PathVariable;
//import org.springframework.web.bind.annotation.PostMapping;
//import org.springframework.web.bind.annotation.PutMapping;
//import org.springframework.web.bind.annotation.RequestBody;
//import org.springframework.web.bind.annotation.RequestParam;
//import org.springframework.web.bind.annotation.RestController;
//import org.springframework.web.multipart.MultipartFile;
//
//import onetomany.Laptops.Laptop;
//import onetomany.Laptops.LaptopRepository;
//
//import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
//import org.springframework.http.HttpStatus;
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
//    User getUserById( @PathVariable int id){
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
//

package onetomany.Users;

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
import org.springframework.web.multipart.MultipartFile;

import onetomany.Laptops.Laptop;
import onetomany.Laptops.LaptopRepository;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.http.HttpStatus;

@RestController
public class UserController {

    @Autowired
    UserRepository userRepository;

    @Autowired
    LaptopRepository laptopRepository;

    @Autowired
    BCryptPasswordEncoder passwordEncoder;

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
    public ResponseEntity<String> signup(@RequestBody User user) {
        if (userRepository.findByEmailId(user.getEmailId()) != null) {
            return new ResponseEntity<>("Email is already registered", HttpStatus.BAD_REQUEST);
        }

        user.setPassword(passwordEncoder.encode(user.getPassword()));
        userRepository.save(user);

        return new ResponseEntity<>("User registered successfully", HttpStatus.CREATED);
    }

    @PostMapping("/login")
    public ResponseEntity<String> login(@RequestBody User user) {
        User existingUser = userRepository.findByEmailId(user.getEmailId());
        if (existingUser == null || !passwordEncoder.matches(user.getPassword(), existingUser.getPassword())) {
            return new ResponseEntity<>("Invalid credentials", HttpStatus.UNAUTHORIZED);
        }

        return new ResponseEntity<>("Login successful", HttpStatus.OK);
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
    String assignLaptopToUser(@PathVariable int userId,@PathVariable int laptopId){
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
    ResponseEntity<Resource> getUserAvatar(@PathVariable int id) throws IOException, SQLException{
        User user = userRepository.findById(id);

        if(user == null || user.getAvatar() == null){
            return null;
        }

        HttpHeaders header = new HttpHeaders();
        header.add(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename="+user.getExtension());
        header.add("Cache-Control", "no-cache, no-store, must-revalidate");
        header.add("Pragma", "no-cache");
        header.add("Expires", "0");

        int blobLength = (int)user.getAvatar().length();
        byte[] byteArray = user.getAvatar().getBytes(1, blobLength);
        ByteArrayResource data = new ByteArrayResource(byteArray);

        return ResponseEntity.ok()
                .headers(header)
                .contentLength(blobLength)
                .contentType(MediaType.parseMediaType("application/octet-stream"))
                .body(data);
    }
}