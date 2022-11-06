package lab1;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class Problem5Tests {

    @Test
    void shouldReturnTrue_ifNumbersAreGeometricProgressionInAscendingOrder() {
        String numbers = "1,2,4,8,16";
        boolean actual = Problem5.isGeometricProgression(numbers);
        assertTrue(actual);
    }

    @Test
    void shouldReturnTrue_ifNumbersAreGeometricProgressionNotInOrder() {
        String numbers = "16,2,8,1,4";
        boolean actual = Problem5.isGeometricProgression(numbers);
        assertTrue(actual);
    }

    @Test
    void shouldReturnFalse_ifNumbersArentGeometricProgression() {
        String numbers = "2,3,5";
        boolean actual = Problem5.isGeometricProgression(numbers);
        assertFalse(actual);
    }

    @Test
    void shouldReturnFalse_ifNumbersArentGeometricProgressionAndCheckRound() {
        String numbers = "2,5,10,20,40";
        boolean actual = Problem5.isGeometricProgression(numbers);
        assertFalse(actual);
    }

    @Test
    void shouldReturnFalse_ifNumbersAreEmpty() {
        String numbers = "";
        boolean actual = Problem5.isGeometricProgression(numbers);
        assertTrue(actual);
    }

    @Test
    void shouldReturnFalse_ifNumbersHasOneNumber() {
        String numbers = "222";
        boolean actual = Problem5.isGeometricProgression(numbers);
        assertTrue(actual);
    }

}