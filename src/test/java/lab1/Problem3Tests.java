package lab1;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class Problem3Tests {

    @Test
    void Test1() {
        double[] values = {2.0, 1.0, 5.0};
        double[] weights = {1.0, 1.0, 2.0};
        double maximumWeightCapacity = 2.5;
        double actual = Problem3.solveKnapsackProblem(values, weights, maximumWeightCapacity);
        double answer = 5.0;
        assertEquals(answer, actual);
    }

    @Test
    void Test2() {
        double[] values = {2.0, 0.0, 5.5, 0.4};
        double[] weights = {1.0, 1.0, 2.0, 0.5};
        double maximumWeightCapacity = 2.5;
        double actual = Problem3.solveKnapsackProblem(values, weights, maximumWeightCapacity);
        double answer = 5.9;
        assertEquals(answer, actual);
    }

}
