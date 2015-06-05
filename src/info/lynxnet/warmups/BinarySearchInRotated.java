package info.lynxnet.warmups;

import java.util.Arrays;

/**
 *
 */
public class BinarySearchInRotated {
    static int searchInRotated(int [] arr, int i) {
        return rsearch(arr, i, 0, arr.length - 1);
    }

    static int rsearch(int[] arr, int val, int left, int right) {
        if (left == right) {
            return arr[left] == val ? left : -1;
        }
        if (left > right) {
            return -1;
        }
        int middle = (right + left) / 2;
        if (arr[middle] == val) {
            return middle;
        }
        if (arr[left] < arr[middle]) {
            if (arr[middle] > val && arr[left] <= val) {
                return bsearch(arr, val, left, middle);
            } else {
                return rsearch(arr, val, middle + 1, right);
            }
        } else {
            if (arr[middle] < val && arr[right] >= val) {
                return bsearch(arr, val, middle + 1, right);
            } else {
                return rsearch(arr, val, left, middle);
            }
        }
    }

    static int bsearch(int[] arr, int val, int left, int right) {
        if (left >= right) {
            return arr[left] == val ? left : -1;
        }
        int middle = (right + left) / 2;
        int diff = arr[middle] - val;
        if (diff < 0) {
            return bsearch(arr, val, middle + 1, right);
        } else if (diff > 0) {
            return bsearch(arr, val, left, middle);
        }
        return arr[middle] == val ? middle : -1;
    }

    static int[] rotate(int[] arr, int idx) {
        int[] result = new int[arr.length];
        for (int i = 0; i < arr.length; i++) {
            result[(i + idx) % arr.length] = arr[i];
        }
        return result;
    }

    public static void main(String[] args) {
        int[] array = {4, 5, 3, 6, 12, 44, 60, 16, 9, 49, 32, 21, 14, 17, 28, 1};
        Arrays.sort(array);
        System.out.println(Arrays.toString(array));
        int[] rotated = rotate(array, 5);
        System.out.println(Arrays.toString(rotated));
        System.out.println(searchInRotated(rotated, 3));
        System.out.println(searchInRotated(rotated, 49));
        System.out.println(searchInRotated(rotated, 80));
        System.out.println(searchInRotated(rotated, 1));
        System.out.println(searchInRotated(rotated, 2));
    }
}
