package com.xuanyue.xojbackendjudgeservice.judge;

import cn.hutool.json.JSONUtil;
import com.xuanyue.xojbackendcommon.common.ErrorCode;
import com.xuanyue.xojbackendcommon.exception.BusinessException;
import com.xuanyue.xojbackendjudgeservice.judge.codesanbox.CodeSandbox;
import com.xuanyue.xojbackendjudgeservice.judge.codesanbox.CodeSandboxFactory;
import com.xuanyue.xojbackendjudgeservice.judge.codesanbox.CodeSandboxProxy;
import com.xuanyue.xojbackendjudgeservice.judge.strategy.JudgeContext;
import com.xuanyue.xojbackendmodel.model.dto.codesandbox.ExecuteCodeRequest;
import com.xuanyue.xojbackendmodel.model.dto.codesandbox.ExecuteCodeResponse;
import com.xuanyue.xojbackendmodel.model.dto.codesandbox.ExecuteResult;
import com.xuanyue.xojbackendmodel.model.dto.question.JudgeCase;
import com.xuanyue.xojbackendmodel.model.dto.question.JudgeConfig;
import com.xuanyue.xojbackendmodel.model.dto.questionsubmit.JudgeInfo;
import com.xuanyue.xojbackendmodel.model.entity.Question;
import com.xuanyue.xojbackendmodel.model.entity.QuestionSubmit;
import com.xuanyue.xojbackendmodel.model.enums.ExecuteCodeStatusEnum;
import com.xuanyue.xojbackendmodel.model.enums.JudgeInfoMessageEnum;
import com.xuanyue.xojbackendmodel.model.enums.QuestionSubmitStatuEnum;
import com.xuanyue.xojbackendserviceclient.service.QuestionFeignClient;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author xuanyue_18
 * @date 2023/9/10 19:43
 */
@Service
@Slf4j
public class JudgeServiceImpl implements JudgeService {

    @Value("${codesandbox.type: exampel}")
    private String type;

    @Resource
    private QuestionFeignClient questionFeignClient;

    @Resource
    private JudgeManager judgeManager;

    @Override
    public QuestionSubmit doJudge(long questionSubmitId) {
        // 1）传入题目的提交 id，获取到对应的题目、提交信息（包含代码、编程语言等）
        QuestionSubmit questionSubmit = questionFeignClient.getQuestionSubmitById(questionSubmitId);
        if (questionSubmit == null) {
            throw new BusinessException(ErrorCode.NOT_FOUND_ERROR, "提交信息不存在");
        }
        Long questionId = questionSubmit.getQuestionId();
        Question question = questionFeignClient.getQuestionById(questionId);
        if (question == null) {
            throw new BusinessException(ErrorCode.NOT_FOUND_ERROR, "题目不存在");
        }
        // 2）如果题目提交状态不为等待中，就不用重复执行了
        if (!questionSubmit.getStatus().equals(QuestionSubmitStatuEnum.WAITING.getValue())) {
            throw new BusinessException(ErrorCode.OPERATION_ERROR, "题目正在判题中");
        }
        // 3）更改判题（题目提交）的状态为 “判题中”，防止重复执行，也能让用户即时看到状态
        QuestionSubmit questionSubmitUpdate = new QuestionSubmit();
        questionSubmitUpdate.setId(questionSubmitId);
        questionSubmitUpdate.setStatus(QuestionSubmitStatuEnum.RUNNING.getValue());
        boolean update = questionFeignClient.updateQuestionSubmitById(questionSubmitUpdate);
        if (!update) {
            throw new BusinessException(ErrorCode.SYSTEM_ERROR, "题目状态更新错误");
        }
        // 4）调用沙箱，获取到执行结果
        CodeSandbox codeSandbox = CodeSandboxFactory.newInstance(type);
        codeSandbox = new CodeSandboxProxy(codeSandbox);
        String language = questionSubmit.getLanguage();
        String code = questionSubmit.getCode();
        // 获取输入用例
        List<JudgeCase> judgeCaseList = JSONUtil.toList(question.getJudgeCase(), JudgeCase.class);
        List<String> inputList = judgeCaseList.stream().map(JudgeCase::getInput).collect(Collectors.toList());
        ExecuteCodeRequest executeCodeRequest = ExecuteCodeRequest.builder()
                .code(code)
                .language(language)
                .inputList(inputList)
                .build();
        ExecuteCodeResponse executeCodeResponse = codeSandbox.executeCode(executeCodeRequest);
        // 5）根据沙箱的执行结果，设置题目的判题状态和信息
        JudgeContext judgeContext = new JudgeContext();
        judgeContext.setJudgeInfo(executeCodeResponse.getJudgeInfo());
        judgeContext.setStatus(executeCodeResponse.getStatus());
        judgeContext.setInputList(inputList);
        judgeContext.setOutputList(executeCodeResponse.getOutputList());
        judgeContext.setJudgeCaseList(judgeCaseList);
        judgeContext.setQuestion(question);
        judgeContext.setQuestionSubmit(questionSubmit);
        judgeContext.setMessage(executeCodeResponse.getMessage());
        judgeContext.setResults(executeCodeResponse.getResults());

        JudgeInfo judgeInfo = judgeManager.doJudge(judgeContext);

        // 6)修改数据库中的判题结果
        boolean judgeResult = judgeInfo.getStatus().equals(JudgeInfoMessageEnum.ACCEPTED.getValue());

        questionSubmitUpdate.setStatus(judgeResult ?
                QuestionSubmitStatuEnum.SUCCESS.getValue() :
                QuestionSubmitStatuEnum.FAILED.getValue());
        questionSubmitUpdate.setJudgeInfo(JSONUtil.toJsonStr(judgeInfo));
        log.info("判题信息{}", JSONUtil.toJsonStr(judgeInfo));
        update = questionFeignClient.updateQuestionSubmitById(questionSubmitUpdate);
        if (!update) {
            throw new BusinessException(ErrorCode.SYSTEM_ERROR, "题目状态更新错误");
        }

        QuestionSubmit questionSubmitResult = questionFeignClient.getQuestionSubmitById(questionId);
        return questionSubmitResult;
    }
}
