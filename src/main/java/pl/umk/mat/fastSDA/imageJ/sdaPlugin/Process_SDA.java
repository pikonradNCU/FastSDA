package pl.umk.mat.fastSDA.imageJ.sdaPlugin;

import ij.IJ;
import ij.ImageJ;
import ij.ImagePlus;
import ij.ImageStack;
import ij.plugin.filter.PlugInFilter;
import ij.process.ImageProcessor;
import pl.umk.mat.fastSDA.SDA.SDAProcessors;
import pl.umk.mat.fastSDA.image.BitScale;
import pl.umk.mat.fastSDA.image.Image2D;
import pl.umk.mat.fastSDA.image.Shape2D;
import pl.umk.mat.fastSDA.imageJ.IJMessenger;
import pl.umk.mat.fastSDA.imageJ.imigeContainers.Cover3D;
import pl.umk.mat.fastSDA.imageJ.imigeContainers.CoveredImige2DGray16;
import pl.umk.mat.fastSDA.imageJ.imigeContainers.CoveredImige2DGray8;
import pl.umk.mat.fastSDA.imageJ.imigeContainers.ImageJProgressBar;
import pl.umk.mat.fastSDA.procesUtils.PikoLog;
import pl.umk.mat.fastSDA.values.SDA_Params;

import java.awt.image.ColorModel;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * A template for processing each pixel of either
 * GRAY8, GRAY16, GRAY32 or COLOR_RGB images.
 *
 * @author Johannes Schindelin
 */
public class Process_SDA implements PlugInFilter {
    PikoLog log=PikoLog.getInstance();

    protected ImagePlus image;
    ImageStack imageStack;

    // image property members
    private int width;
    private int height;

    // plugin parameters
    public double value;
    public String name;
    private SDA_Params params;
    private int methodIndex;

    @Override
    public int setup(String arg, ImagePlus imp) {
        if (arg.equals("about")) {
            showAbout();
            return DONE;
        }
        try {
            image = imp;
            imageStack = imp.getImageStack();
            log.info("SLICES " + image.getSlice());
            log.info("stack Size " + image.getImageStackSize());

            width = imp.getWidth();
            height = imp.getHeight();
            log.info("Image shapes:\n" +
                    "   width:" + width + "\n" +
                    "   height:" + height + "\n" +
                    "   type: " + imp.getType() + "\n" +
                    "   Image type: " + image.getType());
        } catch (Exception e){
            return DONE;
        }
        return DOES_ALL;//DOES_8G | DOES_16 | DOES_32 | DOES_RGB | DOES_STACKS;
    }

    @Override
    public void run(ImageProcessor ip) {
        log.info("run is called");
        // get width and height

        if (showDialog()) {
            long startTime = System.currentTimeMillis();
            process(ip);
            long processTime = System.currentTimeMillis() - startTime;
            image.updateAndDraw();
            log.info("Processing time of methodId "+methodIndex+ " is "+(processTime/1000)+" sek.");
        }
    }
    private boolean showDialog() {
        SdaPluginParams sdaPluginParams = SdaDialogBox.showDialog();
        if (sdaPluginParams==null) return false;
        params = sdaPluginParams.getSdaParams();
        methodIndex = sdaPluginParams.getMethodIndex();

        return true;
    }

    /**
     * Process an image.
     * <p>
     * Please provide this method even if {@link PlugInFilter} does require it;
     * the method {@link PlugInFilter#run(ImageProcessor)} can only
     * handle 2-dimensional data.
     * </p>
     * <p>
     * If your plugin does not change the pixels in-place, make this method return the results and
     * change the {@link #setup(String, ImagePlus)} method to return also the
     * <i>DOES_NOTHING</i> flag.
     * </p>
     *
     * @param image the image (possible multi-dimensional)
     */
    public void process(ImagePlus image) {
        // slice numbers start with 1 for historical reasons
        for (int i = 1; i <= image.getStackSize(); i++)
            process(image.getStack().getProcessor(i));
    }

    // Select processing method depending on image type
    public void process(ImageProcessor ip) {
        PikoLog.getInstance().info("BIT DEPTH"+ip.getBitDepth());
        if ((image.getType() == ImagePlus.GRAY8) || (image.getType() == ImagePlus.GRAY16)) {
            BitScale scale = (image.getType() == ImagePlus.GRAY8) ? BitScale.GRAY_8 : BitScale.GRAY_16;
            int deph = imageStack.getSize();
            Properties properties = image.getProperties();
            if (properties!=null)
                for (String key:properties.stringPropertyNames())
                    log.info("Image propertiy "+key+": "+properties.get(key));
            if (deph == 1){
                ColorModel cm = ip.getColorModel();
                process2D(ip, scale,new ImageJProgressBar(),true);
            } else {
                switch (methodIndex){
                    case 0:{//Cylinder 3D
                        SDAProcessors.sda3DCylinder(new Cover3D(image,scale),params,new ImageJProgressBar(),new IJMessenger());
                        break;
                    }
                    case 1:{//force 2D
                        process2Don3DImage(scale, deph);
                        break;
                    }
                    case 2:{// Menthos
                        if (params.getZ()==0) {
                            process2Don3DImage(scale, deph);
                        } else {
                            SDAProcessors.sda3DPill(new Cover3D(image,scale),params,new ImageJProgressBar(),new IJMessenger());
                        }
                        break;
                    }
                }
            }

        } else {
            throw new RuntimeException("not supported");
        }
    }

