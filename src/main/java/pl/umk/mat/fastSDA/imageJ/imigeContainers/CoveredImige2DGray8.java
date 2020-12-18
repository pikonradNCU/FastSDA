package pl.umk.mat.fastSDA.imageJ.imigeContainers;


import ij.process.ImageProcessor;
import pl.umk.mat.fastSDA.image.BitScale;
import pl.umk.mat.fastSDA.image.Image2D;
import pl.umk.mat.fastSDA.image.Shape2D;

public class CoveredImige2DGray8 implements Image2D {

    private ImageProcessor imige;
    private Shape2D shape;
    private byte[] pixels;
    private int width;

    public CoveredImige2DGray8(ImageProcessor imige, Shape2D shape) {
        this.imige = imige;
        this.shape = shape;
        pixels = (byte[]) imige.getPixels();
        width=shape.getX();
    }

    @Override
    public BitScale getScale() {
        return BitScale.GRAY_8;
    }

    @Override
    public short getGray16Colur(int x, int y) {
        return pixels[x + y * width];
    }

    @Override
    public Shape2D getShape() {
        return shape;
    }

    @Override
    public void put(int x, int y, short c) {
        pixels[x + y * width]=(byte) c;
    }

    @Override
    public void put(int x, int y, byte c) {
        pixels[x + y * width]=c;
    }

    @Override
    public void putIntColors(int[][] colors) {

    }
}
