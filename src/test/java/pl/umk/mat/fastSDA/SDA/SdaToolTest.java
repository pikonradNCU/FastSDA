package pl.umk.mat.fastSDA.SDA;


import org.junit.jupiter.api.Test;
import pl.umk.mat.fastSDA.flyingHistogram.Histogram;
import pl.umk.mat.fastSDA.image.SimpleImage2DGray16;
import pl.umk.mat.fastSDA.sdaUtils.Ring;

import static pl.umk.mat.fastSDA.image.BitScale.GRAY_16;

public class SdaToolTest {
    @Test
    void rinng5Test(){
        SdaTool st = new SdaTool(
                new SimpleImage2DGray16(100,100),
                new Histogram(GRAY_16),
                new Ring(20));
        System.out.println("should be 13:"+st.getRing().getHalfRing(16));
    }
}
