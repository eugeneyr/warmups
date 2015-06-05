package info.lynxnet.warmups.crossword.generational;

import info.lynxnet.warmups.crossword.WordPosition;

import java.util.LinkedHashSet;
import java.util.Set;

public class ConfigTreeNode {
    protected Set<ConfigTreeNode> children = new LinkedHashSet<ConfigTreeNode>();
    protected WordPosition wordPosition;
    protected ConfigTreeNode parent;

    public ConfigTreeNode(WordPosition wordPosition, ConfigTreeNode parent) {
        this.wordPosition = wordPosition;
        this.parent = parent;
        if (parent != null) {
            parent.children.add(this);
        }
    }

    public Set<ConfigTreeNode> getChildren() {
        return children;
    }

    public WordPosition getWordPosition() {
        return wordPosition;
    }

    public ConfigTreeNode getParent() {
        return parent;
    }

    public boolean sameConfig(ConfigTreeNode otherConfig) {
        // TODO implement
        return false;
    }

}
