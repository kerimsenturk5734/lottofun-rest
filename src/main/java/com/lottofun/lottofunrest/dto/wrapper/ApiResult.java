package com.lottofun.lottofunrest.dto.wrapper;

import jakarta.annotation.Nullable;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.http.HttpStatus;

/**
 * Generic wrapper class to standardize API responses.
 *
 * @param <T> the type of the response data
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ApiResult<T> {
    /**
     * Indicates whether the API call was successful.
     */
    private boolean success;

    /**
     * A message providing additional information about the response.
     */
    private String message;

    /**
     * Http status code
     */
    private int status;

    /**
     * The actual data returned by the API.
     */
    @Nullable
    private T data;

    /**
     * Creates a successful ApiResult containing message without a data.
     *
     * @param message the response message
     * @return an ApiResult instance representing success
     */
    public static <T> ApiResult<T> success(String message, @Nullable HttpStatus status) {
        return new ApiResult<>(true, message, status != null ? status.value() : HttpStatus.OK.value(), null);
    }

    /**
     * Creates a successful ApiResult containing a message and data.
     *
     * @param message a descriptive success message
     * @param data the response data
     * @param <T> the type of the response data
     * @return an ApiResult instance representing success
     */
    public static <T> ApiResult<T> success(String message, T data, @Nullable HttpStatus status) {
        return new ApiResult<>(true, message, status != null ? status.value() : HttpStatus.OK.value(), data);
    }

    /**
     * Creates an error ApiResult containing an error message.
     *
     * @param message a descriptive error message
     * @param <T> the type of the response data (typically null)
     * @return an ApiResult instance representing failure
     */
    public static <T> ApiResult<T> error(String message, @Nullable HttpStatus status) {
        return new ApiResult<>(false, message, status != null ? status.value() : HttpStatus.BAD_REQUEST.value(), null);
    }
}
