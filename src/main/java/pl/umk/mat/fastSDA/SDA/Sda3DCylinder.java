package pl.umk.mat.fastSDA.SDA;


import pl.umk.mat.fastSDA.flyingHistogram.Histogram;
import pl.umk.mat.fastSDA.image.Image;
import pl.umk.mat.fastSDA.image.Image2D;
import pl.umk.mat.fastSDA.procesUtils.Messenger;
import pl.umk.mat.fastSDA.procesUtils.ParallelListenProgressBar;
import pl.umk.mat.fastSDA.procesUtils.PikoProgress;
import pl.umk.mat.fastSDA.procesUtils.Tools;
import pl.umk.mat.fastSDA.sdaUtils.Ring;
import pl.umk.mat.fastSDA.values.SDA_Params;

import java.util.ArrayList;
import java.util.List;

public class Sda3DCylinder extends Sda3DAbstract {
    Ring ring;
    public Sda3DCylinder(Image image, SDA_Params params, PikoProgress pbar, Messenger messenger) {
        super(image,params,pbar,messenger);
        ring = new Ring(R);
    }

    public void sda3DCylinder() {

        int numberOfThreads = availableProcessors;
        log.info("Process 3D Cylinder in "+numberOfThreads+" threads");
        int[] bounds = Tools.getBounds(shape2D.getX(), numberOfThreads);
        ParallelListenProgressBar progressBar = new ParallelListenProgressBar(pbar, 500);
        List<Thread> threadList = new ArrayList<>();
        for (int tid = 0; tid < numberOfThreads; tid++) {
            final int t = tid;
            Thread thread = new Thread(() -> {
                int begin = bounds[t];
                int boundX = bounds[t + 1];
                List<SdaTool> toolList = new ArrayList<>();
                progressBar.addListener(t, boundX - begin);
                //todo for wieghted change hist into list of hints
                Histogram hist = SDAProcessors.chooseHistogram(scale);
                for (int z = 0; z < deep; z++) {
                    SdaTool sliceTool = new SdaTool(image2DList.get(z), hist, ring);
                    toolList.add(sliceTool);
                    sliceTool.init(begin);
                }

                Histogram casheHist = hist.getCopy();
                int x = begin;
                int y;
                do {
                    y = 0;
                    do {
                        for (int z = 0; z < deep; z++) {
                            Image2D image2D = image2DList.get(z);
                            int[][] buffer = bufferList.get(z);
//                            int size = hist.getSize();
//                            int color = image2D.getGray16Colur(x, y);
//                            final long darkers = hist.checkColor(color - params.getTreshold());
//                            final int newColor = (int) ((darkers * overWhite) / size);
//                            buffer[x][y] = newColor;
                            //Change here for take weighed color from slices
                            calcColor2(buffer,hist,x,y,image2D);
                        }
                        for (int z = 0; z < deep; z++) toolList.get(z).liftUp();
                        y++;
                    } while (y < shape2D.getY());
                    for (int z = 0; z < deep; z++) toolList.get(z).correctRight(casheHist, boundX);
                    x++;
                    progressBar.showProgress(t, x - begin);
                } while (x < boundX);
                progressBar.removeListener(t);
            });
            thread.start();
            threadList.add(thread);
        }
        progressBar.startProgressNotification();
        joinThreads(threadList);
        log.info("Kontrol val on exit: "+controlVal);
        copyNewImage();
    }
}
