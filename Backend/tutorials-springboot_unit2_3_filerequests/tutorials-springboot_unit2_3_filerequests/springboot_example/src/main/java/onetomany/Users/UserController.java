
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


//package onetomany.Users;

import java.io.IOException;
import java.sql.Blob;
import java.sql.SQLException;
import java.util.List;

import javax.sql.rowset.serial.SerialBlob;


import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;


@RestController
@Tag(name = "User Controller", description = "Manage users, their profiles, and authentication")
public class UserController {

    @Autowired
    UserRepository userRepository;

    @Autowired
    LaptopRepository laptopRepository;

    private String success = "{\"message\":\"success\"}";
    private String failure = "{\"message\":\"failure\"}";

    @Operation(summary = "Get all users")
    @ApiResponse(responseCode = "200", description = "List of users retrieved successfully")
    @GetMapping(path = "/users")
    List<User> getAllUsers(){
        return userRepository.findAll();
    }

    @Operation(summary = "Get user by ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "User found",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = User.class))),
            @ApiResponse(responseCode = "404", description = "User not found",
                    content = @Content)
    })
    @GetMapping(path = "/users/{id}")
    User getUserById(@Parameter(description = "ID of the user to retrieve")  @PathVariable int id){
        return userRepository.findById(id);
    }

    @Operation(summary = "Create new user with avatar")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "User created successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid input provided")
    })
    @PostMapping(path = "/users")
    String createUser( @Parameter(description = "User's avatar image file")  @RequestParam("avatar") MultipartFile avatar, @Parameter(description = "User details in JSON format")  @RequestParam("user") String userString) throws Exception {
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

    @Operation(summary = "Register new user")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "User registered successfully"),
            @ApiResponse(responseCode = "400", description = "Email or username already exists")
    })
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

    @Operation(summary = "User login")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Login successful"),
            @ApiResponse(responseCode = "401", description = "Invalid credentials")
    })
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

    @Operation(summary = "User login")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Login successful"),
            @ApiResponse(responseCode = "401", description = "Invalid credentials")
    })
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

    @Operation(summary = "User logout")
    @ApiResponse(responseCode = "200", description = "Logout successful")
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

    @Operation(summary = "Update user")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "User updated successfully"),
            @ApiResponse(responseCode = "404", description = "User not found")
    })
    @PutMapping("/users/{id}")
    User updateUser( @Parameter(description = "ID of the user to update") @PathVariable int id, @RequestBody User request){
        User user = userRepository.findById(id);
        if(user == null)
            return null;
        userRepository.save(request);
        return userRepository.findById(id);
    }

    @Operation(summary = "Assign laptop to user")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Laptop assigned successfully"),
            @ApiResponse(responseCode = "404", description = "User or laptop not found")
    })
    @PutMapping("/users/{userId}/laptops/{laptopId}")
    String assignLaptopToUser(@Parameter(description = "ID of the user")  @PathVariable int userId, @Parameter(description = "ID of the laptop") @PathVariable int laptopId){
        User user = userRepository.findById(userId);
        Laptop laptop = laptopRepository.findById(laptopId);
        if(user == null || laptop == null)
            return failure;
        laptop.setUser(user);
        user.setLaptop(laptop);
        userRepository.save(user);
        return success;
    }

    @Operation(summary = "Delete user")
    @ApiResponse(responseCode = "200", description = "User deleted successfully")
    @DeleteMapping(path = "/users/{id}")
    String deleteUser(@Parameter(description = "ID of the user to delete") @PathVariable long id){
        userRepository.deleteById(id);
        return success;
    }

    @Operation(summary = "Search users by username")
    @ApiResponse(responseCode = "200", description = "Search results retrieved successfully")

    @GetMapping(path = "/users/search")
    List<User> searchUsers( @Parameter(description = "Username to search for") @RequestParam("username") String username) {
        return userRepository.findByUsernameContainingIgnoreCase(username);
    }

    @Operation(summary = "Update user's theme preference")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Theme updated successfully"),
            @ApiResponse(responseCode = "404", description = "User not found"),
            @ApiResponse(responseCode = "400", description = "Invalid theme mode")
    })
