package info.lynxnet.warmups.crossword.failures;

import info.lynxnet.warmups.Direction;
import info.lynxnet.warmups.crossword.ShapelessWordBucket;
import info.lynxnet.warmups.crossword.WordPosition;

import java.util.*;

/**
 * The key principles of this class:
 * - We do not try to look ahead, so canBeAdded() is not recursive.
 * - At every step the board is a valid crossword.
 * - If there are top or bottom immediate neighbors for the newly placed letter, the word it contains can be added if and only if
 *   the crossing word that will be formed by addition is already present at this location.
 */
public class NoLookAheadsCrosswordInABucket implements Cloneable {
    private int width;
    private int height;
    private List<WordPosition> rightWords = new ArrayList<WordPosition>();
    private List<WordPosition> downWords = new ArrayList<WordPosition>();
    private Set<String> words = new LinkedHashSet<String>();
    private Character[][] playField;
    private Map<Integer, TreeSet<WordPosition>> rightMap = new HashMap<Integer, TreeSet<WordPosition>>();
    private Map<Integer, TreeSet<WordPosition>> downMap = new HashMap<Integer, TreeSet<WordPosition>>();
    private ShapelessWordBucket availableWords;

    public NoLookAheadsCrosswordInABucket(int width, int height) {
        this.width = width;
        this.height = height;
    }

