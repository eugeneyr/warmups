package info.lynxnet.warmups;

import java.util.Arrays;
import java.util.Random;
import java.util.Stack;

public class StackSorter {
    private static int counter = 0;

    static int spillOut(Stack<Integer> mainStack, Stack<Integer> auxStack) {
        int numOfSwaps = 0;
        while (!mainStack.isEmpty()) {
            int v = mainStack.pop();
            while (! mainStack.isEmpty() && mainStack.peek() > v) {
                auxStack.push(mainStack.pop());
                numOfSwaps++;
            }
            auxStack.push(v);
            counter++;
        }
        return numOfSwaps;
    }

    static int refill(Stack<Integer> auxStack, Stack<Integer> mainStack) {
        int numOfSwaps = 0;
        while (!auxStack.isEmpty()) {
            int v = auxStack.pop();
            while (! auxStack.isEmpty() && auxStack.peek() < v) {
                mainStack.push(auxStack.pop());
                numOfSwaps++;
            }
            mainStack.push(v);
            counter++;
        }
        return numOfSwaps;
    }

    static Stack<Integer> sortStack(Stack<Integer> stack) {
        if (stack.isEmpty()) {
            return stack;
        }
        Stack<Integer> aux = new Stack<Integer>();
        int numOfSwaps = 0;
        do {
            numOfSwaps = spillOut(stack, aux);
            numOfSwaps += refill(aux, stack);
        } while (numOfSwaps > 0);
        return stack;
    }

    static Stack<Integer> buildStack(int size, int upperBound) {
        Stack<Integer> result = new Stack<Integer>();
        Random rand = new Random();
        for (int i = 0; i < size; i++) {
            result.push(rand.nextInt(upperBound));
        }
        return result;
    }

    public static void main(String[] argv) {
        for (int step = 0; step < 1010; step += 10) {
            Stack<Integer> stack = buildStack(step, 400);
            counter = 0;
            System.out.println("Before: " + Arrays.deepToString(stack.toArray(new Integer[0])));
            stack = sortStack(stack);
            System.out.println("After: " + Arrays.deepToString(stack.toArray(new Integer[0])));
            System.out.println("Length: " + step);
            System.out.println("Counter: " + counter);
        }
    }
}
