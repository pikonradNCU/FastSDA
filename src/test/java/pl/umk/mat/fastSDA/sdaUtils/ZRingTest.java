package pl.umk.mat.fastSDA.sdaUtils;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class ZRingTest {
    @Test
    void zRing5on5(){
        int R=5;
        int Z=5;
        ZRing zRing = new ZRing(Z,R);

        assertEquals(5,zRing.getR());
        assertEquals(5,zRing.getZ());

        assertEquals(5,zRing.getZR(0));
        assertEquals(4,zRing.getZR(1));
        assertEquals(4,zRing.getZR(3));
        assertEquals(3,zRing.getZR(4));
        assertEquals(0,zRing.getZR(5));

        Ring r=zRing.getRing(3);
        assertEquals(4,r.getR());
    }
}
