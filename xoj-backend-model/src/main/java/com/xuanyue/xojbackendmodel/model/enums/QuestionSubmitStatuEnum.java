package com.xuanyue.xojbackendmodel.model.enums;

import org.apache.commons.lang3.ObjectUtils;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 题目提交枚举
 *
 * @author xuanyue18
 */
public enum QuestionSubmitStatuEnum {

    // 判题状态（0 - 待判题、1 - 判题中、2 - 成功、3 - 失败）
    WAITING("等待中", 1),
    RUNNING("判题中", 2),
    SUCCESS("成功", 3),
    FAILED("失败", 4);

    private final String text;

    private final Integer value;

    QuestionSubmitStatuEnum(String text, Integer value) {
        this.text = text;
        this.value = value;
    }

    /**
     * 获取值列表
     *
     * @return
     */
    public static List<Integer> getValues() {
        return Arrays.stream(values()).map(item -> item.value).collect(Collectors.toList());
    }

    /**
     * 根据 value 获取枚举
     *
     * @param value
     * @return
     */
    public static QuestionSubmitStatuEnum getEnumByValue(Integer value) {
        if (ObjectUtils.isEmpty(value)) {
            return null;
        }
        for (QuestionSubmitStatuEnum anEnum : QuestionSubmitStatuEnum.values()) {
            if (anEnum.value.equals(value)) {
                return anEnum;
            }
        }
        return null;
    }

    public Integer getValue() {
        return value;
    }

    public String getText() {
        return text;
    }
}
