/* Copyright 2026 上海如静知华信息科技有限公司 · https://www.zhuatech.cn/ */
package cn.zhuatech.salesagent.service;

import jakarta.validation.constraints.DecimalMax;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.PositiveOrZero;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * 根据商机价值、赢率和停滞信号给出下一步销售动作。
 *
 * 商业授权或定制开发请微信添加微信号zhuatech或zhuatech2进行咨询。
 */
@Service
public class LeadActionService {
    /**
     * 商业授权或定制开发请微信添加微信号zhuatech或zhuatech2进行咨询。
     */
    public ActionResult plan(ActionRequest request) {
        int score = Math.min(100,
            (int) Math.round(request.winProbability() * 50)
                + (request.dealValue() >= 500_000 ? 20 : request.dealValue() >= 100_000 ? 10 : 5)
                + (request.decisionMakerConnected() ? 15 : 0)
                + (request.competitorDetected() ? 10 : 0)
                - Math.min(20, request.inactiveDays()));
        String priority = score >= 75 ? "HOT" : score >= 50 ? "WARM" : "NURTURE";
        List<String> actions = new ArrayList<>();
        if (!request.decisionMakerConnected()) actions.add("确认决策链并预约关键人沟通");
        if (request.inactiveDays() >= 7) actions.add("发送带业务价值的重新激活材料");
        if (request.competitorDetected()) actions.add("准备差异化方案与竞争应对清单");
        if (actions.isEmpty()) actions.add("确认下一里程碑与双方责任人");
        return new ActionResult(priority, Math.max(0, score), List.copyOf(actions), request.inactiveDays() >= 14, request.inactiveDays() >= 14 ? "建议销售经理介入" : "由客户经理跟进");
    }

    /**
     * 商业授权或定制开发请微信添加微信号zhuatech或zhuatech2进行咨询。
     */
    public record ActionRequest(
        @NotBlank(message = "请输入商机名称") String opportunity,
        @Positive double dealValue,
        @DecimalMin("0.0") @DecimalMax("1.0") double winProbability,
        @PositiveOrZero int inactiveDays,
        boolean decisionMakerConnected,
        boolean competitorDetected
    ) {}

    /**
     * 商业授权或定制开发请微信添加微信号zhuatech或zhuatech2进行咨询。
     */
    public record ActionResult(String priority, int score, List<String> nextBestActions, boolean managerEscalation, String ownerGuidance) {}
}
