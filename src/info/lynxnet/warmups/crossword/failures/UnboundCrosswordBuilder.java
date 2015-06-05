package info.lynxnet.warmups.crossword.failures;

import info.lynxnet.warmups.Direction;
import info.lynxnet.warmups.crossword.*;

import java.io.PrintStream;
import java.util.*;

public class UnboundCrosswordBuilder {
    private ShapelessWordBucket bucket;
    private List<Result> results = new LinkedList<Result>();
    private int max = 0;
    private UnboundCrosswordConfiguration maxConfig = null;
    private long count = 0;
    private long maxCount = 0;

    public UnboundCrosswordBuilder(ShapelessWordBucket bucket) {
        this.bucket = bucket;
    }

    public void twentyThousandMonkeysTypingHamlet() {
        for (String word : bucket.getWords()) {
            UnboundCrosswordConfiguration config = new UnboundCrosswordConfiguration(bucket);
            WordPosition wp = new WordPosition(0, 0, Direction.RIGHT, word);
            config.add(wp);
            monkeyType(config);
        }
    }

    private int countChars(Collection<String> words) {
        int count = 0;
        for (String word : words) {
            if (word != null) {
                count += word.length();
            }
        }
        return count;
    }

    private void monkeyType(UnboundCrosswordConfiguration config) {
        count++;
        Set<String> availableWords = new LinkedHashSet<String>(config.getDictionary().getWords());
        availableWords.removeAll(config.getWords());
        int countInters = config.countIntersections();
        if (availableWords.isEmpty()) {
            if (countInters > max) {
                max = countInters;
                maxConfig = config;
                maxCount++;
                System.out.printf("Max %d (total tries: %d)\n", maxCount, count);
                prettyPrint(config.buildPlayField(), System.out);
            }
            return;
        }
        // a feeble attempt at pruning
        int charNumber = countChars(availableWords);
        if (countInters + charNumber < max) {
            // this configuration is done with, let's forget it
            return;
        }
        // ok, there are no more ways to avoid it
        PriorityQueue<WeightedWordPosition> queue = new PriorityQueue<WeightedWordPosition>(
                availableWords.size(), new WeightedWordPositionComparator());
        ShapelessWordBucket bucket = config.getDictionary();

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
                                //        how many crossings will be there
                                int weight = config.findCrossingWords(x, y, newDir, newWord).size();
                                WeightedWordPosition wwp = new WeightedWordPosition();
                                wwp.setX(x);
                                wwp.setY(y);
                                wwp.setDirection(newDir);
                                wwp.setIntersections(weight);
                                wwp.setWord(newWord);
                                //        add the pair (new WordDirection, number of crossings) to the priority queue
                                queue.add(wwp);
                            }
                        }
                    }
                }
            }
        }
//        if (queue.isEmpty()) {
//            System.out.println("End of wits: ");
//            prettyPrint(config.buildPlayField(), System.out);
//        }
        // here we got a mile-long priority queue
        // repeat for all the pairs in the queue:
        while (!queue.isEmpty()) {
            WeightedWordPosition wwp = queue.poll();
            if (wwp != null) {
                //   clone the config
                UnboundCrosswordConfiguration clone = null;
                try {
                    clone = config.clone();
                    //   add the WordDirection to the clone
                    clone.add(wwp);
                    //   call monkeyType(clone)
                    monkeyType(clone);
                } catch (CloneNotSupportedException e) {
                    e.printStackTrace();
                }
            }
        }
        //   watch the process dying of OutOfMemoryError or StackOverflowError
        // think of the ways to optimize this insanity
    }

    public UnboundCrosswordConfiguration build() {
        max = 0;
        twentyThousandMonkeysTypingHamlet();
//        for (Result result : results) {
//            int count = CrosswordConfiguration.countFilledCells(result.getBoard()) * result.getWordCount();
//            if (count > max) {
//                max = count;
//                maxBoard = result.getBoard();
//            }
//        }
        return maxConfig;
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
        //ShapelessWordBucket bucket = ShapelessBucketFiller.loadBucket("small_dict.txt");
        UnboundCrosswordBuilder builder = new UnboundCrosswordBuilder(bucket);
        UnboundCrosswordConfiguration result = builder.build();
        prettyPrint(result.buildPlayField(), System.out);
    }
}
