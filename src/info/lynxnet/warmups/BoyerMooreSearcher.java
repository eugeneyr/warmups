package info.lynxnet.warmups;

import java.util.*;

public class BoyerMooreSearcher {
    static int[] buildGoodSuffixTable(char[] str) {
        int[] jumps = new int[str.length];
        for (int suffixLength = 1; suffixLength <= str.length; suffixLength++) {
            boolean foundValidMismatch = false;
            int offset;
            // loop over length - 1 downto 0 looking for a valid mismatch (ie, not a_i a_i+1 ... a_N
            for (offset = str.length - 1; !foundValidMismatch && offset >= 0; offset--) {
                // look for a valid mismatch
                // suffix indices: str.length - suffixLength ... strLength - 1
                // mismatching part: str.length - suffixLength
                // matching part: str.length - suffixLength + 1 ... strLength - 1
                // the area to match: offset - suffixLength + 1 ... offset
                boolean allMatching = true;
                for (int suffixIdx = str.length - 1; suffixIdx > str.length - suffixLength; suffixIdx--) {
                    int strIdx = offset - str.length + 1 + suffixIdx;
                    if (strIdx < 0) {
                        allMatching = true;
                        foundValidMismatch = true;
                        break;
                    }
                    if (str[suffixIdx] != str[strIdx]) {
                        allMatching = false;
                        break;
                    }
                }
                if (foundValidMismatch) {
                    break;
                }
                if (!allMatching) {
                    continue;
                }
                if (offset - suffixLength + 1 < 0 || str[offset - suffixLength + 1] != str[str.length - suffixLength]) {
                    foundValidMismatch = true;
                    break;
                }
            }
            jumps[suffixLength - 1] = foundValidMismatch ? str.length - offset - 1: str.length;
        }
        return jumps;
    }

    static Map<Character, Integer> buildBadCharacterTable(char[] str) {
        Map<Character, Integer> result = new HashMap<Character, Integer>();
        for (int i = str.length - 1; i >= 0; i--) {
            if (!result.containsKey(str[i])) {
                result.put(str[i], str.length - 1 - i);
            }
        }
        return result;
    }

    static int getBadCharacterShift(char c, int length, Map<Character, Integer> map) {
        if (map.containsKey(c)) {
            return map.get(c);
        }
        return length;
    }

    public static Collection<Integer> findAllMatches(String needle, String haystack) {
        if (needle == null || haystack == null || needle.length() == 0 || haystack.length() == 0
                || needle.length() > haystack.length()) {
            return Collections.EMPTY_LIST;
        }
        char[] pattern = needle.toCharArray();
        char[] text = haystack.toCharArray();
        int[] goodSuffixTable = buildGoodSuffixTable(pattern);
        Map<Character, Integer> badCharTable = buildBadCharacterTable(pattern);
        int pos = pattern.length - 1;
        Collection<Integer> result = new LinkedList<Integer>();
        while (pos < text.length) {
            boolean match = true;
            int i;
            for (i = 0; i < pattern.length; i++) {
                if (pattern[pattern.length - i - 1] != text[pos - i]) {
                    match = false;
                    break;
                }
            }
            if (!match) {
                int suffixJump = pos + goodSuffixTable[i];
                int charJump = pos - i + getBadCharacterShift(text[pos - i], pattern.length, badCharTable);
                pos = Math.max(charJump, suffixJump);
            } else {
                result.add(pos - pattern.length + 1);
                pos++;
            }
        }
        return result;
    }

    public static void main(String[] args) {
        String pattern = "ANPANMAN";
        String text = "AN PANAMA X ANPANMANPANMANPANMAN Y ANPANFANPANMANANPTANMANARNPANMANANPANMAN";
        Collection<Integer> occurrences = findAllMatches(pattern, text);
        for (int i : occurrences) {
            System.out.println("" + i + ": " + text.substring(i, i + pattern.length()));
        }
        System.out.println("control:");
        for (int i = 0; i < text.length() - pattern.length() + 1; i++) {
            if (text.substring(i).startsWith(pattern)) {
                System.out.println("" + i + ": " + text.substring(i, i + pattern.length()));
            }
        }
    }
}
