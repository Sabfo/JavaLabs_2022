package lab1;

import java.util.ArrayList;
import java.util.LinkedList;

public class Problem4 {

    /**
     * Метод flattenMatrix преобразует матрицу размера nxm в одномерный массив, записывая сперва элементы первого столбца,
     * затем элементы второго столбца и т.д.
     *
     * @param matrix матрица размера nxm
     * @return одномерный массив
     *
     * ПРИМЕР:
     * Вход: matrix = [[1, 2, 3],
     *                 [4, 5, 6],
     *                 [7, 8, 9]]
     * Выход: [1, 4, 7, 2, 5, 8, 3, 6, 9]
     */
    public static int[] flattenMatrix(int[][] matrix) {
        int h = matrix.length;
        if (h == 0)
            return new int[0];
        if (h == 1)
            return matrix[0];
        int w = matrix[0].length;
        int[] result = new int[h * w];
        int cnt = 0;
        for (int i=0; i<w; i++) {
            for (int j=0; j<h; j++) {
                result[cnt] = matrix[j][i];
                cnt++;
            }
        }
        return result;
    }

}