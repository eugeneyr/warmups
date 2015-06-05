package info.lynxnet.warmups;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

public class CircusTower {
    static class Person implements Comparable<Person> {
        int height;
        int weight;

        Person(int height, int weight) {
            this.height = height;
            this.weight = weight;
        }

        public int compareTo(Person o) {
            if (o.weight == weight) {
                return height - o.height;
            }
            return weight - o.weight;

//            if (o.height == height) {
//                return weight - o.weight;
//            }
//            return height - o.height;
        }

        @Override
        public String toString() {
            return "(" + height +", " + weight +')';
        }
    }

    public static List<Person> findMaxSequence(Person[] array) {
        List<List<Person>> sequences = new LinkedList<List<Person>>();
        for (int i = 0; i < array.length; i++) {
            if (i == 0) {
                List<Person> result = new LinkedList<Person>();
                result.add(array[i]);
                sequences.add(result);
            } else {
                int maxLength = 0;
                int bestSequence = -1;
                for (int j = i - 1; j >= 0; j--) {
                    List<Person> prevSequence = sequences.get(j);
                    if (prevSequence.get(prevSequence.size() - 1).weight < array[i].weight) {
                        if (prevSequence.size() > maxLength) {
                            maxLength = prevSequence.size();
                            bestSequence = j;
                        }
                    }
                }
                List<Person> result = new LinkedList<Person>();
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
        Person[] arr = {new Person(65, 100), new Person(70, 150), new Person(56, 90), new Person(75, 190), new Person(60, 95), new Person(68, 110)};
        Arrays.sort(arr);
        for (Person i : findMaxSequence(arr)) {
            System.out.print(i.toString());
            System.out.print(" ");
        }
        System.out.println();
    }
}
