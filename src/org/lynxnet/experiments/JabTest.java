package org.lynxnet.experiments;

import junit.framework.TestCase;

/**
 * Created by IntelliJ IDEA.
 * User: Eugene
 * Date: 25/8/2006
 * Time: 0:43:23
 * To change this template use File | Settings | File Templates.
 */
public class JabTest extends TestCase {
    public JabTest(String name) {
        super(name);
    }

    public void testJab1() {
        doPrint(new JaberwockyImpl1());
        System.out.println();
    }

    public void testJab2() {
        doPrint(new JaberwockyImpl2());
        System.out.println();
    }

    public void testEqual() {
        assertTrue(doTest(new JaberwockyImpl1()).equals(doTest(new JaberwockyImpl2())));
    }


    public String doTest(Jaberwocky jab) {
        String result = "";
        for (int i = 0; i <= 1; i++) {
            for (int j = 0; j <= 1; j++) {
                for (int k = 0; k <= 1; k++) {
                    result += "" + i + " " + j + " " + k + "    " + jab.jabberwock(i == 1, j == 1, k == 1) + "|";
                }
            }
        }
        return result;
    }

    public void doPrint(Jaberwocky jab) {
        for (int i = 0; i <= 1; i++) {
            for (int j = 0; j <= 1; j++) {
                for (int k = 0; k <= 1; k++) {
                    System.out.println(i + " " + j + " " + k + "    " + jab.jabberwock(i == 1, j == 1, k == 1));
                }
            }
        }
    }

}
