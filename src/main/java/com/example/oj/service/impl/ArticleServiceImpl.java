package com.example.oj.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.oj.mapper.domain.entity.Article;
import com.example.oj.mapper.ArticleMapper;
import com.example.oj.service.IArticleService;
import org.springframework.stereotype.Service;
@Service
public class ArticleServiceImpl extends ServiceImpl<ArticleMapper, Article> implements IArticleService{
}

