package web.bookie.util.api;

import lombok.ToString;

import java.time.LocalDateTime;
import java.util.Objects;

@ToString
public class ApiSuccessResponse<T> {

    private T data;
    private LocalDateTime time;

    public ApiSuccessResponse(T data) {
        this.data = data;
        this.time = LocalDateTime.now();
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }

        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        ApiSuccessResponse otherResponse = (ApiSuccessResponse) o;

        boolean isTimeEqual;
        if (this.time != null && otherResponse.time != null) {
            LocalDateTime thisTime = this.time.withSecond(0).withNano(0);
            LocalDateTime otherTime = otherResponse.time.withSecond(0).withNano(0);

            isTimeEqual = thisTime.equals(otherTime);
        } else {
            isTimeEqual = (this.time == otherResponse.time);
        }

        return Objects.equals(this.data, otherResponse.data) && isTimeEqual;
    }

    @Override
    public int hashCode() {
        return Objects.hash(
                data,
                time != null ? time.withSecond(0).withNano(0) : null
        );
    }

}

