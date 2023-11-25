package com.xuanyue.xojbackendquestionservice.controller.inner;

import com.xuanyue.xojbackendmodel.model.entity.Question;
import com.xuanyue.xojbackendmodel.model.entity.QuestionSubmit;
import com.xuanyue.xojbackendquestionservice.service.QuestionService;
import com.xuanyue.xojbackendquestionservice.service.QuestionSubmitService;
import com.xuanyue.xojbackendserviceclient.service.QuestionFeignClient;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

/**
 * 该服务仅内部调用
 *
 * @author xuanyue_18
 * @date 2023/9/20 11:31
 */
@RestController
@RequestMapping(("/inner"))
public class QuestionInnerController implements QuestionFeignClient {
    @Resource
    private QuestionService questionService;

    @Resource
    private QuestionSubmitService questionSubmitService;

    @Override
    @GetMapping("/get/id")
    public Question getQuestionById(@RequestParam("questionId") long questionId) {
        return questionService.getById(questionId);
    }

    @Override
    @GetMapping("/question_submit/get/id")
    public QuestionSubmit getQuestionSubmitById(@RequestParam("questionId") long questionSubmitId) {
        return questionSubmitService.getById(questionSubmitId);
    }

    @Override
    @PostMapping("/question_submit/update")
    public boolean updateQuestionSubmitById(@RequestBody QuestionSubmit questionSubmit) {
       return questionSubmitService.updateById(questionSubmit);
    }

    @Override
    public boolean updateQuestion(Question question) {
        return questionService.updateById(question);
    }


}
