package pl.umk.mat.fastSDA.nonHistogramSDA;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static pl.umk.mat.fastSDA.nonHistogramSDA.NonHistTestTools.startRandomizedValues;

class SlowSdaProcessorTestIT {
    @Test
    public void baseTest(){
        int X=3500;
        int Y=2500;
        int R=100;
        boolean startSingle = false;
        boolean visible = false;

        startRandomizedValues(X, Y, R, visible, startSingle);
        assertTrue(true);
    }

}

/*      int X=1500;
        int Y=1500;
        int R=200;

 Classic SDA on multi thread time for matrix X=1500, Y=1500 is  2min. 8s. 166ms.
 Tuned Classic SDA on multi thread time for matrix X=1500, Y=1500 is  1min. 3s. 152ms.
 uned Classic SDA on multi thread time second version for matrix X=1500, Y=1500 is  0min. 53s. 825ms.
 Shifted Multithread for matrix X=1500, Y=1500 is  0min. 27s. 60ms.
 MultiThread with mask time for matrix X=1500, Y=1500 is  0min. 31s. 270ms.
 MultiThread with float mask time for matrix X=1500, Y=1500 is  0min. 40s. 849ms.
 Histogram based for matrix X=1500, Y=1500 is  0min. 2s. 563ms.

        int X=1500;
        int Y=1500;
        int R=50;
 Classic SDA on multi thread time for matrix X=1500, Y=1500 is  0min. 7s. 737ms.
 Tuned Classic SDA on multi thread time for matrix X=1500, Y=1500 is  0min. 4s. 388ms.
 uned Classic SDA on multi thread time second version for matrix X=1500, Y=1500 is  0min. 4s. 715ms.
 Shifted Multithread for matrix X=1500, Y=1500 is  0min. 2s. 441ms.
 MultiThread with mask time for matrix X=1500, Y=1500 is  0min. 2s. 642ms.
 MultiThread with float mask time for matrix X=1500, Y=1500 is  0min. 3s. 321ms.
 Histogram based for matrix X=1500, Y=1500 is  0min. 1s. 41ms.

        int X=1500;
        int Y=1500;
        int R=100;

 Classic SDA on multi thread time for matrix X=1500, Y=1500 is  0min. 30s. 899ms.
 Tuned Classic SDA on multi thread time for matrix X=1500, Y=1500 is  0min. 17s. 617ms.
 uned Classic SDA on multi thread time second version for matrix X=1500, Y=1500 is  0min. 16s. 321ms.
 Shifted Multithread for matrix X=1500, Y=1500 is  0min. 7s. 704ms.
 MultiThread with mask time for matrix X=1500, Y=1500 is  0min. 8s. 513ms.
 MultiThread with float mask time for matrix X=1500, Y=1500 is  0min. 11s. 213ms.
 Histogram based for matrix X=1500, Y=1500 is  0min. 1s. 595ms.
  */