package info.lynxnet.warmups;

public class LargestSumFinder {
    static class Result {
        int startIndex;
        int endIndex;
        int sum;

        Result(int startIndex, int endIndex, int sum) {
            this.startIndex = startIndex;
            this.endIndex = endIndex;
            this.sum = sum;
        }
    }

    static Result findLargestSum(int[] array, int startIdx, int endIdx) {
        if (startIdx > endIdx) {
            return null;
        }
        if (startIdx < 0 || endIdx >= array.length) {
            return null;
        }
        if (startIdx == endIdx) {
            return new Result(startIdx, endIdx, array[startIdx]);
        }
        int middleIdx = (endIdx + startIdx) / 2;
        Result leftRes = findLargestSum(array, startIdx, middleIdx);
        Result rightRes = findLargestSum(array, middleIdx + 1, endIdx);

        int leftBoundIdx = -1;
        int leftSum = Integer.MIN_VALUE;
        int sum = 0;
        for (int i = middleIdx; i >= startIdx; i--) {
            sum += array[i];
            if (sum > leftSum) {
                leftSum = sum;
                leftBoundIdx = i;
            }
        }
        int rightBoundIdx = -1;
        int rightSum = Integer.MIN_VALUE;
        sum = 0;
        for (int i = middleIdx + 1; i <= endIdx; i++) {
            sum += array[i];
            if (sum > rightSum) {
                rightSum = sum;
                rightBoundIdx = i;
            }
        }
        Result result = new Result(leftBoundIdx, rightBoundIdx, leftSum + rightSum);
        return compareResults(new Result[] {leftRes, rightRes, result});
    }

    static Result compareResults(Result [] results) {
        int max = Integer.MIN_VALUE;
        Result maxRes = null;
        for (Result res : results) {
            if (res != null && res.sum > max) {
                max = res.sum;
                maxRes = res;
            }
        }
        return maxRes;
    }

    public static void main(String[] args) {
        //int[] ints = {2, -8, 3, -2, 4, -10};
        int[] ints = {-3, -10, -5};
        Result res = findLargestSum(ints, 0, ints.length - 1);
        System.out.println("Result: " + res.sum + ", indices: [" + res.startIndex + ".." + res.endIndex + "]");
    }
}
