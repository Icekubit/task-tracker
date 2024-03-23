package icekubit.tasktrackerbackend.service;

import icekubit.tasktrackerbackend.jwt.JwtUtils;
import icekubit.tasktrackerbackend.model.dto.JwtResponse;
import icekubit.tasktrackerbackend.model.dto.SignInRequest;
import icekubit.tasktrackerbackend.model.dto.SignUpRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final AuthenticationManager authenticationManager;
    private final JwtUtils jwtUtils;
    private final UserService userService;

    public JwtResponse register(SignUpRequest signUpRequest) {

        userService.save(signUpRequest);

        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(signUpRequest.getUsername(), signUpRequest.getPassword())
        );
        SecurityContextHolder.getContext().setAuthentication(authentication);
        String jwt = jwtUtils.generateJwtToken(authentication);

        return JwtResponse.builder()
                .token(jwt)
                .username(signUpRequest.getUsername())
                .build();
    }

    public JwtResponse login(SignInRequest signInRequest) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(signInRequest.getUsername(), signInRequest.getPassword())
        );
        SecurityContextHolder.getContext().setAuthentication(authentication);
        String jwt = jwtUtils.generateJwtToken(authentication);

        return JwtResponse.builder()
                .token(jwt)
                .username(signInRequest.getUsername())
                .build();
    }
}
