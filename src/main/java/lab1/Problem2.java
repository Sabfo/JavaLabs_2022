package lab1;

import java.util.Arrays;
import java.util.stream.IntStream;

public class Problem2 {

    /**
     * Метод segregateEvenAndOddNumbers разделяет четные и нечетные числа в массиве array, т.у. возвращает массив,
     * в котором сперва записаны все четные числа массива array в порядке их следования, а затем все нечетные числа
     * массива array в порядке их следования.
     *
     * @param array массив положительных чисел
     * @return массив с разделенными четными и нечетными числами
     * <p>
     * ПРИМЕР:
     * Вход: array = [2, 1, 5, 6, 8]
     * Выход: [2, 6, 8, 1, 5]
     */
    public static int[] segregateEvenAndOddNumbers(int[] array) {
        // Time complexity: O(n), Auxiliary space complexity: O(n)
        int size = array.length, countEven = 0;
        for (int el : array)
            if (el % 2 == 0)
                countEven++;
        if (countEven == 0 || countEven == size)
            return array;
        int[] evens = new int[countEven];
        int[] odds = new int[size - countEven];
        int iEven = 0, iOdd = 0;
        for (int el : array) {
            if (el % 2 == 0) {
                evens[iEven] = el;
                iEven++;
            } else {
                odds[iOdd] = el;
                iOdd++;
            }
        }
        return IntStream.concat(Arrays.stream(evens), Arrays.stream(odds)).toArray();
    }

}