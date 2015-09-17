package info.lynxnet.warmups.crossword.straitforward;

import info.lynxnet.warmups.Direction;
import info.lynxnet.warmups.crossword.ShapelessWordBucket;
import info.lynxnet.warmups.crossword.WordCrossing;
import info.lynxnet.warmups.crossword.WordPosition;

import java.io.Serializable;
import java.util.*;

public class CrosswordConfiguration implements Serializable, Cloneable {
    private int width;
    private int height;
    private char[] board;

    private List<WordPosition> rightWords = new ArrayList<WordPosition>();
    private List<WordPosition> downWords = new ArrayList<WordPosition>();

    private Set<String> words = new LinkedHashSet<String>();
    private Map<String, WordPosition> wordsToPositions = new LinkedHashMap<String, WordPosition>();

    private Map<Integer, HashSet<WordPosition>> rightMap = new HashMap<Integer, HashSet<WordPosition>>();
    private Map<Integer, HashSet<WordPosition>> downMap = new HashMap<Integer, HashSet<WordPosition>>();

    private ShapelessWordBucket dictionary;

    public CrosswordConfiguration(ShapelessWordBucket bucket, int width, int height) {
        dictionary = bucket;
        this.height = height;
        this.width = width;
    }

    public Character[][] buildPlayField() throws IllegalStateException {
        Character[][] board = new Character[width][height];
        for (WordPosition wp : rightWords) {
            int x = wp.getX();
            int y = wp.getY();
            if (x < 0 || x > width) {
                throw new IllegalStateException("Unexpected inconsistency: the X coordinate does not fit in the board");
            }
            if (y < 0 || y > height) {
                throw new IllegalStateException("Unexpected inconsistency: the Y coordinate does not fit in the board");
            }
            String word = wp.getWord();
            if (word.length() + x > width) {
                throw new IllegalStateException("Unexpected inconsistency: the word goes over the right edge of the board");
            }
            for (int i = 0; i < word.length(); i++) {
                char c = word.charAt(i);
                if (board[x + i] == null) {
                    board[x + i] = new Character[height];
                }
                if (board[x + i][y] != null) {
                    if (board[x + i][y] != c) {
                        throw new IllegalStateException("There is a word conflict at: (" + (x + i) + "," + y + "). Conflicting word: " + word);
                    }
                } else {
                    board[x + i][y] = c;
                }
            }
        }

        for (WordPosition wp : downWords) {
            int x = wp.getX();
            int y = wp.getY();
            if (x < 0 || x > width) {
                throw new IllegalStateException("Unexpected inconsistency: the word " + wp.getWord() + " does not fit on the board");
            }
            if (y < 0 || y > height) {
                throw new IllegalStateException("Unexpected inconsistency: the word " + wp.getWord() + " does not fit on the board");
            }
            String word = wp.getWord();
            if (word.length() + y > height) {
                throw new IllegalStateException("Unexpected inconsistency: the word " + wp.getWord() + " does not fit on the board");
            }
            if (board[x] == null) {
                board[x] = new Character[height];
            }
            for (int i = 0; i < word.length(); i++) {
                char c = word.charAt(i);
                if (board[x][y + i] != null) {
                    if (board[x][y + i] != c) {
                        throw new IllegalStateException("There is a word conflict at: (" + x + "," + (y + i) + "). Conflicting word: " + word);
                    }
                } else {
                    board[x][y + i] = c;
                }
            }
        }
        return board;
    }

    public char[] getBoard() {
        if (this.board != null) {
            return board;
        }
        this.board = new char[this.height * this.width];
        Character[][] pf = this.buildPlayField();
        Arrays.fill(this.board, ' ');
        for (int j = 0; j < this.height; j++) {
            for (int i = 0; i < this.width; i++) {
                if (pf[i] != null && pf[i][j] != null) {
                    this.board[j * this.height + i] = pf[i][j];
                }
            }
        }
        return this.board;
    }

