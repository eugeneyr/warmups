package info.lynxnet.warmups;

public class TreeNode extends Node {
    private TreeNode left;
    private TreeNode right;
    private TreeNode parent;

    public TreeNode(String stringValue) {
        super(stringValue);
    }

    public TreeNode(String stringValue, TreeNode left, TreeNode right) {
        super(stringValue);
        this.left = left;
        this.right = right;
    }

    public TreeNode(int intValue, TreeNode left, TreeNode right) {
        super(intValue);
        this.left = left;
        this.right = right;
    }

    public TreeNode() {
    }

    public TreeNode(Node otherNode) {
        super(otherNode);
    }

    public TreeNode(int intValue) {
        super(intValue);
    }

    public TreeNode getLeft() {
        return left;
    }

    public void setLeft(TreeNode left) {
        this.left = left;
    }

    public TreeNode getRight() {
        return right;
    }

    public void setRight(TreeNode right) {
        this.right = right;
    }

    public TreeNode getParent() {
        return parent;
    }

    public void setParent(TreeNode parent) {
        this.parent = parent;
    }
}
