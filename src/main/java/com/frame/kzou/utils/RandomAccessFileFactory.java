package com.frame.kzou.utils;


import java.io.FileNotFoundException;
import java.io.RandomAccessFile;

/**
 * Created with IntelliJ IDEA.
 *
 * @author: HP-zouKun
 * Date: 2022/10/14
 * Time: 10:55
 * Description:
 */
public class RandomAccessFileFactory extends RandomAccessFile {

    private RandomAccessFileFactory(String name, String mode) throws FileNotFoundException {
        super(name, mode);
    }

}
