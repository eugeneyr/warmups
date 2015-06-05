package info.lynxnet.warmups;

import java.util.LinkedList;
import java.util.List;

public class MaxSequenceFinder {

    public static List<Integer> findMaxSequence(int[] array) {
        List<List<Integer>> sequences = new LinkedList<List<Integer>>();
        for (int i = 0; i < array.length; i++) {
            if (i == 0) {
                List<Integer> result = new LinkedList<Integer>();
                result.add(array[i]);
                sequences.add(result);
            } else {
                int maxLength = 0;
                int bestSequence = -1;
                for (int j = i - 1; j >= 0; j--) {
                    List<Integer> prevSequence = sequences.get(j);
                    if (prevSequence.get(prevSequence.size() - 1) < array[i]) {
                        if (prevSequence.size() > maxLength) {
                            maxLength = prevSequence.size();
                            bestSequence = j;
                        }
                    }
                }
                List<Integer> result = new LinkedList<Integer>();
                if (bestSequence >= 0) {
                    result.addAll(sequences.get(bestSequence));
                }
                result.add(array[i]);
                sequences.add(result);
            }
        }
        return sequences.get(sequences.size() - 1);
    }

    public static void main(String[] args) {
        int[] arr = {2, 4, 3, 5, 1, 7, 6, 9, 8};
        //(65, 100) (70, 150) (56, 90) (75, 190) (60, 95) (68, 110)
        for (int i : findMaxSequence(arr)) {
            System.out.print(i);
            System.out.print(" ");
        }
        System.out.println();
    }
}
