package coms309.people;

import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

import java.util.HashMap;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.PathVariable;


public class PetController {


    /**
     * Controller used to showcase Create and Read from a LIST
     *
     * @author Vivek Bengre
     */
    

        // Note that there is only ONE instance of PeopleController in 
        // Springboot system.
        HashMap<String, Pet> peopleList = new  HashMap<>();

        //CRUDL (create/read/update/delete/list)
        // use POST, GET, PUT, DELETE, GET methods for CRUDL

        // THIS IS THE LIST OPERATION
        // gets all the people in the list and returns it in JSON format
        // This controller takes no input. 
        // Springboot automatically converts the list to JSON format 
        // in this case because of @ResponseBody
        // Note: To LIST, we use the GET method
        @GetMapping("/people")
        public  HashMap<String,Pet> getAllPets() {
            return peopleList;
        }

        // THIS IS THE CREATE OPERATION
        // springboot automatically converts JSON input into a Pet object and 
        // the method below enters it into the list.
        // It returns a string message in THIS example.
        // in this case because of @ResponseBody
        // Note: To CREATE we use POST method
        @PostMapping("/people")
        public  String createPet(@RequestBody Pet Pet) {
            System.out.println(Pet);
            peopleList.put(Pet.getFirstName(), Pet);
            peopleList.put(Pet.getAddress(), Pet);
            if(Pet.getAge() >= 30 && Pet.getAge() < 40){
                return "New Pet "+ Pet.getFirstName() + " "  + " Saved with address " + Pet.getAddress() + " and number: " + Pet.getTelephone() + ". You're in your 30's? How's your back?";
            }
            else if(Pet.getAge() >= 40 && Pet.getAge() < 50){
                return "New Pet "+ Pet.getFirstName() + " "  + " Saved with address " + Pet.getAddress() + " and number: " + Pet.getTelephone() + ". You're in your 40's? Don't look a day over 35!";

            }
            else if(Pet.getAge() >= 50 && Pet.getAge() < 60) {
                return "New Pet " + Pet.getFirstName() + " " + " Saved with address " + Pet.getAddress() + " and number: " + Pet.getTelephone() + ". You're in your 50's? That's half a century!";

            }
            else{
                return "New Pet "+ Pet.getFirstName() + " " + " Saved with address " + Pet.getAddress() + " and number: " + Pet.getTelephone() + ".";

            }
        }


        // THIS IS THE READ OPERATION
        // Springboot gets the PATHVARIABLE from the URL
        // We extract the Pet from the HashMap.
        // springboot automatically converts Pet to JSON format when we return it
        // in this case because of @ResponseBody
        // Note: To READ we use GET method
        @GetMapping("/people/{firstName}")
        public Pet getPet(@PathVariable String firstName) {
            Pet p = peopleList.get(firstName);
            return p;
        }

        // THIS IS THE UPDATE OPERATION
        // We extract the Pet from the HashMap and modify it.
        // Springboot automatically converts the Pet to JSON format
        // Springboot gets the PATHVARIABLE from the URL
        // Here we are returning what we sent to the method
        // in this case because of @ResponseBody
        // Note: To UPDATE we use PUT method

//    @PutMapping("/people/{firstName}")
//    public Pet updatePet(@PathVariable String firstName, @RequestBody Pet p) {
//        peopleList.replace(firstName, p);
//        return peopleList.get(firstName);
//    }

        @PutMapping("/people/{firstName}")
        public Pet updatePet(@PathVariable String firstName, @RequestBody Pet p) {
            if (peopleList.containsKey(firstName)) {
                System.out.println("Updating Pet: " + firstName);
                peopleList.replace(firstName, p);
                return peopleList.get(firstName);
            } else {
                throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Pet not found");
            }
        }

        //    update method that takes both the first name and the last name in the path and updates the Pet based on those values
        @PutMapping("/people/{firstName}/{lastName}")
        public Pet updatePetByFullName(
                @PathVariable String firstName,
                @PathVariable String lastName,
                @RequestBody Pet p) {

            if (peopleList.containsKey(firstName) && peopleList.get(firstName).getLastName().equals(lastName)) {
                peopleList.replace(firstName, p);
                return peopleList.get(firstName);
            } else {
                throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Pet not found with given first and last name");
            }
        }



        //    method that allows updating only certain fields of a Pet’s data (e.g., just the address or telephone number
        @PutMapping("/people/{firstName}/updateAttributes")
        public Pet updatePetAttributes(@PathVariable String firstName, @RequestBody HashMap<String, Object> updates) {
            if (peopleList.containsKey(firstName)) {
                Pet p = peopleList.get(firstName);

                // Update specific attributes if they exist in the request body

                if (updates.containsKey("address")) {
                    p.setAddress((String) updates.get("address"));
                }

                // Add more fields as needed

                peopleList.replace(firstName, p);
                return p;
            } else {
                throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Pet not found");
            }
        }

        // THIS IS THE DELETE OPERATION
        // Springboot gets the PATHVARIABLE from the URL
        // We return the entire list -- converted to JSON
        // in this case because of @ResponseBody
        // Note: To DELETE we use delete method

        @DeleteMapping("/people/{firstName}")
        public HashMap<String, Pet> deletePetFirst(@PathVariable String firstName) {
            peopleList.remove(firstName);
            return peopleList;
        }


    }



