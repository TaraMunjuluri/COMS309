package onetomany.controllers;
import org.springframework.http.HttpStatus;

import onetomany.images.Image;
import onetomany.images.ImageRepository;
import onetomany.Users.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

@RestController
@RequestMapping("/api/images")
public class ProfileImageController {

    @Autowired
    private ImageRepository imageRepository;

    @Autowired
    private UserRepository userRepository;

    private final String UPLOAD_DIRECTORY = "./uploads/images/";

    @PostMapping("/upload/{userId}")
    public ResponseEntity<?> uploadImage(@PathVariable Integer userId,
                                         @RequestParam("file") MultipartFile file) {
        try {
            // Verify user exists
            if (!userRepository.existsById(Long.valueOf(userId))) {
                return ResponseEntity.badRequest().body("User not found");
            }

            // Create directory if it doesn't exist
            Path uploadPath = Paths.get(UPLOAD_DIRECTORY);
            if (!Files.exists(uploadPath)) {
                Files.createDirectories(uploadPath);
            }

            // Generate unique filename
            String filename = "user" + userId + "_" + System.currentTimeMillis() + "_" + file.getOriginalFilename();
            Path filePath = uploadPath.resolve(filename);

            // Save file
            Files.copy(file.getInputStream(), filePath);

            // Create image record
            Image image = new Image(userId, "/uploads/images/" + filename);
            Image savedImage = imageRepository.save(image);

            return ResponseEntity.ok(savedImage);

        } catch (IOException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Failed to upload image: " + e.getMessage());
        }
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<?> getUserImages(@PathVariable Integer userId) {
        List<Image> images = imageRepository.findByUserId(userId);
        return ResponseEntity.ok(images);
    }

    @GetMapping("/{imageId}")
    public ResponseEntity<?> getImage(@PathVariable Integer imageId) {
        return imageRepository.findById(imageId)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{userId}/{imageId}")
    public ResponseEntity<?> deleteImage(@PathVariable Integer userId,
                                         @PathVariable Integer imageId) {
        try {
            Image image = imageRepository.findById(imageId).orElse(null);
            if (image == null || !image.getUserId().equals(userId)) {
                return ResponseEntity.notFound().build();
            }

            // Delete file from disk
            String filename = image.getImageLink().substring(image.getImageLink().lastIndexOf('/') + 1);
            Path filePath = Paths.get(UPLOAD_DIRECTORY + filename);
            Files.deleteIfExists(filePath);

            // Delete from database
            imageRepository.deleteByUserIdAndImageId(userId, imageId);
            return ResponseEntity.ok().build();

        } catch (IOException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Failed to delete image: " + e.getMessage());
        }
    }
}