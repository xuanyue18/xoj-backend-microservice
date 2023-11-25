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

    ACCEPTED("通过", "accepted"),
    WRONG_ANSWER("输出结果与预期不符!", "wrong answer"),
    COMPILE_ERROR("编译错误", "compile error"),
    MEMORY_LIMIT_EXCEEDED("内存溢出", "memory limit exceeded"),
    TIME_LIMIT_EXCEEDED("超时", "time limit exceeded"),
    PRESENTATION_ERROR("展示错误", "presentation error"),
    OUTPUT_LIMIT_EXCEEDED("输出溢出", "output limit exceeded"),
    WAITING("等待中", "waiting"),
    DANGEROUS_OPERATION("危险操作", "dangerous operation"),
    RUNTIME_ERROR("运行错误(用户程序的问题)", "runtime error"),
    SYSTEM_ERROR("系统错误(做系统人的问题)", "system error");

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
