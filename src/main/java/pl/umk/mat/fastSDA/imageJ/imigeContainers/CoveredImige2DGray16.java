package pl.umk.mat.fastSDA.imageJ.imigeContainers;


import ij.process.ImageProcessor;
import pl.umk.mat.fastSDA.image.BitScale;
import pl.umk.mat.fastSDA.image.Image2D;
import pl.umk.mat.fastSDA.image.Shape2D;

public class CoveredImige2DGray16 implements Image2D {

    private ImageProcessor imige;
    private Shape2D shape;
    private short[] pixels;
    private int width;

    public CoveredImige2DGray16(ImageProcessor imige, Shape2D shape) {
        this.imige = imige;
        this.shape = shape;
        this.pixels = (short[]) imige.getPixels();
        this.width = shape.getX();
    }

    @Override
    public BitScale getScale() {
        return BitScale.GRAY_16;
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
        pixels[x + y * width] = c;
    }

    @Override
    public void put(int x, int y, byte c) {
        pixels[x + y * width] = c;
    }

    @Override
    public void putIntColors(int[][] colors) {

    }
}
