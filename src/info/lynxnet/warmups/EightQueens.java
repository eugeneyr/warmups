package info.lynxnet.warmups;

import java.util.Arrays;

/**
 *
 */
public class EightQueens {
    private char[] board = new char[8];
    int attempts = 0;

    boolean isUnderThreat(char[] board, int row, char letter) {
        for (int i = 0; i < row; i++) {
            char column = board[i];
            if (column == letter) {
                return true;
            }
            if (row - i == letter - column) {
                return true;
            }
            if (i - row == letter - column) {
                return true;
            }
        }
        return false;
    }

    boolean findPlaces(char[] board, int row) {
        if (row == 8) {
            return true;
        }
        for (char column = 'A'; column < 'I'; column++) {
            if (!isUnderThreat(board, row, column)) {
                board[row] = column;
                printBoard(board);
                attempts++;
                if (findPlaces(board, row + 1)) {
                    return true;
                }
            }
        }
        board[row] = ' ';
        return false;
    }

    void printBoard(char[] board) {
        for (int i = 8; i > 0; i--) {
            System.out.print(i + " ");
            for (char j = 'A'; j < 'I'; j++) {
                System.out.print(j == board[i - 1] ? "[W]" : "[ ]");
            }
            System.out.println();
        }
        System.out.print("  ");
        for (char j = 'A'; j < 'I'; j++) {
            System.out.print(" " + j + " ");
        }
        System.out.println();
    }


    public static void main(String[] args) {
        EightQueens queens = new EightQueens();
        Arrays.fill(queens.board, ' ');
        if( queens.findPlaces(queens.board, 0)) {
            System.out.println("SUCCESS!");
            queens.printBoard(queens.board);
            System.out.println("Attempts: " + queens.attempts);
        } else {
            System.out.println("FAIL");
        }
    }
}
