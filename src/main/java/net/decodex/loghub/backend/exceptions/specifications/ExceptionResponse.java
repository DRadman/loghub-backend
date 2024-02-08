package net.decodex.loghub.backend.exceptions.specifications;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.http.HttpStatus;

import java.time.LocalDateTime;
import java.util.List;

@JsonInclude(JsonInclude.Include.NON_EMPTY)
@Getter
@Setter
@ToString
public class ExceptionResponse {
    private HttpStatus status;
    private LocalDateTime timestamp;
    private String message;
    private String debugMessage;
    private List<String> errors;

    public ExceptionResponse() {
    }

    public ExceptionResponse(HttpStatus status, String message, String debugMessage, List<String> errors) {
        super();
        this.status = status;
        this.timestamp = LocalDateTime.now();
        this.message = message;
        this.debugMessage = debugMessage;
        this.errors = errors;
    }

    public ExceptionResponse(HttpStatus status, String message, String debugMessage) {
        super();
        this.timestamp = LocalDateTime.now();
        this.status = status;
        this.message = message;
        this.debugMessage = debugMessage;
    }

    public ExceptionResponse(HttpStatus status, LocalDateTime timestamp, String message, String debugMessage) {
        super();
        this.status = status;
        this.timestamp = timestamp;
        this.message = message;
        this.debugMessage = debugMessage;
    }
}
