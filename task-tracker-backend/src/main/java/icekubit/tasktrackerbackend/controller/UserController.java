package icekubit.tasktrackerbackend.controller;


import icekubit.tasktrackerbackend.exception.UniqueEmailConstraintException;
import icekubit.tasktrackerbackend.exception.UniqueNameConstraintException;
import icekubit.tasktrackerbackend.jwt.JwtUtils;
import icekubit.tasktrackerbackend.model.UserDetailsImpl;
import icekubit.tasktrackerbackend.model.dto.*;
import icekubit.tasktrackerbackend.model.entity.User;
import icekubit.tasktrackerbackend.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1")
@RequiredArgsConstructor
public class UserController {

    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final JwtUtils jwtUtils;
    private final UserService userService;

    @GetMapping("/users")
    public ResponseEntity<?> getUser(@AuthenticationPrincipal UserDetails userDetails) {

        if (userDetails == null) {
            return new ResponseEntity<>(new ErrorResponse("Unauthorized"), HttpStatus.UNAUTHORIZED);
        }

        UserDetailsImpl customUserDetails = (UserDetailsImpl) userDetails;
        return ResponseEntity.ok(
                GetUserResponse.builder()
                        .id(customUserDetails.getId())
                        .username(customUserDetails.getUsername())
                        .email(customUserDetails.getEmail())
                        .build()
        );
    }

    @PostMapping("/users")
    public ResponseEntity<?> registerUser(@RequestBody SignUpRequest signUpRequest) {
        User user = User.builder()
                .username(signUpRequest.getUsername())
                .email(signUpRequest.getEmail())
                .password(passwordEncoder.encode(signUpRequest.getPassword()))
                .build();

        if (userService.existsByUsername(signUpRequest.getUsername())) {
            return ResponseEntity
                    .badRequest()
                    .body(new ErrorResponse("Error: Username is already taken!"));
        }

        if (userService.existsByEmail(signUpRequest.getEmail())) {
            return ResponseEntity
                    .badRequest()
                    .body(new ErrorResponse("Error: Email is already in use!"));
        }

        try {
            userService.save(user);
        } catch (UniqueNameConstraintException e) {
            return ResponseEntity
                    .badRequest()
                    .body(new ErrorResponse("Error: Username is already taken!"));
        } catch (UniqueEmailConstraintException e) {
            return ResponseEntity
                    .badRequest()
                    .body(new ErrorResponse("Error: Email is already in use!"));
        }

        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(signUpRequest.getUsername(), signUpRequest.getPassword())
        );

        SecurityContextHolder.getContext().setAuthentication(authentication);
        String jwt = jwtUtils.generateJwtToken(authentication);

        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();

        return ResponseEntity.ok(
                JwtResponse.builder()
                        .token(jwt)
                        .id(userDetails.getId())
                        .username(userDetails.getUsername())
                        .email(userDetails.getEmail())
                        .build()
        );
    }

    @PostMapping("/auth/login")
    public ResponseEntity<?> login(@RequestBody SignInRequest signInRequest) {

        Authentication authentication = null;

        try {
            authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(signInRequest.getUsername(), signInRequest.getPassword())
            );
        } catch (BadCredentialsException e) {
            return new ResponseEntity<>(new ErrorResponse("Bad credentials"), HttpStatus.UNAUTHORIZED);
        }

        SecurityContextHolder.getContext().setAuthentication(authentication);
        String jwt = jwtUtils.generateJwtToken(authentication);

        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();

        return ResponseEntity.ok(
                JwtResponse.builder()
                        .token(jwt)
                        .id(userDetails.getId())
                        .username(userDetails.getUsername())
                        .email(userDetails.getEmail())
                        .build()
        );


    }
}
