package pl.umk.mat.fastSDA.SDA;

import pl.umk.mat.fastSDA.flyingHistogram.Histogram;
import pl.umk.mat.fastSDA.image.Image2D;
import pl.umk.mat.fastSDA.procesUtils.Messenger;
import pl.umk.mat.fastSDA.procesUtils.ParallelListenProgressBar;
import pl.umk.mat.fastSDA.procesUtils.PikoProgress;
import pl.umk.mat.fastSDA.procesUtils.Tools;
import pl.umk.mat.fastSDA.sdaUtils.ImageRewriters;
import pl.umk.mat.fastSDA.sdaUtils.Ring;
import pl.umk.mat.fastSDA.values.SDA_Params;

import java.util.ArrayList;
import java.util.List;

public class SDA2D extends SdaAbstract {
    private final Image2D image;
    private Ring ring;
    int[][] buffer;

    public SDA2D(Image2D image, SDA_Params params, PikoProgress pbar, Messenger messenger) {
        super(image.getScale(), image.getShape(), params, pbar, messenger);
        this.image = image;
        ring = new Ring(R);
        buffer = new int[shape2D.getX()][shape2D.getY()];
//        int maxColor = getMaxColor(image);
//        overWhite = maxColor;
//        log.info("Maximal color: " + maxColor);
    }

    public void sda2D(boolean multithreading) {
        ParallelListenProgressBar progressBar = new ParallelListenProgressBar(pbar, 500);
        if (multithreading) {
            int[] bounds = Tools.getBounds(shape2D.getX(), availableProcessors);
            List<Thread> threadList = new ArrayList<>();
            for (int tid = 0; tid < availableProcessors; tid++) {
                final int t = tid;
                Thread thread = new Thread(() -> {
                    int begin = bounds[t];
                    int boundX = bounds[t + 1];
                    progressBar.addListener(t, boundX - begin);
                    SdaOnPartialImige(begin, boundX, progressBar, t);
                    progressBar.removeListener(t);
                });
                threadList.add(thread);
                thread.start();
            }
            progressBar.startProgressNotification();
            joinThreads(threadList);
        } else {
            SdaOnPartialImige(0, shape2D.getX(), null, 1);
        }
        ImageRewriters.putBufferIntoImage2D(image, scale, shape2D, buffer, log);
        log.info("control val: "+controlVal);
    }

    void SdaOnPartialImige(int begin, int boundX, ParallelListenProgressBar progressBar, int partNumber) {
        Histogram hist = SDAProcessors.chooseHistogram(scale);
        SdaTool tool = new SdaTool(image, hist, ring);
        log.info("SDA start");
        //check Histograms
        tool.init(begin);
        log.info("base histogram inited");
        log.info(" overWhite: " + overWhite);
        Histogram casheHist = hist.getCopy();
        do {
            int x = tool.getPosX();
            do {
                int y = tool.posY;
                calcColor2(buffer,hist, x, y,image);

//                //the following if is fordebug
//                if (darkers < 0) {
//                    log.info(" check " + x + "/" + y + " color " + color
//                            + " treshold " + treshold
//                            + " checkColor " + colorTocheck
//                            + " darkers " + darkers
//                            + " hist size " + hist.getSize()
//                            + " overWhite " + overWhite
//                            + " long new color " + longNewColor
//                            + " int new color " + intNewColor
//                            + " check Consistency " + HistogramSpecTools.checkConsistency(hist)
//                            + " check Positive" + HistogramSpecTools.checkPositive(hist)
//                    );
//                }
                tool.liftUp();
            } while (tool.getPosY() < shape2D.getY());
            tool.correctRight(casheHist, boundX);
            if (pbar != null) {
                if (progressBar != null) progressBar.showProgress(partNumber, x - begin);
                else pbar.showProgress(tool.getPosX(), boundX);
            }
        } while (tool.getPosX() < boundX);
    }
}
