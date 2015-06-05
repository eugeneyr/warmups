package info.lynxnet.warmups;

public class BinarySearchInSparse {

    static int searchInSparse(String[] list, String s) {
        return searchInSparsePart(list, s, 0, list.length - 1);
    }

    static int searchInSparsePart(String[] list, String s, int start, int end) {
        if (start > end) {
            return -1;
        }
        if (start == end) {
            return list[start].equals(s) ? start : -1;
        }
        while (start <= end && list[start].length() == 0) {
            start++;
        }
        while (end >= start && list[end].length() == 0) {
            end--;
        }
        if (start >= end) {
            return -1;
        }
        if (list[start].equals(s)) {
            return start;
        }
        if (list[end].equals(s)) {
            return end;
        }
        int middle = (end + start) / 2;
        while (middle >= start && list[middle].length() == 0) {
            middle--;
        }
        int comp = list[middle].compareTo(s);
        if (comp == 0) {
            return middle;
        }
        if (comp < 0) {
            return searchInSparsePart(list, s, (end + start) / 2 + 1, end);
        }
        return searchInSparsePart(list, s, start, middle);
    }

    public static void main(String[] args) {
        String[] arr = {"", "a", "bc", "ca", "", "", "", "cd", "", "d", "ee", "ff", "", "", "", "", "m", "np", "op", "r", "ss", ""};
        System.out.println(searchInSparse(arr, "at"));
        System.out.println(searchInSparse(arr, "a"));
        System.out.println(searchInSparse(arr, "ff"));
        System.out.println(searchInSparse(arr, "op"));
        System.out.println(searchInSparse(arr, "q"));
        System.out.println(searchInSparse(arr, "z"));
    }
}
