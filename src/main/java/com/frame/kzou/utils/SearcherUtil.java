package com.frame.kzou.utils;

import org.lionsoul.ip2region.xdb.Searcher;

import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;

/**
 * Created with IntelliJ IDEA.
 *
 * @author: HP-zouKun
 * Date: 2022/10/12
 * Time: 22:21
 * Description:
 */

public class SearcherUtil {

    public static byte[] loadVectorIndexFromFile(File file) throws IOException {
        final RandomAccessFile handle = new RandomAccessFile(file, "r");
        final byte[] vIndex = Searcher.loadVectorIndex(handle);
        handle.close();
        return vIndex;
    }


 }
