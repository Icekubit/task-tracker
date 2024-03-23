package icekubit.tasktrackerbackend.model.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class SignInRequest {

    @Size(min = 5, max = 50, message = "Username must be between 5 and 50 characters")
    @NotBlank(message = "Username cannot be empty")
    private String username;

    @Size(max = 255, message = "Password length must not exceed 255 characters")
    private String password;
}
