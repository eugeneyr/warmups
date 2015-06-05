package info.lynxnet.warmups;

/**
 *
 */
public class StringReverse {

    static void reverse(char[] arr) {
        if (arr == null || arr.length < 2) {
            return;
        }
        int i = 0;
        int j = arr.length - 1;
        while (i < j) {
            char c = arr[i];
            arr[i] = arr[j];
            arr[j] = c;
            i++; j--;
        }
    }

    static void reversePiece(char[] arr, int start, int end) {
        if (arr == null || start < 0 || end >= arr.length || end - start < 2) {
            return;
        }
        int i = start;
        int j = end;
        while (i < j) {
            char c = arr[i];
            arr[i] = arr[j];
            arr[j] = c;
            i++; j--;
        }
    }

    static void reverseWords(char[] arr) {
        if (arr == null || arr.length < 2) {
            return;
        }
        reversePiece(arr, 0, arr.length - 1);
        int start = 0; int end = 0;
        while (start < arr.length) {
            while (start < arr.length && arr[start] == ' ') {
                start++;
            }
            end = start;
            while (end < arr.length && arr[end] != ' ') {
                end++;
            }
            if (start < arr.length) {
                reversePiece(arr, start, end - 1);
            }
            start = end;
        }
    }

    static String reverse(String s) {
        if (s == null) {
            return s;
        }
        char[] arr = s.toCharArray();
        reverse(arr);
        return new String(arr);
    }

    static String reverseWords(String s) {
        if (s == null) {
            return s;
        }
        char[] arr = s.toCharArray();
        reverseWords(arr);
        return new String(arr);
    }

    public static void main(String[] args) {
        System.out.println(reverse(""));
        System.out.println(reverse("a"));
        System.out.println(reverse("ab"));
        System.out.println(reverse("abc"));
        System.out.println(reverse("a man a plan a canal panama"));

        System.out.println(reverseWords(""));
        System.out.println(reverseWords("a"));
        System.out.println(reverseWords("ab"));
        System.out.println(reverseWords("abc"));
        System.out.println(reverseWords("abc def "));
        System.out.println(reverseWords("abc def ghi jkl"));
        System.out.println(reverseWords(" abc def ghi jkl"));
        System.out.println(reverseWords("a man a plan a canal panama"));
    }
}