    private void process2Don3DImage(BitScale scale, int deph) {
        AtomicInteger slicesDone=new AtomicInteger(0);
        List<Thread> threadList = new ArrayList<>();
        int numberOfThreads= Runtime.getRuntime().availableProcessors();
        for (int threadIndex =0; threadIndex<numberOfThreads;threadIndex++){
            int ti=threadIndex;
            Thread t = new Thread(()->{
                int i=ti;
                while(i<deph){
//                                if (i<1) //for debug work only for x=1
                        process2D(imageStack.getProcessor(i+1),scale,
                                null,false
                );
                    i+=numberOfThreads;
                    slicesDone.incrementAndGet();
                }
            });
            threadList.add(t);
            t.start();
        }
        while (slicesDone.get()<deph){
            try {
                Thread.sleep(500);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            IJ.showProgress(slicesDone.get(),deph);
        }
        for (Thread t : threadList){
            try{
                t.join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    private void process2D(ImageProcessor ip,
                           BitScale scale,
                           ImageJProgressBar imageJProgressBar,
                           boolean multithreading) {
        Shape2D shape = new Shape2D(width,height);
        Image2D pikoImige= (scale == BitScale.GRAY_8) ?
                new CoveredImige2DGray8(ip,shape) : new CoveredImige2DGray16(ip,shape);
        SDAProcessors.sda2D(pikoImige,params, imageJProgressBar,new IJMessenger(),multithreading);
    }

    // processing of GRAY8 images
    public void process(byte[] pixels) {
        int R=params.getR();
        int threshold = params.getTreshold();
        for (int y=0; y < height; y++) {
            for (int x=0; x < width; x++) {
                if ((x+threshold>R) && (x<R+threshold)) pixels[x+y*width]=0;
                // process each pixel of the line
                // example: add 'number' to each pixel
                pixels[x + y * width] += (byte)value;
            }
        }

        log.info("Gray 8 process");
    }

    // processing of GRAY16 images
    public void process(short[] pixels) {
        short min=1000;
        short max=0;
        int R=params.getR();
        int threshold = params.getTreshold();
        for (int y=0; y < height; y++) {
            for (int x=0; x < width; x++) {
                min = (short) Math.min(min,pixels[x+y*width]);
                max = (short) Math.max(max,pixels[x+y*width]);
                if ((x+threshold>R) && (x<R+threshold)) pixels[x+y*width]=0;
                // process each pixel of the line
                // example: add 'number' to each pixel
                pixels[x + y * width] += (short)value;
            }
        }
        log.info("GRAY 16 process, min "+min+", max "+max);
    }

    // processing of GRAY32 images
    public void process(float[] pixels) {
        int R=params.getR();
        int threshold = params.getTreshold();
        for (int y=0; y < height; y++) {
            for (int x=0; x < width; x++) {
                if ((x+threshold>R) && (x<R+threshold)) pixels[x+y*width]=0;
                // process each pixel of the line
                // example: add 'number' to each pixel
                pixels[x + y * width] += (float)value;
            }
        }
        log.info("Gray 32 process");
    }

    // processing of COLOR_RGB images
    public void process(int[] pixels) {
        int R=params.getR();
        int threshold = params.getTreshold();
        for (int y=0; y < height; y++) {
            for (int x=0; x < width; x++) {
                if ((x+threshold>R) && (x<R+threshold)) pixels[x+y*width]=0;
                // process each pixel of the line
                // example: add 'number' to each pixel
                pixels[x + y * width] += (int)value;
            }

        }
        log.info("Color RGB process");
    }

    public void showAbout() {
        IJ.showMessage("SDA Processor",
                "Statistical Domain Analizys"
        );
    }

    /**
     * Main method for debugging.
     *
     * For debugging, it is convenient to have a method that starts ImageJ, loads
     * an image and calls the plugin, e.g. after setting breakpoints.
     *
     * @param args unused
     */
    public static void main(String[] args) throws Exception {
        // set the plugins.dir property to make the plugin appear in the Plugins menu
        // see: https://stackoverflow.com/a/7060464/1207769
        Class<?> clazz = Process_SDA.class;
        java.net.URL url = clazz.getProtectionDomain().getCodeSource().getLocation();
        java.io.File file = new java.io.File(url.toURI());
        System.setProperty("plugins.dir", file.getAbsolutePath());

        // start ImageJ
        new ImageJ();

        // open the Clown sample
        ImagePlus image = IJ.openImage("http://imagej.net/images/clown.jpg");
        image.show();

        // run the plugin
        IJ.runPlugIn(clazz.getName(), "");
    }
}
