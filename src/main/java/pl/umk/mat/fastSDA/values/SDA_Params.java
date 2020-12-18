package pl.umk.mat.fastSDA.values;

import lombok.Value;

@Value
public class SDA_Params {
    int R;
    int treshold;
    int overWhite;
    int Z;
    boolean relationshipDirection; //false means darkBackground
    boolean normalizeTo8Bit;

    public static SDA_Params getDefault(){
        return new SDA_Params(50,
                10,
                4096,
                3,
                false,
                true);
    }
}
