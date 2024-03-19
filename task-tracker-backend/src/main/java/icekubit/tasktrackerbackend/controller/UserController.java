package icekubit.tasktrackerbackend.controller;


import icekubit.tasktrackerbackend.dto.UserDto;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/users")
public class UserController {

    @GetMapping
    public UserDto getUser() {
        return UserDto.builder()
                .id(1)
                .name("John")
                .build();
    }

    @PostMapping
    public UserDto registerUser(@RequestBody UserDto user) {
        System.out.println(user);
        return user;
    }
}
