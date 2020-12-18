package pl.umk.mat.fastSDA.performanceTestTools;

import lombok.Getter;

import static pl.umk.mat.fastSDA.performanceTestTools.RandTools.randMatrix;

@Getter
public class RandomData {
    private int[][] pictureData;
    private int[][] result;
    private long startTime;

    static RandomData getRandomData(int x, int y){
        RandomData data = new RandomData();
        data.pictureData = randMatrix(x, y);
        data.result = new int[x][y];
        data.startTime = System.currentTimeMillis();
        return data;
    }

    long getProcessTime(){ return System.currentTimeMillis()-startTime; }
}
