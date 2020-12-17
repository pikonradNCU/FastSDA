package pl.umk.mat.fastSDA.sdaUtils;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class RingTest {
    @Test
    void rinng5Test(){
        Ring ring = new Ring(5);

        assertEquals(ring.getHalfRing(-5),1);
        assertEquals(ring.getHalfRing(-4),4);
        assertEquals(ring.getHalfRing(-3),5);
        assertEquals(ring.getHalfRing(-2),5);
        assertEquals(ring.getHalfRing(-1),5);
        assertEquals(ring.getHalfRing(0),6);
        assertEquals(ring.getHalfRing(1),5);
        assertEquals(ring.getHalfRing(2),5);
        assertEquals(ring.getHalfRing(3),5);
        assertEquals(ring.getHalfRing(4),4);
        assertEquals(ring.getHalfRing(5),1);

        assertEquals(ring.getHalfRing(-7),0);
        assertEquals(ring.getHalfRing(6),0);
    }

    @Test
    void ring0Test(){
        Ring ring = new Ring(0);
        assertEquals(1,ring.getHalfRing(0));

    }

}