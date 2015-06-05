package org.lynxnet.experiments;

/**
 * Created by IntelliJ IDEA.
 * User: eyaremenko
 * Date: Jul 18, 2006
 * Time: 4:11:56 PM
 * To change this template use File | Settings | File Templates.
 */
public class Runner {
    public static void main(String[] av) {
        ThreadA a = new ThreadA();
        ThreadB b = new ThreadB(a);
        a.start();
        b.start();
    }
}
