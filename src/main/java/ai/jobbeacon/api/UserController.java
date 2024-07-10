package ai.jobbeacon.api;

import ai.jobbeacon.model.User;
import ai.jobbeacon.service.UserService;
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
public class UserController implements UsersApi {

    private final UserService userService;
    Logger logger = LoggerFactory.getLogger(UserController.class);

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @Override
    public ResponseEntity<Object> createUser(User user) {
        logger.info("User registration request received for user: {}", user.getUsername());

        var record = userService.create(user);

        return ResponseEntity
                .created(
                        ServletUriComponentsBuilder
                                .fromCurrentRequest()
                                .path("/{username}")
                                .buildAndExpand(record)
                                .toUri())
                .contentType(MediaType.APPLICATION_JSON)
                .build();
    }

    @Override
    public ResponseEntity<User> findUser(String username) {
        logger.info("User details request received for user: {}", username);

        var record = userService.findBy(username);

        return ResponseEntity.ok()
                .contentType(MediaType.APPLICATION_JSON)
                .body(record);
    }

    @Override
    public ResponseEntity<Object> updateUser(String username, User user) {
        logger.info("User update request received for user: {}", username);

        userService.update(user);

        return ResponseEntity.ok()
                .contentType(MediaType.APPLICATION_JSON)
                .build();
    }

    @Override
    public ResponseEntity<Void> deleteUser(String username) {
        logger.info("User deletion request received for user: {}", username);

        userService.delete(username);

        return ResponseEntity.noContent().build();
    }
}
