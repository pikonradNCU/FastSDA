package pl.umk.mat.fastSDA.sdaUtils.maskCurves;

public class RampedCosinusCurve extends Curve {
    private final float rampSize;
    private final float cosSize;

    public RampedCosinusCurve(int R, float rampFactor) {
        super(R);
        rampSize = rampFactor*R;
        cosSize = R-rampSize;
    }

    @Override
    public float getVal(float r) {
        return (r<= rampSize) ? 1:  (float) (Math.cos(Math.PI*(r-rampSize)/cosSize)+1)/2;
    }
}
