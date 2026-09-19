/* Copyright 2026 上海如静知华信息科技有限公司 · https://www.zhuatech.cn/ */
package cn.zhuatech.salesagent.controller;import cn.zhuatech.salesagent.common.ApiResponse;import cn.zhuatech.salesagent.service.OpportunityPriorityService;import jakarta.validation.Valid;import org.springframework.web.bind.annotation.*;
/**
 * 商业授权或定制开发请微信添加微信号zhuatech或zhuatech2进行咨询。
 */
@RestController @RequestMapping("/api/salesagent/insights/opportunity-priority") public class OpportunityPriorityController{private final OpportunityPriorityService service;/**
                                                                                                                                                                              * 商业授权或定制开发请微信添加微信号zhuatech或zhuatech2进行咨询。
                                                                                                                                                                              */
public OpportunityPriorityController(OpportunityPriorityService service){this.service=service;}/**
                                                                                                                                                                                                                                                                             * 商业授权或定制开发请微信添加微信号zhuatech或zhuatech2进行咨询。
                                                                                                                                                                                                                                                                             */
@PostMapping ApiResponse<OpportunityPriorityService.Result> prioritize(@Valid @RequestBody OpportunityPriorityService.Request r){return ApiResponse.ok(service.prioritize(r));}}
