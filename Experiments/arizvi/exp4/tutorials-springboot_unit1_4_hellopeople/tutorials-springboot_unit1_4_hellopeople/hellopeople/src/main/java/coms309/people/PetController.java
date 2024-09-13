package coms309.people;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.server.ResponseStatusException;

import java.util.HashMap;

@RestController
public class PetController {

    HashMap<String, Pet> petList = new HashMap<>();
    RestTemplate restTemplate = new RestTemplate();

    // CREATE operation - Add a new pet with a random image link
    @PostMapping("/pet")
    public String createPet(@RequestBody Pet pet) {
        if (petList.containsKey(pet.getFirstName())) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Pet with this name already exists");
        }

        // Add the pet to the list
        petList.put(pet.getFirstName(), pet);

        // Get a random image URL based on the type of pet
        String imageUrl = "";
        if (pet.getType().equalsIgnoreCase("cat")) {
            imageUrl = getRandomCatImage();
        } else if (pet.getType().equalsIgnoreCase("dog")) {
            imageUrl = getRandomDogImage();
        }

        // Return a message with the image URL
        return "New Pet " + pet.getFirstName() + " saved with type " + pet.getType() +
                " and color: " + pet.getColor() + ". Image URL: " + imageUrl;
    }

    // Helper method to get a random cat image
    private String getRandomCatImage() {
        String catApiUrl = "https://api.thecatapi.com/v1/images/search";
        ImageResponse[] response = restTemplate.getForObject(catApiUrl, ImageResponse[].class);
        return (response != null && response.length > 0) ? response[0].getUrl() : "No image available";
    }

    // Helper method to get a random dog image
    private String getRandomDogImage() {
        String dogApiUrl = "https://api.thedogapi.com/v1/images/search";
        ImageResponse[] response = restTemplate.getForObject(dogApiUrl, ImageResponse[].class);
        return (response != null && response.length > 0) ? response[0].getUrl() : "No image available";
    }

    // Other methods (GET, PUT, DELETE) can remain unchanged
}
