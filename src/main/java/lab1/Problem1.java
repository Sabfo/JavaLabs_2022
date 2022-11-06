 package lab1;

public class Problem1 {

    /**
     * Метод containsDigitAInHexadecimalRepresentation определяет, содержится ли символ A в шестнадцатиричном
     * представлении числа number.
     *
     * @param number целое положительное число
     * @return true, если шестнадцатиричная запись numbers содержит A
     *         false, если шестнадцатиричная запись numbers не содержит A
     *
     * ПРИМЕР1:
     * Вход: number = 10
     * Выход: true (10 = 0xA, содержит A)
     *
     * ПРИМЕР2:
     * Вход: number = 9
     * Выход: false (9 = 0x9, не содержит A)
     */
    public static boolean containsDigitAInHexadecimalRepresentation(int number) {
        int a = 10, hex = 16;
        while (number > 0) {
            if (number % hex == a)
                return true;
            number /= 10;
        }
        return false;
    }

}
