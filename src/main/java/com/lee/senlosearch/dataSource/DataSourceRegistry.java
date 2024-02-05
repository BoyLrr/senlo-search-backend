package com.lee.senlosearch.dataSource;

import com.lee.senlosearch.model.enums.SearchTypeEnum;
import org.apache.poi.ss.formula.functions.T;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import java.util.HashMap;
import java.util.Map;

@Component
public class DataSourceRegistry {
    @Resource
    private PictureDataSource pictureDataSource;

    @Resource
    private UserDataSource userDataSource;

    @Resource
    private PostDataSource postDataSource;

    private Map<String, DataSource<T>> typeDataSource;

    @PostConstruct
    public void doInit() {
        typeDataSource = new HashMap() {{
            put(SearchTypeEnum.POST.getValue(), postDataSource);
            put(SearchTypeEnum.USER.getValue(), userDataSource);
            put(SearchTypeEnum.PICTURE.getValue(), pictureDataSource);
        }};
    }

    public DataSource getDataSourceByType(String type) {
        if (typeDataSource == null){
            return null;
        }
        return typeDataSource.get(type);
    }
}
