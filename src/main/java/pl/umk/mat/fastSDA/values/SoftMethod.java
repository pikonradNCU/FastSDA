package pl.umk.mat.fastSDA.values;

public enum SoftMethod {
    CLASSIC,
    CLASSIC_ORIGIN,
    COSINES,
    TRIANGLE,
    RAMPED_COS;

    public static String[] getMethods(){
        final SoftMethod[] values = SoftMethod.values();
        int size = values.length;
        String[] methods = new String[size];
        for (int i=0;i<size;i++) methods[i] = values[i].toString();
        return methods;
    }

    public static int getIdx(SoftMethod method) {
        int i=0;
        final SoftMethod[] values = values();
        while (i<values.length) {
            if (method.equals(values[i])) return i;
        }
        return -1;
    }
}
