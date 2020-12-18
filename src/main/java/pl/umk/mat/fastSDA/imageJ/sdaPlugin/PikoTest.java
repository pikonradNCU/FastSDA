package pl.umk.mat.fastSDA.imageJ.sdaPlugin;

import ij.IJ;
import ij.ImagePlus;
import ij.gui.GenericDialog;
import ij.plugin.filter.PlugInFilter;
import ij.process.ImageProcessor;
import pl.umk.mat.fastSDA.procesUtils.PikoLog;

import java.util.stream.IntStream;

public class PikoTest implements PlugInFilter {
    static final String[] tests = new String[]{"gradient","2D on slices","Menthos"};
    PikoLog log=PikoLog.getInstance();
    private ImagePlus image;
    private ImageProcessor imProcessor;
    private int v1,v2;
    private int methodIndex;

    @Override
    public int setup(String s, ImagePlus imagePlus) {

        image = imagePlus;
        return DOES_ALL;
    }

    @Override
    public void run(ImageProcessor imageProcessor) {
        imProcessor = imageProcessor;
        if (showDialog()) {
            switch (methodIndex){
                case 0:{
                    gradient(imageProcessor);
                    break;
                }
                case 1:{
                    test2();
                }
            }

            image.updateAndDraw();
        }

    }

    private void test2() {

    }

    private void gradient(ImageProcessor imageProcessor) {
        final int wide = v1;
        final int overwhite=v2;
        final float factor = ((float) overwhite)/wide;
        String s = "gradient test has been called with wide" + wide + ", overwhite " + overwhite;
        log.info(s);
        IJ.showMessage(s);
        int X=imageProcessor.getWidth();
        IntStream
                .range(0,X)
                .parallel()
                .forEach(x->IntStream
                        .range(0,imageProcessor.getHeight())
                        .forEach(y->((short[]) imageProcessor.getPixels())[x+X*y]=((short)((x%wide)*factor)))
                );
    }

    private boolean showDialog() {

        GenericDialog gd = new GenericDialog("Piko Test process");

        gd.addNumericField(" int value 1  ",100,0);
        gd.addNumericField(" int value 2", 4096, 0);
        gd.addChoice("method",tests,"gradient");
        gd.showDialog();

        if (gd.wasCanceled())  return false;

        v1 = (int) gd.getNextNumber();
        v2 = (int) gd.getNextNumber();
        methodIndex =  gd.getNextChoiceIndex();
        return true;
    }
}
