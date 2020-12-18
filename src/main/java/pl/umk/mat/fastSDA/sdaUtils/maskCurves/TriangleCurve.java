package pl.umk.mat.fastSDA.sdaUtils.maskCurves;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

public class TriangleCurve extends Curve {

    public TriangleCurve(int R) {
        super(R);
    }

    @Override
    public float getVal(float r) {
        return (1-r)/R;
    }
}
