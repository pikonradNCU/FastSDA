package pl.umk.mat.fastSDA.imageJ.imigeContainers;

import ij.ImagePlus;
import ij.ImageStack;
import ij.process.ImageProcessor;
import pl.umk.mat.fastSDA.image.BitScale;
import pl.umk.mat.fastSDA.image.Image;
import pl.umk.mat.fastSDA.image.Image2D;
import pl.umk.mat.fastSDA.image.Shape2D;
import pl.umk.mat.fastSDA.image.Shape3D;

import java.util.ArrayList;
import java.util.List;

public class Cover3D implements Image {

    private final ImagePlus image;
    private final ImageStack stack;
    private final int width;
    private final int height;
    private final int deph;
    private final Shape3D shape;
    private final Shape2D shape2D;
    private final BitScale scale;
    private List<Image2D> sliceList = new ArrayList<>();

    public Cover3D(ImagePlus imp,BitScale scale){
        image = imp;
        stack = imp.getStack();
        width = imp.getWidth();
        height= imp.getHeight();
        deph  = stack.getSize();
        shape = new Shape3D(width,height,deph);
        shape2D=shape.getSliceShape();
        this.scale = scale;
        for (int z=1;z<=deph;z++){
            Image2D im2d;
            ImageProcessor processor = stack.getProcessor(z);
            if (scale == BitScale.GRAY_8) im2d = new CoveredImige2DGray8(processor,shape2D);
            else im2d = new CoveredImige2DGray16(processor,shape2D);
            sliceList.add(im2d);
        }
    }

    @Override
    public BitScale getScale() {
        return scale;
    }

//    @Override
//    public short getGray16Colour(int x, int y, int z) {
//        return 0;
//    }

    @Override
    public Shape2D getShape2D() {
        return shape2D;
    }

    @Override
    public Shape3D getShape3D() {
        return shape;
    }

    @Override
    public Image2D getSlice(int z) {
        return sliceList.get(z);
    }
//
//    @Override
//    public void put(int x, int y, int z, short c) {
//
//    }
//
//    @Override
//    public void put(int x, int y, int z, byte c) {
//
//    }
}
