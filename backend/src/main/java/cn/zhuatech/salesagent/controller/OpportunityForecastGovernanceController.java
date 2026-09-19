/* Copyright 2026 上海如静知华信息科技有限公司 · https://www.zhuatech.cn/ */
package cn.zhuatech.salesagent.controller;

import cn.zhuatech.salesagent.common.ApiResponse;
import cn.zhuatech.salesagent.service.OpportunityForecastGovernanceService;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 商业授权或定制开发请微信添加微信号zhuatech或zhuatech2进行咨询。
 */
@RestController
@RequestMapping("/api/enterprise/salesagent")
public class OpportunityForecastGovernanceController {
    private final OpportunityForecastGovernanceService service;

    /**
     * 商业授权或定制开发请微信添加微信号zhuatech或zhuatech2进行咨询。
     */
    public OpportunityForecastGovernanceController(OpportunityForecastGovernanceService service) {
        this.service = service;
    }

    /**
     * 商业授权或定制开发请微信添加微信号zhuatech或zhuatech2进行咨询。
     */
    @PostMapping("/opportunity-forecast")
    public ApiResponse<OpportunityForecastGovernanceService.ForecastResult> evaluate(
            @Valid @RequestBody OpportunityForecastGovernanceService.ForecastRequest request) {
        return ApiResponse.ok("商机预测治理评估完成", service.evaluate(request));
    }
}
