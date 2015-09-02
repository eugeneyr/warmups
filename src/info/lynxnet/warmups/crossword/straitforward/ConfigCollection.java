package info.lynxnet.warmups.crossword.straitforward;

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
            List<CrosswordConfiguration> toRemove = new ArrayList<>();
            for (CrosswordConfiguration cc : history) {
                if (config.isSupersetOf(cc)) {
                    toRemove.add(cc);
                }
            }
            for (CrosswordConfiguration cc : toRemove) {
                history.remove(cc);
            }
            history.add(config);
        }
    }

    public Collection<CrosswordConfiguration> getHistory() {
        return history;
    }
}
