package com.xuanyue.xojbackendquestionservice.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.xuanyue.xojbackendcommon.common.ErrorCode;
import com.xuanyue.xojbackendcommon.constant.CommonConstant;
import com.xuanyue.xojbackendcommon.exception.BusinessException;
import com.xuanyue.xojbackendcommon.utils.SqlUtils;
import com.xuanyue.xojbackendmodel.model.dto.questionsubmit.QuestionSubmitAddRequest;
import com.xuanyue.xojbackendmodel.model.dto.questionsubmit.QuestionSubmitQueryRequest;
import com.xuanyue.xojbackendmodel.model.entity.Question;
import com.xuanyue.xojbackendmodel.model.entity.QuestionSubmit;
import com.xuanyue.xojbackendmodel.model.entity.User;
import com.xuanyue.xojbackendmodel.model.enums.QuestionSubmitLanguageEnum;
import com.xuanyue.xojbackendmodel.model.enums.QuestionSubmitStatuEnum;
import com.xuanyue.xojbackendmodel.model.vo.QuestionSubmitVO;
import com.xuanyue.xojbackendmodel.model.vo.UserVO;
import com.xuanyue.xojbackendquestionservice.mapper.QuestionSubmitMapper;
import com.xuanyue.xojbackendquestionservice.rabbitmq.MyMessageProducer;
import com.xuanyue.xojbackendquestionservice.service.QuestionService;
import com.xuanyue.xojbackendquestionservice.service.QuestionSubmitService;
import com.xuanyue.xojbackendserviceclient.service.JudgeFeignClient;
import com.xuanyue.xojbackendserviceclient.service.UserFeignClient;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;
import java.util.stream.Collectors;

import static com.xuanyue.xojbackendcommon.constant.MqConstant.CODE_EXCHANGE_NAME;
import static com.xuanyue.xojbackendcommon.constant.MqConstant.CODE_ROUTING_KEY;

/**
 * @author xuanyue18
 * @description 针对表【question_submit(题目提交题目)】的数据库操作Service实现
 * @createDate 2023-09-06 08:23:40
 */
