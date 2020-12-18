package pl.umk.mat.fastSDA.performanceTestTools;

import org.junit.jupiter.api.Test;

import static pl.umk.mat.fastSDA.performanceTestTools.SdaOnRandoms.printClassicTime;

class SdaOnRandomsTestIT {

    @Test
    void getClassicTimeTest() {
        int x=600,y=480,r=50;
        printClassicTime(x,y,r);
    }
}