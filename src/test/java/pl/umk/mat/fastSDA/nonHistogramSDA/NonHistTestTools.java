package pl.umk.mat.fastSDA.nonHistogramSDA;

import static pl.umk.mat.fastSDA.performanceTestTools.RandTools.randFloatMask;
import static pl.umk.mat.fastSDA.performanceTestTools.RandTools.randMatrix;
import static pl.umk.mat.fastSDA.performanceTestTools.SdaOnRandoms.printClassicSdaMultiThreadTime;
import static pl.umk.mat.fastSDA.performanceTestTools.SdaOnRandoms.printClassicTime;
import static pl.umk.mat.fastSDA.performanceTestTools.SdaOnRandoms.printFloatMaskMultiThreadTime;
import static pl.umk.mat.fastSDA.performanceTestTools.SdaOnRandoms.printHistogram16Multithread;
import static pl.umk.mat.fastSDA.performanceTestTools.SdaOnRandoms.printIntMaskMultiThreadTime;
import static pl.umk.mat.fastSDA.performanceTestTools.SdaOnRandoms.printShiftedClassicMultiThreadTime;
import static pl.umk.mat.fastSDA.performanceTestTools.SdaOnRandoms.printTunedClassicMultiThread2Time;
import static pl.umk.mat.fastSDA.performanceTestTools.SdaOnRandoms.printTunedClassicMultiThreadTime;

public class NonHistTestTools {

    static void startRandomizedValues(int x, int y, int r, boolean visible, boolean startSingle) {
        int[][] m = randMatrix(x, y);
        int[][] result = new int[x][y];
        int[][] maskInt = randMatrix(2 * r + 1, 2 * r + 1);
        float[][] maskFloat = randFloatMask(2 * r + 1, 2 * r + 1);
        startAllMethods(x, y, r, startSingle, m, result, maskInt, maskFloat,visible);
    }

    private static void startAllMethods(int x, int y, int r, boolean startSingle, int[][] m, int[][] result,
                                        int[][] maskInt, float[][] maskFloat,boolean visible) {

        if (startSingle) {
            long classicTime = printClassicTime(x,y,r);
            printClassicSdaMultiThreadTime(x,y,r,classicTime);
        } else {
            printClassicSdaMultiThreadTime(x, y, r);
        }
        printTunedClassicMultiThreadTime(x,y,r);
        printTunedClassicMultiThread2Time(x,y,r);
        printShiftedClassicMultiThreadTime(x,y,r);
        printIntMaskMultiThreadTime(x,y,r);
        printFloatMaskMultiThreadTime(x,y,r);
        printHistogram16Multithread(x,y,r);
    }
}
