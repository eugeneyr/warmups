package info.lynxnet.warmups;

public class PatternMatcher {
    public static boolean isMatching(String pattern, String text) {
        if (pattern.length() == 0 && text.length() == 0) {
            return true;
        }
        if (pattern.length() == 0 && text.length() > 0) {
            return false;
        }
        if (pattern.length() > 0 && text.length() == 0) {
            for (int i = 0; i < pattern.length(); i++) {
                if (pattern.charAt(i) != '*') {
                    return false;
                }
            }
            return true;
        }
        if (pattern.charAt(0) == '?') {
            return isMatching(pattern.substring(1), text.substring(1));
        }
        if (pattern.charAt(0) != '*') {
            return pattern.charAt(0) == text.charAt(0) && isMatching(pattern.substring(1), text.substring(1));
        }
        for (int i = 1; i < text.length(); i++) {
            if (isMatching(pattern.substring(1), text.substring(i))) {
                return true;
            }
        }
        return false;
    }

    public static void main(String[] args) {
        System.out.println("abb abb : " + isMatching("abb", "abb"));
        System.out.println("a?b abb : " + isMatching("a?b", "abb"));
        System.out.println("a*d abcd : " + isMatching("a*d", "abcd"));
        System.out.println("a*c abcd : " + isMatching("a*c", "abcd"));
    }
}
