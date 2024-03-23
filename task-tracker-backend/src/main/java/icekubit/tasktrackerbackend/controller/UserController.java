package icekubit.tasktrackerbackend.controller;

import icekubit.tasktrackerbackend.exception.UnauthorizedException;
import icekubit.tasktrackerbackend.model.UserDetailsImpl;
import icekubit.tasktrackerbackend.model.dto.*;
import icekubit.tasktrackerbackend.service.AuthService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1")
@RequiredArgsConstructor
public class UserController {


    private final AuthService authService;

    @GetMapping("/users")
    public GetUserResponse getUser(@AuthenticationPrincipal UserDetails userDetails) {

        if (userDetails == null) {
            throw new UnauthorizedException("Error: unauthorized");
        }

        UserDetailsImpl customUserDetails = (UserDetailsImpl) userDetails;
        return GetUserResponse.builder()
                        .id(customUserDetails.getId())
                        .username(customUserDetails.getUsername())
                        .email(customUserDetails.getEmail())
                        .build();
    }

    @PostMapping("/users")
    public JwtResponse registerUser(@RequestBody @Valid SignUpRequest signUpRequest) {
        return authService.register(signUpRequest);
    }

    @PostMapping("/auth/login")
    public JwtResponse login(@RequestBody @Valid SignInRequest signInRequest) {
        return authService.login(signInRequest);
    }
}
