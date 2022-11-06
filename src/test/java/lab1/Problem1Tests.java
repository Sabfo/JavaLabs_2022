package lab1;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class Problem1Tests {

    @Test
    void shouldReturnTrue_ifNumberContainsAInHexRepresentation() {
        int number = 10;
        boolean actual = Problem1.containsDigitAInHexadecimalRepresentation(number);
        assertTrue(actual);
    }

    @Test
    void shouldReturnFalse_ifNumberDoesNotContainAInHexRepresentation() {
        int number = 9;
        boolean actual = Problem1.containsDigitAInHexadecimalRepresentation(number);
        assertFalse(actual);
    }

    @Test
    void shouldReturnTrue_ifNumberIsAlreadyEqualToZero() {
        int number = 10;
        boolean actual = Problem1.containsDigitAInHexadecimalRepresentation(number);
        assertTrue(actual);
    }

}