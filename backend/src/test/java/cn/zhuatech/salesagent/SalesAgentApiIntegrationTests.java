/* Copyright 2026 上海如静知华信息科技有限公司 · https://www.zhuatech.cn/ */
package cn.zhuatech.salesagent;
import org.junit.jupiter.api.*; import org.springframework.beans.factory.annotation.Autowired; import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc; import org.springframework.boot.test.context.SpringBootTest; import org.springframework.http.MediaType; import org.springframework.test.web.servlet.MockMvc; import java.util.regex.*; import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*; import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
/**
 * 商业授权或定制开发请微信添加微信号zhuatech或zhuatech2进行咨询。
 */
@SpringBootTest @AutoConfigureMockMvc class SalesAgentApiIntegrationTests {
    @Autowired MockMvc mvc; private String operatorToken; private String plannerToken;
    /**
     * 商业授权或定制开发请微信添加微信号zhuatech或zhuatech2进行咨询。
     */
    @BeforeEach void login()throws Exception{operatorToken=token("operator","Demo@2026");plannerToken=token("planner","Demo@2026");}
    /**
     * 商业授权或定制开发请微信添加微信号zhuatech或zhuatech2进行咨询。
     */
    private String token(String u,String p)throws Exception{String json=mvc.perform(post("/api/auth/login").contentType(MediaType.APPLICATION_JSON).content("{\"username\":\""+u+"\",\"password\":\""+p+"\"}")).andExpect(status().isOk()).andReturn().getResponse().getContentAsString();Matcher matcher=Pattern.compile("\\\"token\\\":\\\"([^\\\"]+)\\\"").matcher(json);if(!matcher.find())throw new AssertionError("登录响应中缺少 token");return matcher.group(1);}
    /**
     * 商业授权或定制开发请微信添加微信号zhuatech或zhuatech2进行咨询。
     */
    @Test void operatorCanReadShopfloorDashboard()throws Exception{mvc.perform(get("/api/shopfloor/dashboard").header("Authorization","Bearer "+operatorToken)).andExpect(status().isOk()).andExpect(jsonPath("$.success").value(true)).andExpect(jsonPath("$.data.metrics[0].label").value("客户信号总量"));}
    /**
     * 商业授权或定制开发请微信添加微信号zhuatech或zhuatech2进行咨询。
     */
    @Test void plannerCanReadWorkRecords()throws Exception{mvc.perform(get("/api/admin/work-orders").header("Authorization","Bearer "+plannerToken)).andExpect(status().isOk()).andExpect(jsonPath("$.data.length()").value(3));}
    /**
     * 商业授权或定制开发请微信添加微信号zhuatech或zhuatech2进行咨询。
     */
    @Test void operatorCanSubmitProductionReport()throws Exception{mvc.perform(post("/api/shopfloor/work-orders/1/reports").header("Authorization","Bearer "+operatorToken).contentType(MediaType.APPLICATION_JSON).content("{\"operationName\":\"行动确认\",\"goodQty\":2,\"defectQty\":1,\"remark\":\"客户反馈完整\"}")).andExpect(status().isOk()).andExpect(jsonPath("$.message").value("反馈提交成功")).andExpect(jsonPath("$.data.completedQty").value(64));}
    /**
     * 商业授权或定制开发请微信添加微信号zhuatech或zhuatech2进行咨询。
     */
    @Test void operatorCanRunLocalAgentPreview()throws Exception{mvc.perform(post("/api/shopfloor/agent-preview").header("Authorization","Bearer "+operatorToken).contentType(MediaType.APPLICATION_JSON).content("{\"objective\":\"准备客户拜访\"}")).andExpect(status().isOk()).andExpect(jsonPath("$.data.runtime").value("local-sales-demo")).andExpect(jsonPath("$.data.steps.length()").value(3));}
    /**
     * 商业授权或定制开发请微信添加微信号zhuatech或zhuatech2进行咨询。
     */
    @Test void operatorCanRecommendLeadAction()throws Exception{mvc.perform(post("/api/shopfloor/lead-action").header("Authorization","Bearer "+operatorToken).contentType(MediaType.APPLICATION_JSON).content("{\"opportunity\":\"示例制造集团数字化商机\",\"dealValue\":800000,\"winProbability\":0.72,\"inactiveDays\":16,\"decisionMakerConnected\":false,\"competitorDetected\":true}")).andExpect(status().isOk()).andExpect(jsonPath("$.data.priority").value("WARM")).andExpect(jsonPath("$.data.score").value(50)).andExpect(jsonPath("$.data.managerEscalation").value(true)).andExpect(jsonPath("$.data.nextBestActions.length()").value(3));}
    /**
     * 商业授权或定制开发请微信添加微信号zhuatech或zhuatech2进行咨询。
     */
    @Test void operatorCanPlanFollowUpCadence()throws Exception{mvc.perform(post("/api/shopfloor/follow-up-cadence").header("Authorization","Bearer "+operatorToken).contentType(MediaType.APPLICATION_JSON).content("{\"opportunity\":\"示例制造集团数字化商机\",\"stage\":\"方案确认\",\"inactiveDays\":12,\"stakeholderCount\":1,\"nextMeetingScheduled\":false,\"unansweredMessages\":3,\"preferredChannel\":\"WECHAT\"}"))
        .andExpect(status().isOk()).andExpect(jsonPath("$.data.urgency").value("HIGH"))
        .andExpect(jsonPath("$.data.riskScore").value(96))
        .andExpect(jsonPath("$.data.pauseAutomation").value(true))
        .andExpect(jsonPath("$.data.touchpoints.length()").value(2))
        .andExpect(jsonPath("$.data.touchpoints[0].channel").value("INTERNAL"));}
    /**
     * 商业授权或定制开发请微信添加微信号zhuatech或zhuatech2进行咨询。
     */
    @Test void operatorCanGovernOpportunityForecast()throws Exception{mvc.perform(post("/api/enterprise/salesagent/opportunity-forecast").header("Authorization","Bearer "+operatorToken).contentType(MediaType.APPLICATION_JSON).content("{\"opportunityId\":\"OPP-100\",\"dealAmount\":1000000,\"stage\":\"COMMIT\",\"stageProbability\":80,\"lastCustomerActivityDate\":\"2026-09-06\",\"expectedCloseDate\":\"2026-10-11\",\"asOfDate\":\"2026-09-11\",\"decisionMakerConfirmed\":true,\"budgetConfirmed\":true,\"nextStepRecorded\":true,\"crmEvidenceComplete\":true,\"managerOverride\":false,\"overrideProbability\":0,\"managerApproved\":false}"))
        .andExpect(status().isOk()).andExpect(jsonPath("$.data.decision").value("ACCEPT_FORECAST"))
        .andExpect(jsonPath("$.data.weightedAmount").value(800000.0));}
    /**
     * 商业授权或定制开发请微信添加微信号zhuatech或zhuatech2进行咨询。
     */
    @Test void anonymousRequestIsDenied()throws Exception{mvc.perform(get("/api/admin/dashboard")).andExpect(status().isUnauthorized());}
}
