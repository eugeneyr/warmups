package info.lynxnet.warmups;

import java.util.TreeMap;

/**
 *
 */
public class BucketList {
    private TreeMap<Integer, WordBucket> buckets = new TreeMap<Integer, WordBucket>();

    public TreeMap<Integer, WordBucket> getBuckets() {
        return buckets;
    }

    public WordBucket addWord(String word) {
        return addWord(word, true);
    }

    public WordBucket addWord(String word, boolean sorted) {
        if (word == null || word.length() == 0) {
            return null;
        }
        WordBucket bucket = buckets.get(word.length());
        if (bucket == null) {
            bucket = sorted ? new SortedWordBucket(word.length()) : new UnsortedWordBucket(word.length());
            buckets.put(word.length(), bucket);
        }
        bucket.addWord(word);
        return bucket;
    }


    public WordBucket getBucket(String word) {
        if (word == null || word.length() == 0) {
            return null;
        }
        return buckets.get(word.length());
    }
}
