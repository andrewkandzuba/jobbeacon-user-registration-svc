package ai.jobbeacon.api;

import ai.jobbeacon.model.User;
import ai.jobbeacon.model.UserEntity;
import ai.jobbeacon.persistence.UserRepository;
import ai.jobbeacon.service.UserService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.setup.MockMvcBuilders.standaloneSetup;

@ExtendWith(MockitoExtension.class)
class UserControllerTest {

    private static final ObjectMapper objectMapper = new ObjectMapper();

    @Mock
    private UserRepository mockUserRepository;
    @InjectMocks
    private UserService mockUserService;
    private MockMvc mockMvc;

    @BeforeEach
    void setUp() {
        mockMvc = standaloneSetup(new UserController(mockUserService)).build();
    }

    @Test
    void createUser() throws Exception {
        var userEntity = new UserEntity();

        userEntity.setId(1000L);
        userEntity.setUsername("testUser");
        userEntity.setEmail("testEmail@mail.com");
        userEntity.setFirstName("testFirstName");
        userEntity.setLastName("testLastName");
        userEntity.setPhone("111-111-1111");
        userEntity.setStreet("testStreet");
        userEntity.setCity("testCity");
        userEntity.setState("TT");
        userEntity.setZip("12345");
        userEntity.setCountry("testCountry");

        Mockito.when(mockUserRepository.save(Mockito.any(UserEntity.class))).thenReturn(userEntity);

        var user = new User(
                userEntity.getUsername(),
                userEntity.getEmail(),
                userEntity.getFirstName(),
                userEntity.getLastName(),
                userEntity.getPhone(),
                userEntity.getStreet(),
                userEntity.getCity(),
                userEntity.getState(),
                userEntity.getZip(),
                userEntity.getCountry()
        );

        String userJson = objectMapper.writeValueAsString(user);

        mockMvc.perform(post("/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(userJson))
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated())
                .andExpect(header().string("Location",
                        Matchers.containsString(String.format("/users/%s", userEntity.getUsername()))));
    }

    @Test
    void attemptToCreateDuplicateUser() throws Exception {
        var userEntity = new UserEntity();

        userEntity.setId(1000L);
        userEntity.setUsername("testUser");
        userEntity.setEmail("testEmail@mail.com");
        userEntity.setFirstName("testFirstName");
        userEntity.setLastName("testLastName");
        userEntity.setPhone("111-111-1111");
        userEntity.setStreet("testStreet");
        userEntity.setCity("testCity");
        userEntity.setState("TT");
        userEntity.setZip("12345");
        userEntity.setCountry("testCountry");

        Mockito.when(mockUserRepository.save(Mockito.any(UserEntity.class))).thenReturn(userEntity);

        var user = new User(
                userEntity.getUsername(),
                userEntity.getEmail(),
                userEntity.getFirstName(),
                userEntity.getLastName(),
                userEntity.getPhone(),
                userEntity.getStreet(),
                userEntity.getCity(),
                userEntity.getState(),
                userEntity.getZip(),
                userEntity.getCountry()
        );

        String userJson = objectMapper.writeValueAsString(user);

        mockMvc.perform(post("/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(userJson))
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated())
                .andExpect(header().string("Location",
                        Matchers.containsString(String.format("/users/%s", userEntity.getUsername()))));
    }

    @Test
    void createUserValidation() throws Exception {
        var userEntity = new UserEntity();
        userEntity.setId(1000L);
        userEntity.setUsername("testUser");
        Mockito.when(mockUserRepository.save(Mockito.any(UserEntity.class))).thenReturn(userEntity);

        var user = new User();
        postUserAndExpectResponseCode(user, HttpStatus.BAD_REQUEST);

        user.setUsername("testUser");
        postUserAndExpectResponseCode(user, HttpStatus.BAD_REQUEST);

        user.setEmail("testEmail@mail.com");
        postUserAndExpectResponseCode(user, HttpStatus.BAD_REQUEST);

        user.setFirstName("testFirstName");
        postUserAndExpectResponseCode(user, HttpStatus.BAD_REQUEST);

        user.setLastName("testLastName");
        postUserAndExpectResponseCode(user, HttpStatus.BAD_REQUEST);

        user.setPhone("111-111-1111");
        postUserAndExpectResponseCode(user, HttpStatus.BAD_REQUEST);

        user.setStreet("testStreet");
        postUserAndExpectResponseCode(user, HttpStatus.BAD_REQUEST);

        user.setCity("testCity");
        postUserAndExpectResponseCode(user, HttpStatus.BAD_REQUEST);

        user.setState("NY");
        postUserAndExpectResponseCode(user, HttpStatus.BAD_REQUEST);

        user.setZip("12345");
        postUserAndExpectResponseCode(user, HttpStatus.BAD_REQUEST);

        user.setCountry("USA");
        postUserAndExpectResponseCode(user, HttpStatus.CREATED);
    }

    @Test
    void findUserByName() throws Exception {
        var userEntity = new UserEntity();

        userEntity.setId(1000L);
        userEntity.setUsername("testUser");
        userEntity.setEmail("testEmail@mail.com");
        userEntity.setFirstName("testFirstName");
        userEntity.setLastName("testLastName");
        userEntity.setPhone("111-111-1111");
        userEntity.setStreet("testStreet");
        userEntity.setCity("testCity");
        userEntity.setState("testState");
        userEntity.setZip("12345");
        userEntity.setCountry("testCountry");

        Mockito.when(mockUserRepository.findByUsername(userEntity.getUsername())).thenReturn(userEntity);

        var user = new User(
                userEntity.getUsername(),
                userEntity.getEmail(),
                userEntity.getFirstName(),
                userEntity.getLastName(),
                userEntity.getPhone(),
                userEntity.getStreet(),
                userEntity.getCity(),
                userEntity.getState(),
                userEntity.getZip(),
                userEntity.getCountry()
        );

        mockMvc.perform(get(String.format("/users/%s", user.getUsername()))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.username").value(user.getUsername()))
                .andExpect(jsonPath("$.email").value(user.getEmail()))
                .andExpect(jsonPath("$.firstName").value(user.getFirstName()))
                .andExpect(jsonPath("$.lastName").value(user.getLastName()))
                .andExpect(jsonPath("$.phone").value(user.getPhone()))
                .andExpect(jsonPath("$.street").value(user.getStreet()))
                .andExpect(jsonPath("$.city").value(user.getCity()))
                .andExpect(jsonPath("$.state").value(user.getState()))
                .andExpect(jsonPath("$.zip").value(user.getZip()))
                .andExpect(jsonPath("$.country").value(user.getCountry()));
    }

    @Test
    void userNotFoundByName() throws Exception {
        String userName = "testUserName";
        Mockito.when(mockUserRepository.findByUsername(userName)).thenReturn(null);

        mockMvc.perform(get(String.format("/users/%s", userName)))
                .andExpect(status().isNotFound());
    }

    @Test
    void updateUser() throws Exception {
        var userEntity = new UserEntity();

        userEntity.setId(1000L);
        userEntity.setUsername("testUser");
        userEntity.setEmail("testEmail@mail.com");
        userEntity.setFirstName("testFirstName");
        userEntity.setLastName("testLastName");
        userEntity.setPhone("111-111-1111");
        userEntity.setStreet("testStreet");
        userEntity.setCity("testCity");
        userEntity.setState("TT");
        userEntity.setZip("12345");
        userEntity.setCountry("testCountry");

        Mockito.when(mockUserRepository.findByUsername(userEntity.getUsername())).thenReturn(userEntity);
        Mockito.when(mockUserRepository.save(Mockito.any(UserEntity.class))).thenReturn(userEntity);

        var user = new User(
                userEntity.getUsername(),
                userEntity.getEmail(),
                userEntity.getFirstName(),
                userEntity.getLastName(),
                userEntity.getPhone(),
                userEntity.getStreet(),
                userEntity.getCity(),
                userEntity.getState(),
                userEntity.getZip(),
                userEntity.getCountry()
        );

        String userJson = objectMapper.writeValueAsString(user);

        mockMvc.perform(put(String.format("/users/%s/", user.getUsername()))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(userJson))
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test
    void updateNoneExisting() throws Exception {
        var userEntity = new UserEntity();

        userEntity.setId(1000L);
        userEntity.setUsername("testUser");
        userEntity.setEmail("testEmail@mail.com");
        userEntity.setFirstName("testFirstName");
        userEntity.setLastName("testLastName");
        userEntity.setPhone("111-111-1111");
        userEntity.setStreet("testStreet");
        userEntity.setCity("testCity");
        userEntity.setState("TT");
        userEntity.setZip("12345");
        userEntity.setCountry("testCountry");

        Mockito.when(mockUserRepository.findByUsername(Mockito.any())).thenReturn(null);

        var user = new User(
                userEntity.getUsername(),
                userEntity.getEmail(),
                userEntity.getFirstName(),
                userEntity.getLastName(),
                userEntity.getPhone(),
                userEntity.getStreet(),
                userEntity.getCity(),
                userEntity.getState(),
                userEntity.getZip(),
                userEntity.getCountry()
        );

        String userJson = objectMapper.writeValueAsString(user);

        mockMvc.perform(put(String.format("/users/%s", user.getUsername()))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(userJson))
                .andExpect(status().isNotFound());
    }

    @Test
    void updateUserValidation() throws Exception {
        var userEntity = new UserEntity();
        userEntity.setId(1000L);
        userEntity.setUsername("testUser");

        Mockito.when(mockUserRepository.findByUsername(userEntity.getUsername())).thenReturn(userEntity);
        Mockito.when(mockUserRepository.save(Mockito.any(UserEntity.class))).thenReturn(userEntity);

        var user = new User();
        putUserAndExpectResponseCode(user, HttpStatus.BAD_REQUEST);

        user.setUsername(userEntity.getUsername());
        putUserAndExpectResponseCode(user, HttpStatus.BAD_REQUEST);

        user.setEmail("test@mail.com");
        putUserAndExpectResponseCode(user, HttpStatus.BAD_REQUEST);

        user.setFirstName("testFirstName");
        putUserAndExpectResponseCode(user, HttpStatus.BAD_REQUEST);

        user.setLastName("testLastName");
        putUserAndExpectResponseCode(user, HttpStatus.BAD_REQUEST);

        user.setPhone("111-111-1111");
        putUserAndExpectResponseCode(user, HttpStatus.BAD_REQUEST);

        user.setStreet("testStreet");
        putUserAndExpectResponseCode(user, HttpStatus.BAD_REQUEST);

        user.setCity("testCity");
        putUserAndExpectResponseCode(user, HttpStatus.BAD_REQUEST);

        user.setState("NY");
        putUserAndExpectResponseCode(user, HttpStatus.BAD_REQUEST);

        user.setZip("12345");
        putUserAndExpectResponseCode(user, HttpStatus.BAD_REQUEST);

        user.setCountry("USA");
        putUserAndExpectResponseCode(user, HttpStatus.OK);
    }

    @Test
    void deleteUser() throws Exception {
        var userEntity = new UserEntity();
        userEntity.setId(1000L);
        userEntity.setUsername("testUser");

        Mockito.when(mockUserRepository.findByUsername(userEntity.getUsername())).thenReturn(userEntity);
        Mockito.doNothing().when(mockUserRepository).delete(Mockito.any(UserEntity.class));

        mockMvc.perform(delete("/users/testUser"))
                .andExpect(status().isNoContent());
    }

    @Test
    void deleteNonExistingUser() throws Exception {
        var userEntity = new UserEntity();
        userEntity.setId(1000L);
        userEntity.setUsername("testUser");

        Mockito.when(mockUserRepository.findByUsername(userEntity.getUsername())).thenReturn(null);

        mockMvc.perform(delete("/users/testUser"))
                .andExpect(status().isNotFound());
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

        mockMvc.perform(put(String.format("/users/%s", user.getUsername()))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(userJson))
                .andExpect(status().is(httpStatus.value()));
    }
}