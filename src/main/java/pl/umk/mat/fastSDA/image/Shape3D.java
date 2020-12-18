package pl.umk.mat.fastSDA.image;

import lombok.Value;

@Value
public class Shape3D {
    int X;
    int Y;
    int Z;

    public Shape2D getSliceShape(){
        return new Shape2D(X,Y);
    }
}
