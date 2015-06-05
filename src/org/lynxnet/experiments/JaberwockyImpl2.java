package org.lynxnet.experiments;

/**
 * Created by IntelliJ IDEA.
 * User: Eugene
 * Date: 25/8/2006
 * Time: 0:40:59
 * To change this template use File | Settings | File Templates.
 */
public class JaberwockyImpl2 implements Jaberwocky {
    public int jabberwock(boolean frumious, boolean manxome, boolean uffish) {
        if (frumious && !uffish) {
            return BANDERSNATCH;
        }
        return JUBJUB_BIRD;
    }
}
