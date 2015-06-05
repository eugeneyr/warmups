package info.lynxnet.warmups.crossword.historical;

import info.lynxnet.warmups.Direction;
import info.lynxnet.warmups.crossword.ShapelessBucketFiller;
import info.lynxnet.warmups.crossword.ShapelessWordBucket;
import info.lynxnet.warmups.crossword.WordPosition;

import java.io.PrintStream;
import java.util.Collection;
import java.util.LinkedHashSet;
import java.util.LinkedList;
import java.util.Set;

/*
*
* TODO
*
* - Add the concept of free space and iterating over it (seems to be the only thing missing on the way to recreating the test task)
* - Profile, see how realistic its use is in case of big boards / dictionaries
* - Add all the required tackle from the marathon problem (loading the dictionary, getting the list of words, metrics etc)
* - Parallelize lookups
*
* - Mathematically..... Do we need to go through configurations that do not stem from the current root?
*
* - Get back to thinking what to do with the ultimate builder - the one that allows close neighbors.
*   The cross between the rectangle builder and the crossword builder? Can it be done more or less efficiently?
*
*
* */
public class BadHistoricalCrosswordBuilder {
    private ShapelessWordBucket bucket;
    private Collection<HistoricalCrosswordConfiguration> maxConfigs = new LinkedList<HistoricalCrosswordConfiguration>();
    private long count = 0;
    private long maxCount = 0;
    private ConfigCollection history = new ConfigCollection();
    private int width;
    private int height;
    private int maxWords = 0;
    private int exhaustive = 0;
    private long repeats = 0;

    private long startTimestamp = 0;
    private long recentTimestamp = 0;

    public Collection<HistoricalCrosswordConfiguration> getMaxConfigs() {
        return maxConfigs;
    }

    public BadHistoricalCrosswordBuilder(ShapelessWordBucket bucket, int width, int height) {
        this.bucket = bucket;
        this.width = width;
        this.height = height;
    }

    public void kickOff() {
        startTimestamp = System.currentTimeMillis();
        recentTimestamp = startTimestamp;
        for (String word : bucket.getWords()) {
            System.out.println("Kicking off the word: " + word);
            Set<String> otherWords = new LinkedHashSet<String>();
            otherWords.addAll(bucket.getWords());
            otherWords.remove(word);
            // A simple upper bound for the max position of the
            // find the shortest word in the allwords - current
            // the upper bound will be its length + 1
            int minX = width - word.length();
            for (String otherWord : otherWords) {
                if (minX > otherWord.length()) {
                    minX = otherWord.length();
                }
            }
            System.out.println("Upper bound for it: " + minX);
            for (int y = 0; y < 2; y++) {
                int upperBound = minX;
                if (minX > 0 && y == 1) {
                    upperBound--;
                }
                for (int x = 0; x <= upperBound; x++) {
                    HistoricalCrosswordConfiguration currentConfig = new HistoricalCrosswordConfiguration(bucket, width, height);
                    if (currentConfig.canBeAdded(x, y, Direction.RIGHT, word)) {
                        WordPosition wp = new WordPosition(x, y, Direction.RIGHT, word);
                        currentConfig.add(wp);
                        try {
                            if (!history.alreadyWas(currentConfig)) {
                                ConfigTreeNode root = new ConfigTreeNode(wp, null);
                                diveDeeper(currentConfig);
                            } else {
                                repeats++;
                            }
                        } catch (Throwable e) {
                            e.printStackTrace();
                        }
                    }
                }
            }
        }

        long now = System.currentTimeMillis();
        System.out.printf("*** DONE! Analyzed %d configurations in %d seconds.\n*** Found %d crosswords that include all words.\n"
                + "*** Detected repeating configurations %d times.\n",
                count, (now - startTimestamp) / 1000, exhaustive, repeats);
    }

