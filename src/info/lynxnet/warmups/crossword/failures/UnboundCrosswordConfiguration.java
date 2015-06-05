package info.lynxnet.warmups.crossword.failures;

import info.lynxnet.warmups.Direction;
import info.lynxnet.warmups.crossword.ShapelessWordBucket;
import info.lynxnet.warmups.crossword.WordCrossing;
import info.lynxnet.warmups.crossword.WordPosition;

import java.io.Serializable;
import java.util.*;

public class UnboundCrosswordConfiguration implements Serializable, Cloneable {
    private int minX;
    private int minY;

    private int maxX;
    private int maxY;

    private List<WordPosition> rightWords = new ArrayList<WordPosition>();
    private List<WordPosition> downWords = new ArrayList<WordPosition>();

    private Set<String> words = new LinkedHashSet<String>();
    private Map<String, WordPosition> wordsToPositions = new LinkedHashMap<String, WordPosition>();

    private Map<Integer, TreeSet<WordPosition>> rightMap = new HashMap<Integer, TreeSet<WordPosition>>();
    private Map<Integer, TreeSet<WordPosition>> downMap = new HashMap<Integer, TreeSet<WordPosition>>();

    private ShapelessWordBucket dictionary;

    public UnboundCrosswordConfiguration(ShapelessWordBucket bucket) {
        dictionary = bucket;
    }

    public UnboundCrosswordConfiguration() {
    }

    public Character[][] buildPlayField() throws IllegalStateException {
        Character[][] board = new Character[maxX - minX + 1][maxY - minY + 1];
        for (WordPosition wp : rightWords) {
            int x = wp.getX();
            int y = wp.getY();
            if (x < minX || x > maxX) {
                throw new IllegalStateException("Unexpected inconsistency: maxX or minX were not calculated properly");
            }
            if (y < minY || y > maxY) {
                throw new IllegalStateException("Unexpected inconsistency: maxY or minY were not calculated properly");
            }
            String word = wp.getWord();
            if (word.length() + x > maxX) {
                throw new IllegalStateException("Unexpected inconsistency: maxX was not calculated properly");
            }
            for (int i = 0; i < word.length(); i++) {
                char c = word.charAt(i);
                if (board[x + i - minX] == null) {
                    board[x + i - minX] = new Character[maxY - minY + 1];
                }
                if (board[x + i - minX][y - minY] != null) {
                    if (board[x + i - minX][y - minY] != c) {
                        throw new IllegalStateException("There is a word conflict at: (" + (x + i) + "," + y + "). Conflicting word: " + word);
                    }
                } else {
                    board[x + i - minX][y - minY] = c;
                }
            }
        }

        for (WordPosition wp : downWords) {
            int x = wp.getX();
            int y = wp.getY();
            if (x < minX || x > maxX) {
                throw new IllegalStateException("Unexpected inconsistency: maxX or minX were not calculated properly");
            }
            if (y < minY || y > maxY) {
                throw new IllegalStateException("Unexpected inconsistency: maxY or minY were not calculated properly");
            }
            String word = wp.getWord();
            if (word.length() + y > maxY) {
                throw new IllegalStateException("Unexpected inconsistency: maxY was not calculated properly");
            }
            if (board[x - minX] == null) {
                board[x - minX] = new Character[maxY - minY + 1];
            }
            for (int i = 0; i < word.length(); i++) {
                char c = word.charAt(i);
                if (board[x - minX][y + i - minY] != null) {
                    if (board[x - minX][y + i - minY] != c) {
                        throw new IllegalStateException("There is a word conflict at: (" + x + "," + (y + i) + "). Conflicting word: " + word);
                    }
                } else {
                    board[x - minX][y + i - minY] = c;
                }
            }
        }
        return board;
    }

