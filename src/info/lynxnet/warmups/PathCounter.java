package info.lynxnet.warmups;

import java.util.Scanner;

public class PathCounter {
    public static int getPossiblePaths(int n) {
        return countPossiblePaths(n, 1, 1);
    }

    public static int countPossiblePaths(int n, int x, int y) {
        if (n <= 0) {
            return 0;
        }
        if (y == n) {
            return 1;
        }
        if (x == n) {
            return 1;
        }
        int count = 0;
        if (x < n) {
            count = countPossiblePaths(n, x + 1, y);
        }
        if (y < n) {
            count += countPossiblePaths(n, x, y + 1);
        }
        return count;
    }

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        int n;
        do {
            System.out.println("Enter the number: ");
            n = scanner.nextInt();
            if (n > 0) {
                System.out.println(getPossiblePaths(n));
            }
        } while (n > 0);
    }


}
