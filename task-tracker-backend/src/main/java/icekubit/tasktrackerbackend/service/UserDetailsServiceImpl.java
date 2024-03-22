package icekubit.tasktrackerbackend.service;

import icekubit.tasktrackerbackend.model.UserDetailsImpl;
import icekubit.tasktrackerbackend.model.entity.User;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Collections;

@Service
@RequiredArgsConstructor
public class UserDetailsServiceImpl implements UserDetailsService {

    private final UserService userService;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return userService.getUserByUsername(username)
                .map(this::convertUserToUserDetails)
                .orElseThrow(()
                        -> new UsernameNotFoundException(
                                String.format("User with the username '%s' not found", username)
                ));
    }

    private UserDetailsImpl convertUserToUserDetails(User user) {
        return UserDetailsImpl.builder()
                .id(user.getId())
                .username(user.getUsername())
                .email(user.getEmail())
                .password(user.getPassword())
                .authorities(Collections.singletonList(new SimpleGrantedAuthority("ROLE_USER")))
                .build();
    }
}
