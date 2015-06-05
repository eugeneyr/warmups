package info.lynxnet.warmups;

public class HeapSorter {
    static void maxHeapify(int[] arr, int i, int heapSize) {
        int leftIdx = (i << 1) + 1;
        int rightIdx = leftIdx + 1;
        int maxIdx = i;
        if (leftIdx < heapSize) {
            if (arr[i] < arr[leftIdx]) {
                maxIdx = leftIdx;
            }
        }
        if (rightIdx < heapSize) {
            if (arr[i] < arr[rightIdx]) {
                maxIdx = rightIdx;
            }
        }
        if (maxIdx != i) {
            int a = arr[i];
            arr[i] = arr[maxIdx];
            arr[maxIdx] = a;
            maxHeapify(arr, maxIdx, heapSize);
        }
    }

    static void buildMaxHeap(int[] arr) {
        int heapSize = arr.length;
        for (int i = arr.length - 1; i > 0; i--) {
            int a = arr[i];
            arr[i] = arr[0];
            arr[0] = a;
            maxHeapify(arr, 0, heapSize--);
        }
    }

    static void heapSort(int[] arr) {
        buildMaxHeap(arr);
        int heapSize = arr.length;
        for (int i = arr.length - 1; i > 0; i--) {
            int a = arr[i];
            arr[i] = arr[0];
            arr[0] = a;
            heapSize--;
            maxHeapify(arr, 0, heapSize);
        }
    }

    public static void main(String[] args) {
        int[] arr = {2, 5, 3, 1, 0, 9, -3};
        heapSort(arr);
        for (int i : arr) {
            System.out.print(i);
            System.out.print(" ");
        }
        System.out.println();
    }
}