@Service
public class QuestionSubmitServiceImpl extends ServiceImpl<QuestionSubmitMapper, QuestionSubmit>
        implements QuestionSubmitService {

    @Resource
    private QuestionService questionService;

    @Resource
    private UserFeignClient userFeignClient;

    @Resource
    @Lazy
    private JudgeFeignClient judgeFeignClient;

    @Resource
    private MyMessageProducer myMessageProducer;

    /**
     * 提交题目
     *
     * @param questionSubmitAddRequest
     * @param loginUser
     * @return
     */
    @Override
    public long doQuestionSubmit(QuestionSubmitAddRequest questionSubmitAddRequest, User loginUser) {
        // 校验编程语言是否合法
        String language = questionSubmitAddRequest.getLanguage();
        QuestionSubmitLanguageEnum languageEnum = QuestionSubmitLanguageEnum.getEnumByValue(language);
        if (languageEnum == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "编程语言错误");
        }
        long questionId = questionSubmitAddRequest.getQuestionId();
        // 判断实体是否存在，根据类别获取实体
        Question question = questionService.getById(questionId);
        if (question == null) {
            throw new BusinessException(ErrorCode.NOT_FOUND_ERROR);
        }
        // 设置提交数
        Integer submitNum = question.getSubmitNum();
        Question updateQuestion = new Question();
        synchronized (question.getSubmitNum()) {
            submitNum = submitNum + 1;
            updateQuestion.setId(questionId);
            updateQuestion.setSubmitNum(submitNum);
            boolean save = questionService.updateById(updateQuestion);
            if (!save) {
                throw new BusinessException(ErrorCode.OPERATION_ERROR, "数据保存失败");
            }
        }
        // 是否已提交题目
        long userId = loginUser.getId();
        // 每个用户串行提交题目
        QuestionSubmit questionSubmit = new QuestionSubmit();
        questionSubmit.setUserId(userId);
        questionSubmit.setQuestionId(questionId);
        questionSubmit.setCode(questionSubmitAddRequest.getCode());
        questionSubmit.setLanguage(language);
        // 设置初始状态
        questionSubmit.setStatus(QuestionSubmitStatuEnum.WAITING.getValue());
        questionSubmit.setJudgeInfo("{}");
        boolean save = this.save(questionSubmit);
        if (!save) {
            throw new BusinessException(ErrorCode.SYSTEM_ERROR, "数据插入失败");
        }
        Long questionSubmitId = questionSubmit.getId();
        // 发送消息
        myMessageProducer.sendMessage(CODE_EXCHANGE_NAME, CODE_ROUTING_KEY, String.valueOf(questionSubmitId));

        // 执行判题服务
        // CompletableFuture.runAsync(() -> {
        //     judgeFeignClient.doJudge(questionSubmitId);
        // });
        return questionSubmit.getId();
    }

    /**
     * 获取查询包装类(用户根据哪些字段查询, 根据前端传过来的请求对象,得到mybatis框架支持的查询QueryWrapper类)
     *
     * @param questionSubmitQueryRequest
     * @return
     */
    @Override
    public QueryWrapper<QuestionSubmit> getQueryWrapper(QuestionSubmitQueryRequest questionSubmitQueryRequest) {
        QueryWrapper<QuestionSubmit> queryWrapper = new QueryWrapper<>();
        if (questionSubmitQueryRequest == null) {
            return queryWrapper;
        }
        String language = questionSubmitQueryRequest.getLanguage();
        Integer status = questionSubmitQueryRequest.getStatus();
        Long questionId = questionSubmitQueryRequest.getQuestionId();
        Long userId = questionSubmitQueryRequest.getUserId();
        String sortField = questionSubmitQueryRequest.getSortField();
        String sortOrder = questionSubmitQueryRequest.getSortOrder();

        // 拼接查询条件
        queryWrapper.eq(StringUtils.isNotBlank(language), "language", language);
        queryWrapper.eq(ObjectUtils.isNotEmpty(userId), "userId", userId);
        queryWrapper.eq(ObjectUtils.isNotEmpty(questionId), "questionId", questionId);
        queryWrapper.eq(QuestionSubmitStatuEnum.getEnumByValue(status) != null, "status", status);
        queryWrapper.eq("isDelete", false);
        queryWrapper.orderBy(SqlUtils.validSortField(sortField), sortOrder.equals(CommonConstant.SORT_ORDER_ASC),
                sortField);
        return queryWrapper;
    }

    @Override
    public QuestionSubmitVO getQuestionSubmitVO(QuestionSubmit questionSubmit, User loginUser) {
        QuestionSubmitVO questionSubmitVO = QuestionSubmitVO.objToVo(questionSubmit);
        String title = questionService.getById(questionSubmit.getQuestionId()).getTitle();
        questionSubmitVO.setQuestionTitle(title);
        User user = userFeignClient.getById(questionSubmitVO.getUserId());
        UserVO userVO = userFeignClient.getUserVO(user);
        questionSubmitVO.setUserVO(userVO);
        // 脱敏: 仅本人和管理员能看见自己(提交userId和登录用户id不同)提交的代码
        // long userId = loginUser.getId();
        // 处理脱敏
        // if (userId != questionSubmit.getUserId() && !userFeignClient.isAdmin(loginUser)) {
        //     questionSubmitVO.setCode(null);
        // }
        return questionSubmitVO;
    }

    @Override
    public Page<QuestionSubmitVO> getQuestionSubmitVOPage(Page<QuestionSubmit> questionSubmitPage, User loginUser) {
        List<QuestionSubmit> questionSubmitList = questionSubmitPage.getRecords();
        Page<QuestionSubmitVO> questionSubmitVOPage = new Page<>(questionSubmitPage.getCurrent(), questionSubmitPage.getSize(), questionSubmitPage.getTotal());
        if (CollectionUtils.isEmpty(questionSubmitList)) {
            return questionSubmitVOPage;
        }
        List<QuestionSubmitVO> questionSubmitVOList = questionSubmitList.stream()
                .map(questionSubmit -> getQuestionSubmitVO(questionSubmit, loginUser))
                .collect(Collectors.toList());
        questionSubmitVOPage.setRecords(questionSubmitVOList);
        return questionSubmitVOPage;
    }


}




