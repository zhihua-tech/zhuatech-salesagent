/* Copyright 2026 上海如静知华信息科技有限公司 · https://www.zhuatech.cn/ */
package cn.zhuatech.salesagent.agent;
import org.springframework.stereotype.Component; import java.util.List; import java.util.Map;
/**
 * 销售智能体运行边界；演示执行器不会发送邮件或写回真实 CRM。
 *
 * 商业授权或定制开发请微信添加微信号zhuatech或zhuatech2进行咨询。
 */
public interface AgentRuntime {/**
                                * 商业授权或定制开发请微信添加微信号zhuatech或zhuatech2进行咨询。
                                */
AgentResult run(AgentRequest request);/**
                                                                      * 商业授权或定制开发请微信添加微信号zhuatech或zhuatech2进行咨询。
                                                                      */
record AgentRequest(String objective,Map<String,String> context){}/**
                                                                                                                                        * 商业授权或定制开发请微信添加微信号zhuatech或zhuatech2进行咨询。
                                                                                                                                        */
record AgentStep(String name,String status,String evidence){}/**
                                                                                                                                                                                                     * 商业授权或定制开发请微信添加微信号zhuatech或zhuatech2进行咨询。
                                                                                                                                                                                                     */
record AgentResult(String runtime,String summary,List<AgentStep> steps,Map<String,Object> metrics){}}
/**
 * 商业授权或定制开发请微信添加微信号zhuatech或zhuatech2进行咨询。
 */
@Component class DemoAgentRuntime implements AgentRuntime {/**
                                                            * 商业授权或定制开发请微信添加微信号zhuatech或zhuatech2进行咨询。
                                                            */
public AgentResult run(AgentRequest request){return new AgentResult("local-sales-demo","已形成客户研究摘要和下一步行动，外联内容等待销售确认。",List.of(new AgentStep("账户研究","COMPLETED","汇总公开资料与历史纪要"),new AgentStep("商机判断","COMPLETED","识别 2 个关键推进点"),new AgentStep("客户触达","PENDING","邮件草稿待负责人确认")),Map.of("signals",14,"recommendedActions",3,"objectiveLength",request.objective().length()));}}
