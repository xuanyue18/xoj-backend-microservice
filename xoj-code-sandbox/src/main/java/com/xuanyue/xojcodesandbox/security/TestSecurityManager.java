package com.xuanyue.xojcodesandbox.security;

import cn.hutool.core.io.FileUtil;

import java.nio.charset.Charset;

/**
 * @author xuanyue_18
 * @date 2023/9/14 17:06
 */
public class TestSecurityManager {
    public static void main(String[] args) {
        System.setSecurityManager(new MySecurityManager());
        // List<String> strings = FileUtil.readLines("D:\\project\\starProjects\\xoj-code-sandbox\\src\\main\\resources\\application.yml", StandardCharsets.UTF_8);
        // System.out.println(strings);

        FileUtil.writeString("aa", "aaa", Charset.defaultCharset());
    }
}
