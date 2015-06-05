package info.lynxnet.warmups;

import java.util.Collections;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.Set;

/**
 *
 */
public class SubsetGenerator {

    static public Set<Set<String>> generateSubsets(Set<String> set) {
        if (set == null || set.isEmpty()) {
            Set<Set<String>> result = new LinkedHashSet<Set<String>>();
            result.add(Collections.EMPTY_SET);
            return result;
        }
        String head = set.iterator().next();
        set.remove(head);
        Set<Set<String>> result = generateSubsets(set);
        Set<Set<String>> subs = new LinkedHashSet<Set<String>>();
        for (Set<String> subset : result) {
            Set<String> newsub = new LinkedHashSet<String>(subset);
            newsub.add(head);
            subs.add(newsub);
        }
        result.addAll(subs);
        return result;
    }

    public static void main(String[] args) {
        Set<String> set = new HashSet<String>();
        set.add("a");
        set.add("b");
        set.add("c");
        set.add("d");
        set.add("e");
        set.add("f");
        Set<Set<String>> subsets = generateSubsets(set);
        System.out.println("Number of subsets: " + subsets.size());
        for (Set<String> subset : subsets) {
            System.out.print("[");
            for (String s : subset) {
                System.out.print(s + " ");
            }
            System.out.println("]");
        }

    }
}
