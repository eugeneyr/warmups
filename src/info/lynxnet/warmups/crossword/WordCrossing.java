package info.lynxnet.warmups.crossword;

import java.io.Serializable;

public class WordCrossing implements Serializable {
    private int x;
    private int y;
    private WordPosition right;
    private WordPosition down;
    private char character;

    public WordCrossing() {
    }

    public WordCrossing(WordPosition right, WordPosition down) {
        this.x = down.getX();
        this.y = right.getY();
        this.right = right;
        this.down = down;
        character = right.getWord().charAt(x - right.getX());
    }

    public WordCrossing(int x, int y, WordPosition right, WordPosition down, char character) {
        this.x = x;
        this.y = y;
        this.right = right;
        this.down = down;
        this.character = character;
    }

    public int getX() {
        return x;
    }

    public void setX(int x) {
        this.x = x;
    }

    public int getY() {
        return y;
    }

    public void setY(int y) {
        this.y = y;
    }

    public WordPosition getRight() {
        return right;
    }

    public void setRight(WordPosition right) {
        this.right = right;
    }

    public WordPosition getDown() {
        return down;
    }

    public void setDown(WordPosition down) {
        this.down = down;
    }

    public char getCharacter() {
        return character;
    }

    public void setCharacter(char character) {
        this.character = character;
    }
}
