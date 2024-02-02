package com.lee.senlosearch.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.google.gson.Gson;
import com.lee.senlosearch.common.BaseResponse;
import com.lee.senlosearch.common.ErrorCode;
import com.lee.senlosearch.common.ResultUtils;
import com.lee.senlosearch.exception.ThrowUtils;
import com.lee.senlosearch.model.dto.picture.PictureQueryRequest;
import com.lee.senlosearch.model.entity.Picture;
import com.lee.senlosearch.service.PictureService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

/**
 * 图片接口
 *
 * @author <a href="https://github.com/liyupi">程序员鱼皮</a>
 * @from <a href="https://yupi.icu">编程导航知识星球</a>
 */
@RestController
@RequestMapping("/picture")
@Slf4j
public class PictureController {

    @Resource
    private PictureService pictureService;


    private final static Gson GSON = new Gson();

    /**
     * 分页搜索（从 ES 查询，封装类）
     *
     * @param pictureQueryRequest
     * @param request
     * @return
     */
    @PostMapping("/list/page/vo")
    public BaseResponse<Page<Picture>>searchPostVOByPage(@RequestBody PictureQueryRequest pictureQueryRequest,
                                                         HttpServletRequest request) {
        long current = pictureQueryRequest.getCurrent();
        long size = pictureQueryRequest.getPageSize();

        // 限制爬虫
        ThrowUtils.throwIf(size > 20, ErrorCode.PARAMS_ERROR);

        String searchText = pictureQueryRequest.getSearchText();
        Page<Picture> picturePage = pictureService.searchPicture(searchText,current,size);
        return ResultUtils.success(picturePage);
    }


}
