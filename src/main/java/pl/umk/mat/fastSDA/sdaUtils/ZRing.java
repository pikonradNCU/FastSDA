package pl.umk.mat.fastSDA.sdaUtils;

import lombok.Getter;

public class ZRing {
    @Getter private final int Z;
    @Getter private final int R;
    private final int[] zRing;
    private final Ring[] ringTab;

    public ZRing(int Z, int R){
        this.Z = Z;
        this.R = R;
        zRing = new int[Z+1];
        ringTab = new Ring[Z+1];
        zRing[0]=R;
        ringTab[0] = new Ring(R);
        final float factor= (((float) R+1)/(Z+1));
        final float sf = factor*factor;
        final int sR = R*R;

        for (int delta = 1; delta <= Z; delta++){
            float sbound = sR - (sf*delta*delta);
            int t=(int) Math.sqrt(sbound);
            zRing[delta] = t;
            ringTab[delta] = new Ring(t);
        }
    }

    public int getZR(int delta){
        return zRing[delta];
    }
    public Ring getRing (int delta) { return ringTab[delta];}
}
