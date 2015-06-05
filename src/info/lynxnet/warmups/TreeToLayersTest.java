package info.lynxnet.warmups;

import java.util.LinkedList;

public class TreeToLayersTest {
    public static void main(String[] argv) {
        TreeNode tree = new TreeNode("a1",
                new TreeNode("b1",
                        new TreeNode("c1"), new TreeNode("c2",
                                null, new TreeNode("d0"))),
                new TreeNode("b2",
                        new TreeNode("c3",
                                new TreeNode("d1"), new TreeNode("d2")),
                        new TreeNode("c4",
                                new TreeNode("d3"), new TreeNode("d4"))));
        TreeToBigNodeConverter converter = new TreeToBigNodeConverter();
//        BigNode list = converter.treeToLayers(tree);
//        list.prettyPrint();

        LinkedList<TreeNode> queue = new LinkedList<TreeNode>();
//        queue.add(tree);
        converter.traverse(tree, queue);
//        for (TreeNode node : queue) {
//            System.out.println(node.getStringValue());
//        }
    }
}
