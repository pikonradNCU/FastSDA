package pl.umk.mat.fastSDA.image;

public class SimpleImage2DGray16 implements Image2D {
    private short [][] pixels;
    Shape2D shape;

    public SimpleImage2DGray16(int X, int Y) {
        shape = new Shape2D(X,Y);
        pixels = new short[X][Y];
    }

    public SimpleImage2DGray16(Shape2D s) {
        shape = s;
        pixels = new short[s.getX()][s.getY()];
    }

    @Override
    public BitScale getScale() {
        return BitScale.GRAY_16;
    }

    @Override
    public short getGray16Colur(int x, int y) {
        return pixels[x][y];
    }

    @Override
    public Shape2D getShape() {
        return shape;
    }

    @Override
    public void put(int x, int y, short c) {
        pixels[x][y]=c;
    }

    @Override
    public void put(int x, int y, byte c) {
        pixels[x][y]=c;
    }

    @Override
    public void putIntColors(int[][] colors) {

    }
}
