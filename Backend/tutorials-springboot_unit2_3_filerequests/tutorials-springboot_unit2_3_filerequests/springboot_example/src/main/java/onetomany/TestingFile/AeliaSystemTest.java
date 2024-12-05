package onetomany.TestingFile;

import io.restassured.http.ContentType;
import io.restassured.response.Response;
import org.testng.annotations.Test; // For the @Test annotation
import static io.restassured.RestAssured.*; // For Rest Assured methods like given(), when(), etc.
import static org.hamcrest.Matchers.*; // For assertions like equalTo() and containsString()


import io.restassured.RestAssured;
import org.testng.annotations.BeforeClass;

public class AeliaSystemTest {

    @BeforeClass
    public static void setup() {
        RestAssured.baseURI = "http://localhost";  // Point to localhost
        RestAssured.port = 8081;  // Set the port number to match the Spring Boot app
    }

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


        @Test
        public void testSignupUser() {
            // Create a JSON payload for the user
            String userJson = "{"
                    + "\"username\": \"testuser\","
                    + "\"emailId\": \"testuser@example.com\","
                    + "\"password\": \"password123\""
                    + "}";

            RestAssured
                    .given()
                    .contentType(ContentType.JSON)
                    .body(userJson)
                    .when()
                    .post("http://localhost:8080/signup")
                    .then()
                    .statusCode(201) // Expecting HTTP 201 Created
                    .body("message", equalTo("Signup successful"));
        }

    @Test
    public void testLoginWithInvalidCredentials() {
        // Create a JSON payload for login
        String loginJson = "{"
                + "\"username\": \"nonexistentuser\","
                + "\"password\": \"wrongpassword\""
                + "}";

        RestAssured
                .given()
                .contentType(ContentType.JSON)
                .body(loginJson)
                .when()
                .post("http://localhost:8080/login")
                .then()
                .statusCode(401) // Expecting HTTP 401 Unauthorized
                .body("message", equalTo("Invalid credentials"));
    }

    @Test
    public void testAssignAchievementToUser() {
        // Set the base URI for Rest Assured
        RestAssured.baseURI = "http://localhost:8080";

        // Test data
        int userId = 1;         // Replace with a valid user ID
        int achievementId = 100; // Replace with a valid achievement ID

        // Perform the HTTP POST request to assign an achievement
        Response response = given()
                .contentType("application/json")
                .when()
                .post("/achievements/{userId}/assign/{achievementId}", userId, achievementId)
                .then()
                .statusCode(200) // Expecting 200 OK response
                .body("message", equalTo("Achievement assigned successfully"))
                .extract()
                .response();

        // Log the response for debugging
        System.out.println(response.asString());

        // Additional assertions (if any)
        // Verify achievement assignment in response JSON
        response.then()
                .body("userId", equalTo(userId))
                .body("achievementId", equalTo(achievementId));
    }

}
