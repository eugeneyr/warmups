package org.lynxnet.experiments;

/**
 * Created by IntelliJ IDEA.
 * User: eyaremenko
 * Date: Jul 18, 2006
 * Time: 4:07:50 PM
 * To change this template use File | Settings | File Templates.
 */
public class ThreadB extends Thread {

    ThreadA obj;

    public ThreadB(ThreadA obj) {
        this.obj = obj;
    }

    public void run() {
        while (true) {

            synchronized (obj) {
                System.out.println("Get up!");
                obj.notify();
                System.out.println("Notified.");
            }
        }

    }
}
