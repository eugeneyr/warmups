package org.lynxnet.experiments;

/**
 * Created by IntelliJ IDEA.
 * User: eyaremenko
 * Date: Jul 18, 2006
 * Time: 4:03:06 PM
 * To change this template use File | Settings | File Templates.
 */
public class ThreadA extends Thread {
    public void run() {
        try {
            while (true) {
                synchronized (this) {
                    System.out.println("Gonna take a nap...");
                    wait();
                    System.out.println("Avakened...");
                    sleep(1000);
                    new Thread(new Runnable() {public void run() {}}).start();
                }
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

}
