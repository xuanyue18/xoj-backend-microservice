package com.xuanyue.xojbackendjudgeservice.judge.strategy;


import com.xuanyue.xojbackendmodel.model.dto.codesandbox.ExecuteResult;
import com.xuanyue.xojbackendmodel.model.dto.question.JudgeCase;
import com.xuanyue.xojbackendmodel.model.dto.questionsubmit.JudgeInfo;
import com.xuanyue.xojbackendmodel.model.entity.Question;
import com.xuanyue.xojbackendmodel.model.entity.QuestionSubmit;
import lombok.Data;
import org.aspectj.bridge.Message;

import java.util.List;

/**
 * 上下文(用于定义在策略中传递的参数)
 *
 * @author xuanyue_18
 * @date 2023/9/10 20:33
 */
@Data
public class JudgeContext {

    private JudgeInfo judgeInfo;

    private Integer status;

    private List<ExecuteResult> results;

    private List<String> inputList;

    private List<String> outputList;

    private List<JudgeCase> judgeCaseList;

    private Question question;

    private QuestionSubmit questionSubmit;

    private String message;
}
