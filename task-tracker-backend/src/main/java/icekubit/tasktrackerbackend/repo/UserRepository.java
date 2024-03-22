package icekubit.tasktrackerbackend.repo;

import icekubit.tasktrackerbackend.model.entity.User;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

public interface UserRepository extends CrudRepository<User, Long> {

    Optional<User> getUserByUsername(String username);
    boolean existsByUsername(String username);
    boolean existsByEmail(String email);

}
