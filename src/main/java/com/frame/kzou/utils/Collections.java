package com.frame.kzou.utils;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 *
 * @author: HP-zouKun
 * Date: 2022/10/20
 * Time: 14:38
 * Description:
 */
public class Collections {

    private Collections(){}

    /**
     * 合并2个有序列表
     * @param list1 list1
     * @param list2 list2
     * @param comparator    比较器
     * @param <T>   元素类型
     * @return  List<T>
     */
    public static  <T> List<T> sortListMerge(List<T> list1, List<T> list2, Comparator<T> comparator) {
        if (list1 == null || list1.isEmpty()) {
            return list2;
        }
        if (list2 == null || list2.isEmpty()) {
            return list1;
        }
        ArrayList<T> res = new ArrayList<>(list1.size() + list1.size());
        for (int i1 = 0, i2 = 0; ; ) {
            if (i1 >= list1.size()) {
                for (int i = i2; i < list2.size(); i++) {
                    res.add(list2.get(i));
                }
                break;
            }
            if (i2 >= list2.size()) {
                for (int i = i1; i < list1.size(); i++) {
                    res.add(list1.get(i));
                }
                break;
            }
            T t1 = list1.get(i1);
            T t2 = list2.get(i2);
            if (comparator.compare(t1, t2) > 0) {
                res.add(t2);
                i2++;
            } else {
                res.add(t1);
                i1++;
            }
        }
        return res;
    }
}
