package com.my.blog.job;

import com.my.blog.domain.entity.Article;
import com.my.blog.service.IArticleService;
import com.my.blog.utils.RedisCache;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Component
public class ViewCountJob {

    @Autowired
    private RedisCache redisCache;

    @Autowired
    private IArticleService articleService;

    @Scheduled(cron = "0 0/10 * * * ?")
    public void updateViewCount() {
        Map<String, Integer> viewCount = redisCache.getCacheMap("viewCount");
        List<Article> articleList = viewCount.entrySet().stream().map(
                        e -> new Article(Long.valueOf(e.getKey()), Long.valueOf(e.getValue())))
                .collect(Collectors.toList());
        articleService.updateBatchById(articleList);
    }
}
