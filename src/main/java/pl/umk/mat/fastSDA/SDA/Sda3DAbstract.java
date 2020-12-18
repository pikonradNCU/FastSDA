package pl.umk.mat.fastSDA.SDA;

import pl.umk.mat.fastSDA.image.Image;
import pl.umk.mat.fastSDA.image.Image2D;
import pl.umk.mat.fastSDA.image.Shape3D;
import pl.umk.mat.fastSDA.procesUtils.Messenger;
import pl.umk.mat.fastSDA.procesUtils.PikoProgress;
import pl.umk.mat.fastSDA.sdaUtils.ImageRewriters;
import pl.umk.mat.fastSDA.values.SDA_Params;

import java.util.ArrayList;
import java.util.List;

public class Sda3DAbstract extends SdaAbstract{
    final Image image;
    Shape3D shape3D;
    int deep;
    List<Image2D> image2DList = new ArrayList<>();
    List<int[][]> bufferList = new ArrayList<>();

    public Sda3DAbstract(Image image, SDA_Params params, PikoProgress pbar, Messenger messenger) {
        super(image.getScale(),image.getShape2D(), params, pbar, messenger);
        this.image = image;
        shape3D = image.getShape3D();
        deep = shape3D.getZ();
        for (int z = 0; z < deep; z++) {
            Image2D slice = image.getSlice(z);
            image2DList.add(slice);
            bufferList.add(new int[shape2D.getX()][shape2D.getY()]);
        }
//        overWhite = IntStream
//                .range(0,deep)
//                .map(z->getMaxColor(image.getSlice(z)))
//                .max()
//                .orElse(0);
//        log.info("OverWhite set on "+overWhite);
    }

    void copyNewImage() {
        for (int z = 0; z < deep; z++) ImageRewriters.putBufferIntoImage2D(
                image2DList.get(z), scale, shape2D, bufferList.get(z), log
        );
    }
}
