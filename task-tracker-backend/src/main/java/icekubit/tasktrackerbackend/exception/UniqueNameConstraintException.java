package icekubit.tasktrackerbackend.exception;

public class UniqueNameConstraintException extends RuntimeException {

    public UniqueNameConstraintException(String message) {
        super(message);
    }
}
