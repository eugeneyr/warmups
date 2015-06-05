package info.lynxnet.warmups;

import java.util.List;

public class TreeToBigNodeConverter {
    public BigNode treeToLayers(TreeNode root) {
        if (root == null) {
            return null;
        }
        BigNode head = new BigNode(1);
        buildListOfLists(root, head);
        return head;
    }

    public void buildListOfLists(TreeNode node, BigNode current) {
        if (node == null) {
            return;
        }
        if (current.head == null) {
            current.head = new ListNode(node);
            current.tail = current.head;
        } else {
            current.tail.setNext(new ListNode(node));
            current.tail = current.tail.getNext();
        }
        if (node.getLeft() != null || node.getRight() != null) {
            if (current.next == null) {
                current.next = new BigNode(current.level + 1);
            }
            buildListOfLists(node.getLeft(), current.next);
            buildListOfLists(node.getRight(), current.next);
        }
    }

    public void traverse(TreeNode node, List<TreeNode> queue) {
        if (node == null) {
            return;
        }
        if (queue.isEmpty()) {
            queue.add(node);
        }
        if (node.getLeft() != null) {
            queue.add(node.getLeft());
        }
        if (node.getRight() != null) {
            queue.add(node.getRight());
        }
        if (!queue.isEmpty()) {
            TreeNode head = queue.remove(0);
            System.out.println(head.getStringValue());
        }
        if (!queue.isEmpty()) {
            TreeNode head = queue.get(0);
            traverse(head, queue);
        }
    }
}

