package com.lottofun.lottofunrest.validation.validTicketNumbers;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

import java.util.Set;

public class TicketNumbersValidator implements ConstraintValidator<ValidTicketNumbers, Set<Integer>> {

    @Override
    public boolean isValid(Set<Integer> numbers, ConstraintValidatorContext context) {
        if (numbers == null) return true;

        return numbers.stream().allMatch(n -> n >= 1 && n <= 49);
    }
}