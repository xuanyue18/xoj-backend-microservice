package com.xuanyue.xojcodesandbox;


import com.xuanyue.xojcodesandbox.model.dto.ExecuteCodeRequest;
import com.xuanyue.xojcodesandbox.model.dto.ExecuteCodeResponse;

/**
 * 代码沙箱接口定义
 *
 * @author xuanyue_18
 * @date 2023/9/10 16:15
 */
public interface CodeSandbox {

    /**
     * 执行代码
     *
     * @param executeCodeRequest
     * @return
     */
    ExecuteCodeResponse executeCode(ExecuteCodeRequest executeCodeRequest);
}
