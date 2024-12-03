package web.bookie.util.api;

import lombok.EqualsAndHashCode;
import lombok.ToString;

import java.time.LocalDateTime;

@ToString
@EqualsAndHashCode
public class ApiSuccessResponse<T> {

    private T data;
    private LocalDateTime time;

    public ApiSuccessResponse(T data) {
        this.data = data;
        this.time = LocalDateTime.now();
    }

}

