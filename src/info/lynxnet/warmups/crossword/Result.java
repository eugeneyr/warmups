package info.lynxnet.warmups.crossword;

import java.io.Serializable;

public class Result implements Serializable {
    private int wordCount;
    private Character[][] board;

    public Result(int wordCount, Character[][] board) {
        this.wordCount = wordCount;
        this.board = board;
    }

    public int getWordCount() {
        return wordCount;
    }

    public void setWordCount(int wordCount) {
        this.wordCount = wordCount;
    }

    public Character[][] getBoard() {
        return board;
    }

    public void setBoard(Character[][] board) {
        this.board = board;
    }
}
