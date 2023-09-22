package com.xuanyue.xojbackendmodel.model.codesandbox;

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
     * 消耗内存
     */
    private Long memory;

    /**
     * 消耗时间
     */
    private Long time;
}
