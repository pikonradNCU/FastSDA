package pl.umk.mat.fastSDA.SDA;

import pl.umk.mat.fastSDA.flyingHistogram.Histogram;
import pl.umk.mat.fastSDA.image.BitScale;
import pl.umk.mat.fastSDA.image.Image2D;
import pl.umk.mat.fastSDA.image.Shape2D;
import pl.umk.mat.fastSDA.procesUtils.Messenger;
import pl.umk.mat.fastSDA.procesUtils.PikoLog;
import pl.umk.mat.fastSDA.procesUtils.PikoProgress;
import pl.umk.mat.fastSDA.values.SDA_Params;

import java.util.List;
import java.util.stream.IntStream;

import static pl.umk.mat.fastSDA.image.BitScale.GRAY_8;

public class SdaAbstract {
    final SDA_Params params;
    final PikoProgress pbar;
    final Messenger messenger;
    PikoLog log;
    int availableProcessors;
    BitScale scale;
    int overWhite;
    boolean isNegative;
    Shape2D shape2D;
    int R;

    int treshold;

    long controlVal = 0;

    public SdaAbstract(BitScale scale,Shape2D shape2D, SDA_Params params, PikoProgress pbar, Messenger messenger) {
        this.params = params;
        this.pbar = pbar;
        this.messenger = messenger;
        log = PikoLog.getInstance();
        availableProcessors = Runtime.getRuntime().availableProcessors();
        this.scale = scale;
        overWhite = (scale == GRAY_8) ? 255 : params.getOverWhite();
        isNegative = params.isRelationshipDirection();
        this.shape2D = shape2D;
        R=params.getR();
        log.info("SDA called with following params: \n " +
                " R: "+params.getR()+
                "\n treshhold: "+params.getTreshold()
        );

        treshold = params.getTreshold();
    }

    static void joinThreads(List<Thread> threads) {
        for (Thread t:threads) {
            try{
                t.join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    void calcSdaColor(int[][] buffer, Histogram hist, int x, int y, Image2D image2D) {
        int size = hist.getSize();
        int color = image2D.getGray16Colur(x, y);
        int colorToCheck = color - treshold;
        int advancedColorToCheck = (color < 0 || treshold < color) ? color - treshold : 0;
        final long darkers = hist.checkColor(colorToCheck);
        buffer[x][y] = (int) ((darkers * overWhite) / size);
        if (advancedColorToCheck!=colorToCheck) this.controlVal++;
    }

    void calcColor2(int[][] buffer, Histogram hist, int x, int y, Image2D image) {
        int color = image.getGray16Colur(x, y);
        int size = hist.getSize();
        long darkers = (isNegative)
                //todo sprawdzic warunki brzegowe
                ? size - hist.checkColor(color + treshold -1)
                : hist.checkColor((color < 0 || treshold <= color) ? color - treshold +1: 0);
        final long longNewColor = (darkers * overWhite) / size;
        final int intNewColor = (int) longNewColor;
        buffer[x][y] = intNewColor;
    }

    protected int getMaxColor(Image2D image) {
        return IntStream.range(0, shape2D.getX()-1)
                .parallel()
                .map(x -> IntStream
                        .range(0, shape2D.getY()-1)
                        .map(y -> image.getGray16Colur(x, y))
                        .max().orElse(0)
                ).max().orElse(0);
    }
}
