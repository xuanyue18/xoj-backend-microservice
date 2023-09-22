package com.xuanyue.xojbackendjudgeservice.judge;

import com.xuanyue.xojbackendmodel.model.entity.QuestionSubmit;

/**
 * 判题服务
 *
 * @author xuanyue_18
 * @date 2023/9/10 19:37
 */
public interface JudgeService {

    /**
     * 判题
     *
     * @param questionSubmitId
     * @return
     */
    QuestionSubmit doJudge(long questionSubmitId);
}
