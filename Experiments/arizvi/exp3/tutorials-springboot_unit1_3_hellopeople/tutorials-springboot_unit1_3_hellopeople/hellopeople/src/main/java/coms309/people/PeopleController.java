package coms309.people;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.server.ResponseStatusException;


import java.util.HashMap;

/**
 * Controller used to showcase Create and Read from a LIST
 *
 * @author Vivek Bengre
 */

@RestController
public class PeopleController {

    // Note that there is only ONE instance of PeopleController in 
    // Springboot system.
    HashMap<String, Person> peopleList = new  HashMap<>();

    //CRUDL (create/read/update/delete/list)
    // use POST, GET, PUT, DELETE, GET methods for CRUDL

    // THIS IS THE LIST OPERATION
    // gets all the people in the list and returns it in JSON format
    // This controller takes no input. 
    // Springboot automatically converts the list to JSON format 
    // in this case because of @ResponseBody
    // Note: To LIST, we use the GET method
    @GetMapping("/people")
    public  HashMap<String,Person> getAllPersons() {
        return peopleList;
    }

    // THIS IS THE CREATE OPERATION
    // springboot automatically converts JSON input into a person object and 
    // the method below enters it into the list.
    // It returns a string message in THIS example.
    // in this case because of @ResponseBody
    // Note: To CREATE we use POST method
    @PostMapping("/people")
    public  String createPerson(@RequestBody Person person) {
        System.out.println(person);
        peopleList.put(person.getFirstName(), person);
        peopleList.put(person.getLastName(), person);
        peopleList.put(person.getAddress(), person);
        peopleList.put(person.getTelephone(), person);
        if(person.getAge() >= 30 && person.getAge() < 40){
            return "New person "+ person.getFirstName() + " " + person.getLastName() + " Saved with address " + person.getAddress() + " and number: " + person.getTelephone() + ". You're in your 30's? How's your back?";
        }
        else if(person.getAge() >= 40 && person.getAge() < 50){
            return "New person "+ person.getFirstName() + " " + person.getLastName() + " Saved with address " + person.getAddress() + " and number: " + person.getTelephone() + ". You're in your 40's? Don't look a day over 35!";

        }
        else if(person.getAge() >= 50 && person.getAge() < 60) {
            return "New person " + person.getFirstName() + " " + person.getLastName() + " Saved with address " + person.getAddress() + " and number: " + person.getTelephone() + ". You're in your 50's? That's half a century!";

        }
        else{
            return "New person "+ person.getFirstName() + " " + person.getLastName() + " Saved with address " + person.getAddress() + " and number: " + person.getTelephone() + ".";

        }
    }


    // THIS IS THE READ OPERATION
    // Springboot gets the PATHVARIABLE from the URL
    // We extract the person from the HashMap.
    // springboot automatically converts Person to JSON format when we return it
    // in this case because of @ResponseBody
    // Note: To READ we use GET method
    @GetMapping("/people/{firstName}")
    public Person getPerson(@PathVariable String firstName) {
        Person p = peopleList.get(firstName);
        return p;
    }

    // THIS IS THE UPDATE OPERATION
    // We extract the person from the HashMap and modify it.
    // Springboot automatically converts the Person to JSON format
    // Springboot gets the PATHVARIABLE from the URL
    // Here we are returning what we sent to the method
    // in this case because of @ResponseBody
    // Note: To UPDATE we use PUT method

//    @PutMapping("/people/{firstName}")
//    public Person updatePerson(@PathVariable String firstName, @RequestBody Person p) {
//        peopleList.replace(firstName, p);
//        return peopleList.get(firstName);
//    }

    @PutMapping("/people/{firstName}")
    public Person updatePerson(@PathVariable String firstName, @RequestBody Person p) {
        if (peopleList.containsKey(firstName)) {
            System.out.println("Updating person: " + firstName);
            peopleList.replace(firstName, p);
            return peopleList.get(firstName);
        } else {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Person not found");
        }
    }

//    update method that takes both the first name and the last name in the path and updates the person based on those values
    @PutMapping("/people/{firstName}/{lastName}")
    public Person updatePersonByFullName(
            @PathVariable String firstName,
            @PathVariable String lastName,
            @RequestBody Person p) {

        if (peopleList.containsKey(firstName) && peopleList.get(firstName).getLastName().equals(lastName)) {
            peopleList.replace(firstName, p);
            return peopleList.get(firstName);
        } else {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Person not found with given first and last name");
        }
    }

    

//    method that allows updating only certain fields of a person’s data (e.g., just the address or telephone number
    @PutMapping("/people/{firstName}/updateAttributes")
    public Person updatePersonAttributes(@PathVariable String firstName, @RequestBody HashMap<String, Object> updates) {
        if (peopleList.containsKey(firstName)) {
            Person p = peopleList.get(firstName);

            // Update specific attributes if they exist in the request body
            if (updates.containsKey("lastName")) {
                p.setLastName((String) updates.get("lastName"));
            }
            if (updates.containsKey("address")) {
                p.setAddress((String) updates.get("address"));
            }
            if (updates.containsKey("telephone")) {
                p.setTelephone((String) updates.get("telephone"));
            }
            // Add more fields as needed

            peopleList.replace(firstName, p);
            return p;
        } else {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Person not found");
        }
    }

    // THIS IS THE DELETE OPERATION
    // Springboot gets the PATHVARIABLE from the URL
    // We return the entire list -- converted to JSON
    // in this case because of @ResponseBody
    // Note: To DELETE we use delete method
    
    @DeleteMapping("/people/{firstName}")
    public HashMap<String, Person> deletePersonFirst(@PathVariable String firstName) {
        peopleList.remove(firstName);
        return peopleList;
    }


}

