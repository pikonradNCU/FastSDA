package pl.umk.mat.fastSDA.sdaUtils.maskCurves;

public class FlatCurve extends Curve {

    public FlatCurve(int R) {
        super(R);
    }

    @Override
    public float getVal(float r) {
        return 1;
    }
}
