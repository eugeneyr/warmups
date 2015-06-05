package info.lynxnet.warmups;

import java.util.*;

/**
 *
 */
public class TrieNode {
    private TrieNode parent;
    private Map<Character, TrieNode> children = new TreeMap<Character, TrieNode>();

    public TrieNode(TrieNode parent) {
        this.parent = parent;
    }

    public TrieNode() {
    }

    public TrieNode getParent() {
        return parent;
    }

    public Map<Character, TrieNode> getChildren() {
        return children;
    }

    public TrieNode addWord(String word) {
        if (word == null) {
            return null;
        }
        if (word.length() == 0) {
            return this;
        }
        char c = word.charAt(0);
        TrieNode child = children.get(c);
        if (child == null) {
            child = new TrieNode(this);
            children.put(c, child);
        }
        return child.addWord(word.substring(1));
    }

    public TrieNode findWord(String word) {
        if (word == null) {
            return null;
        }
        if (word.length() == 0) {
            return this;
        }
        char c = word.charAt(0);
        TrieNode child = children.get(c);
        if (child == null) {
            return null;
        }
        return child.findWord(word.substring(1));
    }

    public Collection<String> getAllWords() {
        if (children == null || children.isEmpty()) {
            return Collections.singletonList("");
        }
        LinkedHashSet<String> result = new LinkedHashSet<String>();
        for (Map.Entry<Character, TrieNode> entry : children.entrySet()) {
            char c = entry.getKey();
            TrieNode node = entry.getValue();
            if (node != null) {
                Collection<String> words = node.getAllWords();
                for (String word : words) {
                    result.add(c + word);
                }
            }
        }
        return result;
    }
}
