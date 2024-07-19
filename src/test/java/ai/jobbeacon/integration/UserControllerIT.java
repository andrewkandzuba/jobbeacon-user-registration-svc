package ai.jobbeacon.integration;

import ai.jobbeacon.model.User;
import ai.jobbeacon.persistence.UserRepository;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.MySQLContainer;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("mysql")
public class UserControllerIT {

    private static final String BASE_PATH = "users";
    private static final String BASE_PROTOCOL = "http";
    private static final String BASE_HOST = "localhost";

    private static final MySQLContainer<?> mySQLContainer = new MySQLContainer<>("mysql:8.3.0");
    private static final ObjectMapper objectMapper = new ObjectMapper();

    @BeforeAll
    static void beforeAll() {
        mySQLContainer.start();
    }

    @AfterAll
    static void afterAll() {
        mySQLContainer.stop();
    }

    @DynamicPropertySource
    static void configureProperties(DynamicPropertyRegistry registry) {
        registry.add("spring.datasource.url", mySQLContainer::getJdbcUrl);
        registry.add("spring.datasource.username", mySQLContainer::getUsername);
        registry.add("spring.datasource.password", mySQLContainer::getPassword);
    }

    @LocalServerPort
    private int port;

    @Autowired
    private UserRepository userRepository;

    @BeforeEach
    void setUp() {
        RestAssured.baseURI = String.format("%s://%s:%d/%s", BASE_PROTOCOL, BASE_HOST, port, BASE_PATH);
        userRepository.deleteAll();
    }

    @Test
    public void createAndReadUser() throws JsonProcessingException {
        // Create a user
        var userName = "testUser";
        var user = new User();
        user.setUsername(userName);
        user.setEmail("testEmail@mail.org");
        user.setFirstName("testFirstName");
        user.setLastName("testLastName");
        user.setPhone("111-111-1111");
        user.setStreet("testStreet");
        user.setCity("testCity");
        user.setState("ST");
        user.setZip("11111");
        user.setCountry("testCountry");

        Response response = given()
                .contentType(ContentType.JSON)
                .accept(ContentType.ANY)
                .body(objectMapper.writeValueAsString(user))
                .body(user)
                .post();
        response
                .then()
                .statusCode(HttpStatus.CREATED.value());
        String userLocation = response.getHeader(HttpHeaders.LOCATION);

        response = given()
                .contentType(ContentType.JSON)
                .accept(ContentType.ANY)
                .get(userLocation);

        response
                .then()
                .statusCode(HttpStatus.OK.value())
                                .assertThat()
                                .body("username", equalTo(userName))
                .body("email", equalTo(user.getEmail()))
                .body("firstName", equalTo(user.getFirstName()))
                .body("lastName", equalTo(user.getLastName()))
                .body("phone", equalTo(user.getPhone()))
                .body("street", equalTo(user.getStreet()))
                .body("city", equalTo(user.getCity()))
                .body("state", equalTo(user.getState()))
                .body("zip", equalTo(user.getZip()))
                .body("country", equalTo(user.getCountry()));
    }

    @Test
    public void attemptToCreateDuplicateUser() throws JsonProcessingException {
        // Create a user
        var userName = "testUser";
        var user = new User();
        user.setUsername(userName);
        user.setEmail("testEmail@mail.org");
        user.setFirstName("testFirstName");
        user.setLastName("testLastName");
        user.setPhone("111-111-1111");
        user.setStreet("testStreet");
        user.setCity("testCity");
        user.setState("ST");
        user.setZip("11111");
        user.setCountry("testCountry");

        Response response = given()
                .contentType(ContentType.JSON)
                .accept(ContentType.ANY)
                .body(objectMapper.writeValueAsString(user))
                .body(user)
                .post();
        response
                .then()
                .statusCode(HttpStatus.CREATED.value());

        response = given()
                .contentType(ContentType.JSON)
                .accept(ContentType.ANY)
                .body(objectMapper.writeValueAsString(user))
                .body(user)
                .post();
        response
                .then()
                .statusCode(HttpStatus.CONFLICT.value());
    }

    @Test
    public void attemptToReadNonExistentUser() {
        given()
                .contentType(ContentType.JSON)
                .accept(ContentType.ANY)
                .get("/users/nonExistentUser")
                .then()
                .statusCode(HttpStatus.NOT_FOUND.value());
    }

    @Test
    public void updateExistingUser() throws JsonProcessingException {
        // Create a user
        var userName = "testUser";
        var user = new User();
        user.setUsername(userName);
        user.setEmail("testEmail@mail.org");
        user.setFirstName("testFirstName");
        user.setLastName("testLastName");
        user.setPhone("111-111-1111");
        user.setStreet("testStreet");
        user.setCity("testCity");
        user.setState("ST");
        user.setZip("11111");
        user.setCountry("testCountry");

        Response response = given()
                .contentType(ContentType.JSON)
                .accept(ContentType.ANY)
                .body(objectMapper.writeValueAsString(user))
                .body(user)
                .post();
        response
                .then()
                .statusCode(HttpStatus.CREATED.value());

        // Update the user
        var userUpdate = new User();
        userUpdate.setUsername(userName);
        userUpdate.setEmail("testEmail@mail.org");
        userUpdate.setFirstName("testFirstName");
        userUpdate.setLastName("testLastName");
        userUpdate.setPhone("111-111-1111");
        userUpdate.setStreet("testStreet");
        userUpdate.setCity("testCity");
        userUpdate.setState("ST");
        userUpdate.setZip("11111");
        userUpdate.setCountry("testCountry");
    }
}
