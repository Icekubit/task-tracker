package icekubit.tasktrackerbackend.exception;

public class UniqueUsernameConstraintException extends RuntimeException {

    public UniqueUsernameConstraintException(String message) {
        super(message);
    }
}
