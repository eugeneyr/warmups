package info.lynxnet.warmups;

import java.util.LinkedList;
import java.util.List;

/**
 *
 */
public class Path {
    List<TreeNode> nodes;
    int value;

    public Path() {
        nodes = new LinkedList<TreeNode>();
        value = 0;
    }

    public Path(List<TreeNode> nodes) {
        this.nodes = new LinkedList<TreeNode>();
        if (nodes != null) {
            this.nodes.addAll(nodes);
        }
        value = computeValue(this.nodes);
    }

    private int computeValue(List<TreeNode> nodes) {
        int result = 0;
        for (TreeNode node : nodes) {
            result += node.getIntValue();
        }
        return result;
    }

    public Path(TreeNode node) {
        nodes = new LinkedList<TreeNode>();
        if (node != null) {
            nodes.add(node);
            value = node.getIntValue();
        }
    }

    public Path(Path path, TreeNode node) {
        nodes = new LinkedList<TreeNode>();
        if (path != null) {
            if (path.nodes != null) {
                nodes.addAll(path.nodes);
            }
            value = path.value;
        }
        if (node != null) {
            nodes.add(node);
            value += node.getIntValue();
        }
    }

    public List<TreeNode> getNodes() {
        return nodes;
    }

    public void setNodes(List<TreeNode> nodes) {
        this.nodes = nodes;
    }

    public int getValue() {
        return value;
    }

    public void setValue(int value) {
        this.value = value;
    }

    public void prettyPrint() {
        System.out.print(value + " [ ");
        for (TreeNode node : nodes) {
            System.out.print(node.getIntValue());
            System.out.print(" ");
        }
        System.out.println("]");
    }
}
