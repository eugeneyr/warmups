package info.lynxnet.warmups;

/**
 * 10.4
 */
public class PlusOnlyMath {
    public static int mult(int x, int y) {
        int result = 0;
        int inc = y < 0 ? -1 : 1;
        int counter = 0;
        while (counter != y) {
            result += inc > 0 ? x : sub(0, x);
            counter += inc;
        }
        return result;
    }

    public static int sub(int x, int y) {
        int stepUp = y > 0 ? 1 : -1;
        int stepDown = y > 0 ? -1 : 1;
        int diff = 0;
        int count = 0;
        while (count != y) {
            diff += stepDown;
            count += stepUp;
        }
        return x + diff;
    }

    public static int div(int x, int y) {
        if (y == 0) {
            throw new ArithmeticException("Division by zero error");
        }
        int counter = 0;
        int value = Math.abs(x);
        int decrement = Math.abs(y);
        while (value >= decrement) {
            value = sub(value, decrement);
            counter += 1;
        }
        if (Integer.signum(x) != Integer.signum(y)) {
            counter = sub(0, counter);
        }
        return counter;
    }

    public static void main(String[] args) {
        System.out.println("" + 7 * 4 + " = " + mult(7, 4));
        System.out.println("" + (-7 * -4) + " = " + mult(-7, -4));
        System.out.println("" + (-7 * 4) + " = " + mult(-7, 4));
        System.out.println("" + (7 * -4) + " = " + mult(7, -4));

        System.out.println("" + (7 - 4) + " = " + sub(7, 4));
        System.out.println("" + (7 - 8) + " = " + sub(7, 8));
        System.out.println("" + (7 - -8) + " = " + sub(7, -8));
        System.out.println("" + (-7 - -8) + " = " + sub(-7, -8));

        System.out.println("" + (21 / 7) + " = " + div(21, 7));
        System.out.println("" + (20 / 7) + " = " + div(20, 7));

        System.out.println("" + (-20 / 7) + " = " + div(-20, 7));
        System.out.println("" + (-20 / -7) + " = " + div(-20, -7));
        System.out.println("" + (20 / -7) + " = " + div(20, -7));
    }
}
