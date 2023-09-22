package com.xuanyue.xojbackendserviceclient.service;


import com.xuanyue.xojbackendmodel.model.entity.QuestionSubmit;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;

/**
 * 判题服务
 *
 * @author xuanyue_18
 * @date 2023/9/10 19:37
 */
@FeignClient(name = "xoj-backend-judge-service", path = "/api/judge/inner")
public interface JudgeFeignClient {

    /**
     * 判题
     *
     * @param questionSubmitId
     * @return
     */
    @PostMapping("/do")
    QuestionSubmit doJudge(long questionSubmitId);
}
