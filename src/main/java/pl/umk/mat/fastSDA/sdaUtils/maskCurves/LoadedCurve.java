package pl.umk.mat.fastSDA.sdaUtils.maskCurves;

public class LoadedCurve extends Curve{

    public LoadedCurve(int R, float[] loaded) {
        super(R);
    }

    @Override
    public float getVal(float r) {
        return 0;
    }
}
