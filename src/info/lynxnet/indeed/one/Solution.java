package info.lynxnet.indeed.one;

import java.util.Scanner;

public class Solution {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        int n = sc.nextInt();
        Integer knownDiff = null;
        Integer missing = null;
        Integer lastRead = null;
        for (int i = 0; i < n; i++) {
            int num = sc.nextInt();
            if (lastRead != null) {
                int diff = num - lastRead;
                if (knownDiff == null) {
                    knownDiff = diff;
                } else {
                    if (Math.abs(knownDiff) > Math.abs(diff)) {
                        missing = lastRead - diff;
                        break;
                    } else if (Math.abs(knownDiff) < Math.abs(diff)) {
                        missing = num - knownDiff;
                        break;
                    }
                }
            }
            lastRead = num;
        }
        if (missing != null) {
            System.out.println(missing);
        }
    }
}
