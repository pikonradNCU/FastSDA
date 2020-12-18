package pl.umk.mat.fastSDA.image;

public interface Image {
    BitScale getScale();
//    short getGray16Colour(int x, int y, int z);
    Shape2D getShape2D();
    Shape3D getShape3D();
    Image2D getSlice(int z);
//    void put(int x, int y, int z, short c);
//    void put(int x, int y, int z, byte c);
}
