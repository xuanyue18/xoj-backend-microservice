package com.xuanyue.xojbackendjudgeservice.controller.inner;

import com.xuanyue.xojbackendjudgeservice.judge.JudgeService;
import com.xuanyue.xojbackendmodel.model.entity.QuestionSubmit;
import com.xuanyue.xojbackendserviceclient.service.JudgeFeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

/**
 * 该服务仅内部调用
 *
 * @author xuanyue_18
 * @date 2023/9/20 11:31
 */
@RestController
@RequestMapping("/inner")
public class JudgeInnerController implements JudgeFeignClient {

    @Resource
    private JudgeService judgeService;

    /**
     * 判题
     *
     * @param questionSubmitId
     * @return
     */
    @PostMapping("/do")
    @Override
    public QuestionSubmit doJudge(long questionSubmitId) {
        return judgeService.doJudge(questionSubmitId);
    }
}
