package icekubit.tasktrackerbackend.handler;

import icekubit.tasktrackerbackend.exception.UnauthorizedException;
import icekubit.tasktrackerbackend.exception.UniqueEmailConstraintException;
import icekubit.tasktrackerbackend.exception.UniqueUsernameConstraintException;
import icekubit.tasktrackerbackend.model.dto.ErrorResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;


@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandler {

    @ExceptionHandler(BadCredentialsException.class)
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    public ErrorResponse handleBadCredentialsException() {
        return new ErrorResponse("Bad credentials");
    }

    @ExceptionHandler(UnauthorizedException.class)
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    public ErrorResponse handleAuthenticationException() {
        return new ErrorResponse("Error: unauthorized");
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorResponse handleValidationException(MethodArgumentNotValidException ex) {
        String errorMessage = ex.getBindingResult().getFieldErrors().get(0).getDefaultMessage();
        return new ErrorResponse("Validation error: " + errorMessage);
    }

    @ExceptionHandler(UniqueUsernameConstraintException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorResponse handleUniqueUsernameConstraintException() {
        return new ErrorResponse("Error: username is already taken");
    }

    @ExceptionHandler(UniqueEmailConstraintException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorResponse handleUniqueEmailConstraintException() {
        return new ErrorResponse("Error: email is already taken");
    }

}