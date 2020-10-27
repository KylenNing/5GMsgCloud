package cc.htdf.msgcloud.common.utils;

import net.coobird.thumbnailator.Thumbnails;

import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Objects;

/**
 * author: JT
 * date: 2020/4/9
 * title:
 */
public class ImageUtils {

    public static ByteArrayOutputStream resize(InputStream inputStream, String width, String height) throws IOException {
        Thumbnails.Builder builder = Thumbnails.of(inputStream);
        if (!Objects.isNull(width) && !Objects.equals("-", width)) {
            builder.width(Integer.parseInt(width));
        }
        if (!Objects.isNull(height) && !Objects.equals("-", height)) {
            builder.height(Integer.parseInt(height));
        }
        builder.imageType(BufferedImage.TYPE_INT_ARGB);
        builder.outputFormat("png");
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        builder.toOutputStream(baos);
        return baos;

    }
}
