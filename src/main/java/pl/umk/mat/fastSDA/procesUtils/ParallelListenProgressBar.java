package pl.umk.mat.fastSDA.procesUtils;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class ParallelListenProgressBar {
    private final PikoProgress pbarr;
    private final int showInterval;
    private final PikoLog log;
    ProgressParam full = new ProgressParam(1);
    Map<Integer, ProgressParam> progressMap = new ConcurrentHashMap<>();
    private Thread thread;


    public ParallelListenProgressBar(PikoProgress progress, int showInterval) {
        log = PikoLog.getInstance();
        pbarr = progress;
        this.showInterval = showInterval;
        full.setCurrent(1);
    }

    public void addListener(int index, int total) {
        ProgressParam pp = new ProgressParam(total);
        progressMap.put(index, pp);
    }

    public void showProgress(int index, int current) {
//        log.info("thread " + index + "finish x" + current);
        progressMap.get(index).setCurrent(current);
    }

    public void removeListener(int index) {
        progressMap.remove(index);
    }

    public void startProgressNotification() {
        do {
            try {
                Thread.sleep(showInterval);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            ProgressParam pp = full;
            for (ProgressParam param : progressMap.values()) {
                if (param.getVal() < pp.getVal()) pp = param;
            }

            pbarr.showProgress(pp.getCurrent(), pp.getTotal());
        } while (!progressMap.isEmpty());
    }

}
