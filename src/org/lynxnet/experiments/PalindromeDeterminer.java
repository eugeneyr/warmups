package org.lynxnet.experiments;

public class PalindromeDeterminer {

    public boolean isPalindrome(String s) {
        if (s == null) {
            return false;
        }
        if (s.length() == 0) {
            return true;
        }
        for (int i = 0; i < s.length() / 2; i++) {
            if (Character.toUpperCase(s.charAt(i)) != Character.toUpperCase(s.charAt(s.length() - 1 - i))) {
                return false;
            }
        }
        return true;
    }

}
