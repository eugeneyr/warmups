package com.harmonicinc.examples;

import java.util.ArrayList;
import java.util.List;

class OutputTs {
    // just a static protected class to illustrate object allocation
    private String myName;

    public OutputTs(String name) {
        myName = name;
    }

    public String getMyName() {
        return myName;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("OutputTs{");
        sb.append("myName='").append(myName).append('\'');
        sb.append('}');
        return sb.toString();
    }
}

public class ArrayOfLists {
    public static void main(String[] args) {
        List<OutputTs>[] outputTsLists = new List[4];
        // initializing the data structure:
        for (int i = 0; i < outputTsLists.length; i++) {
            outputTsLists[i] = new ArrayList<OutputTs>();
        }
        // adding some actual data:
        for (int i = 0; i < outputTsLists.length; i++) {
            for (int j = 0; j < 10; j++) {
                outputTsLists[i].add(new OutputTs("Row " + i + ", Col " + j));
            }
        }
        // traversing data:
        for (int i = 0; i < outputTsLists.length; i++) {
            for (OutputTs ts : outputTsLists[i]) {
                System.out.println(ts.toString());
            }
        }
    }
}
