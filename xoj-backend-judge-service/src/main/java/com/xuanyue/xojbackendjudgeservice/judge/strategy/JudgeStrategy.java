package com.xuanyue.xojbackendjudgeservice.judge.strategy;


import com.xuanyue.xojbackendmodel.model.dto.questionsubmit.JudgeInfo;

/**
 * 判题策略
 *
 * @author xuanyue_18
 * @date 2023/9/10 20:32
 */
public interface JudgeStrategy {

    /**
     * 执行判题
     * @param judgeContext
     * @return
     */
    JudgeInfo doJudge(JudgeContext judgeContext);
}
