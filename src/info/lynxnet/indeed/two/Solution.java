package info.lynxnet.indeed.two;

class TreeNode {
    private TreeNode left;
    private TreeNode right;
    private int value;

    public TreeNode(int value, TreeNode left, TreeNode right) {
        this.left = left;
        this.right = right;
        this.value = value;
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

    public int getValue() {
        return value;
    }

    public void setValue(int value) {
        this.value = value;
    }
}

public class Solution {
    static int getHeight(TreeNode node) {
        if (node == null) {
            return 0;
        }
        return Math.max(getHeight(node.getLeft()), getHeight(node.getRight())) + 1;
    }

    static TreeNode findNode(TreeNode root, int value) {
        if (root == null) {
            return null;
        }
        if (root.getValue() == value) {
            return root;
        }
        TreeNode node = findNode(root.getLeft(), value);
        if (node != null) {
            return node;
        }
        return findNode(root.getRight(), value);
    }

    static int getDiameter(TreeNode node) {
        if (node == null) {
            return 0;
        }
        int leftHeight = getHeight(node.getLeft());
        int rightHeight = getHeight(node.getRight());
        int longestPath = leftHeight + rightHeight + 1;
        int leftDiam = getDiameter(node.getLeft());
        int rightDiam = getDiameter(node.getRight());
        return Math.max(longestPath, Math.max(leftDiam, rightDiam));
    }

    public static void main(String[] args) {
        TreeNode tree1 = new TreeNode(1,
                new TreeNode(2,
                        new TreeNode(3, null, null),
                        new TreeNode(4,
                                new TreeNode(5, null, null),
                                new TreeNode(6, null, null))),
                new TreeNode(7, new TreeNode(8,
                        new TreeNode(9,
                                new TreeNode(10,
                                        new TreeNode(11, null, null),
                                        new TreeNode(12, null, null)),
                                new TreeNode(13, null, null)),
                        null),
                        null));
        System.out.println(getDiameter(tree1)); // 9
        System.out.println(getDiameter(findNode(tree1, 2))); // 4
        System.out.println(getDiameter(findNode(tree1, 4))); // 3
        System.out.println(getDiameter(findNode(tree1, 5))); // 1

        System.out.println(getDiameter(findNode(tree1, 11))); // 1
        System.out.println(getDiameter(findNode(tree1, 10))); // 3
        System.out.println(getDiameter(findNode(tree1, 9))); // 4
        System.out.println(getDiameter(findNode(tree1, 8))); // still 4
        System.out.println(getDiameter(findNode(tree1, 7))); // still 4
    }
}
