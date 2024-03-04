package com.xuanyue.xojcodesandbox.template.java;

import com.xuanyue.xojbackendmodel.model.dto.codesandbox.ExecuteResult;
import com.xuanyue.xojcodesandbox.utils.ProcessUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static com.xuanyue.xojbackendcommon.common.SandBoxConstant.TIME_OUT;


/**
 * Java原生代码实现代码沙箱(Args模式)
 *
 * @author xuanyue_18
 * @date 2023/10/19 15:31
 */

@Service
@Slf4j
public class JavaNativeArgsSandbox extends JavaCodeSandboxTemplate {
    @Override
    protected List<ExecuteResult> runCode(String dir, List<String> inputList) throws IOException {
        List<ExecuteResult> messages = new ArrayList<>();
        for (String input : inputList) {
            // Linux下的命令
            // String runCmd = String.format("/software/jdk1.8.0_301/bin/java -Xmx256m -Dfile.encoding=UTF-8 -cp %s Main %s", dir, input);

            // Windows下命令
            String runCmd = String.format("java -Xmx256m -Dfile.encoding=UTF-8 -cp %s Main %s", dir, input);
            log.info("执行命令：{}", runCmd);
            Process runProcess = Runtime.getRuntime().exec(runCmd);
            // 超时控制(过了时间强制中断程序)
            new Thread(() -> {
                try {
                    Thread.sleep(TIME_OUT);
                    log.info("超时了，中断程序");
                    runProcess.destroy();
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            }).start();
            ExecuteResult executeResult = ProcessUtils.getProcessMessage(runProcess, "运行");
            messages.add(executeResult);
        }
        return messages;
    }
}
