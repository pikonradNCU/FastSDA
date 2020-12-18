package pl.umk.mat.fastSDA.image;

public interface Image2D {
    BitScale getScale();
    short getGray16Colur(int x, int y);
    Shape2D getShape();
    void put(int x, int y, short c);
    void put(int x, int y, byte c);
    void putIntColors(int[][] colors);
}
