package org.lynxnet.experiments;

/**
 * Created by IntelliJ IDEA.
 * User: Eugene
 * Date: 25/8/2006
 * Time: 0:39:51
 * To change this template use File | Settings | File Templates.
 */
public class JaberwockyImpl1 implements Jaberwocky {
    public int jabberwock(boolean frumious, boolean manxome, boolean uffish) {
        if (!frumious) {
            if (manxome) {
                if (uffish) {
                    return JUBJUB_BIRD;
                } else if (!manxome) {
                    return BANDERSNATCH;
                } else {
                    return JUBJUB_BIRD;
                }
            } else {
                return JUBJUB_BIRD;
            }
        } else if (!uffish) {
            if (!manxome) {
                return BANDERSNATCH;
            }
        } else if (!manxome) {
            return JUBJUB_BIRD;
        }
        if (!(uffish || !manxome)) {
            return BANDERSNATCH;
        }
        return JUBJUB_BIRD;
    }
}
