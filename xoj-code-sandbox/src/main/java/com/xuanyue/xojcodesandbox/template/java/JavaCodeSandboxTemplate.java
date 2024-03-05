package com.xuanyue.xojcodesandbox.template.java;

import cn.hutool.core.io.FileUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.dfa.FoundWord;
import cn.hutool.dfa.WordTree;
import com.xuanyue.xojcodesandbox.model.enums.ErrorCode;
import com.xuanyue.xojcodesandbox.common.BusinessException;
import com.xuanyue.xojcodesandbox.model.dto.ExecuteCodeRequest;
import com.xuanyue.xojcodesandbox.model.dto.ExecuteCodeResponse;
import com.xuanyue.xojcodesandbox.model.dto.ExecuteResult;
import com.xuanyue.xojcodesandbox.model.dto.JudgeInfo;
import com.xuanyue.xojcodesandbox.model.enums.ExecuteCodeStatusEnum;
import com.xuanyue.xojcodesandbox.CodeSandbox;
import com.xuanyue.xojcodesandbox.utils.ProcessUtils;
import lombok.extern.slf4j.Slf4j;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static com.xuanyue.xojcodesandbox.constants.SandBoxConstant.GLOBAL_JAVA_CLASS_NAME;


/**
 * Java代码沙箱模板方法的实现
 *
 * @author xuanyue_18
 * @date 2023/9/17 9:58
 */
@Slf4j
public abstract class JavaCodeSandboxTemplate implements CodeSandbox {

    /**
     * 使用字典树存放违禁词
     */
    private static final WordTree WORD_TREE;

    static {
        WORD_TREE = new WordTree();
        WORD_TREE.addWords("Files", "exec");
    }


    // private static final String SECURITY_MANAGER_PATH = "D:\\project\\starProjects\\xoj-code-sandbox\\src\\main\\resources\\security";

    // private static final String SECURITY_MANAGER_CLASS_NAME = "MySecurityManager";

    @Override
    public ExecuteCodeResponse executeCode(ExecuteCodeRequest executeCodeRequest) {

        List<String> inputList = executeCodeRequest.getInputList();
        String code = executeCodeRequest.getCode();

        // 1.把用户的代码保存为文件
        String dir;
        File codeFile;
        try {
            dir = System.getProperty("user.dir") + File.separator + "tmpCode" + File.separator + UUID.randomUUID();
            codeFile = saveFile(code, dir);
        } catch (BusinessException e) {
            return ExecuteCodeResponse.builder()
                    .status(ExecuteCodeStatusEnum.COMPILE_FAILED.getValue())
                    .message(ErrorCode.DANGER_CODE.getMessage())
                    .build();
        }

        // 2.编译代码, 得到class文件
        try {
            ExecuteResult compileMessage = compileFile(codeFile);
            log.info("编译信息：{}", compileMessage);
            // 编译已经失败了
            if (compileMessage.getExitValue() != 0) {
                return ExecuteCodeResponse.builder()
                        .status(ExecuteCodeStatusEnum.COMPILE_FAILED.getValue())
                        .message(compileMessage.getErrorOutput())
                        .build();
            }
        } catch (IOException e) {
            return ExecuteCodeResponse.builder()
                    .status(ExecuteCodeStatusEnum.COMPILE_FAILED.getValue())
                    .message(e.toString())
                    .build();
        }

        // 3.执行代码, 得到输出结果
        List<ExecuteResult> executeResults;
        try {
            executeResults = runCode(dir, inputList);
            // 有错误结果
            ExecuteResult lastResult = executeResults.get(executeResults.size() - 1);
            if(StrUtil.isNotEmpty(lastResult.getErrorOutput())){
                return ExecuteCodeResponse.builder()
                        .status(ExecuteCodeStatusEnum.RUN_FAILED.getValue())
                        .message(lastResult.getErrorOutput())
                        .build();
            }
        } catch (IOException e) {
            return ExecuteCodeResponse.builder()
                    .status(ExecuteCodeStatusEnum.RUN_FAILED.getValue())
                    .message(e.toString())
                    .build();
        }

        //4. 收集整理输出结果
        ExecuteCodeResponse response = arrangeResponse(executeResults);

        //5. 文件清理
        clearFile(codeFile, dir);
        return response;
    }

    /**
     * 1.把用户的代码保存为文件
     *
     * @param code 用户代码
     * @param dir  存放目录
     * @return
     */
    protected File saveFile(String code, String dir) {
        // 检查代码内容，是否有黑名单代码
        FoundWord foundWord = WORD_TREE.matchWord(code);
        if (foundWord != null) {
            throw new BusinessException(ErrorCode.DANGER_CODE);
        }

        String path = dir + File.separator + GLOBAL_JAVA_CLASS_NAME;

        log.info("代码保存目录：{}", dir);
        return FileUtil.writeUtf8String(code, path);
    }


    /**
     * 2.编译代码, 得到class文件
     *
     * @param codeFile 用户代码文件
     * @return
     * @throws IOException
     */
    protected ExecuteResult compileFile(File codeFile) throws IOException {

        // Linux下的命令
        // String compileCmd = String.format("/software/jdk1.8.0_301/bin/javac -encoding utf-8 %s", codeFile.getAbsolutePath());

        // Windows下的命令
        String compileCmd = String.format("javac -encoding utf-8 %s", codeFile.getAbsolutePath());

        // 创建并执行process
        log.info("执行命令：{}", compileCmd);

        Process compileProcess = Runtime.getRuntime().exec(compileCmd);
        // 拿到process执行信息
        return ProcessUtils.getProcessMessage(compileProcess, "编译");
    }

    /**
     * 3.运行代码，这一步针对Args和ACM有不同实现
     * @param dir 代码存放目录
     * @param inputList 输入用例
     * @return
     */
    protected abstract List<ExecuteResult> runCode(String dir, List<String> inputList) throws IOException;

    /**
     * 4.收集整理运行结果
     * @param executeResults
     * @return
     */
    protected ExecuteCodeResponse arrangeResponse(List<ExecuteResult> executeResults){
        return ExecuteCodeResponse.builder()
                .status(ExecuteCodeStatusEnum.SUCCESS.getValue())
                .message(ExecuteCodeStatusEnum.SUCCESS.getMsg())
                .results(executeResults)
                .build();
    }

    /**
     * 5.文件清理
     * @param codeFile
     * @param dir
     */
    protected void clearFile(File codeFile, String dir){
        if (codeFile.getParentFile() != null) {
            boolean del = FileUtil.del(dir);
            log.info("删除{}: {}", del ? "成功" : "失败", dir);
        }
    }

    /**
     * 6.获取错误处理
     *
     * @param e
     * @return
     */
    private ExecuteCodeResponse getErrorResponse(Throwable e) {
        ExecuteCodeResponse executeCodeResponse = new ExecuteCodeResponse();
        executeCodeResponse.setOutputList(new ArrayList<>());
        executeCodeResponse.setMessage(e.getMessage());
        // 表示代码沙箱错误
        executeCodeResponse.setStatus(2);
        executeCodeResponse.setJudgeInfo(new JudgeInfo());
        return executeCodeResponse;
    }

}
