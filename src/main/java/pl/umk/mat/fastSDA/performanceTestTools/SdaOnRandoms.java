package pl.umk.mat.fastSDA.performanceTestTools;

import pl.umk.mat.fastSDA.image.Image2D;
import pl.umk.mat.fastSDA.image.SimpleImage2DGray16;
import pl.umk.mat.fastSDA.procesUtils.DevNullPikoTools;
import pl.umk.mat.fastSDA.values.SDA_Params;

import static pl.umk.mat.fastSDA.SDA.SDAProcessors.sda2D;
import static pl.umk.mat.fastSDA.image.BitScale.GRAY_8;
import static pl.umk.mat.fastSDA.nonHistogramSDA.ClassicSDA.classicSDA;
import static pl.umk.mat.fastSDA.nonHistogramSDA.ClassicSDA.classicSdaMultiThread;
import static pl.umk.mat.fastSDA.nonHistogramSDA.ClassicSDA.shiftedClassicMultiThread;
import static pl.umk.mat.fastSDA.nonHistogramSDA.ClassicSDA.tunedClassicMultiThread;
import static pl.umk.mat.fastSDA.nonHistogramSDA.ClassicSDA.tunedClassicMultiThread2;
import static pl.umk.mat.fastSDA.nonHistogramSDA.MaskSDA.floatMaskMultiThread;
import static pl.umk.mat.fastSDA.nonHistogramSDA.MaskSDA.intMaskMultiThread;
import static pl.umk.mat.fastSDA.performanceTestTools.RandTools.randFloatMask;
import static pl.umk.mat.fastSDA.performanceTestTools.RandTools.randMatrix;
import static pl.umk.mat.fastSDA.performanceTestTools.RandTools.stringTime;
import static pl.umk.mat.fastSDA.performanceTestTools.RandomData.getRandomData;

public class SdaOnRandoms {
    // each method return time of processing in miliseconds



    public static long getClassicTime(int x, int y, int r) {
        RandomData rd = getRandomData(x, y);
        classicSDA(rd.getPictureData(), rd.getResult(), x, y, r, GRAY_8);
        return rd.getProcessTime();
    }

    public static long printClassicTime(int x, int y, int r) {
        final long classicTime = getClassicTime(x, y, r);
        System.out.println(" Classic SDA for matrix X=" + x + ", Y=" + y + " is " + stringTime(classicTime));
        return classicTime;
    }

    public static long getClassicSdaMultiThreadTime(int x, int y, int r) {
        RandomData rd = getRandomData(x, y);
        classicSdaMultiThread(rd.getPictureData(), rd.getResult(), x, y, r,GRAY_8);
        return rd.getProcessTime();
    }

    public static long printClassicSdaMultiThreadTime(int x, int y, int r) {
        final long classicSdaMultiThreadTime = getClassicSdaMultiThreadTime(x, y, r);
        System.out.println(" Classic SDA on multi thread time for matrix X=" + x + ", Y=" + y + " is " + stringTime(classicSdaMultiThreadTime));
        return classicSdaMultiThreadTime;
    }

    public static long printClassicSdaMultiThreadTime(int x, int y, int r, long classicTime) {
        long t = printClassicSdaMultiThreadTime(x, y, r);
        System.out.print("Ratio is " + (100 * t / classicTime) + "%");
        return t;
    }

    public static long getTunedClassicMultiThreadTime(int x, int y, int r) {
        RandomData rd = getRandomData(x, y);
        tunedClassicMultiThread(rd.getPictureData(), rd.getResult(), x, y, r);
        return rd.getProcessTime();
    }

    public static void printTunedClassicMultiThreadTime(int x, int y, int r) {
        System.out.println(" Tuned Classic SDA on multi thread time for matrix X=" + x + ", Y=" + y + " is " + stringTime(getTunedClassicMultiThreadTime(x, y, r)));
    }

    public static long getTunedClassicMultiThread2Time(int x, int y, int r) {
        RandomData rd = getRandomData(x, y);
        tunedClassicMultiThread2(rd.getPictureData(), rd.getResult(), x, y, r);
        return rd.getProcessTime();
    }

    public static void printTunedClassicMultiThread2Time(int x, int y, int r) {
        System.out.println(" Tuned Classic SDA on multi thread time second version for matrix X=" + x + ", Y=" + y + " is " + stringTime(getTunedClassicMultiThread2Time(x, y, r)));
    }

    public static long getShiftedClassicMultiThreadTime(int x, int y, int r) {
        RandomData rd = getRandomData(x, y);
        shiftedClassicMultiThread(rd.getPictureData(), rd.getResult(), x, y, r);
        return rd.getProcessTime();
    }

    public static void printShiftedClassicMultiThreadTime(int x, int y, int r) {
        System.out.println(" Shifted Multithread for matrix X=" + x + ", Y=" + y + " is " + stringTime(getShiftedClassicMultiThreadTime(x, y, r)));
    }

    public static long getIntMaskMultiThreadTime(int x, int y, int r) {
        int[][] maskInt = randMatrix(2 * r + 1, 2 * r + 1);
        RandomData rd = getRandomData(x, y);
        intMaskMultiThread(rd.getPictureData(), rd.getResult(), maskInt, x, y, r);
        return rd.getProcessTime();
    }

    public static void printIntMaskMultiThreadTime(int x, int y, int r) {
        System.out.println(" MultiThread with mask time for matrix X=" + x + ", Y=" + y + " is " + stringTime(getIntMaskMultiThreadTime(x, y, r)));
    }

    public static long getFloatMaskMultiThreadTime(int x, int y, int r) {
        float[][] maskFloat = randFloatMask(2 * r + 1, 2 * r + 1);
        RandomData rd = getRandomData(x, y);
        floatMaskMultiThread(rd.getPictureData(), rd.getResult(), maskFloat, x, y, r);
        return rd.getProcessTime();
    }

    public static void printFloatMaskMultiThreadTime(int x, int y, int r) {
        System.out.println(" MultiThread with float mask time for matrix X=" + x + ", Y=" + y + " is " + stringTime(getFloatMaskMultiThreadTime(x, y, r)));
    }

    public static long printHistogram16Multithread(int x, int y, int r) {
        RandomData rd = getRandomData(x, y);
        Image2D image = new SimpleImage2DGray16(x, y);
//        tick();
        image.putIntColors(rd.getPictureData());
//        tick();
        SDA_Params params = new SDA_Params(r, 3, 2000, 1, true, true);
//        tick();
        DevNullPikoTools devTool = new DevNullPikoTools();
        sda2D(image, params, devTool, devTool, true);
        long t = rd.getProcessTime();
        System.out.println(" Histogram based for matrix X=" + x + ", Y=" + y + " is " + stringTime(t));
        return t;
    }

    static private void tick(){System.out.println("tick");}


}
