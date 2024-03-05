package com.xuanyue.xojbackendjudgeservice.judge.strategy;

import cn.hutool.json.JSONUtil;
import com.xuanyue.xojbackendmodel.model.dto.codesandbox.ExecuteResult;
import com.xuanyue.xojbackendmodel.model.dto.question.JudgeCase;
import com.xuanyue.xojbackendmodel.model.dto.question.JudgeConfig;
import com.xuanyue.xojbackendmodel.model.dto.questionsubmit.JudgeInfo;
import com.xuanyue.xojbackendmodel.model.entity.Question;
import com.xuanyue.xojbackendmodel.model.enums.ExecuteCodeStatusEnum;
import com.xuanyue.xojbackendmodel.model.enums.JudgeInfoMessageEnum;


import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Java程序判题策略
 *
 * @author xuanyue_18
 * @date 2023/9/10 20:32
 */
public class JavaLanguageJudgeStrategy implements JudgeStrategy {

    /**
     * 执行判题
     *
     * @param judgeContext
     * @return
     */
    @Override
    public JudgeInfo doJudge(JudgeContext judgeContext) {

        JudgeInfo judgeInfo = new JudgeInfo();
        int total = judgeContext.getInputList().size();
        judgeInfo.setTotal(total);
        // 执行成功
        if (judgeContext.getStatus().equals(ExecuteCodeStatusEnum.SUCCESS.getValue())) {
            // 期望输出
            List<String> expectedOutput = judgeContext.getJudgeCaseList().stream().map(JudgeCase::getOutput).collect(Collectors.toList());
            // 测试用例详细信息
            List<ExecuteResult> results = judgeContext.getResults();
            // 实际输出
            List<String> output = results.stream().map(ExecuteResult::getOutput).collect(Collectors.toList());
            // 判题配置
            JudgeConfig judgeConfig = JSONUtil.toBean(judgeContext.getQuestion().getJudgeConfig(), JudgeConfig.class);

            // 设置通过的测试用例
            int pass = 0;
            // 设置最大运行时间
            long maxTime = Long.MIN_VALUE;
            // Java 程序本身需要额外执行 5 秒钟
            long JAVA_PROGRAM_TIME_COST = 5000L;
            for (int i = 0; i < total; i++) {
                // 判断执行时间
                Long time = results.get(i).getTime();
                if (time > maxTime) {
                    maxTime = time;
                }
                // 期望输出与实际输出比较,相等则通过
                if (expectedOutput.get(i).equals(output.get(i))) {
                    // 超时
                    if (maxTime-JAVA_PROGRAM_TIME_COST > judgeConfig.getTimeLimit()) {
                        judgeInfo.setTime(maxTime);
                        judgeInfo.setPass(pass);
                        judgeInfo.setStatus(JudgeInfoMessageEnum.TIME_LIMIT_EXCEEDED.getValue());
                        judgeInfo.setMessage(JudgeInfoMessageEnum.TIME_LIMIT_EXCEEDED.getText());
                        break;
                    } else {
                        pass++;
                    }
                } else {
                    // 遇到了一个没通过的
                    judgeInfo.setPass(pass);
                    judgeInfo.setTime(maxTime);
                    judgeInfo.setStatus(JudgeInfoMessageEnum.WRONG_ANSWER.getValue());
                    judgeInfo.setMessage(JudgeInfoMessageEnum.WRONG_ANSWER.getText());
                    // 设置输出和预期输出信息
                    judgeInfo.setInput(judgeContext.getInputList().get(i));
                    judgeInfo.setOutput(output.get(i));
                    judgeInfo.setExpectedOutput(expectedOutput.get(i));
                    break;
                }
            }
            if (pass == total) {
                judgeInfo.setPass(total);
                judgeInfo.setTime(maxTime);
                judgeInfo.setStatus(JudgeInfoMessageEnum.ACCEPTED.getValue());
                judgeInfo.setMessage(JudgeInfoMessageEnum.ACCEPTED.getText());
            }
        } else if (judgeContext.getStatus().equals(ExecuteCodeStatusEnum.RUN_FAILED.getValue())) {
            judgeInfo.setPass(0);
            judgeInfo.setStatus(JudgeInfoMessageEnum.RUNTIME_ERROR.getValue());
            judgeInfo.setMessage(JudgeInfoMessageEnum.RUNTIME_ERROR.getText() + judgeContext.getMessage());
        } else if (judgeContext.getStatus().equals(ExecuteCodeStatusEnum.COMPILE_FAILED.getValue())) {
            judgeInfo.setPass(0);
            judgeInfo.setStatus(JudgeInfoMessageEnum.COMPILE_ERROR.getValue());
            judgeInfo.setMessage(JudgeInfoMessageEnum.COMPILE_ERROR.getText() + judgeContext.getMessage());
        }
        return judgeInfo;
    }
}
