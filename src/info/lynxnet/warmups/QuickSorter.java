package info.lynxnet.warmups;

import java.util.Random;

public class QuickSorter {
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

    static void quickSort(int[] arr, int l, int r) {
        if (l >= r) {
            return;
        }
        int q = partition(arr, l, r);
        for (int i = l; i <= r; i++) {
            System.out.print(arr[i]);
            System.out.print(" ");
        }
        System.out.println();
       
        quickSort(arr, l, q - 1);
        quickSort(arr, q + 1, r);
    }

    public static void main(String[] args) {
        int[] arr = {2, 5, 3, 1, -4, 0, 9, -3};
        quickSort(arr, 0, arr.length - 1);
        for (int i : arr) {
            System.out.print(i);
            System.out.print(" ");
        }
        System.out.println();
    }
}
