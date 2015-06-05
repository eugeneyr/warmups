package info.lynxnet.warmups.crossword.straitforward;

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
* VERDICT:
*
* Did not fly.
*
*/
public class BadAndSlowStraightforwardCrosswordBuilder {
    private ShapelessWordBucket bucket;
    private Collection<CrosswordConfiguration> maxConfigs = new LinkedList<CrosswordConfiguration>();
    private long count = 0;
    private long maxCount = 0;
    private static Direction[] directions = {Direction.RIGHT, Direction.DOWN};

    private ConfigCollection history = new ConfigCollection();

    private int width;
    private int height;
    private int maxWords = 0;
    private int exhaustive = 0;
    private long repeats = 0;
    private long misfits = 0;

    private long startTimestamp = 0;
    private long recentTimestamp = 0;

    public Collection<CrosswordConfiguration> getMaxConfigs() {
        return maxConfigs;
    }

    public BadAndSlowStraightforwardCrosswordBuilder(ShapelessWordBucket bucket, int width, int height) {
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
            for (String otherWord : bucket.getWords()) {
                if (otherWord.equals(word)) {
                    continue;
                }
                otherWords.add(otherWord);
            }

            // A simple upper bound for the max position of the
            // find the shortest word in the allwords - current
            // the upper bound will be its length + 1
            int minX = width - word.length();
            System.out.println("Upper bound for it: " + minX);
            for (int y = 0; y < height; y++) {
                for (int x = 0; x <= minX; x++) {
                    CrosswordConfiguration currentConfig = new CrosswordConfiguration(bucket, width, height);
                    if (currentConfig.canBeAdded(x, y, Direction.RIGHT, word)) {
                        WordPosition wp = new WordPosition(x, y, Direction.RIGHT, word);
                        currentConfig.add(wp);
                        try {
                            if (!history.alreadyWas(currentConfig)) {
                                diveDeeper(currentConfig);
                            } else {
                                repeats++;
                            }
                        } catch (Throwable e) {
                            e.printStackTrace();
                        }
                    } else {
                        misfits++;
                    }
                }
            }
        }

        long now = System.currentTimeMillis();
        System.out.printf("*** DONE! Analyzed %d configurations in %d seconds.\n*** Found %d crosswords that include all words.\n"
                + "*** Detected repeating configurations %d times.\n",
                count, (now - startTimestamp) / 1000, exhaustive, repeats);
    }

    public void diveDeeper(CrosswordConfiguration config) {
        history.add(config);
        count++;

        if (count % 10000 == 0) {
            long now = System.currentTimeMillis();
            prettyPrint(config, System.out);
            System.out.printf("*** Iteration: %d. Saved configurations: %d. Rejections: %d. Misfits: %d.\n*** Time it took to process 10000 placements: %d sec\n",
                    count + 1, history.getHistory().size(), repeats, misfits, (now - recentTimestamp) / 1000);
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

        boolean wentDeeper = false;

        for (String word : availableWords) {
            for (int x = 0; x < width; x++) {
                for (int y = 0; y < height; y++) {
                    for (Direction direction : directions) {
                        if (config.canBeAdded(x, y, direction, word)) {
                            WordPosition wd = new WordPosition(x, y, direction, word);
                            try {
                                CrosswordConfiguration clone = config.clone();
                                clone.add(wd);
                                if (!history.alreadyWas(clone)) {
                                    wentDeeper = true;
                                    diveDeeper(clone);
                                } else {
                                    repeats++;
                                }
                            } catch (CloneNotSupportedException e) {
                                e.printStackTrace();
                            }
                        } else {
                            misfits++;
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

    static void prettyPrint(CrosswordConfiguration config, PrintStream out) {
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
        CrosswordConfiguration config = new CrosswordConfiguration(bucket, width, height);
        config.add(new WordPosition(0, 1, Direction.RIGHT, "linen"));
        config.add(new WordPosition(3, 0, Direction.DOWN, "lemon"));
        config.add(new WordPosition(1, 0, Direction.DOWN, "win"));
        System.out.println(config.canBeAdded(0, 3, Direction.RIGHT, "taxol"));
    }

    public static void main(String[] args) {
//        ShapelessWordBucket bucket = ShapelessBucketFiller.loadBucket("test_simple.txt");
//        BadAndSlowStraightforwardCrosswordBuilder builder = new BadAndSlowStraightforwardCrosswordBuilder(bucket, 5, 5);

//        ShapelessWordBucket bucket = ShapelessBucketFiller.loadBucket("test.txt");
//        BoundBoardlessCrosswordBuilder builder = new BoundBoardlessCrosswordBuilder(bucket, 13, 13);

        ShapelessWordBucket bucket = ShapelessBucketFiller.loadBucket("topcoder.txt");
        BadAndSlowStraightforwardCrosswordBuilder builder = new BadAndSlowStraightforwardCrosswordBuilder(bucket, 11, 11);

        builder.kickOff();
        for (CrosswordConfiguration config : builder.getMaxConfigs()) {
            prettyPrint(config, System.out);
        }
    }
}
