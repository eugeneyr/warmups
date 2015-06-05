package info.lynxnet.warmups;

import java.util.Arrays;
import java.util.Random;

public class DutchFlagTreat {
    static void swap(int[] arr, int i, int j) {
        int tmp = arr[i];
        arr[i] =  arr[j];
        arr[j] = tmp;
    }

    static int[] sortDutchFlag(int[] arr) {
        int zerosIdx = 0;
        int onesIdx = 0;
        int twosIdx = arr.length - 1;
        while (onesIdx <= twosIdx) {
            int item = arr[onesIdx];
            switch(item) {
                case 0:
                    swap(arr, zerosIdx, onesIdx);
                    zerosIdx++;
                    onesIdx++;
                    break;
                case 1:
                    onesIdx++;
                    break;
                case 2:
                    swap(arr, twosIdx, onesIdx);
                    twosIdx--;
                    break;
            }
        }
        return arr;
    }

    static int[] initializeArray(int size) {
        int[] result = new int[size];
        Random rnd = new Random();
        for (int i = 0; i < size; i++) {
            result[i] = rnd.nextInt(3);
        }
        return result;
    }

    public static void main(String[] args) {
        int[] arr = initializeArray(20);
        System.out.println(Arrays.toString(arr));
        sortDutchFlag(arr);
        System.out.println(Arrays.toString(arr));
    }
}
