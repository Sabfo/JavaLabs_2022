package lab1;


import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.stream.Stream;

public class Problem5 {

    /**
     * Метод isGeometricProgression определяет, является ли данная последовательность чисел numbers геометрической
     * прогрессией (возможно, при перестановке элементов)
     *
     * @param numbers строка, содержащая n положительных целых чисел, разделенных запятой
     * @return true, если последовательность является геометрической прогрессией
     *         false, если последовательность не является геометрической прогрессией
     *
     * ПРИМЕР1:
     * Вход: numbers = "1,2,4,8,16"
     * Выход: true
     *
     * ПРИМЕР2:
     * Вход: numbers = "16,2,8,1,4"
     * Выход: true (так как в результате перестановки элементов можно получить геометрическую прогрессию [1,2,4,8,16])
     *
     * ПРИМЕР3:
     * Вход: numbers = "2,3,5"
     * Выход: false
     */
    public static boolean isGeometricProgression(String numbers) {
        // Будем считать, что последовательность состоящая из 0 и 1 элемента является геометрической последовательностью
        if (!numbers.contains(","))
            return true;
        String[] nums = numbers.split(",");
        int[] arr = Stream.of(nums).mapToInt(Integer::parseInt).toArray();
        if (arr.length < 2)
            return true;
        Arrays.sort(arr);
        int step = arr[1] / arr[0];
        for (int i=1; i< arr.length; i++) {
            if (arr[i-1] * step != arr[i])
                return false;
        }
        return true;
    }

}