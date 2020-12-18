package pl.umk.mat.fastSDA.procesUtils;

import lombok.Data;

@Data
public class ProgressParam {
    private int current=0;
    private final int total;

    public float getVal(){
        return (1f * current)/total;
    }
}
