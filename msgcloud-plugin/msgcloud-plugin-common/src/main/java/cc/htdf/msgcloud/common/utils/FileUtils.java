package cc.htdf.msgcloud.common.utils;

import com.google.common.io.Files;
import com.google.common.math.IntMath;

import java.io.File;
import java.io.IOException;
import java.text.DecimalFormat;

/**
 * Created by JT on 2017/11/4.
 */
public class FileUtils {

    /**
     * 获取文件扩展名
     * @param fullname：文件全名或路径
     * @return
     */
    public static String getFileExtensionName(String fullname) {
        return Files.getFileExtension(fullname);
    }

    /**
     * 数据写入文件 字节流
     * @param file：文件
     * @param bytes：字节流
     * @throws IOException
     */
    public static void writeFileUsingBytes(File file, byte[] bytes) throws IOException {
        Files.asByteSink(file).write(bytes);
    }

    /**
     * 数据写入文件 字节流
     * @param filepath：文件路径
     * @param bytes：字节流
     * @throws IOException
     */
    public static void writeFileUsingBytes(String filepath, byte[] bytes) throws IOException {
        writeFileUsingBytes(new File(filepath), bytes);
    }


    /**
     * 文件大小格式转换
     * @param size
     * @return
     */
    public static String transformFilesizeToString(double size) {
        char[] unit = {'B', 'K', 'M', 'G'};

        DecimalFormat format = new DecimalFormat("#.00");
        if (size >= 0 && size < 1024)
            return String.valueOf(size) + unit[0];
        else if (size >= 1024 && size < IntMath.pow(1024, 2))
            return format.format((size/1024)) + unit[1] + unit[0];
        else if (size > IntMath.pow(1024, 2) && size < IntMath.pow(1024, 3))
            return format.format((size/(IntMath.pow(1024, 2)))) + unit[2] + unit[0];
        else
            return format.format((size/(IntMath.pow(1024, 3)))) + unit[3] + unit[0];
    }

    /**
     * 根据全路径创建文件父目录
     * @param fullpath：全路径
     */
    public static void createParentDirs(String fullpath) throws IOException {
        createParentDirs(new File(fullpath));
    }

    /**
     * 创建文件父目录
     * @param file
     * @throws IOException
     */
    public static void createParentDirs(File file) throws IOException {
        Files.createParentDirs(file);
    }
}
