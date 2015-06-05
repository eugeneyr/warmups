package info.lynxnet.warmups;

import java.util.*;

public class UnsortedWordBucket implements WordBucket {
    private int wordLength;
    private HashSet<String> words = new HashSet<String>();
    private HashMap<Character, Map<Integer, Collection<String>>> charsToPositions = new HashMap<Character, Map<Integer, Collection<String>>>();

    public UnsortedWordBucket(int wordLength) {
        this.wordLength = wordLength;
    }

    public int getWordLength() {
        return wordLength;
    }

    public void setWordLength(int wordLength) {
        this.wordLength = wordLength;
    }

    public HashMap<Character, Map<Integer, Collection<String>>> getCharsToPositions() {
        return charsToPositions;
    }

    public void setCharsToPositions(HashMap<Character, Map<Integer, Collection<String>>> charsToPositions) {
        this.charsToPositions = charsToPositions;
    }

    public void addWord(String word) {
        if (word == null || word.length() != wordLength) {
            throw new IllegalArgumentException("Wrong word length");
        }
        words.add(word);
        for (int i = 0; i < wordLength; i++) {
            char c = word.charAt(i);
            Map<Integer, Collection<String>> positionMap = charsToPositions.get(c);
            if (positionMap == null) {
                positionMap = new HashMap<Integer, Collection<String>>();
                charsToPositions.put(c, positionMap);
            }
            Collection<String> strings = positionMap.get(i);
            if (strings == null) {
                strings = new HashSet<String>();
                positionMap.put(i, strings);
            }
            strings.add(word);
        }
    }

    public Collection<String> getWords(char c, int i) {
        Map<Integer, Collection<String>> positionMap = charsToPositions.get(c);
        if (positionMap != null) {
            Collection<String> strings = positionMap.get(i);
            if (strings != null) {
                return Collections.unmodifiableCollection(strings);
            }
        }
        return Collections.EMPTY_LIST;
    }

    public Set<String> getWords() {
        return Collections.unmodifiableSet(words);
    }
}
