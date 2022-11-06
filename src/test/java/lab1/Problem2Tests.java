package lab1;

import org.junit.jupiter.api.Test;

import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.*;

public class Problem2Tests {

    @Test
    void Test1() {
        int[] numbers = {2, 1, 5, 6, 8};
        int[] answer = {2, 6, 8, 1, 5};
        int[] actual = Problem2.segregateEvenAndOddNumbers(numbers);
        assertArrayEquals(answer, actual);
    }

    @Test
    void Test2_ArraysContainsOnlyEvens() {
        int[] numbers = {2, 12, 0, 6, 8};
        int[] answer = {2, 12, 0, 6, 8};
        int[] actual = Problem2.segregateEvenAndOddNumbers(numbers);
        assertArrayEquals(answer, actual);
    }

    @Test
    void Test3_ArraysContainsOnlyOdds() {
        int[] numbers = {1, 7, 5, -1, 99};
        int[] answer = {1, 7, 5, -1, 99};
        int[] actual = Problem2.segregateEvenAndOddNumbers(numbers);
        assertArrayEquals(answer, actual);
    }

    @Test
    void Test4_ArraysIsEmpty() {
        int[] numbers = {};
        int[] answer = {};
        int[] actual = Problem2.segregateEvenAndOddNumbers(numbers);
        assertArrayEquals(answer, actual);
    }

    @Test
    void Test5() {
        int[] numbers = {2, 1, 5, 6, 8, 7, 0, 2};
        int[] answer = {2, 6, 8, 0, 2, 1, 5, 7};
        int[] actual = Problem2.segregateEvenAndOddNumbers(numbers);
        assertArrayEquals(answer, actual);
    }

}