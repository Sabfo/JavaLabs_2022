package lab1;

import static java.lang.Double.max;

public class Problem3 {

    /**
     * Метод solveKnapsackProblem решает классическую задачу о рюкзаке:
     * https://ru.wikipedia.org/wiki/%D0%97%D0%B0%D0%B4%D0%B0%D1%87%D0%B0_%D0%BE_%D1%80%D1%8E%D0%BA%D0%B7%D0%B0%D0%BA%D0%B5
     *
     * Пусть даны n предметов с индексами 0, 1, ..., n-1.
     * @param values массив положительных чисел длины n, определяющий ценности n предметов
     * @param weights массив положительных чисел длины n, определяющий массы n предметов
     * @param maximumWeightCapacity положительное число, определяющее грузоподъемность рюкзака
     * @return максимальное значение общей ценности предметов, которые можно положить в рюкзак с учетом ограничения
     *         на грузоподъемность.
     *
     * ПРИМЕР:
     * Вход: values = [2.0, 1.0, 5.0]
     *       weights = [1.0, 1.0, 2.0]
     *       maximumWeightCapacity = 2.5
     * Выход: 5.0 (максимальная ценность достигается при выборе одного предмета с индексом 2)
     */
    public static double solveKnapsackProblem(double[] values, double[] weights, double maximumWeightCapacity) {
        return solve(values, weights, maximumWeightCapacity, values.length);
    }

    private static double solve(double[] costs, double[] weights, double maxW, int n){
        // В теории наверно можно переписать это в виде динамического программирования, но меня смущают дробные числа.
        // Поэтому оставил так
        if ((n == 0) || (maxW == 0) || (costs.length != weights.length))
            return 0;

        if (weights[n - 1] > maxW)
            return solve(costs, weights, maxW, n - 1);

        double a = costs[n - 1] + solve(costs, weights, maxW - weights[n - 1], n - 1);
        double b = solve(costs, weights, maxW, n - 1);
        return Math.max(a, b);
    }

}