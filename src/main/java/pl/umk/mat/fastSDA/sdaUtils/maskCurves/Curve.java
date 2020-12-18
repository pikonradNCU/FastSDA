package pl.umk.mat.fastSDA.sdaUtils.maskCurves;


import lombok.Getter;
import lombok.RequiredArgsConstructor;
@RequiredArgsConstructor
public abstract class Curve {
//    public Curve(int R){
//        this.R = R;
//    }
    @Getter final int R;
    abstract public float getVal(float r);
    public float extGetVal(float r){ return (R<r)? 0 : getVal(r);}

}
