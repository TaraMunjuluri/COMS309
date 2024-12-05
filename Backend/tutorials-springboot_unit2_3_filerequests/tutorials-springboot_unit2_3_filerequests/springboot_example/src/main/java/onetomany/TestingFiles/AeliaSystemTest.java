package onetomany.TestingFiles;
import org.testng.annotations.Test; // For the @Test annotation
import static io.restassured.RestAssured.*; // For Rest Assured methods like given(), when(), etc.
import static org.hamcrest.Matchers.*; // For assertions like equalTo() and containsString()

import io.restassured.RestAssured;

public class AeliaSystemTest {


    @Test
    public void testCreateAndRetrieveUser() {
        // Step 1: Create a new user
        String requestBody = "{"
                + "\"username\": \"testuser\","
                + "\"email\": \"testuser@example.com\","
                + "\"password\": \"securePassword123\""
                + "}";

        int userId = given()
                .contentType("application/json")
                .body(requestBody)
                .when()
                .post("/users")
                .then()
                .statusCode(201) // Check that the user was created successfully
                .extract().path("id"); // Extract the ID of the created user

        // Step 2: Retrieve the user and verify details
        given()
                .pathParam("id", userId)
                .when()
                .get("/users/{id}")
                .then()
                .statusCode(200) // Ensure retrieval is successful
                .body("username", equalTo("testuser"))
                .body("email", equalTo("testuser@example.com"));
    }



}
