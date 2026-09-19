/* Copyright 2026 上海如静知华信息科技有限公司 · https://www.zhuatech.cn/ */
package cn.zhuatech.salesagent.service;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;

/**
 * 将 CRM 阶段、客户证据、停滞和经理覆盖转化为可审计的销售预测。
 *
 * 商业授权或定制开发请微信添加微信号zhuatech或zhuatech2进行咨询。
 */
@Service
public class OpportunityForecastGovernanceService {
    /**
     * 商业授权或定制开发请微信添加微信号zhuatech或zhuatech2进行咨询。
     */
    public ForecastResult evaluate(ForecastRequest request) {
        List<String> blockers = new ArrayList<>();
        List<String> reasons = new ArrayList<>();
        List<String> actions = new ArrayList<>();
        long idleDays = Math.max(0, ChronoUnit.DAYS.between(request.lastCustomerActivityDate(), request.asOfDate()));
        long overdueCloseDays = Math.max(0, ChronoUnit.DAYS.between(request.expectedCloseDate(), request.asOfDate()));

        if (request.managerOverride() && !request.managerApproved()) blockers.add("预测覆盖尚未获得销售经理批准");
        if (request.managerOverride() && blank(request.overrideReason())) blockers.add("预测覆盖缺少可审计原因");
        if (request.expectedCloseDate().isBefore(request.lastCustomerActivityDate())) {
            blockers.add("预计成交日期早于最近客户活动日期");
        }

        int evidenceItems = (request.decisionMakerConfirmed() ? 1 : 0)
                + (request.budgetConfirmed() ? 1 : 0)
                + (request.nextStepRecorded() ? 1 : 0)
                + (request.crmEvidenceComplete() ? 1 : 0);
        int evidenceCoverage = evidenceItems * 25;
        int adjustedProbability = request.managerOverride()
                ? request.overrideProbability() : request.stageProbability();

        if (!request.decisionMakerConfirmed()) {
            adjustedProbability = Math.min(adjustedProbability, 60);
            reasons.add("尚未确认最终决策人");
        }
        if (!request.budgetConfirmed()) {
            adjustedProbability = Math.min(adjustedProbability, 50);
            reasons.add("客户预算尚未确认");
        }
        if (!request.nextStepRecorded()) reasons.add("缺少客户确认的下一步行动");
        if (!request.crmEvidenceComplete()) reasons.add("CRM 阶段证据不完整");
        if (idleDays > 30) {
            adjustedProbability = Math.min(adjustedProbability, 25);
            reasons.add("商机超过 30 天无客户活动");
        } else if (idleDays > 14) {
            adjustedProbability = Math.max(0, adjustedProbability - 20);
            reasons.add("商机超过 14 天无客户活动");
        }
        if (overdueCloseDays > 0) {
            adjustedProbability = Math.min(adjustedProbability, 20);
            reasons.add("预计成交日期已经过期");
        }

        BigDecimal weightedAmount = request.dealAmount()
                .multiply(BigDecimal.valueOf(adjustedProbability))
                .divide(BigDecimal.valueOf(100), 2, RoundingMode.HALF_UP);

        if (!blockers.isEmpty()) {
            actions.add("停止覆盖预测并由销售运营核对 CRM 证据");
            return result(Decision.BLOCKED, "OMIT", adjustedProbability, evidenceCoverage,
                    idleDays, overdueCloseDays, weightedAmount, blockers, reasons, actions);
        }

        if (request.stage() == Stage.COMMIT && (idleDays > 30 || overdueCloseDays > 14)) {
            actions.add("从 Commit 中移除并重新确认客户成交计划");
            return result(Decision.REMOVE_FROM_COMMIT, "OMIT", adjustedProbability, evidenceCoverage,
                    idleDays, overdueCloseDays, weightedAmount, blockers, reasons, actions);
        }

        if (evidenceCoverage < 100 || idleDays > 14 || overdueCloseDays > 0
                || (request.stage() == Stage.COMMIT && adjustedProbability < 70)) {
            actions.add("补齐预算、决策链、下一步和阶段证据后由经理复核");
            return result(Decision.REVIEW_FORECAST, category(adjustedProbability), adjustedProbability,
                    evidenceCoverage, idleDays, overdueCloseDays, weightedAmount, blockers, reasons, actions);
        }

        actions.add("接受预测并按最近客户证据持续滚动更新");
        return result(Decision.ACCEPT_FORECAST, category(adjustedProbability), adjustedProbability,
                evidenceCoverage, idleDays, overdueCloseDays, weightedAmount, blockers, reasons, actions);
    }

    /**
     * 商业授权或定制开发请微信添加微信号zhuatech或zhuatech2进行咨询。
     */
    private ForecastResult result(Decision decision, String category, int probability, int evidenceCoverage,
                                  long idleDays, long overdueCloseDays, BigDecimal weightedAmount,
                                  List<String> blockers, List<String> reasons, List<String> actions) {
        return new ForecastResult(decision, category, probability, evidenceCoverage, idleDays,
                overdueCloseDays, weightedAmount, List.copyOf(blockers), List.copyOf(reasons),
                List.copyOf(actions));
    }

    /**
     * 商业授权或定制开发请微信添加微信号zhuatech或zhuatech2进行咨询。
     */
    private String category(int probability) {
        return probability >= 75 ? "COMMIT" : probability >= 50 ? "BEST_CASE" : "PIPELINE";
    }

    /**
     * 商业授权或定制开发请微信添加微信号zhuatech或zhuatech2进行咨询。
     */
    private boolean blank(String value) {
        return value == null || value.isBlank();
    }

    /**
     * 商业授权或定制开发请微信添加微信号zhuatech或zhuatech2进行咨询。
     */
    public record ForecastRequest(
            @NotBlank String opportunityId,
            @NotNull @DecimalMin("0.01") BigDecimal dealAmount,
            @NotNull Stage stage,
            @Min(0) @Max(100) int stageProbability,
            @NotNull LocalDate lastCustomerActivityDate,
            @NotNull LocalDate expectedCloseDate,
            @NotNull LocalDate asOfDate,
            boolean decisionMakerConfirmed,
            boolean budgetConfirmed,
            boolean nextStepRecorded,
            boolean crmEvidenceComplete,
            boolean managerOverride,
            @Min(0) @Max(100) int overrideProbability,
            String overrideReason,
            boolean managerApproved
    ) {}

    /**
     * 商业授权或定制开发请微信添加微信号zhuatech或zhuatech2进行咨询。
     */
    public record ForecastResult(Decision decision, String forecastCategory, int adjustedProbability,
                                 int evidenceCoveragePercent, long idleDays, long overdueCloseDays,
                                 BigDecimal weightedAmount, List<String> blockers,
                                 List<String> reasons, List<String> actions) {}

    /**
     * 商业授权或定制开发请微信添加微信号zhuatech或zhuatech2进行咨询。
     */
    public enum Stage { PIPELINE, QUALIFICATION, PROPOSAL, NEGOTIATION, COMMIT }
    /**
     * 商业授权或定制开发请微信添加微信号zhuatech或zhuatech2进行咨询。
     */
    public enum Decision { ACCEPT_FORECAST, REVIEW_FORECAST, REMOVE_FROM_COMMIT, BLOCKED }
}
