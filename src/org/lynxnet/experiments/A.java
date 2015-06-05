package org.lynxnet.experiments;

import java.util.List;
import java.util.LinkedList;


/**
 * Created by IntelliJ IDEA.
 * User: Eugene
 * Date: 13/9/2006
 * Time: 22:41:39
 * To change this template use File | Settings | File Templates.
 */
public class A {
    public static void main(String[] ac) {
        try {
            B b = new B();
            C c1 = new C("Thread1", b);
            c1.setDaemon(false);
            c1.start();
            C c2 = new C("Thread2", b);
            c2.setDaemon(false);
            c2.start();
//            BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
            int i = 0;

//            Random rnd = new Random(System.currentTimeMillis());

            while (true) {
//                String input = reader.readLine();
//                if (input != null && input.equals("fuckoff")) {
//                    System.out.println("Counter 1:" + c1.getAge() );
//                    System.out.println("Counter 2:" + c2.getAge() );
//                    System.exit(0);
//                }

                if (i == 100) {
                    b.put("shutup");
                    b.put("shutup");
//                    c1.start();
//                    c2.start();

//                    System.out.println("Counter 1: " + c1.getAge());
//                    System.out.println("Counter 2: " + c2.getAge());
//                    System.exit(0);
                    break;
                }
                b.put(Integer.toString(i++));
                if (i % 10 == 0) {
                    Thread.sleep(1000 /*rnd.nextInt(100)*/);
                }

            }
//        } catch (IOException e) {
//            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            System.out.println("Ceterum censeo everybody fuck off");
        }
    }
}

class B {
    private List<String> queue;

    B() {
        queue = new LinkedList<String>();
    }

    synchronized void put(String item) {
        System.out.println("Put: " + item);
        queue.add(item);
        notify();
    }

    synchronized String get() {
        try {
            while (queue.isEmpty()) {
                wait();
            }
            return queue.remove(0);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return null;
    }
}

class C extends Thread {
    private String name;
    private B b;
    private int counter;

    C(String name, B b) {
        super(name);
        this.name = name;
        this.b = b;
    }

    public void run() {
        while (true) {
//            synchronized (b) {
                String s = b.get();
                System.out.println(name + ": " + s);
                counter++;
                if ("shutup".equals(s)) {
                    break;
                }
//            }
        }
        System.out.println(name + " counter: " + counter);
    }

    public int getCounter() {
        return counter;
    }
}
