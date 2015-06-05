package info.lynxnet.warmups.simplestuff;

public class NoOfProcs {
    final String something;

    public NoOfProcs() {
        something = "";
    }
    public static void main(String[] args) {
        System.out.println(Runtime.getRuntime().availableProcessors());
    }
}
