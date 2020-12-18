package pl.umk.mat.fastSDA.image;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

public class SimpleImage3DGray16 implements Image {
    List<SimpleImage2DGray16> slices =new CopyOnWriteArrayList<>();
    Shape3D shape;

    public SimpleImage3DGray16(int X, int Y, int Z){

    }

    @Override
    public BitScale getScale() {
        return BitScale.GRAY_16;
    }

    public SimpleImage3DGray16(Shape3D s){
        shape = s;
        for (int z=0;z<s.getZ();z++) slices.add(new SimpleImage2DGray16(s.getSliceShape()));
    }

//    @Override
    public short getGray16Colour(int x, int y, int z) {
        return slices.get(z).getGray16Colur(x,y);
    }

    @Override
    public Shape2D getShape2D() {
        return shape.getSliceShape();
    }

    @Override
    public Shape3D getShape3D() {
        return shape;
    }

    @Override
    public Image2D getSlice(int z) {
        return slices.get(z);
    }

//    @Override
    public void put(int x, int y, int z, short c) {
        slices.get(z).put(x,y,c);
    }

//    @Override
    public void put(int x, int y, int z, byte c) {
        slices.get(z).put(x,y,c);
    }
}
