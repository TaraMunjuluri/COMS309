package onetomany.Phones;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;

/**
 * REST Controller for managing Phone entities.
 * Provides endpoints to handle CRUD operations for Phone records.
 */
@RestController
@Tag(name = "Phone Controller", description = "Manage Phone entities in the system.")
public class PhoneController {

    @Autowired
    PhoneRepository phoneRepository;

    private String success = "{\"message\":\"success\"}";
    private String failure = "{\"message\":\"failure\"}";

    /**
     * Retrieves all phones from the repository.
     *
     * @return List of all Phone entities.
     */
    @Operation(summary = "Get all phones", description = "Retrieve a list of all phones in the system.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "List of phones retrieved successfully")
    })
    @GetMapping(path = "/phones")
    public List<Phone> getAllPhones() {
        return phoneRepository.findAll();
    }

    /**
     * Retrieves a single phone by its ID.
     *
     * @param id The ID of the phone to retrieve.
     * @return The Phone entity if found, or null if not.
     */
    @Operation(summary = "Get phone by ID", description = "Retrieve a phone by its unique ID.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Phone found"),
            @ApiResponse(responseCode = "404", description = "Phone not found")
    })
    @GetMapping(path = "/phones/{id}")
    public Phone getPhoneById(
            @Parameter(description = "ID of the phone to retrieve", required = true) @PathVariable int id) {
        return phoneRepository.findById(id);
    }

    /**
     * Creates a new phone entry in the repository.
     *
     * @param phone The Phone object to be created.
     * @return A JSON response indicating success or failure.
     */
    @Operation(summary = "Create a new phone", description = "Create a new phone record in the system.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Phone created successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid input provided")
    })
    @PostMapping(path = "/phones")
    public String createPhone(
            @io.swagger.v3.oas.annotations.parameters.RequestBody(description = "Details of the phone to create", required = true,
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = Phone.class)))
            @RequestBody Phone phone) {
        if (phone == null) {
            return failure;
        }
        phoneRepository.save(phone);
        return success;
    }

    /**
     * Updates an existing phone by its ID.
     *
     * @param id      The ID of the phone to update.
     * @param request The Phone object containing updated information.
     * @return The updated Phone entity, or null if the phone was not found.
     */
    @Operation(summary = "Update phone by ID", description = "Update an existing phone record by its ID.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Phone updated successfully"),
            @ApiResponse(responseCode = "404", description = "Phone not found")
    })
    @PutMapping("/phones/{id}")
    public Phone updatePhone(
            @Parameter(description = "ID of the phone to update", required = true) @PathVariable int id,
            @RequestBody Phone request) {
        Phone phone = phoneRepository.findById(id);
        if (phone == null) {
            return null;
        }
        phoneRepository.save(request);
        return phoneRepository.findById(id);
    }
}
