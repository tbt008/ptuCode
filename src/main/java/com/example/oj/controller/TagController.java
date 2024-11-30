package com.example.oj.controller;


import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.oj.common.ErrorCode;
import com.example.oj.common.Result;
import com.example.oj.domain.dto.TagDTO;
import com.example.oj.domain.entity.Tag;
import com.example.oj.exception.BusinessException;
import com.example.oj.service.ITagService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author tbt
 * @since 2024-11-26
 */
@RestController
@RequestMapping("/tag")
public class TagController {

    @Autowired
    private ITagService iTagService;
    /**
     * 新增题目（管理员）
     * @param tag
     * @return tagId
     */
    @PostMapping("/add")
    public Result<Long> addQuestion(@RequestBody Tag tag, HttpServletRequest request) {
        if (tag == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        boolean vis = iTagService.save(tag);
        if(vis==false){
            throw new BusinessException(ErrorCode.OPERATION_ERROR);
        }
        long tagId = tag.getId();
        return Result.success(tagId);
    }
    /**
     * 标签列表
     * @param
     * @return
     */
    @PostMapping("/list")
    public Result<Page<Tag>> QuestionList(@RequestBody TagDTO tag, HttpServletRequest request) {
        if (tag == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        Long id = tag.getId();
        String name = tag.getName();

        Page<Tag> page = Page.of(tag.getPageStart(), tag.getPageSize());
        Page<Tag> tags= iTagService.lambdaQuery()
                .eq(id!=null,Tag::getId,id)
                .like(name!=null,Tag::getName,name)
                .page(page);

        return Result.success(tags);
    }
}
