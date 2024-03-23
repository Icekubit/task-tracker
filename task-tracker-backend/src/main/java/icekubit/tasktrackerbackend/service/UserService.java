package icekubit.tasktrackerbackend.service;

import icekubit.tasktrackerbackend.exception.UniqueEmailConstraintException;
import icekubit.tasktrackerbackend.exception.UniqueUsernameConstraintException;
import icekubit.tasktrackerbackend.model.dto.SignUpRequest;
import icekubit.tasktrackerbackend.model.entity.User;
import icekubit.tasktrackerbackend.repo.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;


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

    public void save(SignUpRequest signUpRequest) {
        User user = User.builder()
                .username(signUpRequest.getUsername())
                .email(signUpRequest.getEmail())
                .password(passwordEncoder.encode(signUpRequest.getPassword()))
                .build();

        if (userRepository.existsByUsername(signUpRequest.getUsername())) {
            throw new UniqueUsernameConstraintException(
                    String.format("The user with username '%s' already exists", user.getUsername())
            );
        }

        if (userRepository.existsByEmail(signUpRequest.getEmail())) {
            throw new UniqueEmailConstraintException(
                    String.format("The user with email '%s' already exists", user.getEmail())
            );
        }


        try {
            userRepository.save(user);
        } catch (DataIntegrityViolationException e) {
            if (e.getMessage().contains(usernameConstraint)) {
                throw new UniqueUsernameConstraintException(
                        String.format("The user with username '%s' already exists", user.getUsername())
                );
            }
            if (e.getMessage().contains(emailConstraint)) {
                throw new UniqueEmailConstraintException(
                        String.format("The user with email '%s' already exists", user.getEmail())
                );
            }
        }
    }

}