    public boolean isValid() {
        for (WordPosition righty : rightWords) {
            // words across
            for (int i = 0; i < righty.getWord().length(); i++) {
                TreeSet<WordPosition> set = downMap.get(righty.getX() + i);
                if (set != null) {
                    Set<WordPosition> crossers = new LinkedHashSet<WordPosition>();
                    for (WordPosition downy : set) {
                        try {
                            WordCrossing wc = getCrossing(righty, downy);
                            if (wc != null) {
                                crossers.add(downy);
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                            return false;
                        }
                    }
                    if (crossers.size() > 1) {
                        System.out.println("Too many words crossing " + righty.toString() + " at " + i);
                        return false;
                    }
                }
            }
            // words in the same row
            TreeSet<WordPosition> set = rightMap.get(righty.getY());
            for (WordPosition wp : set) {
                if (!wp.equals(righty) && isOverlapping(righty, wp)) {
                    System.out.println("Overlapping words: " + righty.toString() + ",  " + wp.toString());
                    return false;
                }
            }
        }
        for (WordPosition down : downWords) {
            // words across
            for (int i = 0; i < down.getWord().length(); i++) {
                TreeSet<WordPosition> set = rightMap.get(down.getY() + i);
                if (set != null) {
                    Set<WordPosition> crossers = new LinkedHashSet<WordPosition>();
                    for (WordPosition right : set) {
                        try {
                            WordCrossing wc = getCrossing(right, down);
                            if (wc != null) {
                                crossers.add(right);
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                            return false;
                        }
                    }
                    if (crossers.size() > 1) {
                        System.out.println("Too many words crossing " + down.toString() + " at " + i);
                        return false;
                    }
                }
            }
            TreeSet<WordPosition> set = downMap.get(down.getX());
            for (WordPosition wp : set) {
                if (!wp.equals(down) && isOverlapping(down, wp)) {
                    System.out.println("Overlapping words: " + down.toString() + ",  " + wp.toString());
                    return false;
                }
            }
        }
        return true;
    }

    public boolean canBeAdded(int x, int y, Direction dir, String word) {
        // prohibit duplicates
        if (words.contains(word)) {
            return false;
        }
        if (dir == Direction.RIGHT) {
            WordPosition wp = new WordPosition(x, y, dir, word);
            TreeSet<WordPosition> set = rightMap.get(y);
            if (set != null) {
                for (WordPosition righty : set) {
                    if (isOverlapping(righty, wp)) {
                        return false;
                    }
                }
            }
            // check for intersecting "down" words
            for (int i = 0; i < word.length(); i++) {
                TreeSet<WordPosition> downSet = downMap.get(x + i);
                if (downSet != null) {
                    char c = word.charAt(i);
                    for (WordPosition downWord : downSet) {
                        if (downWord.getY() <= y && downWord.getWord().length() + downWord.getY() > y) {
                            // an intersecting word - we don't check here if there is more than one
                            char downC = downWord.getWord().charAt(y - downWord.getY());
                            if (downC != c) {
                                return false;
                            }
                        }
                    }
                }
            }
            return true;
        } else if (dir == Direction.DOWN) {
            for (int i = 0; i < word.length(); i++) {
                char c = word.charAt(i);
            }
            // check for overlapping with other "down" words
            WordPosition wp = new WordPosition(x, y, dir, word);
            TreeSet<WordPosition> set = downMap.get(x);
            if (set != null) {
                for (WordPosition downy : set) {
                    if (isOverlapping(downy, wp)) {
                        return false;
                    }
                }
            }
            // check for intersecting "right" words
            for (int i = 0; i < word.length(); i++) {
                TreeSet<WordPosition> rightSet = rightMap.get(y + i);
                if (rightSet != null) {
                    char c = word.charAt(i);
                    for (WordPosition rightWord : rightSet) {
                        if (rightWord.getX() <= x && rightWord.getWord().length() + rightWord.getX() > x) {
                            // an intersecting word - we don't check here if there is more than one
                            char downC = rightWord.getWord().charAt(x - rightWord.getX());
                            if (downC != c) {
                                return false;
                            }
                        }
                    }
                }
            }
            return true;
        }
        return false;
    }

    public Collection<WordPosition> findCrossingWords(int x, int y, Direction dir, String word) {
        // prohibit duplicates
        if (words.contains(word)) {
            throw new IllegalStateException("The word " + word + " is already in the puzzle");
        }
        Set<WordPosition> crossers = new LinkedHashSet<WordPosition>();
        if (dir == Direction.RIGHT) {
            WordPosition wp = new WordPosition(x, y, dir, word);
            TreeSet<WordPosition> set = rightMap.get(y);
            if (set != null) {
                for (WordPosition righty : set) {
                    if (isOverlapping(righty, wp)) {
                        throw new IllegalStateException("The word " + word + " is overlapping with " + righty.toString());
                    }
                }
            }
            // check for intersecting "down" words
            for (int i = 0; i < word.length(); i++) {
                TreeSet<WordPosition> downSet = downMap.get(x + i);
                if (downSet != null) {
                    char c = word.charAt(i);
                    for (WordPosition downWord : downSet) {
                        if (downWord.getY() <= y && downWord.getWord().length() + downWord.getY() > y) {
                            // an intersecting word - we don't check here if there is more than one
                            char downC = downWord.getWord().charAt(y - downWord.getY());
                            if (downC != c) {
                                throw new IllegalStateException("The word " + word + " is conflicting with " + downWord);
                            }
                            crossers.add(downWord);
                        }
                    }
                }
            }
        } else if (dir == Direction.DOWN) {
            for (int i = 0; i < word.length(); i++) {
                char c = word.charAt(i);
            }
            // check for overlapping with other "down" words
            WordPosition wp = new WordPosition(x, y, dir, word);
            TreeSet<WordPosition> set = downMap.get(x);
            if (set != null) {
                for (WordPosition downy : set) {
                    if (isOverlapping(downy, wp)) {
                        throw new IllegalStateException("The word " + word + " is overlapping with " + downy.toString());
                    }
                }
            }
            // check for intersecting "right" words
            for (int i = 0; i < word.length(); i++) {
                TreeSet<WordPosition> rightSet = rightMap.get(y + i);
                if (rightSet != null) {
                    char c = word.charAt(i);
                    for (WordPosition rightWord : rightSet) {
                        if (rightWord.getX() <= x && rightWord.getWord().length() + rightWord.getX() > x) {
                            // an intersecting word - we don't check here if there is more than one
                            char downC = rightWord.getWord().charAt(x - rightWord.getX());
                            if (downC != c) {
                                throw new IllegalStateException("The word " + word + " is conflicting with " + rightWord);
                            }
                            crossers.add(rightWord);
                        }
                    }
                }
            }
        }
        return crossers;
    }

    public static boolean isOverlapping(WordPosition wp1, WordPosition wp2) {
        if (wp1.getDirection() == wp2.getDirection()) {
            if (wp1.getDirection() == Direction.RIGHT) {
                if (wp1.getY() != wp2.getY()) {
                    return false;
                }
                if (wp1.getX() >= wp2.getX() + wp2.getWord().length()) {
                    return false;
                }
                if (wp2.getX() >= wp1.getX() + wp1.getWord().length()) {
                    return false;
                }
            }
            if (wp1.getDirection() == Direction.DOWN) {
                if (wp1.getX() != wp2.getX()) {
                    return false;
                }
                if (wp1.getY() >= wp2.getY() + wp2.getWord().length()) {
                    return false;
                }
                if (wp2.getY() >= wp1.getY() + wp1.getWord().length()) {
                    return false;
                }
            }
            return true;
        }
        WordPosition down;
        WordPosition right;
        if (wp1.getDirection() == Direction.DOWN) {
            down = wp1;
            right = wp2;
        } else {
            down = wp2;
            right = wp1;
        }
        if (down.getY() > right.getY()) {
            return false;
        }
        if (right.getX() > down.getX()) {
            return false;
        }
        if (down.getY() + down.getWord().length() <= right.getY()) {
            return false;
        }
        if (right.getX() + right.getWord().length() <= down.getX()) {
            return false;
        }
        return true;
    }

    public static WordCrossing getCrossing(WordPosition right, WordPosition down) {
        if (right.getDirection() != Direction.RIGHT || down.getDirection() != Direction.DOWN) {
            throw new IllegalArgumentException("Wrong word directions");
        }
        if (!isOverlapping(right, down)) {
            return null;
        }
        int x = down.getX();
        int y = right.getY();
        char rightChar = right.getWord().charAt(x - right.getX());
        char downChar = down.getWord().charAt(y - down.getY());
        if (rightChar != downChar) {
            throw new IllegalStateException("Unexpected inconsistency: " + rightChar + " != " + downChar);
        }
        return new WordCrossing(right, down);
    }

    private static void addToMap(WordPosition wp, Map<Integer, TreeSet<WordPosition>> map) {
        int position = wp.getDirection() == Direction.RIGHT ? wp.getY() : wp.getX();
        TreeSet<WordPosition> set = map.get(position);
        if (set == null) {
            set = new TreeSet<WordPosition>();
            map.put(position, set);
        }
        set.add(wp);
    }

    private static void removeFromMap(WordPosition wp, Map<Integer, TreeSet<WordPosition>> map) {
        int position = wp.getDirection() == Direction.RIGHT ? wp.getY() : wp.getX();
        TreeSet<WordPosition> set = map.get(position);
        if (set != null) {
            set.remove(wp);
        }
    }

    public WordPosition add(int x, int y, Direction dir, String word) {
        if (canBeAdded(x, y, dir, word)) {
            if (minX > x) {
                minX = x;
            }
            if (minY > y) {
                minY = y;
            }
            WordPosition wp = new WordPosition(x, y, dir, word);
            if (dir == Direction.RIGHT) {
                rightWords.add(wp);
                addToMap(wp, rightMap);
                if (x + word.length() > maxX) {
                    maxX = x + word.length();
                }
            } else if (dir == Direction.DOWN) {
                downWords.add(wp);
                addToMap(wp, downMap);
                if (y + word.length() > maxY) {
                    maxY = y + word.length();
                }
            } else {
                throw new IllegalArgumentException("Wrong direction: " + wp.getDirection() + ", word: " + word);
            }
            words.add(word);
            wordsToPositions.put(word, wp);
            return wp;
        }
        return null;
    }

    public boolean deleteAt(int x, int y) {
        Collection<WordPosition> wordsAt = getWordsAt(x, y);
        if (wordsAt.size() == 0) {
            return false;
        }
        for (WordPosition wp : wordsAt) {
            delete(wp);
        }
        return true;
    }

    public void delete(WordPosition wp) {
        if (wp.getDirection() == Direction.RIGHT) {
            rightWords.remove(wp);
            removeFromMap(wp, rightMap);
        } else if (wp.getDirection() == Direction.DOWN) {
            downWords.remove(wp);
            removeFromMap(wp, downMap);
        } else {
            throw new IllegalStateException("Wrong direction: " + wp.getDirection() + ", word: " + wp.getWord());
        }
        words.remove(wp.getWord());
        wordsToPositions.remove(wp.getWord());
    }


    public Collection<WordPosition> getWordsAt(int x, int y) {
        if (x < minX || y < minY || x > maxX || y > maxY) {
            return Collections.EMPTY_SET;
        }
        Set<WordPosition> result = new LinkedHashSet<WordPosition>();
        for (WordPosition wp : rightWords) {
            if (y == wp.getY() && x >= wp.getX() && x < wp.getX() + wp.getWord().length()) {
                result.add(wp);
            }
        }
        for (WordPosition wp : downWords) {
            if (x == wp.getX() && y >= wp.getY() && y < wp.getY() + wp.getWord().length()) {
                result.add(wp);
            }
        }
        return result;
    }

    public int getMinX() {
        return minX;
    }

    public void setMinX(int minX) {
        this.minX = minX;
    }

    public int getMinY() {
        return minY;
    }

    public void setMinY(int minY) {
        this.minY = minY;
    }

    public int getMaxX() {
        return maxX;
    }

    public void setMaxX(int maxX) {
        this.maxX = maxX;
    }

    public int getMaxY() {
        return maxY;
    }

    public void setMaxY(int maxY) {
        this.maxY = maxY;
    }

    public List<WordPosition> getRightWords() {
        return rightWords;
    }

    public List<WordPosition> getDownWords() {
        return downWords;
    }

    public Set<String> getWords() {
        return words;
    }

    public Map<Integer, WordPosition> getIntersectingWords(WordPosition wp) {
        Map<Integer, WordPosition> result = new LinkedHashMap<Integer, WordPosition>();
        switch (wp.getDirection()) {
            case RIGHT:
                if (!rightWords.contains(wp)) {
                    return result;
                }
                for (int i = wp.getX(); i < wp.getX() + wp.getWord().length(); i++) {
                    TreeSet<WordPosition> set = downMap.get(i);
                    if (set != null) {
                        for (WordPosition downer : set) {
                            if (downer.getY() <= wp.getY() && downer.getY() + downer.getWord().length() > wp.getY()) {
                                result.put(i, downer);
                            }
                        }
                    }
                }
                break;
            case DOWN:
                if (!downWords.contains(wp)) {
                    return result;
                }
                for (int i = wp.getY(); i < wp.getY() + wp.getWord().length(); i++) {
                    TreeSet<WordPosition> set = rightMap.get(i);
                    if (set != null) {
                        for (WordPosition righter : set) {
                            if (righter.getX() <= wp.getX() && righter.getX() + righter.getWord().length() > wp.getX()) {
                                result.put(i, righter);
                            }
                        }
                    }
                }
                break;
        }
        return result;
    }

    public int countIntersections() {
        int total = 0;
        for (WordPosition wp : rightWords) {
            total += getIntersectingWords(wp).size();
        }
        return total;
    }

    public void add(WordPosition wp) {
        if (canBeAdded(wp.getX(), wp.getY(), wp.getDirection(), wp.getWord())) {
            int x = wp.getX();
            int y = wp.getY();
            Direction dir = wp.getDirection();
            String word = wp.getWord();
            if (minX > x) {
                minX = x;
            }
            if (minY > y) {
                minY = y;
            }
            if (dir == Direction.RIGHT) {
                rightWords.add(wp);
                addToMap(wp, rightMap);
                if (x + word.length() > maxX) {
                    maxX = x + word.length();
                }
            } else if (dir == Direction.DOWN) {
                downWords.add(wp);
                addToMap(wp, downMap);
                if (y + word.length() > maxY) {
                    maxY = y + word.length();
                }
            } else {
                throw new IllegalArgumentException("Wrong direction: " + wp.getDirection() + ", word: " + word);
            }
            words.add(word);
            wordsToPositions.put(word, wp);
        } else {
            System.out.println("Cannot be added: " + wp.toString());
        }
    }

    public void remove(WordPosition wp) {
        if (wp.getDirection() == Direction.RIGHT) {
            rightWords.remove(wp);
            removeFromMap(wp, rightMap);
        } else if (wp.getDirection() == Direction.DOWN) {
            downWords.remove(wp);
            removeFromMap(wp, downMap);
        } else {
            throw new IllegalStateException("Wrong direction: " + wp.getDirection() + ", word: " + wp.getWord());
        }
        words.remove(wp.getWord());
    }

    public Map<Integer, TreeSet<WordPosition>> getRightMap() {
        return rightMap;
    }

    public Map<Integer, TreeSet<WordPosition>> getDownMap() {
        return downMap;
    }

    public Map<String, WordPosition> getWordsToPositions() {
        return wordsToPositions;
    }

    public ShapelessWordBucket getDictionary() {
        return dictionary;
    }

    public void setDictionary(ShapelessWordBucket dictionary) {
        this.dictionary = dictionary;
    }

    @Override
    public UnboundCrosswordConfiguration clone() throws CloneNotSupportedException {
        UnboundCrosswordConfiguration other = (UnboundCrosswordConfiguration) super.clone();
        other.rightWords = new ArrayList<WordPosition>(rightWords);
        other.downWords = new ArrayList<WordPosition>(downWords);
        other.words = new LinkedHashSet<String>(words);
        other.rightMap = new HashMap<Integer, TreeSet<WordPosition>>();
        other.downMap = new HashMap<Integer, TreeSet<WordPosition>>();
        for (Map.Entry<Integer, TreeSet<WordPosition>> entry : rightMap.entrySet()) {
            other.rightMap.put(entry.getKey(), new TreeSet<WordPosition>(entry.getValue()));
        }
        for (Map.Entry<Integer, TreeSet<WordPosition>> entry : downMap.entrySet()) {
            other.downMap.put(entry.getKey(), new TreeSet<WordPosition>(entry.getValue()));
        }
        other.wordsToPositions = new LinkedHashMap<String, WordPosition>();
        for (Map.Entry<String, WordPosition> entry : wordsToPositions.entrySet()) {
            other.wordsToPositions.put(entry.getKey(), entry.getValue());
        }
        return other;
    }
}

