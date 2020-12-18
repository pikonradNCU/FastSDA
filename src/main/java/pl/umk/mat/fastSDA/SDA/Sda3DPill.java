package pl.umk.mat.fastSDA.SDA;


import pl.umk.mat.fastSDA.flyingHistogram.Histogram;
import pl.umk.mat.fastSDA.image.Image;
import pl.umk.mat.fastSDA.image.Image2D;
import pl.umk.mat.fastSDA.procesUtils.Messenger;
import pl.umk.mat.fastSDA.procesUtils.PikoProgress;
import pl.umk.mat.fastSDA.sdaUtils.Ring;
import pl.umk.mat.fastSDA.sdaUtils.ZRing;
import pl.umk.mat.fastSDA.values.SDA_Params;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

public class Sda3DPill extends Sda3DAbstract {
    final int Z;
    final ZRing zRing;
    final AtomicInteger done = new AtomicInteger(0);
    public Sda3DPill(Image image, SDA_Params params, PikoProgress pbar, Messenger messenger) {
        super(image, params, pbar, messenger);
        Z = params.getZ();
        zRing = new ZRing(Z,R);
    }

    public void sda3DPill(){
        log.info("Process 3D Menthos in "+availableProcessors+" threads");
        List<Thread> threads = new ArrayList<>();
        int z=0;
        while (z<deep){
            int finalZ = z;
            Thread t = new Thread(()->{
                sdaPillOnZ(finalZ);
            });
            threads.add(t);
            t.start();

            if (threads.size()==availableProcessors){
                joinThreads(threads);
                threads = new ArrayList<>();
            }
            z++;
        }
        joinThreads(threads);

        copyNewImage();
    }

    private void sdaPillOnZ(int z){
        int[][] buffer = bufferList.get(z);
        int z0 = Math.max(0,z-Z);
        int zEnd = Math.min(deep,z+Z);

        Histogram hist = SDAProcessors.chooseHistogram(scale);
        List<SdaTool> toolList = new ArrayList<>();

        for(int sliceZ = z0; sliceZ<zEnd;sliceZ++){
            int delta = (sliceZ<z) ? z-sliceZ : sliceZ - z; // absolut delta
            Ring sliceRing = zRing.getRing(delta);
            SdaTool sliceTool = new SdaTool(image2DList.get(sliceZ), hist, sliceRing);
            toolList.add(sliceTool);
            sliceTool.init(0);
        }
        Histogram casheHist = hist.getCopy();
        int x = 0;
        int y;
        do {
            y = 0;
            do {
                final Image2D image2D = image2DList.get(z);
                calcColor2(buffer, hist, x, y, image2D);
                for (int sliceZIndexInList = 0; sliceZIndexInList<zEnd-z0;sliceZIndexInList++) {
                    final SdaTool sdaTool = toolList.get(sliceZIndexInList);
                    sdaTool.liftUp();
                }
                y++;
            } while (y < shape2D.getY());
            for (int sliceZIndexInList = 0; sliceZIndexInList<zEnd-z0;sliceZIndexInList++) {
                final SdaTool sdaTool = toolList.get(sliceZIndexInList);
                sdaTool.correctRight(casheHist, shape2D.getX());
            }
            x++;
        } while (x < shape2D.getX());

    }
}
