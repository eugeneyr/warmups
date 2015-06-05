package com.harmonicinc.examples;

public class ArrayOfArrays {
    public static void main(String[] args) {
        OutputTs[][] outputTsArrays = new OutputTs[4][];
        // initializing the data structure:
        for (int i = 0; i < outputTsArrays.length; i++) {
            outputTsArrays[i] = new OutputTs[10];
        }
        // adding some actual data:
        for (int i = 0; i < outputTsArrays.length; i++) {
            for (int j = 0; j < 10; j++) {
                outputTsArrays[i][j] = new OutputTs("Row " + i + ", Col " + j);
            }
        }
        // traversing data:
        for (OutputTs[] row : outputTsArrays) {
            for (OutputTs ts : row) {
                System.out.println(ts.toString());
            }
        }
    }
}
