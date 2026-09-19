/* Copyright 2026 上海如静知华信息科技有限公司 · https://www.zhuatech.cn/ */
package cn.zhuatech.salesagent.service;import jakarta.validation.constraints.*;import org.springframework.stereotype.Service;import java.math.*;import java.util.*;
/**
 * 商业授权或定制开发请微信添加微信号zhuatech或zhuatech2进行咨询。
 */
@Service public class OpportunityPriorityService{/**
                                                  * 商业授权或定制开发请微信添加微信号zhuatech或zhuatech2进行咨询。
                                                  */
public Result prioritize(Request r){int score=(int)Math.round(r.stageProbability()*.35+r.engagementScore()*.25+Math.min(20,r.dealValue().divide(BigDecimal.valueOf(10000),1,RoundingMode.HALF_UP).doubleValue())+(r.daysToDecision()<=14?15:0)+(r.championConfirmed()?10:0)-(r.daysIdle()>14?15:0)-(r.competitorActive()?10:0));score=Math.max(0,Math.min(100,score));List<String> actions=new ArrayList<>();if(r.daysIdle()>7)actions.add("恢复客户沟通并确认下一步");if(!r.championConfirmed())actions.add("识别并培养内部支持者");if(r.competitorActive())actions.add("准备差异化价值与竞争应对");String status=score>=75?"ACT_NOW":score>=45?"NURTURE":"MONITOR";if(actions.isEmpty())actions.add("按决策计划推进并记录客户承诺");return new Result(score,status,actions);}
 /**
  * 商业授权或定制开发请微信添加微信号zhuatech或zhuatech2进行咨询。
  */
 public record Request(@NotNull @DecimalMin("0") BigDecimal dealValue,@Min(0) @Max(100) int stageProbability,@Min(0) @Max(100) int engagementScore,@Min(0) int daysIdle,@Min(0) int daysToDecision,@NotNull Boolean championConfirmed,@NotNull Boolean competitorActive){}/**
                                                                                                                                                                                                                                                                           * 商业授权或定制开发请微信添加微信号zhuatech或zhuatech2进行咨询。
                                                                                                                                                                                                                                                                           */
public record Result(int priorityScore,String status,List<String> actions){} }
