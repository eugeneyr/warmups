package info.lynxnet.warmups.simplestuff;

public class Reverse {
    public <T> T[] reverse(final T[] arr) {
        if (arr != null && arr.length != 0) {
            T val;
            for (int i = 0; i < arr.length / 2; i++) {
                val = arr[i];
                arr[i] = arr[arr.length - i - 1];
                arr[arr.length - i - 1] = val;
            }
        }
        return arr;
    }

    public Object[] reverseObj(final Object[] arr) {
        if (arr != null && arr.length != 0) {
            Object val;
            for (int i = 0; i < arr.length / 2; i++) {
                val = arr[i];
                arr[i] = arr[arr.length - i - 1];
                arr[arr.length - i - 1] = val;
            }
        }
        return arr;
    }


    public static void main(String[] args) {
        Reverse r = new Reverse();
        for (String s : r.reverse(new String[] {"a", "b", "c"})) {
            System.out.println(s);
        }

        String[] arr = new String[] {"a", "b", "c"};
        for (Object s : r.reverseObj((Object[])arr)) {
            System.out.println(s);
        }

    }
}
