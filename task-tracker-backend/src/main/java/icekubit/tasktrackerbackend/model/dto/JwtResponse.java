package icekubit.tasktrackerbackend.model.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class JwtResponse {
    private String token;
    private Long id;
    private String username;
    private String email;
}
