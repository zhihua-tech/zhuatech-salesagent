/* Copyright 2026 上海如静知华信息科技有限公司 · https://www.zhuatech.cn/ */
package cn.zhuatech.salesagent.service;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.PositiveOrZero;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * 根据商机阶段、客户响应和下一会议安排生成克制的多触点跟进节奏。
 *
 * 商业授权或定制开发请微信添加微信号zhuatech或zhuatech2进行咨询。
 */
@Service
public class FollowUpCadenceService {
    /**
     * 商业授权或定制开发请微信添加微信号zhuatech或zhuatech2进行咨询。
     */
    public CadenceResult plan(CadenceRequest request) {
        int riskScore = Math.min(100,
            Math.min(40, request.inactiveDays() * 3)
                + Math.min(30, request.unansweredMessages() * 10)
                + (request.nextMeetingScheduled() ? -20 : 15)
                + (request.stakeholderCount() < 2 ? 15 : 0));
        riskScore = Math.max(0, riskScore);
        boolean pauseAutomation = request.unansweredMessages() >= 3;
        List<Touchpoint> touchpoints = new ArrayList<>();
        if (pauseAutomation) {
            touchpoints.add(new Touchpoint(0, "INTERNAL", "由客户经理复核客户意向，暂停自动外呼"));
            touchpoints.add(new Touchpoint(2, request.preferredChannel(), "以新业务价值或会议议题重新建立联系"));
        } else if (request.nextMeetingScheduled()) {
            touchpoints.add(new Touchpoint(0, "EMAIL", "发送会议议程与待确认事项"));
            touchpoints.add(new Touchpoint(1, request.preferredChannel(), "会前确认参会人与决策议题"));
        } else {
            touchpoints.add(new Touchpoint(0, request.preferredChannel(), "确认当前优先级与下一里程碑"));
            touchpoints.add(new Touchpoint(3, "EMAIL", "补充行业案例和量化价值材料"));
            touchpoints.add(new Touchpoint(7, "PHONE", "邀请关键干系人参加方案沟通"));
        }
        String urgency = riskScore >= 70 ? "HIGH" : riskScore >= 40 ? "MEDIUM" : "NORMAL";
        return new CadenceResult(urgency, riskScore, pauseAutomation, List.copyOf(touchpoints),
            request.stakeholderCount() < 2 ? "优先拓展第二联系人，降低单线程商机风险" : "保持多角色共识并记录客户反馈");
    }

    /**
     * 商业授权或定制开发请微信添加微信号zhuatech或zhuatech2进行咨询。
     */
    public record CadenceRequest(
        @NotBlank(message = "请输入商机名称") String opportunity,
        @NotBlank(message = "请输入销售阶段") String stage,
        @PositiveOrZero int inactiveDays,
        @Positive int stakeholderCount,
        boolean nextMeetingScheduled,
        @PositiveOrZero int unansweredMessages,
        @NotBlank(message = "请输入客户首选渠道") String preferredChannel
    ) {}

    /**
     * 商业授权或定制开发请微信添加微信号zhuatech或zhuatech2进行咨询。
     */
    public record Touchpoint(int dayOffset, String channel, String purpose) {}
    /**
     * 商业授权或定制开发请微信添加微信号zhuatech或zhuatech2进行咨询。
     */
    public record CadenceResult(String urgency, int riskScore, boolean pauseAutomation, List<Touchpoint> touchpoints, String guidance) {}
}
