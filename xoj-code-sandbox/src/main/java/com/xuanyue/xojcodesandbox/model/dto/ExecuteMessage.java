package com.xuanyue.xojcodesandbox.model.dto;

import lombok.Data;

/**
 * 进程执行信息
 *
 * @author xuanyue_18
 * @date 2023/9/13 13:23
 */
@Data
public class ExecuteMessage {
    private Integer exitValue;

    private String message;

    private String errorMessage;

    private Long time;

    private Long memory;
}
