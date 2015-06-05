package info.lynxnet.warmups.crossword.historical;

import info.lynxnet.warmups.crossword.WordPosition;

import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.Set;

public class TreeConfigHolder {
    protected Set<ConfigTreeNode> roots = new LinkedHashSet<ConfigTreeNode>();
    protected Set<ConfigTreeNode> leaves = new LinkedHashSet<ConfigTreeNode>();

    public TreeConfigHolder() {
    }

    public Set<ConfigTreeNode> getRoots() {
        return roots;
    }

    public Set<ConfigTreeNode> getLeaves() {
        return leaves;
    }

    public boolean alreadyWas(HistoricalCrosswordConfiguration config) {
        return alreadyWas(config, false);
    }

    public boolean alreadyWas(HistoricalCrosswordConfiguration config, boolean descend) {
        if (descend) {
            for (ConfigTreeNode root : roots) {
                if (descend(root, config)) {
                    return true;
                }
            }
        } else {
            for (ConfigTreeNode leaf : leaves) {
                HashSet<WordPosition> allWps = new HashSet<WordPosition>();
                allWps.addAll(config.getDownWords());
                allWps.addAll(config.getRightWords());
                if (ascendAndLook(leaf, allWps)) {
                    return true;
                }
            }
        }
        return false;
    }

    public boolean descend(ConfigTreeNode node, HistoricalCrosswordConfiguration config) {
        if (node == null) {
            return false;
        }
        if (node.getChildren().isEmpty()) {
            HashSet<WordPosition> allWps = new HashSet<WordPosition>();
            allWps.addAll(config.getDownWords());
            allWps.addAll(config.getRightWords());
            return ascendAndLook(node, allWps);
        }
        for (ConfigTreeNode child : node.getChildren()) {
            if (descend(child, config)) {
                return true;
            }
        }
        return false;
    }

    public boolean ascendAndLook(ConfigTreeNode node, Set<WordPosition> wps) {
        if (wps.isEmpty()) {
            return true;
        }
        if (node == null) {
            return false;
        }
        if (wps.contains(node.getWordPosition())) {
            wps.remove(node.getWordPosition());
        }
        return ascendAndLook(node.getParent(), wps);
    }

    public boolean alreadyWas(WordPosition rootWp, HistoricalCrosswordConfiguration config) {
        for (ConfigTreeNode root : roots) {
            if (root.getWordPosition().equals(rootWp)) {
                if (descend(root, config)) {
                    return true;
                }
            }
        }
        return false;
    }

    public boolean descend(WordPosition root, ConfigTreeNode node, HistoricalCrosswordConfiguration config) {
        if (node == null) {
            return false;
        }
        if (node.getChildren().isEmpty()) {
            HashSet<WordPosition> allWps = new HashSet<WordPosition>();
            allWps.addAll(config.getDownWords());
            allWps.addAll(config.getRightWords());
            return ascendAndLook(node, allWps);
        }
        for (ConfigTreeNode child : node.getChildren()) {
            if (descend(child, config)) {
                return true;
            }
        }
        return false;
    }

    public void addNode(ConfigTreeNode node) {
        if (node.getChildren().isEmpty()) {
            leaves.add(node);
        } else {
            leaves.remove(node);
        }
        if (node.getParent() != null) {
            leaves.remove(node.getParent());
        } else {
            roots.add(node);
        }
    }
}
