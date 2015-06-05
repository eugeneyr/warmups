package info.lynxnet.warmups;

import java.util.LinkedList;
import java.util.List;

/**
 *
 */
public class Permutator {
    public static List<String> generatePermutations(String s) {
        if (s == null || s.length() == 0) {
            return new LinkedList<String>();
        }
        if (s.length() == 1) {
            List<String> list = new LinkedList<String>();
            list.add(s);
            return list;
        }
        List<String> result = generatePermutations(s.substring(0, s.length() - 1));
        char c = s.charAt(s.length() - 1);
        List<String> newList = new LinkedList<String>();
        for (String perm : result) {
            for (int i = perm.length(); i >= 0; i--) {
                newList.add(perm.substring(0, i) + c + perm.substring(i));
            }
        }
        return newList;
    }

    public static void main(String[] vals) {
        List<String> perms = generatePermutations("abcd");
        for (String s : perms) {
            System.out.println(s);
        }
        System.out.println(perms.size());
    }
}
