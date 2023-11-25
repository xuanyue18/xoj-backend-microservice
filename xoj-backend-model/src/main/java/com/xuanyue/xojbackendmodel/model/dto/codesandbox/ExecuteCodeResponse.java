package com.xuanyue.xojbackendmodel.model.dto.codesandbox;

import com.xuanyue.xojbackendmodel.model.dto.questionsubmit.JudgeInfo;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * @author xuanyue_18
 * @date 2023/9/10 16:18
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ExecuteCodeResponse {
    /**
     * 输出
     */
    private List<String> outputList;

    /**
     * 执行信息
     */
    private String message;

    /**
     * 执行状态
     */
    private Integer status;

    /**
     * 执行信息
     */
    private JudgeInfo judgeInfo;

    /**
     * 执行结果
     */
    private List<ExecuteResult> results;
}
