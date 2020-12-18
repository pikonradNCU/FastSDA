package pl.umk.mat.fastSDA.values;

import lombok.Builder;
import lombok.Value;

@Builder
@Value
public class SoftSDAParams {
    int R;
    SoftMethod method;
    boolean isMaskFloat;
    float rampFactor;// between 0 and 1 if it is 0 rampCos is equivalent to cos. if It is 1 it is equivalent to classic

    public static SoftSDAParams getDefault() {
        return new SoftSDAParams(30,SoftMethod.CLASSIC,true,0.5f);
    }
}
