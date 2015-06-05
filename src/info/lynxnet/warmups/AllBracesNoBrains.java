package info.lynxnet.warmups;

public class AllBracesNoBrains {
    public static void embraceBraces(String s, int lefts, int rights) {
        if (lefts == rights) {
            if (lefts == 0) {
                System.out.println(s);
                return;
            } else {
                embraceBraces(s + "(", lefts - 1, rights);
            }
        } else if (lefts == 0) {
            embraceBraces(s + ")", lefts, rights - 1);
        } else {
            embraceBraces(s + "(", lefts - 1, rights);
            embraceBraces(s + ")", lefts, rights - 1);
        }
    }

    public static void main(String[] args) {
        embraceBraces("", 3, 3);
    }
}
