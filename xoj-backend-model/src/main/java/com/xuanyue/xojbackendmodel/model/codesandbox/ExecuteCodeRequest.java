package com.xuanyue.xojbackendmodel.model.codesandbox;

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
public class ExecuteCodeRequest {
    /**
     * 输入
     */
    private List<String> inputList;

    /**
     * 执行代码
     */
    private String code;

    /**
     * 编程语言
     */
    private String language;
}
