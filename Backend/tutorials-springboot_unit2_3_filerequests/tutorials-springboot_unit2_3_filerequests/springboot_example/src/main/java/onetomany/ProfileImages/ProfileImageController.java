package onetomany.ProfileImages;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import onetomany.ExceptionHandlers.ImageNotFoundException;
import onetomany.ExceptionHandlers.UnauthorizedException;
import onetomany.Users.UserRepository;
import onetomany.images.Image;
import onetomany.images.ImageRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/images")
public class ProfileImageController {

    @Autowired
    private ImageRepository imageRepository;

    @Autowired
    private UserRepository userRepository;

    private final String UPLOAD_DIRECTORY = "./uploads/images/";

    @Operation(summary = "Upload a new profile image for a user")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Image uploaded successfully",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = Image.class))),
            @ApiResponse(responseCode = "400", description = "User not found", content = @Content),
            @ApiResponse(responseCode = "500", description = "Failed to upload image", content = @Content)
    })
    @PostMapping("/upload/{userId}")
    public ResponseEntity<?> uploadImage(
            @PathVariable Integer userId,
            @RequestParam("file") MultipartFile file) {
        try {
            // Verify user exists
            if (!userRepository.existsById(Long.valueOf(userId))) {
                throw new UnauthorizedException("User not found");
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
            throw new RuntimeException("Failed to upload image: " + e.getMessage());
        }
    }

    @Operation(summary = "Get all images uploaded by a specific user")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Images retrieved successfully",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = Image.class)))
    })
    @GetMapping("/user/{userId}")
    public List<Image> getUserImages(@PathVariable Integer userId) {
        return imageRepository.findByUserId(userId);
    }

    @Operation(summary = "Get image details by image ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Image retrieved successfully",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = Image.class))),
            @ApiResponse(responseCode = "404", description = "Image not found", content = @Content)
    })
    @GetMapping("/{imageId}")
    public Image getImage(@PathVariable Integer imageId) {
        Optional<Image> optionalImage = imageRepository.findById(imageId);
        if (optionalImage.isEmpty()) {
            try {
                throw new ImageNotFoundException("Image not found with ID: " + imageId);
            } catch (ImageNotFoundException e) {
                throw new RuntimeException(e);
            }
        }
        return optionalImage.get();
    }

    @Operation(summary = "Delete an image uploaded by a specific user")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Image deleted successfully"),
            @ApiResponse(responseCode = "404", description = "Image not found or unauthorized", content = @Content),
            @ApiResponse(responseCode = "500", description = "Failed to delete image", content = @Content)
    })
    @DeleteMapping("/{userId}/{imageId}")
    public String deleteImage(@PathVariable Integer userId, @PathVariable Integer imageId) {
        try {
            Optional<Image> optionalImage = imageRepository.findById(imageId);
            if (optionalImage.isEmpty()) {
                throw new ImageNotFoundException("Image not found with ID: " + imageId);
            }

            Image image = optionalImage.get();

            if (!image.getUserId().equals(userId)) {
                throw new UnauthorizedException("User is not authorized to delete this image");
            }

            // Delete file from disk
            String filename = image.getImageLink().substring(image.getImageLink().lastIndexOf('/') + 1);
            Path filePath = Paths.get(UPLOAD_DIRECTORY + filename);
            Files.deleteIfExists(filePath);

            // Delete from database
            imageRepository.deleteByUserIdAndImageId(userId, imageId);
            return "Image deleted successfully";

        } catch (IOException | ImageNotFoundException e) {
            throw new RuntimeException("Failed to delete image: " + e.getMessage());
        }
    }

    @Operation(summary = "Get the file path of an image by ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Image path retrieved successfully"),
            @ApiResponse(responseCode = "404", description = "Image not found", content = @Content)
    })
    @GetMapping("/path/{imageId}")
    public String getImagePathById(@PathVariable Integer imageId) throws ImageNotFoundException {
        Optional<Image> optionalImage = imageRepository.findById(imageId);
        if (optionalImage.isEmpty()) {
            throw new ImageNotFoundException("Image not found with ID: " + imageId);
        }
        return optionalImage.get().getImageLink();
    }
}