    public boolean isValid() {
        for (WordPosition righty : rightWords) {
            // words across
            for (int i = 0; i < righty.getWord().length(); i++) {
                HashSet<WordPosition> set = downMap.get(righty.getX() + i);
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
            HashSet<WordPosition> set = rightMap.get(righty.getY());
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
                HashSet<WordPosition> set = rightMap.get(down.getY() + i);
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
            HashSet<WordPosition> set = downMap.get(down.getX());
            for (WordPosition wp : set) {
                if (!wp.equals(down) && isOverlapping(down, wp)) {
                    System.out.println("Overlapping words: " + down.toString() + ",  " + wp.toString());
                    return false;
                }
            }
        }
        return true;
    }

    private LinkedHashSet<WordPosition> filter(Collection<WordPosition> wps, Direction direction) {
        LinkedHashSet<WordPosition> result = new LinkedHashSet<WordPosition>();
        for (WordPosition wp : wps) {
            if (wp.getDirection() == direction) {
                result.add(wp);
            }
        }
        return result;
    }

    public boolean canBeAdded(int x, int y, Direction dir, String word) {
        // prohibit duplicates
        if (words.contains(word)) {
            return false;
        }
        if (dir == Direction.RIGHT) {
            if (x < 0 || x >= width) {
                return false;
            }
            if (y < 0 || y >= height) {
                return false;
            }
            if (word.length() + x > width) {
                return false;
            }

            // cannot touch other words with either of it ends
            if (getCharacterAt(x - 1, y) != null) {
                return false;
            }
            if (getCharacterAt(x + word.length(), y) != null) {
                return false;
            }

            WordPosition wp = new WordPosition(x, y, dir, word);
            HashSet<WordPosition> set = rightMap.get(y);
            // check for overlapping with or "touching" other "right" words
            if (set != null) {
                for (WordPosition righty : set) {
                    if (isOverlapping(righty, wp)) {
                        return false;
                    }
                }
            }
            // check for intersecting "down" words
            for (int i = 0; i < word.length(); i++) {
                // a somewhat simpler, more exhaustive, but probably less effective, way to check
                char c = word.charAt(i);
                Character at = getCharacterAt(x + i, y);
                if (at != null && at.charValue() != c) {
                    // there is another character at this spot, and it is different from what we got
                    return false;
                }
                Collection<WordPosition> crossers = getWordsAt(x + i, y);
                if (crossers.size() > 1) {
                    // a basic check: if there is more than one word at this spot, we cannot place more
                    return false;
                }
                // We are not interested in other "right" words above and below as long as they are covered with
                Character up = getCharacterAt(x + i, y - 1);
                if (up != null && at == null) {
                    return false;
                }
                Character down = getCharacterAt(x + i, y + 1);
                if (down != null && at == null) {
                    return false;
                }
                LinkedHashSet<WordPosition> uppers = filter(getWordsAt(x + i, y - 1), Direction.DOWN);
                if (up != null && uppers.isEmpty()) {
                    // it means there is a "righty" right above the position not crossed by the "downy"
                    return false;
                }
                Collection<WordPosition> downers = filter(getWordsAt(x + i, y + 1), Direction.DOWN);
                if (down != null && downers.isEmpty()) {
                    // it means there is a "righty" right below the position not crossed by the "downy"
                    return false;
                }
                // The funny part:
                // if there is a character right above or right below, the word can be placed iif the letter it brings in is already
                // a part of another existing word with a DIFFERENT direction

                // 1) the spot is clean, it means that we "bridge" two different words - we cannot do it
                if (crossers.isEmpty() && (!uppers.isEmpty() || !downers.isEmpty())) {
                    return false;
                } else if (!crossers.isEmpty() && !uppers.isEmpty() && !downers.isEmpty()) {
                    WordPosition upper = uppers.iterator().next();
                    WordPosition downer = downers.iterator().next();
                    WordPosition crosser = uppers.iterator().next();
                    if (!upper.equals(downer) || !upper.equals(crosser) || !downer.equals(crosser)) {
                        return false;
                    }
                } else if (!crossers.isEmpty() && !uppers.isEmpty()) {
                    WordPosition upper = uppers.iterator().next();
                    WordPosition crosser = crossers.iterator().next();
                    if (!upper.equals(crosser)) {
                        return false;
                    }
                } else if (!crossers.isEmpty() && !downers.isEmpty()) {
                    WordPosition downer = downers.iterator().next();
                    WordPosition crosser = crossers.iterator().next();
                    if (!downer.equals(crosser)) {
                        return false;
                    }
                }
            }
            return true;
        } else if (dir == Direction.DOWN) {
            if (x < 0 || x >= width) {
                return false;
            }
            if (y < 0 || y >= height) {
                return false;
            }
            if (word.length() + y > height) {
                return false;
            }

            // cannot touch other words with either of it ends
            if (getCharacterAt(x, y - 1) != null) {
                return false;
            }
            if (getCharacterAt(x, y + word.length()) != null) {
                return false;
            }

            // check for overlapping with or "touching" other "down" words
            WordPosition wp = new WordPosition(x, y, dir, word);
            HashSet<WordPosition> set = downMap.get(x);
            if (set != null) {
                for (WordPosition downy : set) {
                    if (isOverlapping(downy, wp)) {
                        return false;
                    }
                }
            }
            // check for intersecting "right" words
            for (int i = 0; i < word.length(); i++) {
                // (Transpose the checks for righties)
                // a somewhat simpler, more exhaustive, but probably less effective, way to check
                char c = word.charAt(i);
                Character at = getCharacterAt(x, y + i);
                if (at != null && at.charValue() != c) {
                    // there is another character at this spot, and it is different from what we got
                    return false;
                }
                Collection<WordPosition> crossers = getWordsAt(x, y + i);
                if (crossers.size() > 1) {
                    // a basic check: if there is more than one word at this spot, we cannot place more
                    return false;
                }
                // We are not interested in other "right" words above and below as long as they are covered with
                Character left = getCharacterAt(x - 1, y + i);
                if (left != null && at == null) {
                    return false;
                }
                Character right = getCharacterAt(x + 1, y + i);
                if (right != null && at == null) {
                    return false;
                }

                LinkedHashSet<WordPosition> lefters = filter(getWordsAt(x - 1, y + i), Direction.RIGHT);
                if (left != null && lefters.isEmpty()) {
                    // it means there is a "righty" right above the position not crossed by the "righty"
                    return false;
                }
                Collection<WordPosition> righters = filter(getWordsAt(x + 1, y + i), Direction.RIGHT);
                if (right != null && righters.isEmpty()) {
                    // it means there is a "righty" right above the position not crossed by the "righty"
                    return false;
                }
                // The funny part:
                // if there is a character right above or right below, the word can be placed iif the letter it brings in is already
                // a part of another existing word with a DIFFERENT direction

                // 1) the spot is clean, it means that we "bridge" two different words - we cannot do it
                if (crossers.isEmpty() && (!lefters.isEmpty() || !righters.isEmpty())) {
                    return false;
                } else if (!crossers.isEmpty() && !lefters.isEmpty() && !righters.isEmpty()) {
                    WordPosition lefter = lefters.iterator().next();
                    WordPosition righter = righters.iterator().next();
                    WordPosition crosser = lefters.iterator().next();
                    if (!lefter.equals(righter) || !lefter.equals(crosser) || !righter.equals(crosser)) {
                        return false;
                    }
                } else if (!crossers.isEmpty() && !lefters.isEmpty()) {
                    WordPosition lefter = lefters.iterator().next();
                    WordPosition crosser = crossers.iterator().next();
                    if (!lefter.equals(crosser)) {
                        return false;
                    }
                } else if (!crossers.isEmpty() && !righters.isEmpty()) {
                    WordPosition righter = righters.iterator().next();
                    WordPosition crosser = crossers.iterator().next();
                    if (!righter.equals(crosser)) {
                        return false;
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
            HashSet<WordPosition> set = rightMap.get(y);
            if (set != null) {
                for (WordPosition righty : set) {
                    if (isOverlapping(righty, wp)) {
                        throw new IllegalStateException("The word " + word + " is overlapping with " + righty.toString());
                    }
                }
            }
            // check for intersecting "down" words
            for (int i = 0; i < word.length(); i++) {
                HashSet<WordPosition> downSet = downMap.get(x + i);
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
            HashSet<WordPosition> set = downMap.get(x);
            if (set != null) {
                for (WordPosition downy : set) {
                    if (isOverlapping(downy, wp)) {
                        throw new IllegalStateException("The word " + word + " is overlapping with " + downy.toString());
                    }
                }
            }
            // check for intersecting "right" words
            for (int i = 0; i < word.length(); i++) {
                HashSet<WordPosition> rightSet = rightMap.get(y + i);
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
                if (wp1.getX() >= wp2.getX() + wp2.getWord().length() + 1) {
                    return false;
                }
                if (wp2.getX() >= wp1.getX() + wp1.getWord().length() + 1) {
                    return false;
                }
            }
            if (wp1.getDirection() == Direction.DOWN) {
                if (wp1.getX() != wp2.getX()) {
                    return false;
                }
                if (wp1.getY() >= wp2.getY() + wp2.getWord().length() + 1) {
                    return false;
                }
                if (wp2.getY() >= wp1.getY() + wp1.getWord().length() + 1) {
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

    private static void addToMap(WordPosition wp, Map<Integer, HashSet<WordPosition>> map) {
        int position = wp.getDirection() == Direction.RIGHT ? wp.getY() : wp.getX();
        HashSet<WordPosition> set = map.get(position);
        if (set == null) {
            set = new HashSet<WordPosition>();
            map.put(position, set);
        }
        set.add(wp);
    }

    private static void removeFromMap(WordPosition wp, Map<Integer, HashSet<WordPosition>> map) {
        int position = wp.getDirection() == Direction.RIGHT ? wp.getY() : wp.getX();
        HashSet<WordPosition> set = map.get(position);
        if (set != null) {
            set.remove(wp);
        }
    }

    public WordPosition add(int x, int y, Direction dir, String word) {
        if (canBeAdded(x, y, dir, word)) {
            if (x < 0) {
                throw new IllegalArgumentException("The X coordinate is Beyond The Zero");
            }
            if (y < 0) {
                throw new IllegalArgumentException("The Y coordinate is Beyond The Zero");
            }
            WordPosition wp = new WordPosition(x, y, dir, word);
            if (dir == Direction.RIGHT) {
                rightWords.add(wp);
                addToMap(wp, rightMap);
                if (x + word.length() > width) {
                    throw new IllegalArgumentException("The X coordinate is Beyond The Pale");
                }
            } else if (dir == Direction.DOWN) {
                downWords.add(wp);
                addToMap(wp, downMap);
                if (y + word.length() > height) {
                    throw new IllegalArgumentException("The Y coordinate is Beyond The Pale");
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
        if (x < 0 || y < 0 || x > width || y > height) {
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

    public Character getCharacterAt(int x, int y) {
        if (x < 0 || y < 0 || x > width || y > height) {
            return null;
        }
        for (WordPosition wp : rightWords) {
            if (y == wp.getY() && x >= wp.getX() && x < wp.getX() + wp.getWord().length()) {
                return wp.getWord().charAt(x - wp.getX());
            }
        }
        for (WordPosition wp : downWords) {
            if (x == wp.getX() && y >= wp.getY() && y < wp.getY() + wp.getWord().length()) {
                return wp.getWord().charAt(y - wp.getY());
            }
        }
        return null;
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
                    HashSet<WordPosition> set = downMap.get(i);
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
                    HashSet<WordPosition> set = rightMap.get(i);
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
            if (0 > x) {
                throw new IllegalArgumentException("x < 0: " + x);
            }
            if (0 > y) {
                throw new IllegalArgumentException("y < 0: " + y);
            }
            if (dir == Direction.RIGHT) {
                rightWords.add(wp);
                addToMap(wp, rightMap);
                if (x + word.length() > width) {
                    throw new IllegalArgumentException("x + word > width: " + (x + word.length()));
                }
            } else if (dir == Direction.DOWN) {
                downWords.add(wp);
                addToMap(wp, downMap);
                if (y + word.length() > height) {
                    throw new IllegalArgumentException("y + word > height: " + (y + word.length()));
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

    public Map<Integer, HashSet<WordPosition>> getRightMap() {
        return rightMap;
    }

    public Map<Integer, HashSet<WordPosition>> getDownMap() {
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
    public CrosswordConfiguration clone() throws CloneNotSupportedException {
        CrosswordConfiguration other = (CrosswordConfiguration) super.clone();
        other.board = null;
        other.rightWords = new ArrayList<WordPosition>(rightWords);
        other.downWords = new ArrayList<WordPosition>(downWords);
        other.words = new LinkedHashSet<String>(words);
        other.rightMap = new HashMap<Integer, HashSet<WordPosition>>();
        other.downMap = new HashMap<Integer, HashSet<WordPosition>>();
        for (Map.Entry<Integer, HashSet<WordPosition>> entry : rightMap.entrySet()) {
            other.rightMap.put(entry.getKey(), new HashSet<WordPosition>(entry.getValue()));
        }
        for (Map.Entry<Integer, HashSet<WordPosition>> entry : downMap.entrySet()) {
            other.downMap.put(entry.getKey(), new HashSet<WordPosition>(entry.getValue()));
        }
        other.wordsToPositions = new LinkedHashMap<String, WordPosition>();
        for (Map.Entry<String, WordPosition> entry : wordsToPositions.entrySet()) {
            other.wordsToPositions.put(entry.getKey(), entry.getValue());
        }
        return other;
    }

    public int getWidth() {
        return width;
    }

    public void setWidth(int width) {
        this.width = width;
    }

    public int getHeight() {
        return height;
    }

    public void setHeight(int height) {
        this.height = height;
    }

//    public boolean isSupersetOf(CrosswordConfiguration otherConfig) {
//        for (WordPosition wp : otherConfig.rightWords) {
//            if (!rightWords.contains(wp)) {
//                return false;
//            }
//        }
//        for (WordPosition wp : otherConfig.downWords) {
//            if (!downWords.contains(wp)) {
//                return false;
//            }
//        }
//        return true;
//    }

    public boolean isSupersetOf(CrosswordConfiguration otherConfig) {
        char[] otherBoard = otherConfig.getBoard();
        char[] myBoard = this.getBoard();
        for (int i = 0; i < otherBoard.length; i++) {
            if (myBoard[i] != otherBoard[i] && otherBoard[i] != ' ') {
                return false;
            }
        }
        return true;
    }

}

