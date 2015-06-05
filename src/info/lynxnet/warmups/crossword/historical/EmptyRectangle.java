package info.lynxnet.warmups.crossword.historical;

import java.io.Serializable;

public class EmptyRectangle implements Serializable {
    protected int startX;
    protected int endX;
    protected int startY;
    protected int endY;

    public EmptyRectangle(int startX, int endX, int startY, int endY) {
        this.startX = startX;
        this.endX = endX;
        this.startY = startY;
        this.endY = endY;
    }

    public int getStartX() {
        return startX;
    }

    public void setStartX(int startX) {
        this.startX = startX;
    }

    public int getEndX() {
        return endX;
    }

    public void setEndX(int endX) {
        this.endX = endX;
    }

    public int getStartY() {
        return startY;
    }

    public void setStartY(int startY) {
        this.startY = startY;
    }

    public int getEndY() {
        return endY;
    }

    public void setEndY(int endY) {
        this.endY = endY;
    }
    
    public boolean contains(EmptyRectangle otherRectangle) {
        if (otherRectangle == null) {
            return false;
        }
        return startX <= otherRectangle.startX && startY <= otherRectangle.startY && endX >= otherRectangle.endX && endY >= otherRectangle.endY;
    }

    public EmptyRectangle intersect(EmptyRectangle otherRectangle) {
        if (otherRectangle == null) {
            return null;
        }
        if (contains(otherRectangle)) {
            return otherRectangle;
        }
        if (otherRectangle.contains(this)) {
            return this;
        }
        if (otherRectangle.startX > this.endX) {
            return null;
        }
        if (otherRectangle.startY > this.endY) {
            return null;
        }
        if (otherRectangle.endX < this.startX) {
            return null;
        }
        if (otherRectangle.endY < startY) {
            return null;
        }
        int maxStartX = Math.max(this.startX, otherRectangle.startX);
        int maxStartY = Math.max(this.startY, otherRectangle.startY);
        int minEndX = Math.min(this.endX, otherRectangle.endX);
        int minEndY = Math.min(this.endY, otherRectangle.endY);
        return new EmptyRectangle(maxStartX, minEndX, maxStartY, minEndY);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof EmptyRectangle)) return false;

        EmptyRectangle emptyArea = (EmptyRectangle) o;

        if (endX != emptyArea.endX) return false;
        if (endY != emptyArea.endY) return false;
        if (startX != emptyArea.startX) return false;
        if (startY != emptyArea.startY) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = startX;
        result = 31 * result + endX;
        result = 31 * result + startY;
        result = 31 * result + endY;
        return result;
    }

}
