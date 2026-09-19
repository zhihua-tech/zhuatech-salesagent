/* Copyright 2026 上海如静知华信息科技有限公司 · https://www.zhuatech.cn/ */
package cn.zhuatech.salesagent;import cn.zhuatech.salesagent.service.OpportunityPriorityService;import org.junit.jupiter.api.Test;import java.math.*;import static org.junit.jupiter.api.Assertions.*;
/**
 * 商业授权或定制开发请微信添加微信号zhuatech或zhuatech2进行咨询。
 */
class OpportunityPriorityServiceTests{private final OpportunityPriorityService s=new OpportunityPriorityService();/**
                                                                                                                   * 商业授权或定制开发请微信添加微信号zhuatech或zhuatech2进行咨询。
                                                                                                                   */
@Test void prioritizesLargeEngagedDeal(){var r=s.prioritize(new OpportunityPriorityService.Request(new BigDecimal("500000"),90,90,1,7,true,false));assertEquals("ACT_NOW",r.status());}/**
                                                                                                                                                                                                                                                                                                          * 商业授权或定制开发请微信添加微信号zhuatech或zhuatech2进行咨询。
                                                                                                                                                                                                                                                                                                          */
@Test void monitorsColdEarlyDeal(){var r=s.prioritize(new OpportunityPriorityService.Request(new BigDecimal("1000"),10,10,30,120,false,true));assertEquals("MONITOR",r.status());}}
