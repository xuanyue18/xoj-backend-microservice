package com.xuanyue.xojbackendjudgeservice.judge.strategy;


import com.xuanyue.xojbackendmodel.model.codesandbox.JudgeInfo;
import com.xuanyue.xojbackendmodel.model.dto.question.JudgeCase;
import com.xuanyue.xojbackendmodel.model.entity.Question;
import com.xuanyue.xojbackendmodel.model.entity.QuestionSubmit;
import lombok.Data;

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

    private List<String> inputList;

    private List<String> outputList;

    private List<JudgeCase> judgeCaseList;

    private Question question;

    private QuestionSubmit questionSubmit;
}
