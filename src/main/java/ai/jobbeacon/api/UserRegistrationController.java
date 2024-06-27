package ai.jobbeacon.api;

import ai.jobbeacon.model.User;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

@RestController
@Validated
@Tag(name = "users", description = "Users Registration API")
public class UserRegistrationController implements UsersApi {

    Logger logger = LoggerFactory.getLogger(UserRegistrationController.class);

    @Override
    public ResponseEntity<Object> usersPost(User user) {
        logger.info("User registration request received for user: {}", user.getUsername());
        return ResponseEntity
                .created(
                        ServletUriComponentsBuilder
                                .fromCurrentRequest()
                                .path("/{username}")
                                .buildAndExpand(user.getUsername())
                                .toUri())
                .contentType(MediaType.APPLICATION_JSON)
                .build();
    }

    @Override
    public ResponseEntity<User> usersUsernameGet(String username) {
        logger.info("User details request received for user: {}", username);
        return ResponseEntity.ok(new User(
                username,
                "testPassword",
                "testFirstName",
                "testLastName",
                "testUser@mail.com",
                "testMailingAddress"
        ));
    }

    @Override
    public ResponseEntity<Object> usersUsernamePut(String username, User user) {
        return ResponseEntity.ok()
                .contentType(MediaType.APPLICATION_JSON)
                .build();
    }

    @Override
    public ResponseEntity<Void> usersUsernameDelete(String username) {
        return ResponseEntity.noContent().build();
    }
}
