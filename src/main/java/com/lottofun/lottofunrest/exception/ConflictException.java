package com.lottofun.lottofunrest.exception;

/**
 * Exception thrown when there is a conflict with an existing domain object.
 * Custom conflict exceptions can be created by inheriting this exception class.
 *
 * <p>Example usage:
 * <pre>
 *     throw new ConflictException("User", "email", "example@example.com");
 * </pre>
 * Will result in: "Conflict: User already exists with email = example@example.com"
 */
public class ConflictException extends RuntimeException {

    /**
     * Constructs a ConflictException with a detailed message.
     *
     * @param domain the name of the domain/entity class (e.g., "User", "Order")
     * @param field the name of the field causing the conflict (e.g., "email", "username")
     * @param value the conflicting value (e.g., "example@example.com", "johndoe")
     */
    public ConflictException(String domain, String field, Object value) {
        super(String.format("Conflict: %s already exists with %s = %s", domain, field, value));
    }

    /**
     * Optional constructor allowing a custom conflict message.
     *
     * @param message custom conflict message
     */
    public ConflictException(String message) {
        super(message);
    }
}
