package onetomany.Laptops;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

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

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;

/**
 * Controller class for managing laptop-related operations.
 *
 * Provides endpoints to create, update, retrieve, and delete laptops,
 * as well as handle file uploads and downloads associated with laptops.
 */
@RestController
@Tag(name = "Laptop Controller", description = "Manage Laptop entities and associated files.")
public class LaptopController {

    private static final String uploadPath = "/uploadedFiles/";

    @Autowired
    LaptopRepository laptopRepository;

    @Autowired
    HttpServletRequest httpServletRequest;

    private static final String success = "{\"message\":\"success\"}";
    private static final String failure = "{\"message\":\"failure\"}";

    /**
     * Retrieves all laptops from the repository.
     *
     * @return List of all laptops.
     */
    @Operation(summary = "Get all laptops", description = "Retrieve a list of all laptops in the system.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "List of laptops retrieved successfully")
    })
    @GetMapping(path = "/laptops")
    public List<Laptop> getAllLaptops() {
        return laptopRepository.findAll();
    }

    /**
     * Retrieves a specific laptop by its ID.
     *
     * @param id Laptop ID.
     * @return The laptop with the specified ID.
     */
    @Operation(summary = "Get laptop by ID", description = "Retrieve a laptop by its unique ID.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Laptop found"),
            @ApiResponse(responseCode = "404", description = "Laptop not found")
    })
    @GetMapping(path = "/laptops/{id}")
    public Laptop getLaptopById(
            @Parameter(description = "ID of the laptop to retrieve", required = true) @PathVariable int id) {
        return laptopRepository.findById(id);
    }

    /**
     * Creates a new laptop.
     *
     * @param laptop The laptop to be created.
     * @return Success or failure message.
     */
    @Operation(summary = "Create a new laptop", description = "Create a new laptop record in the system.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Laptop created successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid input provided")
    })
    @PostMapping(path = "/laptops")
    public String createLaptop(
            @Parameter(description = "Details of the laptop to create", required = true,
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = Laptop.class)))
            @RequestBody Laptop laptop) {
        if (laptop == null) {
            return failure;
        }
        laptopRepository.save(laptop);
        return success;
    }

    /**
     * Updates an existing laptop by its ID.
     *
     * @param id Laptop ID.
     * @param request Updated laptop details.
     * @return Updated laptop.
     */
    @Operation(summary = "Update laptop by ID", description = "Update an existing laptop record by its ID.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Laptop updated successfully"),
            @ApiResponse(responseCode = "404", description = "Laptop not found")
    })
    @PutMapping(path = "/laptops/{id}")
    public Laptop updateLaptop(
            @Parameter(description = "ID of the laptop to update", required = true) @PathVariable int id,
            @RequestBody Laptop request) {
        Laptop laptop = laptopRepository.findById(id);
        if (laptop == null) {
            return null;
        }
        laptopRepository.save(request);
        return laptopRepository.findById(id);
    }

    /**
     * Deletes a specific laptop by its ID.
     *
     * @param id Laptop ID.
     * @return Success message.
     */
    @Operation(summary = "Delete laptop by ID", description = "Delete a laptop record by its unique ID.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Laptop deleted successfully"),
            @ApiResponse(responseCode = "404", description = "Laptop not found")
    })
    @DeleteMapping(path = "/laptops/{id}")
    public String deleteLaptop(
            @Parameter(description = "ID of the laptop to delete", required = true) @PathVariable int id) {
        Laptop laptop = laptopRepository.findById(id);
        if (laptop == null) {
            return success;
        }

        // Delete the associated file if it exists
        File currentInvoice = new File(laptop.getInvoicePath());
        if (currentInvoice != null && currentInvoice.exists()) {
            currentInvoice.delete();
        }

        laptopRepository.deleteById(id);
        return success;
    }

    /**
     * Uploads an invoice for a laptop and associates it with the laptop's record.
     *
     * @param id Laptop ID.
     * @param invoiceFile The invoice file to be uploaded.
     * @return Success or failure message.
     * @throws IOException If file handling fails.
     */
    @Operation(summary = "Upload invoice for laptop", description = "Upload an invoice for a laptop and associate it with the laptop's record.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Invoice uploaded successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid file or laptop not found")
    })
    @PutMapping(path = "/laptops/{id}/invoice")
    public String uploadLaptopInvoice(
            @Parameter(description = "ID of the laptop to upload invoice for", required = true) @PathVariable int id,
            @RequestParam("invoice") MultipartFile invoiceFile) throws IOException {
        if (invoiceFile.isEmpty()) {
            return failure;
        }

        // Get the full path for file upload
        String fullPath = httpServletRequest.getServletContext().getRealPath(uploadPath);

        // Create directory if it doesn't exist
        if (!new File(fullPath).exists()) {
            new File(fullPath).mkdir();
        }

        fullPath += ("laptop_invoice_" + id + "_" + invoiceFile.getOriginalFilename());

        // Get the laptop associated with the ID
        Laptop laptop = laptopRepository.findById(id);
        String currentPath = laptop.getInvoicePath();

        // Delete the existing invoice file if present
        if (currentPath != null) {
            File currentInvoice = new File(currentPath);
            if (currentInvoice.exists()) {
                currentInvoice.delete();
            }
        }

        // Update the laptop record with the new invoice path
        laptop.setInvoicePath(fullPath);
        laptopRepository.save(laptop);

        // Save the file to disk
        File tempFile = new File(fullPath);
        invoiceFile.transferTo(tempFile);

        return success;
    }

    /**
     * Retrieves the invoice associated with a laptop.
     *
     * @param id Laptop ID.
     * @return The invoice file as a ResponseEntity.
     * @throws IOException If file handling fails.
     */
    @Operation(summary = "Get invoice for laptop", description = "Retrieve the invoice file associated with a laptop.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Invoice retrieved successfully"),
            @ApiResponse(responseCode = "404", description = "Invoice not found")
    })
    @GetMapping(path = "/laptops/{id}/invoice")
    public ResponseEntity<Resource> getLaptopInvoice(
            @Parameter(description = "ID of the laptop to retrieve the invoice for", required = true) @PathVariable int id) throws IOException {
        Laptop laptop = laptopRepository.findById(id);

        if (laptop == null || laptop.getInvoicePath() == null) {
            return null;
        }

        File file = new File(laptop.getInvoicePath());
        if (!file.exists()) {
            return null;
        }

        // Extract file name from path
        String[] splitPath = laptop.getInvoicePath().split("/");
        String fileName = splitPath[splitPath.length - 1];

        // Set headers for the response
        HttpHeaders header = new HttpHeaders();
        header.add(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=" + fileName);
        header.add("Cache-Control", "no-cache, no-store, must-revalidate");
        header.add("Pragma", "no-cache");
        header.add("Expires", "0");

        // Convert file to ByteArrayResource
        Path path = Paths.get(file.getAbsolutePath());
        ByteArrayResource resource = new ByteArrayResource(Files.readAllBytes(path));

        return ResponseEntity.ok()
                .headers(header)
                .contentLength(file.length())
                .contentType(MediaType.APPLICATION_OCTET_STREAM)
                .body(resource);
    }
}
