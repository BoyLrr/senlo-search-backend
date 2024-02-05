package com.lee.senlosearch.manager;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.lee.senlosearch.common.BaseResponse;
import com.lee.senlosearch.common.ErrorCode;
import com.lee.senlosearch.common.ResultUtils;
import com.lee.senlosearch.config.CosClientConfig;
import com.lee.senlosearch.dataSource.*;
import com.lee.senlosearch.exception.BusinessException;
import com.lee.senlosearch.exception.ThrowUtils;
import com.lee.senlosearch.model.dto.post.PostQueryRequest;
import com.lee.senlosearch.model.dto.search.SearchRequest;
import com.lee.senlosearch.model.dto.user.UserQueryRequest;
import com.lee.senlosearch.model.entity.Picture;
import com.lee.senlosearch.model.enums.SearchTypeEnum;
import com.lee.senlosearch.model.vo.PostVO;
import com.lee.senlosearch.model.vo.SearchVO;
import com.lee.senlosearch.model.vo.UserVO;
import com.lee.senlosearch.service.PictureService;
import com.lee.senlosearch.service.PostService;
import com.lee.senlosearch.service.UserService;
import com.qcloud.cos.COSClient;
import com.qcloud.cos.model.PutObjectRequest;
import com.qcloud.cos.model.PutObjectResult;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.poi.ss.formula.functions.T;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RequestBody;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.CompletableFuture;

/**
 * 搜索门面
 *
 */
@Component
@Slf4j
public class SearchFacade {
    @Resource
    private PictureDataSource pictureDataSource;

    @Resource
    private UserDataSource userDataSource;

    @Resource
    private PostDataSource postDataSource;

    @Resource
    private DataSourceRegistry dataSourceRegistry;

    public SearchVO searchAll(@RequestBody SearchRequest searchRequest, HttpServletRequest request) {
        String type = searchRequest.getType();
        SearchTypeEnum searchTypeEnum = SearchTypeEnum.getEnumByValue(type);
        ThrowUtils.throwIf(StringUtils.isBlank(type), ErrorCode.PARAMS_ERROR);
        String searchText = searchRequest.getSearchText();
        long current = searchRequest.getCurrent();
        long pageSize = searchRequest.getPageSize();
        //搜索所有数据
        if (searchTypeEnum == null) {
            CompletableFuture<Page<UserVO>> userTask = CompletableFuture.supplyAsync(() -> {
                UserQueryRequest userQueryRequest = new UserQueryRequest();
                userQueryRequest.setUserName(searchText);
                Page<UserVO> userVOPage = userDataSource.doSearch(searchText,current,pageSize);
                return userVOPage;
            });

            CompletableFuture<Page<PostVO>> postTask = CompletableFuture.supplyAsync(() -> {
                PostQueryRequest postQueryRequest = new PostQueryRequest();
                postQueryRequest.setSearchText(searchText);
                Page<PostVO> postVOPage = postDataSource.doSearch(searchText,current,pageSize);
                return postVOPage;
            });

            CompletableFuture<Page<Picture>> pictureTask = CompletableFuture.supplyAsync(() -> {
                Page<Picture> picturePage = pictureDataSource.doSearch(searchText,current,pageSize);
                return picturePage;
            });

            CompletableFuture.allOf(userTask, postTask, pictureTask).join();

            try {
                Page<UserVO> userVOPage = userTask.get();
                Page<PostVO> postVOPage = postTask.get();
                Page<Picture> picturePage = pictureTask.get();
                SearchVO searchVO = new SearchVO();
                searchVO.setPictureList(picturePage.getRecords());
                searchVO.setUserList(userVOPage.getRecords());
                searchVO.setPostList(postVOPage.getRecords());
                return searchVO;
            } catch (Exception e) {
                log.error("查询异常", e);
                throw new BusinessException(ErrorCode.SYSTEM_ERROR, "查询异常");
            }
        } else {
            SearchVO searchVO = new SearchVO();
            DataSource dataSource = dataSourceRegistry.getDataSourceByType(type);
            Page<?> page = dataSource.doSearch(searchText, current, pageSize);
            searchVO.setDataList(page.getRecords());
            return searchVO;
        }
    }
}
