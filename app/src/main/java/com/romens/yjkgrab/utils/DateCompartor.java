package com.romens.yjkgrab.utils;

import com.avos.avoscloud.AVObject;
import com.romens.yjkgrab.table.RuntimeGrabingTable;

import java.util.Comparator;

/**
 * Created by myq on 15-12-12.
 */
public class DateCompartor implements Comparator<AVObject> {
    @Override
    public int compare(AVObject lhs, AVObject rhs) {
        return compare(lhs.getCreatedAt().getTime(), rhs.getCreatedAt().getTime());
    }

    private int compare(long lhs, long rhs) {
        return lhs < rhs ? -1 : (lhs == rhs ? 0 : 1);
    }
}
