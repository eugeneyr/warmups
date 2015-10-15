package info.lynxnet.warmups;

import java.util.*;

public class LargestWordSquareFinder {
    static BucketList betterGetTheBucket(String fileName) {
        return BucketFiller.loadBucket(fileName);
    }

    static long timestamp;
    static long counter = 0;

    static List<String> findSquare(BucketList bucketList, int x) {
        WordBucket bucket = bucketList.getBuckets().get(x);
        System.out.println("Preprocessing...");

        if (bucket == null) {
            return null;
        }

        Set<String> words = bucket.getWords();
        TrieNode trie = new TrieNode();
        for (String word : words) {
            trie.addWord(word);
        }

        counter = 0;
        timestamp = System.currentTimeMillis();
        return buildSquare(words, x, 0, trie, new ArrayList<String>(x));
    }

    static List<String> buildSquare(Set<String> dictionary, int size, int currIdx, TrieNode trie, List<String> result) {
        if (counter % 10000000 == 1) {
            long now = System.currentTimeMillis();
            long difference = ((now - timestamp) / 1000);
            System.out.println("Time spent so far: " + difference);
            System.out.println("Variants checked: " + counter + "(" + (counter / (difference + 1)) + " per second)");
            System.out.println("\t\t\t\tTrying:");
            for (String s : result) {
                System.out.println("\t\t\t\t\t" + s);
            }
        }
        counter++;

        if (currIdx == size) {
            // found!!!
            return result;
        }

        Set<String> current = new HashSet<String>(result);
        LinkedHashSet<String> megaset = new LinkedHashSet<String>();

        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < currIdx; i++) {
            sb.append(result.get(i).charAt(currIdx));
        }
        String prefix = sb.toString();
        TrieNode node = trie.findWord(prefix);
        if (node == null) {
            // no words with this prefix, backtracking
            return null;
        }
        Collection<String> suffixes = node.getAllWords();
        for (String suffix : suffixes) {
            String word = prefix + suffix;
            if (!current.contains(word)) {
                megaset.add(word);
            }
        }
        if (megaset.isEmpty()) {
            // no words with this prefix, backtracking
            return null;
        }

        for (String candidate : megaset) {
            List<String> newList = new LinkedList<String>(result);
            newList.add(candidate);
            List<String> words = buildSquare(dictionary, size, currIdx + 1, trie, newList);
            if (words != null) {
                return words;
            }
        }
        // nothing found, backtracking
        return null;
    }

    static void test() {
        timestamp = System.currentTimeMillis();
        System.out.println("Started at: " + new Date(timestamp));
        List<String> result = findSquare(betterGetTheBucket("WORD.LST"), 7);
        if (result != null) {
            for (String s : result) {
                System.out.println(s);
            }
        }
        long now = System.currentTimeMillis();
        System.out.println("Variants checked: " + counter);
        System.out.println("Finished at: " + new Date());
        System.out.println("Time spent: " + ((now - timestamp) / 1000));
    }

    public static void main(String[] args) {
        test();
    }
}
