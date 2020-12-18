package pl.umk.mat.fastSDA.SDA;


import pl.umk.mat.fastSDA.flyingHistogram.Histogram;
import pl.umk.mat.fastSDA.image.BitScale;
import pl.umk.mat.fastSDA.image.Image;
import pl.umk.mat.fastSDA.image.Image2D;
import pl.umk.mat.fastSDA.procesUtils.Messenger;
import pl.umk.mat.fastSDA.procesUtils.PikoProgress;
import pl.umk.mat.fastSDA.values.SDA_Params;

import static pl.umk.mat.fastSDA.image.BitScale.GRAY_16;
import static pl.umk.mat.fastSDA.image.BitScale.GRAY_8;

public class SDAProcessors {

    private static final int THREADS_NUMBER = numberOfThreads();

    static final int debugX = -1;
    static final int debugY = -1;

    public static void sda3DCylinder(Image image, SDA_Params params, PikoProgress pbar, Messenger messenger){
        Sda3DCylinder sdaC = new Sda3DCylinder(image,params,pbar,messenger);
        sdaC.sda3DCylinder();
    }

    public static void sda3DPill(Image image, SDA_Params params, PikoProgress pbar, Messenger messenger) {
        Sda3DPill sdaP = new Sda3DPill(image,params,pbar,messenger);
        sdaP.sda3DPill();
    }

    public static void sda2D(Image2D image, SDA_Params params, PikoProgress imageJProgressBar, Messenger messenger, boolean multithreading) {
        SDA2D sda= new SDA2D(image,params,imageJProgressBar,messenger);
        sda.sda2D(multithreading);
    }

    static Histogram chooseHistogram(BitScale scale) {
        switch (scale) {
            case GRAY_8: {
                return new Histogram(GRAY_8);
            }
            default: {
                return new Histogram(GRAY_16);
            }
        }
    }

    private static int numberOfThreads() {
        int availableProcessors = Runtime.getRuntime().availableProcessors();
        int threadsNumber = availableProcessors - 1;
        if (6 < threadsNumber) threadsNumber--;
        if (12 < threadsNumber) threadsNumber--;
        return threadsNumber;
    }



    public static int numberOfCores(){
        int availableProcessors = Runtime.getRuntime().availableProcessors();
        return (availableProcessors>4) ? availableProcessors - (availableProcessors / 2) : availableProcessors;
    }

}
