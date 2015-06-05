package info.lynxnet.warmups.crossword;

import info.lynxnet.warmups.BucketList;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;

public class ShapelessBucketFiller {
    public static ShapelessWordBucket loadBucket(String fileName) {
        ShapelessWordBucket result = new ShapelessWordBucket();
        try {
            BufferedReader reader = new BufferedReader(new InputStreamReader(new FileInputStream(fileName)));
            String s;
            do {
                s = reader.readLine();
                if (s != null) {
                    s = s.toLowerCase().trim();
                    if (s.length() > 0) {
                        result.addWord(s);
                    }
                }
            } while (s != null);
        } catch (IOException e) {
            e.printStackTrace(System.out);
            return null;
        }
        return result;
    }
}
