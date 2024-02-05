package com.lee.senlosearch.dataSource;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.poi.ss.formula.functions.T;

/**
 * 数据源接口（接入的数据源必须实现）
 * @param <T>
 */
public interface DataSource<T> {

    /**
     * 搜索规范
     * @param searchText
     * @param pageNum
     * @param pageSize
     * @return
     */
    Page<T> doSearch(String searchText,long pageNum,long pageSize);
}
