package pl.umk.mat.fastSDA.imageJ.softSdaPlugin;

import ij.IJ;
import ij.ImagePlus;
import ij.plugin.filter.PlugInFilter;
import ij.process.ImageProcessor;
import pl.umk.mat.fastSDA.image.BitScale;
import pl.umk.mat.fastSDA.image.Image2D;
import pl.umk.mat.fastSDA.image.Shape2D;
import pl.umk.mat.fastSDA.imageJ.imigeContainers.CoveredImige2DGray16;
import pl.umk.mat.fastSDA.imageJ.imigeContainers.CoveredImige2DGray8;
import pl.umk.mat.fastSDA.imageJ.imigeContainers.ImageJProgressBar;
import pl.umk.mat.fastSDA.nonHistogramSDA.SlowSdaProcessor;
import pl.umk.mat.fastSDA.values.SoftSDAParams;

public class SoftSda implements PlugInFilter {
    SoftSDAParams params;
    ImagePlus image = null;

    int width;
    int height;
    private ImageJProgressBar progressBar;

    @Override
    public int setup(String arg, ImagePlus imagePlus) {
        if (arg.equals("about")) {
            showAbout();
            return DONE;
        }
        image = imagePlus;
        width = image.getWidth();
        height = image.getHeight();



        return DOES_8G | DOES_16;
    }

    @Override
    public void run(ImageProcessor imageProcessor) {
        if (showDialog()) {
            for (int i = 1; i<=image.getStackSize();i++){
                progressBar = new ImageJProgressBar();
                process2D(i);
            }
        }
    }

    private void process2D(int i) {
        Image2D pikoImige = getImage2DFromSlice(i);

        SlowSdaProcessor.sda(pikoImige,params);

     }

    private Image2D getImage2DFromSlice(int i) {
        final ImageProcessor ip = image.getStack().getProcessor(i);
        Shape2D shape = new Shape2D(width, height);
        BitScale scale = (image.getType() == ImagePlus.GRAY8) ? BitScale.GRAY_8 : BitScale.GRAY_16;
        return (scale == BitScale.GRAY_8) ?
                new CoveredImige2DGray8(ip, shape) : new CoveredImige2DGray16(ip, shape);
    }

    void messageNotImplemented(){
        IJ.showMessage("Soft SDA not implemented for parameters : "+getStringFromParams());
    }

    String getStringFromParams() {
        String s = "\n Radius "+params.getR();
        s+="\n method "+params.getMethod();
        s+="\n is mask float? "+params.isMaskFloat();
        s+="\n ramp size "+params.getRampFactor();
        return s;
    }

    private boolean showDialog() {
        params = SoftSdaDialogBox.showDialog();
        if (params == null) return false;

        return true;
    }

    private void showAbout() {
        IJ.showMessage("Soft SDA",
                "new approach for Statistical Domain Analizys, experimental !!!"
        );
    }
}
