package info.lynxnet.warmups.crossword.historical;

import java.util.Collection;
import java.util.LinkedList;

public class ConfigCollection {
    protected Collection<HistoricalCrosswordConfiguration> history = new LinkedList<HistoricalCrosswordConfiguration>();

    public ConfigCollection() {
    }

    public boolean alreadyWas(HistoricalCrosswordConfiguration config) {
        for (HistoricalCrosswordConfiguration cc : history) {
            if (cc.isSupersetOf(config)) {
                return true;
            }
        }
        return false;
    }

    public void add(HistoricalCrosswordConfiguration config) {
//        if (!alreadyWas(config)) {
            history.add(config);
//        }
    }

    public Collection<HistoricalCrosswordConfiguration> getHistory() {
        return history;
    }
}
