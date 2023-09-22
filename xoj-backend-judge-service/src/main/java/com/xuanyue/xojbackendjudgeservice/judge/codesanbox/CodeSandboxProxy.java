package com.xuanyue.xojbackendjudgeservice.judge.codesanbox;

import com.xuanyue.xojbackendmodel.model.codesandbox.ExecuteCodeRequest;
import com.xuanyue.xojbackendmodel.model.codesandbox.ExecuteCodeResponse;
import lombok.extern.slf4j.Slf4j;

/**
 * @author xuanyue_18
 * @date 2023/9/10 19:15
 */
@Slf4j
public class CodeSandboxProxy implements CodeSandbox {

    private CodeSandbox codeSandbox;

    public CodeSandboxProxy(CodeSandbox codeSandbox) {
        this.codeSandbox = codeSandbox;
    }

    @Override
    public ExecuteCodeResponse executeCode(ExecuteCodeRequest executeCodeRequest) {
        log.info("代码沙箱请求信息: {}", executeCodeRequest.toString());
        ExecuteCodeResponse executeCodeResponse = codeSandbox.executeCode(executeCodeRequest);
        log.info("代码沙箱响应信息: {}", executeCodeResponse.toString());
        return executeCodeResponse;
    }
}
