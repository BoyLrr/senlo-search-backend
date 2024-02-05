package com.lee.senlosearch.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.google.gson.Gson;
import com.lee.senlosearch.common.BaseResponse;
import com.lee.senlosearch.common.ErrorCode;
import com.lee.senlosearch.common.ResultUtils;
import com.lee.senlosearch.exception.BusinessException;
import com.lee.senlosearch.exception.ThrowUtils;
import com.lee.senlosearch.manager.SearchFacade;
import com.lee.senlosearch.model.dto.picture.PictureQueryRequest;
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
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.concurrent.CompletableFuture;

/**
 * 聚合搜索接口
 *
 * @author <a href="https://github.com/liyupi">程序员鱼皮</a>
 * @from <a href="https://yupi.icu">编程导航知识星球</a>
 */
@RestController
@RequestMapping("/search")
@Slf4j
public class SearchController {

    @Resource
    private PictureService pictureService;

    @Resource
    private UserService userService;

    @Resource
    private PostService postService;

    @Resource
    private SearchFacade searchFacade;
    @PostMapping("all")
    public BaseResponse<SearchVO> searchAll(@RequestBody SearchRequest searchRequest, HttpServletRequest request) {
        return ResultUtils.success(searchFacade.searchAll(searchRequest,request));
    }
}
