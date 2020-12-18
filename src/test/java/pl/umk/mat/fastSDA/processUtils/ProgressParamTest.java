package pl.umk.mat.fastSDA.processUtils;

import org.junit.jupiter.api.Test;
import pl.umk.mat.fastSDA.procesUtils.ProgressParam;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class ProgressParamTest {
    @Test
    public void getValTest(){
        ProgressParam pp = new ProgressParam(7);
        pp.setCurrent(3);
        assertEquals(7*pp.getVal(),3,0.00001);
    }
}
