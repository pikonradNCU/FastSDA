package pl.umk.mat.fastSDA.imageJ.sdaPlugin;


import lombok.Value;
import pl.umk.mat.fastSDA.values.SDA_Params;

@Value
public class SdaPluginParams {
    SDA_Params sdaParams;
    int methodIndex;
}
