package info.lynxnet.warmups;

public class ListNode extends Node {
    private ListNode next;

    public ListNode(Node otherNode) {
        super(otherNode);
    }

    public ListNode() {
    }

    public ListNode(String value) {
        super(value);
    }

    public ListNode(int value) {
        super(value);
    }

    public ListNode getNext() {
        return next;
    }

    public void setNext(ListNode next) {
        this.next = next;
    }

    public void prettyPrint() {
        System.out.print("[");
        if (stringValue != null) {
            System.out.print(stringValue);
        } else if (intValue >= 0) {
            System.out.print(intValue);
        }
        System.out.print("]");
        if (next != null) {
            System.out.print(" -> ");
            next.prettyPrint();
        } else {
            System.out.println();
        }
    }
}
