package com.dfire.products.util;

import com.dfire.products.bean.ColumnNode;
import com.dfire.products.dao.MetaDataDao;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MetaCache {
    private static MetaCache instance = new MetaCache();

    public static MetaCache getInstance() {
        return instance;
    }

    private MetaCache() {
    }

    private        MetaDataDao                   dao       = new MetaDataDao();
    private static Map<String, List<ColumnNode>> cMap      = new HashMap<>();
    private static Map<String, Long>             tableMap  = new HashMap<>();
    private static Map<String, Long>             columnMap = new HashMap<>();

    public void init(String table) {
        String[] pdt = ParseUtil.parseDBTable(table);
        List<ColumnNode> list = dao.getColumn(pdt[0], pdt[1]);
        if (Check.notEmpty(list)) {
            cMap.put(table.toLowerCase(), list);
            tableMap.put(table.toLowerCase(), list.get(0).getTableId());
            for (ColumnNode cn : list) {
                columnMap.put((cn.getDb() + "." + cn.getTable() + "." + cn.getColumn()).toLowerCase(), cn.getId());
            }
        }
    }

    public void release() {
        cMap.clear();
        tableMap.clear();
        columnMap.clear();
    }

    public List<String> getColumnByDBAndTable(String table) {
        List<ColumnNode> list = cMap.get(table.toLowerCase());
        List<String> list2 = new ArrayList<>();
        if (Check.notEmpty(list)) {
            for (ColumnNode columnNode : list) {
                list2.add(columnNode.getColumn());
            }
        }
        return list2;
    }

    public Map<String, List<ColumnNode>> getcMap() {
        return cMap;
    }

    public Map<String, Long> getTableMap() {
        return tableMap;
    }

    public Map<String, Long> getColumnMap() {
        return columnMap;
    }
}
