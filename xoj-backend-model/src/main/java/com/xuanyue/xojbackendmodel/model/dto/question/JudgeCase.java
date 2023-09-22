package com.xuanyue.xojbackendmodel.model.dto.question;

import lombok.Data;

/**
 * 题目用例
 *
 * @author xuanyue_18
 * @date 2023/9/6 9:19
 */
@Data
public class JudgeCase {

    /**
     * 输入用例
     */
    private String input;

    /**
     * 输出用例
     */
    private String output;
}
