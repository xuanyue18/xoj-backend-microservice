package com.xuanyue.xojbackendmodel.model.enums;

import org.apache.commons.lang3.ObjectUtils;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 用户角色枚举
 *
 * @author xuanyue18
 */
public enum JudgeInfoMessageEnum {

    ACCEPTED("success", "成功"),
    WRONG_ANSWER("wrong answer", "答案错误"),
    COMPILE_ERROR("compile error", "编译错误"),
    MEMORY_LIMIT_EXCEEDED("memory limit exceeded", "内存溢出"),
    TIME_LIMIT_EXCEEDED("time limit exceeded", "超时"),
    PRESENTATION_ERROR("presentation error", "展示错误"),
    OUTPUT_LIMIT_EXCEEDED("output limit exceeded", "输出溢出"),
    WAITING("waiting", "等待中"),
    DANGEROUS_OPERATION("dangerous operation", "危险操作"),
    RUNTIME_ERROR("runtime error", "运行错误（用户程序的问题）"),
    SYSTEM_ERROR("system error", "系统错误（做系统人的问题）");

    private final String text;

    private final String value;

    JudgeInfoMessageEnum(String text, String value) {
        this.text = text;
        this.value = value;
    }

    /**
     * 获取值列表
     *
     * @return
     */
    public static List<String> getValues() {
        return Arrays.stream(values()).map(item -> item.value).collect(Collectors.toList());
    }

    /**
     * 根据 value 获取枚举
     *
     * @param value
     * @return
     */
    public static JudgeInfoMessageEnum getEnumByValue(String value) {
        if (ObjectUtils.isEmpty(value)) {
            return null;
        }
        for (JudgeInfoMessageEnum anEnum : JudgeInfoMessageEnum.values()) {
            if (anEnum.value.equals(value)) {
                return anEnum;
            }
        }
        return null;
    }

    public String getValue() {
        return value;
    }

    public String getText() {
        return text;
    }
}
