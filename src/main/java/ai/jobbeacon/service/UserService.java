package ai.jobbeacon.service;

import ai.jobbeacon.model.User;
import ai.jobbeacon.model.UserEntity;
import ai.jobbeacon.persistence.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class UserService {
    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Transactional
    public String create(User user) {
        UserEntity userEntity = new UserEntity();
        userEntity.setUsername(user.getUsername());
        userEntity.setEmail(user.getEmail());
        userEntity.setFirstName(user.getFirstName());
        userEntity.setLastName(user.getLastName());
        userEntity.setPhone(user.getPhone());
        userEntity.setStreet(user.getStreet());
        userEntity.setCity(user.getCity());
        userEntity.setState(user.getState());
        userEntity.setZip(user.getZip());
        userEntity.setCountry(user.getCountry());

        if (userRepository.findByUsername(user.getUsername()) != null) {
            throw new UserAlreadyExistsException(String.format("User %s already exists", user.getUsername()));
        }

        var createdUser = userRepository.save(userEntity);

        return createdUser.getUsername();
    }

    @Transactional(readOnly = true)
    public User findBy(String username) {
        var userEntity = userRepository.findByUsername(username);

        if (userEntity == null) {
            throw new UserNotFoundException(String.format("User %s not found", username));
        }

        return new User(
                userEntity.getUsername(),
                userEntity.getEmail(),
                userEntity.getFirstName(),
                userEntity.getLastName(),
                userEntity.getPhone(),
                userEntity.getStreet(),
                userEntity.getCity(),
                userEntity.getState(),
                userEntity.getZip(),
                userEntity.getCountry());
    }

    @Transactional
    public void update(User user) {
        UserEntity userEntity = userRepository.findByUsername(user.getUsername());

        if (userEntity == null) {
            throw new UserNotFoundException(String.format("User %s not found", user.getUsername()));
        }

        userEntity.setEmail(user.getEmail());
        userEntity.setFirstName(user.getFirstName());
        userEntity.setLastName(user.getLastName());
        userEntity.setPhone(user.getPhone());
        userEntity.setStreet(user.getStreet());
        userEntity.setCity(user.getCity());
        userEntity.setState(user.getState());
        userEntity.setZip(user.getZip());
        userEntity.setCountry(user.getCountry());

        userRepository.save(userEntity);
    }

    @Transactional
    public void delete(String username) {
        UserEntity userEntity = userRepository.findByUsername(username);

        if (userEntity == null) {
            throw new UserNotFoundException(String.format("User %s not found", username));
        }

        userRepository.delete(userEntity);
    }
}
