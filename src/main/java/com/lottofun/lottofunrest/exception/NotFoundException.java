package com.lottofun.lottofunrest.exception;

/**
 * Exception thrown when a domain object cannot be found.
 * This can be extended to define custom not-found exceptions.
 *
 * <p>Example usage:
 * <pre>
 *     throw new NotFoundException("User", "id", 42);
 * </pre>
 * Will result in: "User not found with id = 42"
 */
public class NotFoundException extends RuntimeException {

    /**
     * Constructs a NotFoundException with a detailed message.
     *
     * @param domain the name of the domain/entity class (e.g., "User", "Order")
     * @param field the name of the field used to search (e.g., "id", "email")
     * @param value the value that was not found (e.g., 42, "johndoe")
     */
    public NotFoundException(String domain, String field, Object value) {
        super(String.format("%s not found with %s = %s", domain, field, value));
    }

    /**
     * Optional constructor allowing custom message.
     *
     * @param message custom not-found message
     */
    public NotFoundException(String message) {
        super(message);
    }
}
