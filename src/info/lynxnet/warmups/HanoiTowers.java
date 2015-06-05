package info.lynxnet.warmups;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.PrintStream;
import java.util.Arrays;
import java.util.Stack;

public class HanoiTowers {
    private Spindle spindle1 = new Spindle("Sp1");
    private Spindle spindle2 = new Spindle("Sp2");
    private Spindle spindle3 = new Spindle("Sp3");
    private Spindle[] spindles = new Spindle[]{spindle1, spindle2, spindle3};
    private static int count = 0;

    public int move(Spindle s1, Spindle s2) {
        if (s1.empty()) {
            throw new IllegalStateException("Source is empty: " + s1.getName());
        }
        int i = s1.peek();
        if (!s2.isEmpty() && s2.peek() <= i) {
            throw new IllegalStateException("The destination cannot accept the disc: " + s2.getName());
        }
        s1.pop();
        s2.push(i);
        System.out.println(s1.getName() + " -> " + i + " -> " + s2.getName());
        printOut(spindles);
        count++;
        return i;
    }

    public boolean done(Spindle[] spindles) {
        return spindles[0].empty() && spindles[1].empty()
                && !spindles[2].empty() && spindles[2].peek() == 1;
    }

    public void prepare(Spindle[] spindles, int n) {
        spindles[1].clear();
        spindles[2].clear();
        for (int i = n; i > 0; i--) {
            spindles[0].push(i);
        }
    }

    private void printOut(Spindle[] s) {
        System.out.println(s[0]);
        System.out.println(s[1]);
        System.out.println(s[2]);
    }

    boolean solve(Spindle a, Spindle b, Spindle c, int n) {
        if (n == 1) {
            move(a, c);
            return true;
        }
        if (n == 2) {
            move(a, b);
            move(a, c);
            move(b, c);
            return true;
        }
        solve(a, c, b, n - 1);
        move(a, c);
        solve(b, a, c, n - 1);
        return true;
    }

    public boolean solve(int n) {
        prepare(spindles, n);
        printOut(spindles);
        return solve(spindle1, spindle2, spindle3, n);
    }

    /**
     * @param args
     */
    public static void main(String[] args) {
        HanoiTowers towers = new HanoiTowers();
        try {
            System.setOut(new PrintStream(new FileOutputStream("log.txt")));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        count = 0;
        towers.solve(80);
        System.out.println("Solved: " + towers.done(towers.spindles));
        System.out.println("Moves: " + count);
    }
}

class Spindle extends Stack<Integer> {
    private String name;

    public Spindle(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    @Override
    public Integer peek() {
        if (isEmpty()) {
            return -1;
        }
        return super.peek();
    }

    @Override
    public String toString() {
        return Arrays.deepToString(toArray(new Integer[0]));
    }
}
