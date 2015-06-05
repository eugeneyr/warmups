package info.lynxnet.warmups;

import java.util.HashMap;
import java.util.Map;

/**
 *
 */
public class SimpleTrieNode {
    private SimpleTrieNode parent;
    private SimpleTrieNode[] children = new SimpleTrieNode[26];

    public SimpleTrieNode(SimpleTrieNode parent) {
        this.parent = parent;
    }

    public SimpleTrieNode() {
    }

    public SimpleTrieNode getParent() {
        return parent;
    }

    public SimpleTrieNode[] getChildren() {
        return children;
    }

    public SimpleTrieNode addWord(String word) {
        if (word == null) {
            return null;
        }
        if (word.length() == 0) {
            return this;
        }
        char c = word.charAt(0);
        int idx = Character.toLowerCase(c) - 'a';
        if (idx < 0 || idx > 25) {
            throw new IllegalArgumentException("This stuff works only with 26 Latin alphabet characters");
        }
        SimpleTrieNode child = children[idx];
        if (child == null) {
            child = new SimpleTrieNode(this);
            children[idx] = child;
        }
        return child.addWord(word.substring(1));
    }

    public SimpleTrieNode findWord(String word) {
        if (word == null) {
            return null;
        }
        if (word.length() == 0) {
            return this;
        }
        char c = word.charAt(0);
        int idx = Character.toLowerCase(c) - 'a';
        if (idx < 0 || idx > 25) {
            throw new IllegalArgumentException("This stuff works only with 26 Latin alphabet characters");
        }
        SimpleTrieNode child = children[idx];
        if (child == null) {
            return null;
        }
        return child.findWord(word.substring(1));
    }

}
