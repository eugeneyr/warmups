package info.lynxnet.warmups;

import java.util.*;

public class LargestWordRectangleFinder {
    static BucketList betterGetTheBucket(String fileName) {
        return BucketFiller.loadBucket(fileName);
    }

    static long timestamp;
    static long counter = 0;

    static List<String> findRectangle(BucketList bucketList, int sizeX, int sizeY) {
        WordBucket xBucket = bucketList.getBuckets().get(sizeX);
        WordBucket yBucket = bucketList.getBuckets().get(sizeY);
        System.out.println("Preprocessing...");

        if (xBucket == null || yBucket == null) {
            // no words of given size
            return null;
        }

        Set<String> xWords = xBucket.getWords();
        WordBucket xMiniBucket = new SortedWordBucket(xBucket.getWordLength());
        WordBucket yMiniBucket = new SortedWordBucket(yBucket.getWordLength());

        Map<Integer, Set<String>> xCandidates = new HashMap<Integer, Set<String>>();
        for (int j = 0; j < yBucket.getWordLength(); j++) {
            for (String xWord : xWords) {
                boolean valid = true;
                for (int i = 0; i < xWord.length(); i++) {
                    char ch = xWord.charAt(i);
                    if (yBucket.getWords(ch, j).isEmpty()) {
                        valid = false;
                        break;
                    }
                }
                if (valid) {
                    addCandidate(xCandidates, xWord, j);
                    xMiniBucket.addWord(xWord);
                }
            }
            if (xCandidates.get(j) == null) {
                // the square cannot be built
                return null;
            }
        }

        Set<String> yWords = yBucket.getWords();
        Map<Integer, Set<String>> yCandidates = new HashMap<Integer, Set<String>>();
        Map<Integer, TrieNode> yTries = new HashMap<Integer, TrieNode>();
        for (int j = 0; j < xBucket.getWordLength(); j++) {
            for (String yWord : yWords) {
                boolean valid = true;
                for (int i = 0; i < yWord.length(); i++) {
                    char ch = yWord.charAt(i);
                    if (xMiniBucket.getWords(ch, j).isEmpty()) {
                        valid = false;
                        break;
                    }
                }
                if (valid) {
                    addCandidate(yCandidates, yWord, j);
                    addToTrie(yTries, yWord, j);
                    yMiniBucket.addWord(yWord);
                }
            }
            if (yCandidates.get(j) == null) {
                // the square cannot be built
                return null;
            }
        }
        counter = 0;
        timestamp = System.currentTimeMillis();
        return buildRectangle(yTries, xMiniBucket, yMiniBucket, 0, new ArrayList<String>(sizeY));
    }

    static List<String> buildRectangle(Map<Integer, TrieNode> yTries,
                                       WordBucket xBucket, WordBucket yBucket,
                                       int y, List<String> xResult) {
        if (counter % 10000 == 1) {
            long now = System.currentTimeMillis();
            long difference = ((now - timestamp) / 1000);
            System.out.println("Time spent so far: " + difference);
            System.out.println("Variants checked: " + counter + "(" + (counter / (difference + 1)) + " per second)");
            System.out.println("\t\t\t\tTrying:");
            for (String s : xResult) {
                System.out.println("\t\t\t\t\t" + s);
            }
        }
        counter++;

        if (y == yBucket.getWordLength()) {
            // found!!!
            return xResult;
        }
        //Set<String> xSet = xCandidates.get(y);

        Set<String> megaset = new LinkedHashSet<String>();

        StringBuilder sb;
        for (int i = 0; i < xBucket.getWordLength(); i++) {
            sb = new StringBuilder();
            for (int j = 0; j < y; j++) {
                sb.append(xResult.get(j).charAt(i));
            }
            TrieNode trie = yTries.get(i);
            TrieNode node = trie.findWord(sb.toString());
            Set<String> xSet = new LinkedHashSet<String>();
            for (char c : node.getChildren().keySet()) {
                xSet.addAll(xBucket.getWords(c, i));
            }
            if (i == 0) {
                megaset.addAll(xSet);
            } else {
                megaset.retainAll(xSet);
            }
        }

        //Set<String> xSet = xCandidates.get(y);
        for (String xWord : megaset) {
        //for (String xWord : xSet) {
            boolean valid = true;
            for (int i = 0; i < xBucket.getWordLength(); i++) {
                sb = new StringBuilder();
                for (int j = 0; j < y; j++) {
                    sb.append(xResult.get(j).charAt(i));
                }
                sb.append(xWord.charAt(i));
                // prohibit symmetric "squares"
                if (xResult.size() > i &&  xResult.get(i).startsWith(sb.toString())) {
                    valid = false;
                    break;
                }
                TrieNode trie = yTries.get(i);
                if (trie.findWord(sb.toString()) == null) {
                    valid = false;
                    break;
                }
            }
            if (valid) {
                List<String> attempt = new ArrayList<String>(xResult);
                attempt.add(xWord);
                List<String> result = buildRectangle(yTries, xBucket, yBucket, y + 1, attempt);
                if (result != null) {
                    return result;
                }
            }
        }
        return null;
    }

    static private void addCandidate(Map<Integer, Set<String>> candidates, String word, int pos) {
        Set<String> list = candidates.get(pos);
        if (list == null) {
            list = new LinkedHashSet<String>();
            candidates.put(pos, list);
        }
        list.add(word);
    }

    static private void addToTrie(Map<Integer, TrieNode> trieNodes, String word, int pos) {
        TrieNode root = trieNodes.get(pos);
        if (root == null) {
            root = new TrieNode();
            trieNodes.put(pos, root);
        }
        root.addWord(word);
    }

    static void findLargest() {
        BucketList dictionary = betterGetTheBucket("WORD.LST");
        TreeMap<Integer, WordBucket> buckets = dictionary.getBuckets();
        for (Map.Entry<Integer, WordBucket> entry : buckets.descendingMap().entrySet()) {
            int length = entry.getKey();
            System.out.println("Length = " + length);
            for (int i = length; i > 0; i--) {
                System.out.println("Height = " + i);
                if (buckets.containsKey(i)) {
                    timestamp = System.currentTimeMillis();
                    System.out.println("Started at: " + new Date(timestamp));
                    List<String> rectangle = findRectangle(dictionary, length, i);
                    long now = System.currentTimeMillis();
                    System.out.println("Finished at: " + new Date());
                    System.out.println("Time spent: " + ((now - timestamp) / 1000));

                    if (rectangle != null) {
                        for (String s : rectangle) {
                            System.out.println(s);
                        }
                    } else {
                        System.out.println("NOT FOUND");
                    }
                }
            }
        }
    }

    static void test() {
        timestamp = System.currentTimeMillis();
        System.out.println("Started at: " + new Date(timestamp));
        List<String> result = findRectangle(betterGetTheBucket("WORD.LST"), 7, 6);
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
        //findLargest();
    }
}
