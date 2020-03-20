package Lesson6;

import java.util.Arrays;

public class ArrayTask {

    private static final int NUM_FOUR = 4;
    private static final int NUM_ONE = 1;

    public int[] toArrayAfterFour(int[] arr) {
        for (int i = arr.length - 1; i >= 0; i--) {
            if (arr[i] == NUM_FOUR) return Arrays.copyOfRange(arr, i + 1, arr.length);
        }
        throw new RuntimeException();
    }

    public boolean isUnitAndFourInArray(int[] arr) {
        boolean one = false, four = false;
        for (int num : arr) {
            if (num == NUM_ONE) {
                one = true;
                continue;
            }
            if (num == NUM_FOUR) {
                four = true;
                continue;
            }
            return false;
        }
        return one && four;
    }
}

