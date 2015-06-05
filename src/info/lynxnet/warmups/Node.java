package info.lynxnet.warmups;

public abstract class Node {
    protected String stringValue;
    protected int intValue = -1;

    protected Node(String stringValue) {
        this.stringValue = stringValue;
    }

    protected Node(int intValue) {
        this.intValue = intValue;
    }

    protected Node() {
    }

    protected Node(Node otherNode) {
        this.stringValue = otherNode.stringValue;
        this.intValue = otherNode.intValue;
    }

    public String getStringValue() {
        return stringValue;
    }

    public void setStringValue(String stringValue) {
        this.stringValue = stringValue;
    }

    public int getIntValue() {
        return intValue;
    }

    public void setIntValue(int intValue) {
        this.intValue = intValue;
    }
}
