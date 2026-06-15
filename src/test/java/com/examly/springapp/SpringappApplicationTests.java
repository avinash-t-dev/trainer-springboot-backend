package com.examly.springapp;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class SpringappApplicationTests {

    private String coordinatorToken;
    private String managerToken;

    @Autowired
    private TestRestTemplate restTemplate;

    @Autowired
    private ObjectMapper objectMapper;

    private HttpHeaders createHeaders() {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        return headers;
    }

    @Test
    @Order(1)
    void backend_testRegisterManager() {
        String requestBody = "{\"userId\": 1,\"email\": \"demoadmin@gmail.com\", \"password\": \"admin@1234\", \"username\": \"admin123\", \"userRole\": \"Manager\", \"mobileNumber\": \"9876543210\"}";
        ResponseEntity<String> response = restTemplate.postForEntity("/api/register",
                new HttpEntity<>(requestBody, createHeaders()), String.class);

        Assertions.assertEquals(HttpStatus.CREATED, response.getStatusCode());
    }

    @Test
    @Order(2)
    void backend_testRegisterCoordinator() {
        String requestBody = "{\"userId\": 2,\"email\": \"demouser@gmail.com\", \"password\": \"user@1234\", \"username\": \"user123\", \"userRole\": \"Coordinator\", \"mobileNumber\": \"1122334455\"}";
        ResponseEntity<String> response = restTemplate.postForEntity("/api/register",
                new HttpEntity<>(requestBody, createHeaders()), String.class);

        Assertions.assertEquals(HttpStatus.CREATED, response.getStatusCode());
    }

    @Test
    @Order(3)
    void backend_testLoginManager() throws Exception {
        String requestBody = "{\"email\": \"demoadmin@gmail.com\", \"password\": \"admin@1234\"}";

        ResponseEntity<String> response = restTemplate.postForEntity("/api/login",
                new HttpEntity<>(requestBody, createHeaders()), String.class);

        Assertions.assertNotNull(response.getBody(), "Response body is null!");
        JsonNode responseBody = objectMapper.readTree(response.getBody());
        String token = responseBody.get("token").asText();
        managerToken = token;

        Assertions.assertEquals(HttpStatus.OK, response.getStatusCode());
        Assertions.assertNotNull(token);
    }

    @Test
    @Order(4)
    void backend_testLoginCoordinator() throws Exception {
        String requestBody = "{\"email\": \"demouser@gmail.com\", \"password\": \"user@1234\"}";

        ResponseEntity<String> response = restTemplate.postForEntity("/api/login",
                new HttpEntity<>(requestBody, createHeaders()), String.class);

        JsonNode responseBody = objectMapper.readTree(response.getBody());
        String token = responseBody.get("token").asText();
        coordinatorToken = token;

        Assertions.assertEquals(HttpStatus.OK, response.getStatusCode());
        Assertions.assertNotNull(token);
    }

@Test
@Order(5)
void backend_testAddTrainerWithRoleValidation() throws Exception {
    Assertions.assertNotNull(managerToken, "Manager token should not be null");
    Assertions.assertNotNull(coordinatorToken, "Coordinator token should not be null");

    String requestBody = "{"
        + "\"name\": \"John Doe\","
        + "\"email\": \"john.doe@example.com\","
        + "\"phone\": \"9876543210\","
        + "\"expertise\": \"Java\","
        + "\"experience\": \"5 years\","
        + "\"certification\": \"Oracle Certified\","
        + "\"resume\": \"sample-base64-resume==\","
        + "\"joiningDate\": \"2023-09-01\","
        + "\"status\": \"Active\""
        + "}";

    // Try with Coordinator (should be ALLOWED)
    HttpHeaders coordinatorHeaders = createHeaders();
    coordinatorHeaders.set("Authorization", "Bearer " + coordinatorToken);
    HttpEntity<String> coordinatorRequest = new HttpEntity<>(requestBody, coordinatorHeaders);

    ResponseEntity<String> coordinatorResponse = restTemplate.exchange(
        "/api/trainer", HttpMethod.POST, coordinatorRequest, String.class);

    JsonNode coordinatorJson = objectMapper.readTree(coordinatorResponse.getBody());

    System.out.println(coordinatorResponse.getStatusCode() + " Status code for Coordinator adding trainer");
    Assertions.assertEquals(HttpStatus.CREATED, coordinatorResponse.getStatusCode());
    Assertions.assertEquals("John Doe", coordinatorJson.get("name").asText());
    Assertions.assertEquals("john.doe@example.com", coordinatorJson.get("email").asText());
    Assertions.assertEquals("Java", coordinatorJson.get("expertise").asText());

    // Try with Manager (should be FORBIDDEN)
    HttpHeaders managerHeaders = createHeaders();
    managerHeaders.set("Authorization", "Bearer " + managerToken);
    HttpEntity<String> managerRequest = new HttpEntity<>(requestBody, managerHeaders);

    ResponseEntity<String> managerResponse = restTemplate.exchange(
        "/api/trainer", HttpMethod.POST, managerRequest, String.class);

    System.out.println(managerResponse.getStatusCode() + " Status code for Manager trying to add trainer");
    Assertions.assertEquals(HttpStatus.FORBIDDEN, managerResponse.getStatusCode());
}

  
    @Test
    @Order(6)
    void backend_testGetTrainerById_AccessibleByManagerAndCoordinator() throws Exception {
        Long trainerId = 1L;

        Assertions.assertNotNull(managerToken, "Manager token should not be null");
        Assertions.assertNotNull(coordinatorToken, "Coordinator token should not be null");

        HttpHeaders managerHeaders = createHeaders();
        managerHeaders.set("Authorization", "Bearer " + managerToken);
        HttpEntity<Void> managerRequest = new HttpEntity<>(managerHeaders);

        ResponseEntity<String> managerResponse = restTemplate.exchange(
            "/api/trainer/" + trainerId, HttpMethod.GET, managerRequest, String.class);

        JsonNode managerJson = objectMapper.readTree(managerResponse.getBody());

        System.out.println(managerResponse.getStatusCode() + " Status code for Manager fetching trainer");
        Assertions.assertEquals(HttpStatus.OK, managerResponse.getStatusCode());
        Assertions.assertEquals(trainerId, managerJson.get("trainerId").asLong());

        HttpHeaders coordinatorHeaders = createHeaders();
        coordinatorHeaders.set("Authorization", "Bearer " + coordinatorToken);
        HttpEntity<Void> coordinatorRequest = new HttpEntity<>(coordinatorHeaders);

        ResponseEntity<String> coordinatorResponse = restTemplate.exchange(
            "/api/trainer/" + trainerId, HttpMethod.GET, coordinatorRequest, String.class);

        JsonNode coordinatorJson = objectMapper.readTree(coordinatorResponse.getBody());

        System.out.println(coordinatorResponse.getStatusCode() + " Status code for Coordinator fetching trainer");
        Assertions.assertEquals(HttpStatus.OK, coordinatorResponse.getStatusCode());
        Assertions.assertEquals(trainerId, coordinatorJson.get("trainerId").asLong());
    }


@Test
@Order(7)
void backend_testGetAllTrainers_AccessibleByManagerAndCoordinator() throws Exception {
    Assertions.assertNotNull(managerToken, "Manager token should not be null");
    Assertions.assertNotNull(coordinatorToken, "Coordinator token should not be null");

    // --- Manager tries to get all trainers ---
    HttpHeaders managerHeaders = createHeaders();
    managerHeaders.set("Authorization", "Bearer " + managerToken);
    HttpEntity<Void> managerRequest = new HttpEntity<>(managerHeaders);

    ResponseEntity<String> managerResponse = restTemplate.exchange(
        "/api/trainer", HttpMethod.GET, managerRequest, String.class);

    System.out.println(managerResponse.getStatusCode() + " Status code for Manager fetching all trainers");
    Assertions.assertEquals(HttpStatus.OK, managerResponse.getStatusCode());
    Assertions.assertTrue(managerResponse.getBody().contains("trainerId"), "Response body should contain 'trainerId'");

    // --- Coordinator tries to get all trainers ---
    HttpHeaders coordinatorHeaders = createHeaders();
    coordinatorHeaders.set("Authorization", "Bearer " + coordinatorToken);
    HttpEntity<Void> coordinatorRequest = new HttpEntity<>(coordinatorHeaders);

    ResponseEntity<String> coordinatorResponse = restTemplate.exchange(
        "/api/trainer", HttpMethod.GET, coordinatorRequest, String.class);

    System.out.println(coordinatorResponse.getStatusCode() + " Status code for Coordinator fetching all trainers");
    Assertions.assertEquals(HttpStatus.OK, coordinatorResponse.getStatusCode());
    Assertions.assertTrue(coordinatorResponse.getBody().contains("trainerId"), "Response body should contain 'trainerId'");
}

@Test
@Order(8)
void backend_testUpdateTrainer_AccessibleByCoordinatorOnly() throws Exception {
    Long trainerId = 1L;  // Use an existing trainer ID or create one before running the test

    // Ensure tokens are available
    Assertions.assertNotNull(coordinatorToken, "Coordinator token should not be null");
    Assertions.assertNotNull(managerToken, "Manager token should not be null");

    // Trainer update request body
    String requestBody = "{"
        + "\"name\": \"John Doe Updated\","
        + "\"email\": \"john.doe.updated@example.com\","
        + "\"phone\": \"9876543210\","
        + "\"expertise\": \"Advanced Java\","
        + "\"experience\": \"6 years\","
        + "\"certification\": \"Oracle Certified Updated\","
        + "\"resume\": \"updated-base64-resume==\","
        + "\"joiningDate\": \"2023-09-01\","
        + "\"status\": \"Active\""
        + "}";

    // --- Coordinator tries to update trainer (should succeed) ---
    HttpHeaders coordinatorHeaders = createHeaders();
    coordinatorHeaders.set("Authorization", "Bearer " + coordinatorToken);
    HttpEntity<String> coordinatorRequest = new HttpEntity<>(requestBody, coordinatorHeaders);

    ResponseEntity<String> coordinatorResponse = restTemplate.exchange(
        "/api/trainer/" + trainerId, HttpMethod.PUT, coordinatorRequest, String.class);

    JsonNode coordinatorJson = objectMapper.readTree(coordinatorResponse.getBody());

    System.out.println(coordinatorResponse.getStatusCode() + " Status code for Coordinator updating trainer");
    Assertions.assertEquals(HttpStatus.OK, coordinatorResponse.getStatusCode());
    Assertions.assertEquals("John Doe Updated", coordinatorJson.get("name").asText());
    Assertions.assertEquals("john.doe.updated@example.com", coordinatorJson.get("email").asText());

    // --- Manager tries to update trainer (should be forbidden) ---
    HttpHeaders managerHeaders = createHeaders();
    managerHeaders.set("Authorization", "Bearer " + managerToken);
    HttpEntity<String> managerRequest = new HttpEntity<>(requestBody, managerHeaders);

    ResponseEntity<String> managerResponse = restTemplate.exchange(
        "/api/trainer/" + trainerId, HttpMethod.PUT, managerRequest, String.class);

    System.out.println(managerResponse.getStatusCode() + " Status code for Manager trying to update trainer");
    Assertions.assertEquals(HttpStatus.FORBIDDEN, managerResponse.getStatusCode());
}


@Test
@Order(9)
void backend_testAddRequirement_AccessibleByManagerOnly() throws Exception {
    // Ensure tokens are available
    Assertions.assertNotNull(managerToken, "Manager token should not be null");
    Assertions.assertNotNull(coordinatorToken, "Coordinator token should not be null");

    // Updated requirement add request body
    String requestBody = "{"
        + "\"requirementId\": 1,"
        + "\"title\": \"Java Developer\","
        + "\"description\": \"Looking for an experienced Java Developer to work on an enterprise-level application.\","
        + "\"department\": \"IT\","
        + "\"postedDate\": \"2025-05-07\","
        + "\"status\": \"Open\","
        + "\"duration\": \"6 months\","
        + "\"mode\": \"Remote\","
        + "\"location\": \"Remote\","
        + "\"skillLevel\": \"Advanced\","
        + "\"budget\": 10000.0,"
        + "\"priority\": \"High\","
        + "\"trainer\": {"
        + "\"trainerId\": 1"
        + "}"
        + "}";

    // --- Manager tries to add requirement (should succeed) ---
    HttpHeaders managerHeaders = createHeaders();
    managerHeaders.set("Authorization", "Bearer " + managerToken);
    HttpEntity<String> managerRequest = new HttpEntity<>(requestBody, managerHeaders);

    ResponseEntity<String> managerResponse = restTemplate.exchange(
        "/api/requirement", HttpMethod.POST, managerRequest, String.class);

    JsonNode managerJson = objectMapper.readTree(managerResponse.getBody());

    System.out.println(managerResponse.getStatusCode() + " Status code for Manager adding requirement");
    Assertions.assertEquals(HttpStatus.CREATED, managerResponse.getStatusCode());
    Assertions.assertEquals("Java Developer", managerJson.get("title").asText());
    Assertions.assertEquals("IT", managerJson.get("department").asText());
    Assertions.assertEquals(1, managerJson.get("trainer").get("trainerId").asInt());

    // --- Coordinator tries to add requirement (should be forbidden) ---
    HttpHeaders coordinatorHeaders = createHeaders();
    coordinatorHeaders.set("Authorization", "Bearer " + coordinatorToken);
    HttpEntity<String> coordinatorRequest = new HttpEntity<>(requestBody, coordinatorHeaders);

    ResponseEntity<String> coordinatorResponse = restTemplate.exchange(
        "/api/requirement", HttpMethod.POST, coordinatorRequest, String.class);

    System.out.println(coordinatorResponse.getStatusCode() + " Status code for Coordinator trying to add requirement");
    Assertions.assertEquals(HttpStatus.FORBIDDEN, coordinatorResponse.getStatusCode());
}

@Test
@Order(10)
void backend_testViewRequirementById_ManagerOnlyAccess() throws Exception {
    Long requirementId = 1L; // Assuming this requirement exists

    Assertions.assertNotNull(managerToken, "Manager token should not be null");
    Assertions.assertNotNull(coordinatorToken, "Coordinator token should not be null");

    // --- Manager access (should succeed) ---
    HttpHeaders managerHeaders = createHeaders();
    managerHeaders.set("Authorization", "Bearer " + managerToken);
    HttpEntity<Void> managerRequest = new HttpEntity<>(managerHeaders);

    ResponseEntity<String> managerResponse = restTemplate.exchange(
        "/api/requirement/" + requirementId, HttpMethod.GET, managerRequest, String.class);

    JsonNode managerJson = objectMapper.readTree(managerResponse.getBody());

    System.out.println(managerResponse.getStatusCode() + " Status code for Manager fetching requirement");
    Assertions.assertEquals(HttpStatus.OK, managerResponse.getStatusCode());
    Assertions.assertEquals(requirementId, managerJson.get("requirementId").asLong());

    // --- Coordinator access (should be forbidden) ---
    HttpHeaders coordinatorHeaders = createHeaders();
    coordinatorHeaders.set("Authorization", "Bearer " + coordinatorToken);
    HttpEntity<Void> coordinatorRequest = new HttpEntity<>(coordinatorHeaders);

    ResponseEntity<String> coordinatorResponse = restTemplate.exchange(
        "/api/requirement/" + requirementId, HttpMethod.GET, coordinatorRequest, String.class);

    System.out.println(coordinatorResponse.getStatusCode() + " Status code for Coordinator fetching requirement");
    Assertions.assertEquals(HttpStatus.FORBIDDEN, coordinatorResponse.getStatusCode());
}


@Test
@Order(11)
void backend_testGetAllRequirementsWithRoleValidation() throws Exception {
    // Ensure tokens are available
    Assertions.assertNotNull(managerToken, "Manager token should not be null");
    Assertions.assertNotNull(coordinatorToken, "Coordinator token should not be null");

    String url = "/api/requirement";

    // Test with Manager token (Expecting 200 OK)
    HttpHeaders managerHeaders = createHeaders();
    managerHeaders.set("Authorization", "Bearer " + managerToken);
    HttpEntity<Void> managerRequest = new HttpEntity<>(managerHeaders);

    ResponseEntity<String> managerResponse = restTemplate.exchange(url, HttpMethod.GET, managerRequest, String.class);
    Assertions.assertEquals(HttpStatus.OK, managerResponse.getStatusCode());

    // Test with Coordinator token (Expecting 200 OK)
    HttpHeaders coordinatorHeaders = createHeaders();
    coordinatorHeaders.set("Authorization", "Bearer " + coordinatorToken);
    HttpEntity<Void> coordinatorRequest = new HttpEntity<>(coordinatorHeaders);

    ResponseEntity<String> coordinatorResponse = restTemplate.exchange(url, HttpMethod.GET, coordinatorRequest, String.class);
    Assertions.assertEquals(HttpStatus.OK, coordinatorResponse.getStatusCode());
}
@Test
@Order(12)
void backend_testAddFeedback() throws Exception {
    Assertions.assertNotNull(managerToken, "Manager token should not be null");
    Assertions.assertNotNull(coordinatorToken, "Coordinator token should not be null");

    String requestBody = "{" +
    "\"feedbackText\": \"Great session, very informative!\", " +
    "\"date\": \"2025-05-07\", " +
    "\"user\": {\"userId\": 1}, " +
    "\"trainer\": {\"trainerId\": 1}, " +
    "\"category\": \"Session Quality\"" +
    "}";


    //  Manager should be able to add feedback
    HttpHeaders managerHeaders = createHeaders();
    managerHeaders.set("Authorization", "Bearer " + managerToken);
    HttpEntity<String> managerRequest = new HttpEntity<>(requestBody, managerHeaders);
    ResponseEntity<String> managerResponse = restTemplate.exchange("/api/feedback", HttpMethod.POST, managerRequest, String.class);
    Assertions.assertEquals(HttpStatus.CREATED, managerResponse.getStatusCode(), "Manager should be able to add feedback");

    //  Coordinator should NOT be able to add feedback
    HttpHeaders coordinatorHeaders = createHeaders();
    coordinatorHeaders.set("Authorization", "Bearer " + coordinatorToken);
    HttpEntity<String> coordinatorRequest = new HttpEntity<>(requestBody, coordinatorHeaders);
    ResponseEntity<String> coordinatorResponse = restTemplate.exchange("/api/feedback", HttpMethod.POST, coordinatorRequest, String.class);
    Assertions.assertEquals(HttpStatus.FORBIDDEN, coordinatorResponse.getStatusCode(), "Coordinator should NOT be able to add feedback");
}

@Test
@Order(13)
void backend_testGetAllFeedback() throws Exception {
    Assertions.assertNotNull(managerToken, "Manager token should not be null");
    Assertions.assertNotNull(coordinatorToken, "Coordinator token should not be null");

    //  Coordinator should be able to get all feedback
    HttpHeaders coordinatorHeaders = createHeaders();
    coordinatorHeaders.set("Authorization", "Bearer " + coordinatorToken);
    HttpEntity<Void> coordinatorRequest = new HttpEntity<>(coordinatorHeaders);
    ResponseEntity<String> coordinatorResponse = restTemplate.exchange("/api/feedback", HttpMethod.GET, coordinatorRequest, String.class);
    Assertions.assertEquals(HttpStatus.OK, coordinatorResponse.getStatusCode(), "Coordinator should be able to view all feedback");

    //  Manager should be able to get all feedback
    HttpHeaders managerHeaders = createHeaders();
    managerHeaders.set("Authorization", "Bearer " + managerToken);
    HttpEntity<Void> managerRequest = new HttpEntity<>(managerHeaders);
    ResponseEntity<String> managerResponse = restTemplate.exchange("/api/feedback", HttpMethod.GET, managerRequest, String.class);
    Assertions.assertEquals(HttpStatus.OK, managerResponse.getStatusCode(), "Manager should be able to view all feedback");
}

@Test
@Order(14)
void backend_testGetFeedbackByUserId() throws Exception {
    Assertions.assertNotNull(managerToken, "Manager token should not be null");
    Assertions.assertNotNull(coordinatorToken, "Coordinator token should not be null");

    //  Manager should be able to get their own feedback
    HttpHeaders managerHeaders = createHeaders();
    managerHeaders.set("Authorization", "Bearer " + managerToken);
    HttpEntity<Void> managerRequest = new HttpEntity<>(managerHeaders);
    ResponseEntity<String> managerResponse = restTemplate.exchange("/api/feedback/user/2", HttpMethod.GET, managerRequest, String.class);
    Assertions.assertEquals(HttpStatus.OK, managerResponse.getStatusCode(), "Manager should be able to get their own feedback");

    //  Coordinator should NOT be able to get user feedback
    HttpHeaders coordinatorHeaders = createHeaders();
    coordinatorHeaders.set("Authorization", "Bearer " + coordinatorToken);
    HttpEntity<Void> coordinatorRequest = new HttpEntity<>(coordinatorHeaders);
    ResponseEntity<String> coordinatorResponse = restTemplate.exchange("/api/feedback/user/2", HttpMethod.GET, coordinatorRequest, String.class);
    Assertions.assertEquals(HttpStatus.FORBIDDEN, coordinatorResponse.getStatusCode(), "Coordinator should NOT be able to get user feedback");
}


@Test
@Order(15)
void backend_testGetFeedbackById() throws Exception {
    Assertions.assertNotNull(managerToken, "Manager token should not be null");
    Assertions.assertNotNull(coordinatorToken, "Coordinator token should not be null");

    //  Manager should be able to get feedback by ID
    HttpHeaders managerHeaders = createHeaders();
    managerHeaders.set("Authorization", "Bearer " + managerToken);
    HttpEntity<Void> managerRequest = new HttpEntity<>(managerHeaders);
    ResponseEntity<String> managerResponse = restTemplate.exchange("/api/feedback/1", HttpMethod.GET, managerRequest, String.class);
    Assertions.assertEquals(HttpStatus.OK, managerResponse.getStatusCode(), "Manager should be able to get feedback by ID");

    //  Coordinator should be able to get feedback by ID
    HttpHeaders coordinatorHeaders = createHeaders();
    coordinatorHeaders.set("Authorization", "Bearer " + coordinatorToken);
    HttpEntity<Void> coordinatorRequest = new HttpEntity<>(coordinatorHeaders);
    ResponseEntity<String> coordinatorResponse = restTemplate.exchange("/api/feedback/1", HttpMethod.GET, coordinatorRequest, String.class);
    Assertions.assertEquals(HttpStatus.OK, coordinatorResponse.getStatusCode(), "Coordinator should be able to get feedback by ID");
}

}
