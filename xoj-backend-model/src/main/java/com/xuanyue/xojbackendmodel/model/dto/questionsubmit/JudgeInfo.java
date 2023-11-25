package com.xuanyue.xojbackendmodel.model.dto.questionsubmit;

import lombok.Data;

/**
 * 判题信息
 *
 * @author xuanyue_18
 * @date 2023/9/6 9:28
 */
@Data
public class JudgeInfo {

    /**
     * 程序执行信息
     */
    private String message;

    /**
     * 消耗内存(MB)
     */
    private Long memory;

    /**
     * 消耗时间(ms)
     */
    private Long time;

    /**
     * 总用例数
     */
    private Integer total;

    /**
     * 通过用例数
     */
    private Integer pass;

    /**
     * 状态
     */
    private String status;

    /**
     * 输入
     */
    private String input;

    /**
     * 输出
     */
    private String output;

    /**
     * 期待输出
     */
    private String expectedOutput;

}
