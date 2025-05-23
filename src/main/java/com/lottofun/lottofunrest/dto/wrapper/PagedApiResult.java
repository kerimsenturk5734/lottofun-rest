package com.lottofun.lottofunrest.dto.wrapper;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;

import java.util.List;

/**
 * A specialized API response wrapper for paginated results.
 * Extends {@link ApiResult} with pagination metadata such as page number,
 * page size, total pages, and total elements.
 *
 * @param <T> the type of elements contained in the paged response
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class PagedApiResult<T> extends ApiResult<List<T>> {

    /**
     * The current page number (zero-based).
     */
    private int page;

    /**
     * The size of the page (number of elements per page).
     */
    private int size;

    /**
     * The total number of pages available.
     */
    private int totalPages;

    /**
     * The total number of elements across all pages.
     */
    private long totalElements;

    /**
     * Constructs a new {@code PagedApiResult} wrapping the content and pagination info
     * extracted from the given {@link Page} object.
     *
     * @param message  a descriptive message for the API response
     * @param pageData the {@link Page} containing the paged data and metadata
     * @param status   the HTTP status of the response
     */
    public PagedApiResult(String message, Page<T> pageData, HttpStatus status) {
        super(true, message, status.value(), pageData.getContent());
        this.page = pageData.getNumber();
        this.size = pageData.getSize();
        this.totalPages = pageData.getTotalPages();
        this.totalElements = pageData.getTotalElements();
    }

    /**
     * Static factory method to create a {@code PagedApiResult} from a {@link Page}.
     *
     * @param message  a descriptive message for the API response
     * @param pageData the {@link Page} containing the paged data and metadata
     * @param status   the HTTP status of the response
     * @param <T>      the type of elements contained in the paged response
     * @return a new {@code PagedApiResult} instance
     */
    public static <T> PagedApiResult<T> of(String message, Page<T> pageData, HttpStatus status) {
        return new PagedApiResult<>(message, pageData, status);
    }
}