    public void diveDeeper(HistoricalCrosswordConfiguration config) {
        count++;
        history.add(config);

        if (count % 10000 == 0) {
            long now = System.currentTimeMillis();
            System.out.printf("*** Iteration: %d. Unique configurations: %d\n*** Time it took to process 10000 placements: %d sec\n",
                    count + 1, history.getHistory().size(), (now - recentTimestamp) / 1000);
            recentTimestamp = now;
        }

        Set<String> availableWords = new LinkedHashSet<String>(config.getDictionary().getWords());
        availableWords.removeAll(config.getWords());

        if (availableWords.isEmpty()) {
            try {
                maxConfigs.add(config.clone());
            } catch (CloneNotSupportedException e) {
                e.printStackTrace();
            }
            maxCount++;
            exhaustive++;
            System.out.printf("Exhaustive: Max %d (total tries: %d)\n", exhaustive, count);
            prettyPrint(config, System.out);
            return;
        }

        ShapelessWordBucket bucket = config.getDictionary();

        boolean wentDeeper = false;
        for (String newWord : availableWords) {
            // for each letter in the word:
            for (int idx = 0; idx < newWord.length(); idx++) {
                char c = newWord.charAt(idx);
                Set<String> potentialCrossingWords = bucket.getWords(c);
                //   find all words in the crossword that have the same letter
                potentialCrossingWords.retainAll(config.getWords());
                //   for each word in the set:
                for (String potentCross : potentialCrossingWords) {
                    WordPosition crossWp = config.getWordsToPositions().get(potentCross);
                    for (int crossIdx = 0; crossIdx < potentCross.length(); crossIdx++) {
                        char cc = potentCross.charAt(crossIdx);
                        //      find all possible crossings (coinciding letters)
                        if (cc == c) {
                            //      for each crossing:
                            //        check if the new word can be placed
                            Direction newDir = crossWp.getDirection() == Direction.RIGHT ? Direction.DOWN : Direction.RIGHT;
                            int x = 0;
                            int y = 0;
                            if (newDir == Direction.DOWN) {
                                x = crossWp.getX() + crossIdx;
                                y = crossWp.getY() - idx;
                            } else {
                                x = crossWp.getX() - idx;
                                y = crossWp.getY() + crossIdx;
                            }
                            if (config.canBeAdded(x, y, newDir, newWord)) {
                                HistoricalCrosswordConfiguration newConfig = null;
                                try {
                                    newConfig = config.clone();
                                } catch (CloneNotSupportedException e) {
                                    e.printStackTrace();
                                }

                                WordPosition wp = new WordPosition(x, y, newDir, newWord);
                                newConfig.add(wp);

                                if (!history.alreadyWas(newConfig)) {
                                    wentDeeper = true;
                                    diveDeeper(newConfig);
                                } else {
                                    repeats++;
                                }
                            }
                        }
                    }
                }
            }
        }
        if (!wentDeeper) {
            if (maxWords < config.getWords().size()) {
                maxWords = config.getWords().size();
                try {
                    maxConfigs.add(config.clone());
                } catch (CloneNotSupportedException e) {
                    e.printStackTrace();
                }
                System.out.printf("The new best dead-end: %d words (total tries: %d)\n", config.getWords().size(), count);
                prettyPrint(config, System.out);
//            } else if (maxWords == config.getWords().size()) {
//                System.out.printf("Not the worst dead-end: %d words (total tries: %d)\n", config.getWords().size(), count);
//                prettyPrint(config, System.out);
            }
        }
    }

    static void prettyPrint(HistoricalCrosswordConfiguration config, PrintStream out) {
        Character[][] board = config.buildPlayField();
        if (board != null) {
            out.println("=============");
            for (Character[] row : board) {
                if (row != null) {
                    for (Character c : row) {
                        if (c != null) {
                            out.print(c);
                        } else {
                            out.print(' ');
                        }
                    }
                    out.println();
                } else {
                    out.println();
                }
            }
//            out.println("-------------");
//            for (String word : config.getWords()) {
//                out.println(config.getWordsToPositions().get(word));
//            }
            out.println("=============");
        }
    }

    void testX() {
        HistoricalCrosswordConfiguration config = new HistoricalCrosswordConfiguration(bucket, width, height);
        config.add(new WordPosition(0, 1, Direction.RIGHT, "linen"));
        config.add(new WordPosition(3, 0, Direction.DOWN, "lemon"));
        config.add(new WordPosition(1, 0, Direction.DOWN, "win"));
        System.out.println(config.canBeAdded(0, 3, Direction.RIGHT, "taxol"));
    }

    public static void main(String[] args) {
//        ShapelessWordBucket bucket = ShapelessBucketFiller.loadBucket("test5_simple.txt");
//        BoundBoardlessCrosswordBuilder builder = new BoundBoardlessCrosswordBuilder(bucket, 5, 5);

//        ShapelessWordBucket bucket = ShapelessBucketFiller.loadBucket("test.txt");
//        BoundBoardlessCrosswordBuilder builder = new BoundBoardlessCrosswordBuilder(bucket, 13, 13);

        ShapelessWordBucket bucket = ShapelessBucketFiller.loadBucket("topcoder.txt");
        BadHistoricalCrosswordBuilder builder = new BadHistoricalCrosswordBuilder(bucket, 11, 11);

        builder.kickOff();
        for (HistoricalCrosswordConfiguration config : builder.getMaxConfigs()) {
            prettyPrint(config, System.out);
        }
    }
}
