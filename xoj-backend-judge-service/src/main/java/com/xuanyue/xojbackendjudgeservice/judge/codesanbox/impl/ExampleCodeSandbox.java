package com.xuanyue.xojbackendjudgeservice.judge.codesanbox.impl;

import com.xuanyue.xojbackendjudgeservice.judge.codesanbox.CodeSandbox;
import com.xuanyue.xojbackendmodel.model.dto.codesandbox.ExecuteCodeRequest;
import com.xuanyue.xojbackendmodel.model.dto.codesandbox.ExecuteCodeResponse;
import com.xuanyue.xojbackendmodel.model.dto.questionsubmit.JudgeInfo;
import com.xuanyue.xojbackendmodel.model.enums.JudgeInfoMessageEnum;
import com.xuanyue.xojbackendmodel.model.enums.QuestionSubmitStatuEnum;

import java.util.List;

/**
 * 示例代码沙箱(仅为了跑通业务流程)
 *
 * @author xuanyue_18
 * @date 2023/9/10 16:30
 */
public class ExampleCodeSandbox implements CodeSandbox {
    @Override
    public ExecuteCodeResponse executeCode(ExecuteCodeRequest executeCodeRequest) {
        // System.out.println("示例代码沙箱");
        List<String> inputList = executeCodeRequest.getInputList();
        ExecuteCodeResponse executeCodeResponse = new ExecuteCodeResponse();
        executeCodeResponse.setOutputList(inputList);
        executeCodeResponse.setMessage("测试执行成功");
        executeCodeResponse.setStatus(QuestionSubmitStatuEnum.SUCCESS.getValue());
        JudgeInfo judgeInfo = new JudgeInfo();
        judgeInfo.setMessage(JudgeInfoMessageEnum.ACCEPTED.getText());
        judgeInfo.setMemory(100l);
        judgeInfo.setTime(100l);
        executeCodeResponse.setJudgeInfo(judgeInfo);
        return executeCodeResponse;
    }
}
