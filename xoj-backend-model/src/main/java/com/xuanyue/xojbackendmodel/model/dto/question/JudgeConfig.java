package com.xuanyue.xojbackendmodel.model.dto.question;

import lombok.Data;

/**
 * 题目配置
 *
 * @author xuanyue_18
 * @date 2023/9/6 9:19
 */
@Data
public class JudgeConfig {

    /**
     * 时间限制(ms)
     */
    private Long timeLimit;

    /**
     * 内存限制(KB)
     */
    private Long memoryLimit;

    /**
     * 堆栈限制(KB)
     */
    private Long stackLimit;
}
