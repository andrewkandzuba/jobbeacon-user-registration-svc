package ai.jobbeacon.api;

import ai.jobbeacon.model.User;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(SpringExtension.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
class UserRegistrationControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void usersPost() throws Exception {
        User user = new User();
        user.setUsername("testUser");
        user.setPassword("testPassword");
        user.setFirstName("testFirstName");
        user.setLastName("testLastName");
        user.setEmail("testEmail@mail.com");
        user.setMailingAddress("testAddress");

        String userJson = objectMapper.writeValueAsString(user);

        mockMvc.perform(post("/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(userJson))
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated())
                .andExpect(header().string("Location", Matchers.containsString("/users/testUser")));
    }

    @Test
    void usersPostRequestValidation() throws Exception {
        User user = new User();
        postUserAndExpectResponseCode(user, HttpStatus.BAD_REQUEST);

        user.setUsername("testUser");
        postUserAndExpectResponseCode(user, HttpStatus.BAD_REQUEST);

        user.setPassword("testPassword");
        postUserAndExpectResponseCode(user, HttpStatus.BAD_REQUEST);

        user.setFirstName("testFirstName");
        postUserAndExpectResponseCode(user, HttpStatus.BAD_REQUEST);

        user.setLastName("testLastName");
        postUserAndExpectResponseCode(user, HttpStatus.BAD_REQUEST);

        user.setEmail("testEmail@mail.com");
        postUserAndExpectResponseCode(user, HttpStatus.BAD_REQUEST);

        user.setMailingAddress("testMailingAddress");
        postUserAndExpectResponseCode(user, HttpStatus.CREATED);
    }

    @Test
    void usersUsernameGet() throws Exception {
        String userName = "testUserName";
        mockMvc.perform(get(STR."/users/\{userName}")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.username").value(userName))
                .andExpect(jsonPath("$.password").value("testPassword"))
                .andExpect(jsonPath("$.firstName").value("testFirstName"))
                .andExpect(jsonPath("$.lastName").value("testLastName"))
                .andExpect(jsonPath("$.email").value("testUser@mail.com"))
                .andExpect(jsonPath("$.mailingAddress").value("testMailingAddress"));
    }

    @Test
    void usersUsernamePut() throws Exception {
        User user = new User();
        user.setUsername("testUser");
        user.setPassword("testPassword");
        user.setFirstName("testFirstName");
        user.setLastName("testLastName");
        user.setEmail("testEmail@mail.com");
        user.setMailingAddress("testAddress");

        String userJson = objectMapper.writeValueAsString(user);

        mockMvc.perform(put(STR."/users/\{user.getUsername()}")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(userJson))
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test
    void usersPutRequestValidation() throws Exception {
        User user = new User();
        putUserAndExpectResponseCode(user, HttpStatus.BAD_REQUEST);

        user.setUsername("testUser");
        putUserAndExpectResponseCode(user, HttpStatus.BAD_REQUEST);

        user.setPassword("testPassword");
        putUserAndExpectResponseCode(user, HttpStatus.BAD_REQUEST);

        user.setFirstName("testFirstName");
        putUserAndExpectResponseCode(user, HttpStatus.BAD_REQUEST);

        user.setLastName("testLastName");
        putUserAndExpectResponseCode(user, HttpStatus.BAD_REQUEST);

        user.setEmail("testEmail@mail.com");
        putUserAndExpectResponseCode(user, HttpStatus.BAD_REQUEST);

        user.setMailingAddress("testMailingAddress");
        putUserAndExpectResponseCode(user, HttpStatus.OK);
    }

    @Test
    void usersUsernameDelete() throws Exception {
        mockMvc.perform(delete("/users/testUser"))
                .andExpect(status().isNoContent());
    }

    private void postUserAndExpectResponseCode(User user, HttpStatus httpStatus) throws Exception {
        String userJson = objectMapper.writeValueAsString(user);

        mockMvc.perform(post("/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(userJson))
                .andExpect(status().is(httpStatus.value()));
    }

    private void putUserAndExpectResponseCode(User user, HttpStatus httpStatus) throws Exception {
        String userJson = objectMapper.writeValueAsString(user);

        mockMvc.perform(put(STR."/users/\{user.getUsername()}")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(userJson))
                .andExpect(status().is(httpStatus.value()));
    }
}