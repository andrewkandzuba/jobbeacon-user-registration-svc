package ai.jobbeacon.api;

import ai.jobbeacon.model.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.util.Assert;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.context.request.ServletWebRequest;

import java.util.Optional;

@SpringBootTest
class UsersApiTest {

    private User user;

    private UsersApi usersApi;

    @BeforeEach
    void setUp() {
        user = new User();
        user.setUsername("testUser");
        user.setPassword("testPassword");
        user.setFirstName("testFirstName");
        user.setLastName("testLastName");
        user.setEmail("testEmail@mail.com");
        user.setMailingAddress("testAddress");
        usersApi = new UsersApi() {};
    }

    @Test
    void usersPost() {
        var responseEntity = usersApi.usersPost(user);
        Assert.isTrue(responseEntity.getStatusCode().equals(HttpStatus.NOT_IMPLEMENTED), "Status code should be 501");
    }

    @Test
    void usersUsernameDelete() {
        var responseEntity = usersApi.usersUsernameDelete(user.getUsername());
        Assert.isTrue(responseEntity.getStatusCode().equals(HttpStatus.NOT_IMPLEMENTED), "Status code should be 501");
    }

    @Test
    void usersUsernameGet() {
        var responseEntity = usersApi.usersUsernameGet(user.getUsername());
        Assert.isTrue(responseEntity.getStatusCode().equals(HttpStatus.NOT_IMPLEMENTED), "Status code should be 501");
    }

    @Test
    void usersUsernamePut() {
        var responseEntity = usersApi.usersUsernamePut(user.getUsername(), user);
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

        var responseEntity = extendedUsersApi.usersUsernameGet(user.getUsername());
        Assert.isTrue(responseEntity.getStatusCode().equals(HttpStatus.NOT_IMPLEMENTED), "Status code should be 501");
    }
}