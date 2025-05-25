package com.lottofun.lottofunrest.validation.validTicketNumbers;


import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.*;

@Documented
@Constraint(validatedBy = TicketNumbersValidator.class)
@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
public @interface ValidTicketNumbers {
    String message() default "All numbers must be between 1 and 49";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}