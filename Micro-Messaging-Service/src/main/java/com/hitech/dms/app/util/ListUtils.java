package com.hitech.dms.app.util;

import java.util.List;
import java.util.stream.Collectors;

import org.apache.commons.collections4.CollectionUtils;

import com.hitech.dms.app.common.Constant;

public class ListUtils<T> {

    public List<T> intersection(List<List<T>> lists, boolean removeDuplication) {
        if (CollectionUtils.isEmpty(lists)) {
            return null;
        }

        int size = lists.size();
        List<T> list = lists.get(Constant.INDEX_ZERO);
        if (size == Constant.NUMBER_ONE) {
            return list;
        }

        for (int i = 1; i < size; i++) {
            list.retainAll(lists.get(i));
        }

        if (removeDuplication) {
            list = list.stream().distinct().collect(Collectors.toList());
        }

        return list;
    }

}
