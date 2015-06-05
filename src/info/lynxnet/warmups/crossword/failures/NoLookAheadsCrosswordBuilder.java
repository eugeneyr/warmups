package info.lynxnet.warmups.crossword.failures;

import info.lynxnet.warmups.Direction;
import info.lynxnet.warmups.crossword.Result;
import info.lynxnet.warmups.crossword.ShapelessBucketFiller;
import info.lynxnet.warmups.crossword.ShapelessWordBucket;
import info.lynxnet.warmups.crossword.WordPosition;

import java.io.PrintStream;
import java.util.LinkedHashSet;
import java.util.LinkedList;
import java.util.List;

public class NoLookAheadsCrosswordBuilder {
    private int width;
    private int height;
    private ShapelessWordBucket bucket;
    private NoLookAheadsCrosswordInABucket config;
    private List<Result> results = new LinkedList<Result>();
    private int max = 0;
    private Character[][] maxBoard = null;

    public NoLookAheadsCrosswordBuilder(int width, int height, ShapelessWordBucket bucket) {
        this.width = width;
        this.height = height;
        this.bucket = bucket;
        config = new NoLookAheadsCrosswordInABucket(width, height);
        config.setAvailableWords(bucket);
    }

    public void bruteForceBuild(NoLookAheadsCrosswordInABucket config, Direction direction) {
        boolean changed = false;
        LinkedHashSet<String> availableWords = new LinkedHashSet<String>(bucket.getWords());
        availableWords.removeAll(config.getWords());
        for (String word : availableWords) {
            for (int x = 0; x < width; x++) {
                for (int y = 0; y < height; y++) {
                    if (config.canBeAdded(x, y, direction, word)) {
                        WordPosition wd = new WordPosition(x, y, direction, word);
                        changed = true;
                        try {
                            NoLookAheadsCrosswordInABucket clone = config.clone();
                            clone.add(wd);
                            bruteForceBuild(clone, direction == Direction.RIGHT ? Direction.DOWN : Direction.RIGHT);
                        } catch (CloneNotSupportedException e) {
                            e.printStackTrace();
                        }
                    }
                }
            }
        }
        if (!changed) {
            int count = config.getWords().size();
            if (count > max) {
                max = count;
                config.rebuildPlayField();
                maxBoard = config.getPlayField().clone();
                System.out.printf("Words used: %d\n", max);
                prettyPrint(maxBoard, System.out);
//                results.add(new Result(config.getWords().size(), config.getPlayField()));
            }
        }
    }

    public Character[][] build() {
        maxBoard = null;
        max = 0;
        bruteForceBuild(config, Direction.RIGHT);
//        for (Result result : results) {
//            int count = CrosswordConfiguration.countFilledCells(result.getBoard()) * result.getWordCount();
//            if (count > max) {
//                max = count;
//                maxBoard = result.getBoard();
//            }
//        }
        return maxBoard;
    }

    static void prettyPrint(Character[][] board, PrintStream out) {
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
            out.println("=============");
        }
    }

    public static void main(String[] args) {
        ShapelessWordBucket bucket = ShapelessBucketFiller.loadBucket("test5.txt");
        NoLookAheadsCrosswordBuilder builder = new NoLookAheadsCrosswordBuilder(5, 5, bucket);
        Character[][] result = builder.build();
        prettyPrint(result, System.out);
    }
}