@PutMapping("/users/{id}/theme")
public ResponseEntity<Map<String, Object>> toggleThemeMode(
            @Parameter(description = "ID of the user")
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

    @Operation(summary = "Update user password")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Password updated successfully"),
            @ApiResponse(responseCode = "404", description = "User not found"),
            @ApiResponse(responseCode = "400", description = "Invalid password")
    })
    // Endpoint to change password
    @PutMapping("/updatePassword/{username}")
    public ResponseEntity<String> updatePassword( @Parameter(description = "Username of the user")  @PathVariable("username") String username, @RequestBody Map<String, String> passwordMap) {
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

    @Operation(summary = "Delete user account by username")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "User deleted successfully"),
            @ApiResponse(responseCode = "404", description = "User not found")
    })
    // Endpoint to delete user account by username
    @DeleteMapping("/delete")
    public String deleteUser( @Parameter(description = "Username of the user to delete") @RequestParam String username) {
        User user = userRepository.findByUsername(username);
        if (user != null) {
            userRepository.delete(user);
            return "User account deleted successfully.";
        } else {
            return "User not found.";
        }
    }

    //demo 4
    @Operation(summary = "Switch user language preference")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Language updated successfully"),
            @ApiResponse(responseCode = "401", description = "User not logged in"),
            @ApiResponse(responseCode = "400", description = "Invalid language code")
    })
    @PutMapping("/users/language")
    public ResponseEntity<Map<String, Object>> switchLanguage(
            @RequestBody Map<String, String> languageRequest,
            HttpServletRequest request
    ) {

        HttpSession session = request.getSession(false);


        if (session == null || session.getAttribute("loggedInUser") == null) {
            Map<String, Object> response = new HashMap<>();
            response.put("message", "User not logged in");
            return ResponseEntity.status(401).body(response);
        }


        User loggedInUser = (User) session.getAttribute("loggedInUser");


        String languageCode = languageRequest.get("languageCode");


        try {
            Language newLanguage = Language.fromCode(languageCode);


            loggedInUser.setLanguage(newLanguage);
            userRepository.save(loggedInUser);


            Map<String, Object> response = new HashMap<>();
            response.put("message", "Language updated successfully");
            response.put("language", newLanguage.getCode());

            return ResponseEntity.ok(response);
        } catch (IllegalArgumentException e) {
            Map<String, Object> response = new HashMap<>();
            response.put("message", "Invalid language code");
            return ResponseEntity.badRequest().body(response);
        }
    }

    @Operation(summary = "Get user's current language preference")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Language retrieved successfully"),
            @ApiResponse(responseCode = "401", description = "User not logged in")
    })
    @GetMapping("/users/language")
    public ResponseEntity<Map<String, Object>> getCurrentLanguage(HttpServletRequest request) {
        // Get the current session
        HttpSession session = request.getSession(false);

        // Check if user is logged in
        if (session == null || session.getAttribute("loggedInUser") == null) {
            Map<String, Object> response = new HashMap<>();
            response.put("message", "User not logged in");
            return ResponseEntity.status(401).body(response);
        }

        // Get the logged-in user
        User loggedInUser = (User) session.getAttribute("loggedInUser");

        // Prepare response
        Map<String, Object> response = new HashMap<>();
        response.put("language", loggedInUser.getLanguage().getCode());

        return ResponseEntity.ok(response);
    }
    //demo 4

    @Operation(summary = "Get user's avatar")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Avatar retrieved successfully",
                    content = @Content(mediaType = "application/octet-stream")),
            @ApiResponse(responseCode = "404", description = "User or avatar not found")
    })
    @GetMapping(path = "/users/{id}/avatar")
    ResponseEntity<Resource> getUserAvatar(@Parameter(description = "ID of the user") @PathVariable int id) throws IOException, SQLException {
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
