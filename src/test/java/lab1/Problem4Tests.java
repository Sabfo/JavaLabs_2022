package lab1;

import org.junit.jupiter.api.Test;

import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.*;

public class Problem4Tests {

    @Test
    void shouldReturnTrue_Test1() {
        int[][] matrix = {{1, 2, 3},{4, 5, 6},{7, 8, 9}};
        int[] answer = {1, 4, 7, 2, 5, 8, 3, 6, 9};
        int[] actual = Problem4.flattenMatrix(matrix);
        assertArrayEquals(answer, actual);
    }

    @Test
    void shouldReturnTrue_Test2() {
        int[][] matrix = {{1, 2, 3, 7, 8, 9}};
        int[] answer = {1, 2, 3, 7, 8, 9};
        int[] actual = Problem4.flattenMatrix(matrix);
        assertArrayEquals(answer, actual);
    }

    @Test
    void shouldReturnTrue_ifMatrixIsEmpty() {
        int[][] matrix = {{}};
        int[] answer = {};
        int[] actual = Problem4.flattenMatrix(matrix);
        assertArrayEquals(answer, actual);
    }

}