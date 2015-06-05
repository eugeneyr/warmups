package info.lynxnet.warmups;

public class YoungTableauSearch {
    static class Tuple {
        int row;
        int col;

        Tuple(int row, int col) {
            this.row = row;
            this.col = col;
        }

        @Override
        public String toString() {
            final StringBuilder sb = new StringBuilder();
            sb.append("[").append(row);
            sb.append(", ").append(col);
            sb.append(']');
            return sb.toString();
        }
    }

    static Tuple searchElement(int[][] matrix, int val) {
        int startRow = 0;
        int endRow = matrix.length - 1;
        int startCol = 0;
        int endCol = matrix[0].length - 1;
        do {
            if (matrix[startRow][startCol] == val) {
                return new Tuple(startRow, startCol);
            }
            if (matrix[startRow][endCol] == val) {
                return new Tuple(startRow, endCol);
            }
            if (matrix[endRow][startCol] == val) {
                return new Tuple(endRow, startCol);
            }
            if (matrix[endRow][endCol] == val) {
                return new Tuple(endRow, endCol);
            }

            while (startRow < endRow && matrix[startRow][endCol] < val) {
                startRow++;
            }
            while (endRow > startRow && matrix[endRow][startCol] > val) {
                endRow--;
            }
            while (startCol < endCol && matrix[endRow][startCol] < val) {
                startCol++;
            }
            while (endCol > startCol && matrix[startRow][endCol] > val) {
                endCol--;
            }
            if (startRow == endRow && startCol == endCol) {
                if (matrix[startRow][startCol] == val) {
                    return new Tuple(startRow, startCol);
                }
                return new Tuple(-1, -1);
            }
        } while (true);
    }

    static Tuple searchElementRec(int[][] matrix, int val, int startRow, int endRow, int startCol, int endCol) {
        if (startRow == endRow && startCol == endCol) {
            if (matrix[startRow][startCol] == val) {
                return new Tuple(startRow, startCol);
            }
            return new Tuple(-1, -1);
        }
        if (startRow > endRow || startCol > endCol) {
            return new Tuple(-1, -1);
        }
        int midRow = (startRow + endRow) / 2;
        int midCol = (startCol + endCol) / 2;
        int midElement = matrix[midRow][midCol];
        if (midElement == val) {
            return new Tuple(midRow, midCol);
        }

        if (midElement > val) {
            Tuple tuple;
            tuple = searchElementRec(matrix, val, startRow, midRow - 1, startCol, midCol - 1);
            if (tuple.row >= 0) {
                return tuple;
            }
            tuple = searchElementRec(matrix, val, midRow, endRow, startCol, midCol - 1);
            if (tuple.row >= 0) {
                return tuple;
            }
            return searchElementRec(matrix, val, startRow, midRow - 1, midCol, endCol);
        }

        Tuple tuple = searchElementRec(matrix, val, midRow + 1, endRow, midCol + 1, endCol);
        if (tuple.row >= 0) {
            return tuple;
        }
        tuple = searchElementRec(matrix, val, midRow + 1, endRow, startCol, midCol);
        if (tuple.row >= 0) {
            return tuple;
        }
        return searchElementRec(matrix, val, startRow, midRow, midCol + 1, endCol);
    }

    static int removeTop(int[][] matrix) {
        int top = matrix[0][0];
        matrix[0][0] = Integer.MAX_VALUE;
        youngify(matrix, 0, 0);
        return top;
    }

    static void youngify(int[][] matrix, int i, int j) {
        int lower = i < matrix.length - 1 ? matrix[i + 1][j] : Integer.MAX_VALUE;
        int righter = j < matrix[0].length - 1 ? matrix[i][j + 1] : Integer.MAX_VALUE;
        int element = matrix[i][j];
        if (lower >= element && righter >= element) {
            return;
        }
        if (lower < righter) {
            matrix[i][j] = lower;
            matrix[i + 1][j] = element;
            youngify(matrix, i + 1, j);
        } else {
            matrix[i][j] = righter;
            matrix[i][j + 1] = element;
            youngify(matrix, i, j + 1);
        }
    }

    static int searchMedian(int[][] matrix, int idx) {
        int [][] newMatrix = new int[matrix.length][];
        for (int i = 0; i < matrix.length; i++) {
            newMatrix[i] = new int[matrix[i].length];
            System.arraycopy(matrix[i], 0, newMatrix[i], 0, matrix[i].length);
        }

        int result = 0;
        for (int count = 0; count <= idx; count++) {
            result = removeTop(newMatrix);
        }
        return result;
    }


    // 5: 0 + 5, 1 + 4, 2 + 3, 3 + 2, 4 + 1, 5 + 0                       4

    public static void main(String[] args) {
        int[][] matrix = {{2, 3, 5, 8},
                {4, 6, 10, 15},
                {7, 14, 16, 20},
                {9, 17, 19, 21}};
        for (int i = 0; i < 22; i++) {
            System.out.print("I val = " + i + ": ");
            System.out.println(searchElement(matrix, i).toString());
            System.out.print("R val = " + i + ": ");
            System.out.println(searchElementRec(matrix, i, 0, matrix.length - 1, 0, matrix[0].length - 1).toString());
        }

        for (int i = 0; i < 10; i++) {
            System.out.println("median #" + i + " = " + searchMedian(matrix, i));
        }
    }
}
