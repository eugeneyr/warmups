package info.lynxnet.warmups;

import java.io.*;
import java.util.*;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicLong;

public class ParallelWordRectangleFinder {
    static BucketList betterGetTheBucket(String fileName) {
        return BucketFiller.loadBucket(fileName);
    }

    static long timestamp;
    static AtomicLong counter = new AtomicLong(0);
    static volatile boolean stop = false;
    static AtomicBoolean found = new AtomicBoolean(false);
    static LinkedList<List<String>> results = new LinkedList<List<String>>();
    static Set<String> badWords = new HashSet<String>();
    static BlockingQueue<String> failedStarts = new ArrayBlockingQueue<String>(1024, true);

    static void findAllRectangles(BucketList bucketList, int x, int y) {
        WordBucket xBucket = bucketList.getBuckets().get(x);
        WordBucket yBucket = bucketList.getBuckets().get(y);
        System.out.println("Preprocessing...");

        if (xBucket == null || yBucket == null) {
            return;
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
                return;
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
                return;
            }
        }
        counter.set(0);
        timestamp = System.currentTimeMillis();
        buildRectangle(yTries, xMiniBucket, yMiniBucket, 0, new ArrayList<String>(y));
    }

    static void buildRectangle(final Map<Integer, TrieNode> yTries, final WordBucket xBucket, final WordBucket yBucket,
                               final int y, final List<String> xResult) {
        if (stop || found.get()) {
//            System.out.println("Bailing out");
            return;
        }
        long c = counter.incrementAndGet();
        if (c % 100000 == 1) {
            synchronized (results) {
                long now = System.currentTimeMillis();
                long difference = ((now - timestamp) / 1000);
                System.out.println("Time spent so far: " + difference);
                System.out.println("Variants checked: " + counter + "(" + (c / (difference + 1)) + " per second)");
                System.out.println("\t\t\t\tTrying:");
                for (String s : xResult) {
                    System.out.println("\t\t\t\t\t" + s);
                }
            }
        }

        if (y == yBucket.getWordLength()) {
            // found!!!
            synchronized (results) {
                System.out.println("----------------------------------------------------");
                for (String s : xResult) {
                    System.out.println(s);
                }
                System.out.println("----------------------------------------------------");
                results.add(new LinkedList<String>(xResult));
                found.set(true);
            }
            return;
        }

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
            for (char ch : node.getChildren().keySet()) {
                xSet.addAll(xBucket.getWords(ch, i));
            }
            if (i == 0) {
                megaset.addAll(xSet);
            } else {
                megaset.retainAll(xSet);
            }
        }

        ExecutorService service = y == 0 ? Executors.newFixedThreadPool(8) : null;

        if (y > 0) {
            for (String xWord : megaset) {
                processSingleCandidate(yTries, xBucket, yBucket, y, xResult, xWord);
            }
        } else {
            final int numberOfProcs = Runtime.getRuntime().availableProcessors();
            final CountDownLatch doneLatch = new CountDownLatch(numberOfProcs);
            final CyclicBarrier barrier = new CyclicBarrier(numberOfProcs);
//            final CountDownLatch startLatch = new CountDownLatch(1);
            Collection<Collection<String>> parts = splitCollection(megaset, numberOfProcs);
            for (final Collection<String> part : parts) {
                service.submit(new Callable<Void>() {
                    @Override
                    public Void call() throws Exception {
                        // startLatch.await();
                        barrier.await();
                        System.out.println("*** Started a thread: " + Thread.currentThread().getName());
                        for (String xWord : part) {
                            if (badWords.contains(xWord)) {
                                System.out.println(Thread.currentThread().getName() + " ****** Skipped a Bad Word: " + xWord);
                                continue;
                            }
                            if (found.get()) {
                                return null;
                            }

//                            System.out.println("Start word: " + xWord);
                            processSingleCandidate(yTries, xBucket, yBucket, y, new ArrayList<String>(xResult), xWord);
System.out.println(Thread.currentThread().getName() + " ******** Posting a bad word: " + xWord);
                            failedStarts.add(xWord);
                        }
                        doneLatch.countDown();
                        return null;
                    }
                });
            }
//            startLatch.countDown();
            try {
                doneLatch.await();
            } catch (InterruptedException e) {
                e.printStackTrace();
            } finally {
                service.shutdownNow();
            }
        }
    }

    private static Collection<Collection<String>> splitCollection(Collection<String> set, int pieces) {
        List<Collection<String>> result = new ArrayList<Collection<String>>(pieces);
        for (int i = 0; i < pieces; i++) {
            result.add(new LinkedHashSet<String>());
        }
        int i = 0;
        for (String word : set) {
            result.get(i % pieces).add(word);
            i++;
        }
        return result;
    }

    private static void processSingleCandidate(Map<Integer, TrieNode> yTries, WordBucket xBucket, WordBucket yBucket, int y, List<String> xResult, String xWord) {
        if (found.get()) {
            return;
        }

        StringBuilder sb;
        boolean valid = true;
        for (int i = 0; i < xBucket.getWordLength(); i++) {
            sb = new StringBuilder();
            for (int j = 0; j < y; j++) {
                sb.append(xResult.get(j).charAt(i));
            }
            sb.append(xWord.charAt(i));
            TrieNode trie = yTries.get(i);
            if (trie.findWord(sb.toString()) == null) {
                valid = false;
                break;
            }
        }
        if (valid) {
            List<String> attempt = new ArrayList<String>(xResult);
            attempt.add(xWord);
            buildRectangle(yTries, xBucket, yBucket, y + 1, attempt);
        }
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

//    static void findLargest() {
//        BucketList dictionary = betterGetTheBucket("WORD.LST");
//        TreeMap<Integer, WordBucket> buckets = dictionary.getBuckets();
//        for (Map.Entry<Integer, WordBucket> entry : buckets.descendingMap().entrySet()) {
//            int length = entry.getKey();
//            System.out.println("Length = " + length);
//            for (int i = length; i > 0; i--) {
//                System.out.println("Height = " + i);
//                if (buckets.containsKey(i)) {
//                    timestamp = System.currentTimeMillis();
//                    System.out.println("Started at: " + new Date(timestamp));
//                    findAllRectangles(dictionary, length, i);
//                    long now = System.currentTimeMillis();
//                    System.out.println("Finished at: " + new Date());
//                    System.out.println("Time spent: " + ((now - timestamp) / 1000));
//
//                    if (!results.isEmpty()) {
//                        for (List<String> rectangle : results) {
//                            System.out.println("=======================================================");
//                            for (String s : rectangle) {
//                                System.out.println(s);
//                            }
//                        }
//                    } else {
//                        System.out.println("NOT FOUND");
//                    }
//                }
//            }
//        }
//    }

    static void loadBadWords() {
        File file = new File("badWords.txt");
        if (file.exists()) {
            try {
                BufferedReader reader = new BufferedReader(new FileReader(file));
                String what;
                while ((what = reader.readLine()) != null) {
                    badWords.add(what);
                }
                reader.close();
            } catch (Exception e) {
                e.printStackTrace(System.err);
            }
        }
    }

    static void test() {
        timestamp = System.currentTimeMillis();
        System.out.println("Started at: " + new Date(timestamp));
        Runtime.getRuntime().addShutdownHook(new Thread() {
            @Override
            public void run() {
                stop = true;
            }
        });
        loadBadWords();
        new BadWordsWriter().start();
        findAllRectangles(betterGetTheBucket("WORD.LST"), 8, 7);

        if (!results.isEmpty()) {
            for (List<String> rectangle : results) {
                System.out.println("========================================================");
                for (String s : rectangle) {
                    System.out.println(s);
                }
            }
        } else {
            System.out.println("NOT FOUND");
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

    static class BadWordsWriter extends Thread {
        @Override
        public void run() {
            System.out.println("******* BadWordsWriter started");
            while (!stop) {
                try {
                    // a non-optimal stuff in reopening and closing the file each time we got a new word to write,
                    // but we are very effin' parallel and such, so we don't effin' care
                    // What we really care here is making sure that all the words are saved in the file if
                    // the program is stopped
                    String word = failedStarts.take();
                    BufferedWriter writer = new BufferedWriter(new FileWriter(new File("badWords.txt"), true));
                    System.out.println("****** BadWordsWriter got a word: " + word);
                    writer.write(word);
                    writer.newLine();
                    writer.flush();
                    writer.close();
                } catch (Exception e) {
                    e.printStackTrace(System.err);
                }
            }
            System.out.println("******* BadWordsWriter shut down");
        }
    }
}
