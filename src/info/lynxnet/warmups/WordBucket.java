package info.lynxnet.warmups;

import java.util.Collection;
import java.util.Set;

public interface WordBucket {
    int getWordLength();

    void setWordLength(int wordLength);

    void addWord(String word);

    Collection<String> getWords(char c, int i);

    Set<String> getWords();
}
