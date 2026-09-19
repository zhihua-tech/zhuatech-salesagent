/* Copyright 2026 上海如静知华信息科技有限公司 · https://www.zhuatech.cn/ */
package cn.zhuatech.salesagent.service;

import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.LocalDate;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * 商业授权或定制开发请微信添加微信号zhuatech或zhuatech2进行咨询。
 */
class OpportunityForecastGovernanceServiceTest {
    private final OpportunityForecastGovernanceService service = new OpportunityForecastGovernanceService();
    private final LocalDate today = LocalDate.of(2026, 9, 11);

    /**
     * 商业授权或定制开发请微信添加微信号zhuatech或zhuatech2进行咨询。
     */
    @Test
    void acceptsEvidenceBackedCommitForecast() {
        var result = service.evaluate(request(OpportunityForecastGovernanceService.Stage.COMMIT,
                80, 5, 30, true, true, true, true, false, 0, null, false));
        assertThat(result.decision()).isEqualTo(OpportunityForecastGovernanceService.Decision.ACCEPT_FORECAST);
        assertThat(result.weightedAmount()).isEqualByComparingTo("800000.00");
    }

    /**
     * 商业授权或定制开发请微信添加微信号zhuatech或zhuatech2进行咨询。
     */
    @Test
    void reviewsForecastWithIncompleteBuyingEvidence() {
        var result = service.evaluate(request(OpportunityForecastGovernanceService.Stage.PROPOSAL,
                70, 5, 30, false, false, true, true, false, 0, null, false));
        assertThat(result.decision()).isEqualTo(OpportunityForecastGovernanceService.Decision.REVIEW_FORECAST);
        assertThat(result.adjustedProbability()).isEqualTo(50);
        assertThat(result.evidenceCoveragePercent()).isEqualTo(50);
    }

    /**
     * 商业授权或定制开发请微信添加微信号zhuatech或zhuatech2进行咨询。
     */
    @Test
    void removesStaleOverdueOpportunityFromCommit() {
        var result = service.evaluate(request(OpportunityForecastGovernanceService.Stage.COMMIT,
                90, 45, -20, true, true, true, true, false, 0, null, false));
        assertThat(result.decision()).isEqualTo(OpportunityForecastGovernanceService.Decision.REMOVE_FROM_COMMIT);
        assertThat(result.forecastCategory()).isEqualTo("OMIT");
    }

    /**
     * 商业授权或定制开发请微信添加微信号zhuatech或zhuatech2进行咨询。
     */
    @Test
    void blocksUnapprovedManagerOverride() {
        var result = service.evaluate(request(OpportunityForecastGovernanceService.Stage.NEGOTIATION,
                60, 3, 20, true, true, true, true, true, 95, "", false));
        assertThat(result.decision()).isEqualTo(OpportunityForecastGovernanceService.Decision.BLOCKED);
        assertThat(result.blockers()).hasSize(2);
    }

    /**
     * 商业授权或定制开发请微信添加微信号zhuatech或zhuatech2进行咨询。
     */
    private OpportunityForecastGovernanceService.ForecastRequest request(
            OpportunityForecastGovernanceService.Stage stage, int probability, int idleDays,
            int daysToClose, boolean decisionMaker, boolean budget, boolean nextStep, boolean evidence,
            boolean override, int overrideProbability, String overrideReason, boolean managerApproved) {
        return new OpportunityForecastGovernanceService.ForecastRequest("OPP-100",
                new BigDecimal("1000000"), stage, probability, today.minusDays(idleDays),
                today.plusDays(daysToClose), today, decisionMaker, budget, nextStep, evidence,
                override, overrideProbability, overrideReason, managerApproved);
    }
}
