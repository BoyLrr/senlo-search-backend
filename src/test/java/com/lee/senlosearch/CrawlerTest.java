package com.lee.senlosearch;

import cn.hutool.http.HttpRequest;
import cn.hutool.json.JSONArray;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.lee.senlosearch.model.entity.Picture;
import com.lee.senlosearch.model.entity.Post;
import com.lee.senlosearch.service.PictureService;
import com.lee.senlosearch.service.PostService;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import javax.annotation.Resource;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@SpringBootTest
public class CrawlerTest {
    @Resource
    private PostService postService;
    @Resource
    private PictureService pictureService;

    @Test
    void testPictureService(){
        Page<Picture> picturePage = pictureService.searchPicture("lala", 3, 5);
    }
    @Test
    void testFetchPicture() throws IOException {
//        String url = "https://cn.bing.com/images/search?q=%E6%89%8B%E6%9C%BA%E5%A3%81%E7%BA%B8&form=HDRSC2&first=1&cw=1177&ch=928";
//        String url = "https://cn.bing.com/images/search?q=%alalal&first=%1";
        String url = "https://cn.bing.com/images/search?q=lalala&first=1";
        Document doc = Jsoup.connect(url).get();
        Elements newsHeadlines = doc.select(".iuscp.varh.isv");
        List<Picture> pictures = new ArrayList<>();
        for (Element element : newsHeadlines) {
            //取图片地址
            String m = element.select(".iusc").get(0).attr("m");
            Map<String,Object> map = JSONUtil.toBean(m,Map.class);
            String murl = (String) map.get("murl");
            //取标题地址
            String title = element.select(".inflnk").get(0).attr("aria-label");
            //放到pictures列表
            Picture picture = new Picture();
            picture.setTitle(title);
            picture.setUrl(murl);
            pictures.add(picture);
        }

    }
    @Test
    void testFetchPassage(){
        String json = "{\"current\":1,\"pageSize\":8,\"sortField\":\"createTime\",\"sortOrder\":\"descend\",\"category\":\"文章\",\"reviewStatus\":1}";
        String url = "https://www.code-nav.cn/api/post/search/page/vo";
        String result = HttpRequest
                .post(url)
                .body(json)
                .execute()
                .body();
        Map<String,Object> map = JSONUtil.toBean(result,Map.class);
        JSONObject data = (JSONObject) map.get("data");
        JSONArray records = (JSONArray) data.get("records");
        List<Post> postList = new ArrayList<>();
        for (Object record : records) {
            JSONObject temp = (JSONObject) record;
            Post post = new Post();
            post.setTitle(temp.getStr("title"));
            post.setContent(temp.getStr("content"));
            JSONArray tags = (JSONArray) temp.get("tags");
            List<String> tagList = tags.toList(String.class);
            post.setTags(JSONUtil.toJsonStr(tagList));
            post.setUserId(1L);
            postList.add(post);
        }
        System.out.println(postList);
        boolean b = postService.saveBatch(postList);
        Assertions.assertTrue(b);
    }
}
