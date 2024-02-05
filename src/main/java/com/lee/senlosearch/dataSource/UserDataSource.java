package com.lee.senlosearch.dataSource;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.lee.senlosearch.model.dto.user.UserQueryRequest;
import com.lee.senlosearch.model.entity.User;
import com.lee.senlosearch.model.vo.UserVO;
import com.lee.senlosearch.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * 用户数据源
 */
@Service
@Slf4j
public class UserDataSource  implements DataSource<UserVO> {
    @Resource
    private UserService userService;

    @Override
    public Page<UserVO> doSearch(String searchText, long pageNum, long pageSize) {
        UserQueryRequest userQueryRequest = new UserQueryRequest();
        userQueryRequest.setUserName(searchText);
        userQueryRequest.setCurrent(pageNum);
        userQueryRequest.setPageSize(pageSize);
        Page<UserVO> userVOPage = userService.listUserVOByPage(userQueryRequest);
        return userVOPage;
    }
}
