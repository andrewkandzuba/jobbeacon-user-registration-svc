package ai.jobbeacon.api;

import ai.jobbeacon.model.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.util.Assert;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.context.request.ServletWebRequest;

import java.util.Optional;

class UsersApiTest {

    private User user;

    private UsersApi usersApi;

    @BeforeEach
    void setUp() {
        user = new User();
        user.setUsername("testUser");
        user.setEmail("testEmail@mail.com");

        user.setFirstName("testFirstName");
        user.setLastName("testLastName");
        user.setPhone("111-111-1111");
        user.setStreet("testStreet");
        user.setCity("testCity");
        user.setState("testState");
        user.setZip("12345");
        user.setCountry("testCountry");

        usersApi = new UsersApi() {};
    }

    @Test
    void usersPost() {
        var responseEntity = usersApi.createUser(user);
        Assert.isTrue(responseEntity.getStatusCode().equals(HttpStatus.NOT_IMPLEMENTED), "Status code should be 501");
    }

    @Test
    void usersUsernameDelete() {
        var responseEntity = usersApi.deleteUser(user.getUsername());
        Assert.isTrue(responseEntity.getStatusCode().equals(HttpStatus.NOT_IMPLEMENTED), "Status code should be 501");
    }

    @Test
    void usersUsernameGet() {
        var responseEntity = usersApi.findUser(user.getUsername());
        Assert.isTrue(responseEntity.getStatusCode().equals(HttpStatus.NOT_IMPLEMENTED), "Status code should be 501");
    }

    @Test
    void usersUsernamePut() {
        var responseEntity = usersApi.updateUser(user.getUsername(), user);
        Assert.isTrue(responseEntity.getStatusCode().equals(HttpStatus.NOT_IMPLEMENTED), "Status code should be 501");
    }

    @Test
    void usersUsernameGetWithAcceptHeader() {

        var extendedUsersApi = new UsersApi() {
            @Override
            public Optional<NativeWebRequest> getRequest() {
                var httpServletRequest = new MockHttpServletRequest();
                httpServletRequest.addHeader("Accept", "application/json");
                var nativeWebRequest = new ServletWebRequest(httpServletRequest, new MockHttpServletResponse());

                return Optional.of(nativeWebRequest);
            }
        };

        var responseEntity = extendedUsersApi.findUser(user.getUsername());
        Assert.isTrue(responseEntity.getStatusCode().equals(HttpStatus.NOT_IMPLEMENTED), "Status code should be 501");
    }
}