package info.lynxnet.warmups;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

public class AllPathsInTree {
    public List<Path> allPaths(TreeNode node, int value, List<Path> result) {
        if (node == null) {
            return Collections.EMPTY_LIST;
        }
        List<Path> childrenPaths = new LinkedList<Path>();
        List<Path> newPaths = new LinkedList<Path>();
        childrenPaths.addAll(allPaths(node.getLeft(), value, result));
        childrenPaths.addAll(allPaths(node.getRight(), value, result));
        for (Path path : childrenPaths) {
            Path newPath = new Path(path, node);
            if (newPath.getValue() == value) {
                result.add(newPath);
                // ... or print it out
            }
            newPaths.add(newPath);
        }
        Path startedHere = new Path(node);
        if (startedHere.getValue() == value) {
            result.add(startedHere);
            // ... or print it out
        }
        newPaths.add(startedHere);
        return newPaths; 
    }

    public static void main(String[] args) {
        TreeNode tree = new TreeNode(4,
                new TreeNode(3,
                        new TreeNode(1), null),
                new TreeNode(-1,
                        new TreeNode(2,
                                new TreeNode(1), new TreeNode(2)),
                        new TreeNode(4,
                                null,
                                new TreeNode(-1))));
        AllPathsInTree pathSearcher = new AllPathsInTree();
        List<Path> results = new LinkedList<Path>();
        pathSearcher.allPaths(tree, 3, results);
        for (Path path : results) {
            path.prettyPrint();
        }
    }
}
