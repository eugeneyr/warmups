package info.lynxnet.warmups;

import java.util.*;

/**
 *
 */
public class MegawordSearcher {
    public static String findMegaword(String[] list) {
        if (list == null) {
            return null;
        }
        Arrays.sort(list, new Comparator<String>() {
            public int compare(String s1, String s2) {
                if (s1.length() != s2.length()) {
                    return s2.length() - s1.length();
                }
                return s1.compareTo(s2);
            }
        });
        Set<String> set = new HashSet<String>();
        for (int i = 0; i < list.length; i++) {
            set.clear();
            set.add(list[i]);
            if (reduce(set, list, i)) {
                return list[i];
            }
        }
        return null;
    }

    static void prettyPrint(Set<String> set) {
        System.out.print("[ ");
        for (String s : set) {
            System.out.print(s);
            System.out.print(" ");
        }
        System.out.println("]");
    }

    public static boolean reduce(Set<String> set, String[] list, int i) {
        System.out.print("Called reduce(): ");
        prettyPrint(set);
        if (set.isEmpty()) {
            return true;
        }
        if (i >= list.length) {
            return false;
        }
        Set<String> result = new HashSet<String>(set);
        for (String word : set) {
            for (int j = i + 1; j < list.length; j++) {
                String w = list[j];
                if (word.equals(w)) {
                    result.remove(w);
                }
            }
        }
        if (result.isEmpty()) {
            return true;
        }
        for (String word : result) {
            boolean found = false;
            for (int j = i + 1; j < list.length; j++) {
                String w = list[j];
                if (word.startsWith(w)) {
                    Set<String> spawned = new HashSet<String>(result);
                    spawned.remove(word);
                    spawned.add(word.substring(w.length()));
                    found = reduce(spawned, list, i);
                }
                if (! found && word.endsWith(w)) {
                    Set<String> spawned = new HashSet<String>(result);
                    spawned.remove(word);
                    spawned.add(word.substring(0, word.length() - w.length()));
                    found = reduce(spawned, list, i);
                }
                if (! found && word.contains(w) && !word.startsWith(w) && !word.endsWith(w)) {
                    Set<String> spawned = new HashSet<String>(result);
                    spawned.remove(word);
                    String prefix = word.substring(0, word.indexOf(w));
                    String suffix = word.substring(word.indexOf(w) + w.length());
                    spawned.add(prefix);
                    spawned.add(suffix);
                    found = reduce(spawned, list, i);
                }
                if (found) {
                    return true;
                }
            }
        }
        return false;
    }

    public static void main(String[] args) {
        String[] params = {"test", "tester", "testertest", "testing", "testingtest", "testingtesttester"};
        String longestOne = findMegaword(params);
        if (longestOne != null) {
            System.out.println(longestOne);
        } else {
            System.out.println("NOT FOUND");
        }
    }
}