    public Character[][] buildPlayField() throws IllegalStateException {
        Character[][] board = new Character[width][height];
        for (WordPosition wp : rightWords) {
            int x = wp.getX();
            int y = wp.getY();
            if (x < 0 || x >= width) {
                throw new IllegalStateException("The word does not fit in the board");
            }
            if (y < 0 || y >= height) {
                throw new IllegalStateException("The word does not fit in the board");
            }
            String word = wp.getWord();
            if (word.length() + x > width) {
                throw new IllegalStateException("The word does not fit in the board");
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
            if (x < 0 || x >= width) {
                throw new IllegalStateException("The word does not fit in the board");
            }
            if (y < 0 || y >= height) {
                throw new IllegalStateException("The word does not fit in the board");
            }
            String word = wp.getWord();
            if (word.length() + y > height) {
                throw new IllegalStateException("The word does not fit in the board");
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

    public boolean isValid() {
        try {
            Character[][] board = buildPlayField();
        } catch (IllegalStateException e) {
            return false;
        }
        return true;
    }

    public boolean canBeAdded(int x, int y, Direction dir, String word) {
        if (playField == null) {
            playField = buildPlayField();
        }
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

            // cannot place the word right next to another one, there should be at least 1-cell gap
            if (x > 0 && playField[x - 1] != null && playField[x - 1][y] != null) {
                return false;
            }
            if (word.length() + x < width && playField[word.length() + x] != null && playField[word.length() + x][y] != null) {
                return false;
            }
            // placing the letters
            for (int i = 0; i < word.length(); i++) {
                char c = word.charAt(i);

                if (playField[x + i][y] != null) {
                    if (playField[x + i][y] != c) {
                        // the cell is occupied, and the letter does not match
                        return false;
                    }
                    // this letter matches, moving on
                } else {
                    // we are about to set a new letter in a cell
                    String prefix = "";
                    String suffix = "";
                    int yIdx = y - 1;
                    while (yIdx >= 0 && playField[x] != null && playField[x][yIdx] != null) {
                        prefix = playField[x][yIdx] + prefix;
                        yIdx--;
                    }
                    yIdx = y + 1;
                    while (yIdx < height && playField[x] != null && playField[x][yIdx] != null) {
                        suffix = suffix + playField[x][yIdx];
                        yIdx++;
                    }
                    if (prefix.length() > 0 && suffix.length() > 0) {
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

            // cannot place the word right next to another one, there should be at least 1-cell gap
            if (y > 0 && playField[x] != null && playField[x][y - 1] != null) {
                return false;
            }
            if (word.length() + y < height && playField[x] != null && playField[x][word.length() + y] != null) {
                return false;
            }

            for (int i = 0; i < word.length(); i++) {
                char c = word.charAt(i);
                if (playField[x][y + i] != null) {
                    if (playField[x][y + i] != c) {
                        return false;
                    }
                } else {
                    String prefix = "";
                    String suffix = "";
                    int xIdx = x - 1;
                    while (xIdx >= 0 && playField[xIdx] != null && playField[xIdx][y] != null) {
                        prefix = playField[xIdx][y] + prefix;
                        xIdx--;
                    }
                    xIdx = x + 1;
                    while (xIdx < width && playField[xIdx] != null && playField[xIdx][y] != null) {
                        suffix = suffix + playField[xIdx][y];
                        xIdx++;
                    }
                    if (prefix.length() > 0 && suffix.length() > 0) {
                        return false;
                    }
                }
            }
            return true;
        }
        return false;
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
//        if (canBeAdded(x, y, dir, word)) {
            WordPosition wp = new WordPosition(x, y, dir, word);
            if (dir == Direction.RIGHT) {
                for (int i = 0; i < word.length(); i++) {
                    char c = word.charAt(i);
                    if (playField[x + i] == null) {
                        playField[x + i] = new Character[height];
                    }
                    if (playField[x + i][y] != null) {
                        if (playField[x + i][y] != c) {
                            throw new IllegalStateException("There is a word conflict at: (" + (x + i) + "," + y
                                    + "). Conflicting word: " + word);
                        }
                    } else {
                        playField[x + i][y] = c;
                    }
                }
                rightWords.add(wp);
                addToMap(wp, rightMap);
            } else if (dir == Direction.DOWN) {
                if (playField[x] == null) {
                    playField[x] = new Character[height];
                }
                for (int i = 0; i < word.length(); i++) {
                    char c = word.charAt(i);
                    if (playField[x][y + i] != null) {
                        if (playField[x][y + i] != c) {
                            throw new IllegalStateException("There is a word conflict at: (" + x + ","
                                    + (y + i) + "). Conflicting word: " + word);
                        }
                    } else {
                        playField[x][y + i] = c;
                    }
                }
                downWords.add(wp);
                addToMap(wp, downMap);
            } else {
                throw new IllegalArgumentException("Wrong direction: " + wp.getDirection() + ", word: " + word);
            }
            words.add(word);
            return wp;
//        }
//        return null;
    }

    public boolean deleteAt(int x, int y) {
        Collection<WordPosition> words = getWordsAt(x, y);
        if (words.size() == 0) {
            return false;
        }
        for (WordPosition wp : words) {
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
        playField = buildPlayField();
        return true;
    }

    public Collection<WordPosition> getWordsAt(int x, int y) {
        if (playField == null) {
            playField = buildPlayField();
        }
        if (x < 0 || y < 0 || x >= width || y >= height) {
            return Collections.EMPTY_SET;
        }
        if (playField[x][y] == null) {
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
        if (playField == null) {
            playField = buildPlayField();
        }
        if (x < 0 || y < 0 || x >= width || y >= height) {
            return null;
        }
        return playField[x][y];
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

    public int countFilledCells() {
        if (playField == null) {
            playField = buildPlayField();
        }
        return countFilledCells(playField);
    }

    public static int countFilledCells(Character[][] board) {
        if (board == null) {
            return 0;
        }
        int count = 0;
        for (Character[] row : board) {
            if (row != null) {
                for (Character c : row) {
                    if (c != null) {
                        count++;
                    }
                }
            }
        }
        return count;
    }

    public void add(WordPosition wp) {
        if (canBeAdded(wp.getX(), wp.getY(), wp.getDirection(), wp.getWord())) {
            int x = wp.getX();
            int y = wp.getY();
            Direction dir = wp.getDirection();
            String word = wp.getWord();
            if (dir == Direction.RIGHT) {
                for (int i = 0; i < word.length(); i++) {
                    char c = word.charAt(i);
                    if (playField[x + i] == null) {
                        playField[x + i] = new Character[height];
                    }
                    if (playField[x + i][y] != null) {
                        if (playField[x + i][y] != c) {
                            throw new IllegalStateException("There is a word conflict at: (" + (x + i) + "," + y
                                    + "). Conflicting word: " + word);
                        }
                    } else {
                        playField[x + i][y] = c;
                    }
                }
                rightWords.add(wp);
                addToMap(wp, rightMap);
            } else if (dir == Direction.DOWN) {
                if (playField[x] == null) {
                    playField[x] = new Character[height];
                }
                for (int i = 0; i < word.length(); i++) {
                    char c = word.charAt(i);
                    if (playField[x][y + i] != null) {
                        if (playField[x][y + i] != c) {
                            throw new IllegalStateException("There is a word conflict at: (" + x + ","
                                    + (y + i) + "). Conflicting word: " + word);
                        }
                    } else {
                        playField[x][y + i] = c;
                    }
                }
                downWords.add(wp);
                addToMap(wp, downMap);
            } else {
                throw new IllegalArgumentException("Wrong direction: " + wp.getDirection() + ", word: " + word);
            }
            words.add(word);
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
        playField = buildPlayField();
    }

    public void rebuildPlayField() {
        playField = buildPlayField();
    }

    public Character[][] getPlayField() {
        return playField;
    }

    public Map<Integer, TreeSet<WordPosition>> getRightMap() {
        return rightMap;
    }

    public Map<Integer, TreeSet<WordPosition>> getDownMap() {
        return downMap;
    }

    public ShapelessWordBucket getAvailableWords() {
        return availableWords;
    }

    public void setAvailableWords(ShapelessWordBucket availableWords) {
        this.availableWords = availableWords;
    }

    @Override
    protected NoLookAheadsCrosswordInABucket clone() throws CloneNotSupportedException {
        NoLookAheadsCrosswordInABucket other = (NoLookAheadsCrosswordInABucket) super.clone();
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
        other.playField = other.buildPlayField();
        return other;
    }
}

