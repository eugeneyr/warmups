package info.lynxnet.warmups;

import java.util.Arrays;
import java.util.Random;

public class MedianFinder {
    static int partition(int[] arr, int l, int r) {
        if (r <= l) {
            return l;
        }
        Random rand = new Random();
        int randIdx = rand.nextInt(r - l) + l;
        int a = arr[r];
        arr[r] = arr[randIdx];
        arr[randIdx] = a;
        System.out.println("Pivot: " + arr[r]);
        int i = l;
        int j = r - 1;
        while (i <= j) {
            while (arr[i] < arr[r]) {
                i++;
            }
            if (i == r) {
                return r;
            }
            while (j >= l && arr[j] > arr[r]) {
                j--;
            }
            if (i < j) {
                a = arr[i];
                arr[i] = arr[j];
                arr[j] = a;
            }
        }
        a = arr[i];
        arr[i] = arr[r];
        arr[r] = a;
        return i;
    }

    static int findMedian(int[] arr, int m) {
        if (m < 0 || m >= arr.length) {
            throw new IllegalArgumentException("The median index is out of the array bounds");
        }
        int l = 0;
        int r = arr.length - 1;
        while (true) {
            int q = partition(arr, l, r);
            if (q == m) {
                return arr[q];
            }
            if (q < m) {
                l = q + 1;
            } else {
                r = q - 1;
            }
        }
    }

    public static void main(String[] args) {
        int[] arr = {2, 5, 3, 1, -4, 0, 9, -3};
        int medIdx = 4;
        int median = findMedian(arr, medIdx);
        Arrays.sort(arr);
        System.out.println("" + median + " = " + arr[medIdx]);
    }
}
