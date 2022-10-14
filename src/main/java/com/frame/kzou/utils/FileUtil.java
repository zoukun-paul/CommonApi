package com.frame.kzou.utils;

import org.springframework.util.DigestUtils;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.UUID;

/**
 * Created with IntelliJ IDEA.
 *
 * @author: HP-zouKun
 * Date: 2022/10/7
 * Time: 21:46
 * Description:
 */
public class FileUtil {


    /**
     *
     * @param data  字节类容
     * @param file  存储到的文件
     * @throws IOException
     */
    public static void saveFile(byte[] data, File file) throws IOException {
        try (FileOutputStream outputStream = new FileOutputStream(file)) {
            outputStream.write(data);
        }
    }

    /**
     * 随机文件名称
     * @param type 文件类型 eg： png、mp3、gif、zip
     * @return  随机文件名称，带后缀 eg: 9d6323-221007.png
     */
    public static String randomFileName(String type) {
        String uuid = UUID.randomUUID().toString().replaceAll("-", "");
        String fileName = uuid.substring(0, 6) +"-"+ DateUtil.getNow("yyMMdd") + "." + type;
        return fileName.toLowerCase();
    }

    /**
     * 获取文件类型
     * @param fileName  文件名称 eg ：12313.png --> png
     * @return  eg： png  , 注意：没有点
     */
    public static String getFileType(String fileName) {
        int idx = fileName.lastIndexOf(".");
        return fileName.substring(idx + 1).toLowerCase();
    }

    /**
     * md5 文件
     * @param data  文件类容
     * @return  md5散列结果
     */
    public static String getMd5Code(byte[] data) {
       return DigestUtils.md5DigestAsHex(data);
    }

    /**
     * 创建文件夹
     * @param path  文件夹
     */
    public static void createDirectoryIfNotExist(String path) {
        File file = new File(path);
        if (file.isDirectory() && !file.exists()) {
            file.mkdir();
        }
    }
}
