package pl.umk.mat.fastSDA.sdaUtils.maskCurves;


public class CosinusCurve extends Curve {

    public CosinusCurve(int R) {
        super(R);
    }

    @Override
    public float getVal(float r) {
        return (float) (Math.cos(Math.PI*r/R)+1)/2;
    }
}
