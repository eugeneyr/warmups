package info.lynxnet.warmups.crossword.straitforward;

import info.lynxnet.warmups.crossword.WordPosition;

import java.util.*;

public class ConfigCollection {
    protected Collection<CrosswordConfiguration> history = new LinkedList<CrosswordConfiguration>();

    public ConfigCollection() {
    }

    public boolean alreadyWas(CrosswordConfiguration config) {
        for (CrosswordConfiguration cc : history) {
            if (cc.isSupersetOf(config)) {
                return true;
            }
        }
        return false;
    }

    public void add(CrosswordConfiguration config) {
        if (!alreadyWas(config)) {
            history.add(config);
        }
    }

    public Collection<CrosswordConfiguration> getHistory() {
        return history;
    }
}
