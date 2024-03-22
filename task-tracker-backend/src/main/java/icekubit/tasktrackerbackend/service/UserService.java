package icekubit.tasktrackerbackend.service;

import icekubit.tasktrackerbackend.exception.UniqueEmailConstraintException;
import icekubit.tasktrackerbackend.exception.UniqueNameConstraintException;
import icekubit.tasktrackerbackend.model.entity.User;
import icekubit.tasktrackerbackend.repo.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;

    /* The strings "usernameConstraint" and "emailConstraint" should correspond to the constraint names
    in the Liquibase changelog-1.0
     */

    @Value("unique_username")
    private String usernameConstraint;

    @Value("unique_email")
    private String emailConstraint;

    public Optional<User> getUserByUsername(String username) {
        return userRepository.getUserByUsername(username);
    }

    public void save(User user) {
        try {
            userRepository.save(user);
        } catch (DataIntegrityViolationException e) {
            if (e.getMessage().contains(usernameConstraint)) {
                throw new UniqueNameConstraintException("The user with username "
                        + user.getUsername() + " already exists");
            }
            if (e.getMessage().contains(emailConstraint)) {
                throw new UniqueEmailConstraintException("The user with email "
                        + user.getEmail() + " already exists");
            }
        }
    }

    public boolean existsByUsername(String username) {
        return userRepository.existsByUsername(username);
    }

    public boolean existsByEmail(String email) {
        return userRepository.existsByEmail(email);
    }
}
