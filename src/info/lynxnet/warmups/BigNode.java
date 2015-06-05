package info.lynxnet.warmups;

public class BigNode {
    int level;
    BigNode next;
    ListNode head;
    ListNode tail;

    public BigNode(int level) {
        this.level = level;
    }

    @Override
    public String toString() {
        return "BigNode{" +
                "level=" + level +
                '}';
    }

    public void prettyPrint() {
        System.out.print("(" + level + ")");
        if (head != null) {
            System.out.print(" -> ");
            head.prettyPrint();
        } else {
            System.out.println();
        }
        if (next != null) {
            next.prettyPrint();
        }
    }
}
