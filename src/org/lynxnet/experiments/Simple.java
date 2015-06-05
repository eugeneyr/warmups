package org.lynxnet.experiments;

/**
 * Created by IntelliJ IDEA.
 * User: Eugene
 * Date: 24/8/2006
 * Time: 20:58:30
 * To change this template use File | Settings | File Templates.
 */
public class Simple {
    private int digits;
    private String text;

    public Simple(int i, String s) {
        digits = i;
        text = s;
    }

    public boolean equals(Object o) {
        if ((o == null) || (! (o instanceof Simple))) {
            return false;
        }
        Simple other = (Simple) o;
        if (digits != other.digits) {
            return false;
        }
        if (other.text != null) {
            return other.text.equals(text);
        }
        return text == null;
    }


}
