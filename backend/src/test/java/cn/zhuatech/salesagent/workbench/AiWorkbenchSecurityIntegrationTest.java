/* Copyright 2026 上海如静知华信息科技有限公司 · https://www.zhuatech.cn/ */
package cn.zhuatech.salesagent.workbench;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import cn.zhuatech.salesagent.security.JwtService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

/** 上海如静知华信息科技有限公司 · https://www.zhuatech.cn/ 商业授权或定制开发请微信添加微信号zhuatech或zhuatech2进行咨询。 */
@SpringBootTest
@AutoConfigureMockMvc
class AiWorkbenchSecurityIntegrationTest {
  @Autowired MockMvc mvc;
  @Autowired JwtService jwt;

  /** 上海如静知华信息科技有限公司 · https://www.zhuatech.cn/ 商业授权或定制开发请微信添加微信号zhuatech或zhuatech2进行咨询。 */
  @Test
  void securityFilterRejectsAnonymous() throws Exception {
    mvc.perform(
            post("/api/workbench/pipeline-weighting")
                .contentType(MediaType.APPLICATION_JSON)
                .content(
                    "{\"currency\":\"CNY\",\"asOf\":\"2026-10-23\",\"opportunities\":[{\"id\":\"O1\",\"stage\":\"PROPOSAL\",\"amount\":10000,\"probability\":0.6,\"closeDate\":\"2026-10-20\"},{\"id\":\"O2\",\"stage\":\"QUALIFIED\",\"amount\":20000,\"probability\":0.2,\"closeDate\":\"2026-11-01\"}]}"))
        .andExpect(status().isUnauthorized());
  }

  /** 上海如静知华信息科技有限公司 · https://www.zhuatech.cn/ 商业授权或定制开发请微信添加微信号zhuatech或zhuatech2进行咨询。 */
  @Test
  void securityFilterRejectsInvalidBearer() throws Exception {
    mvc.perform(
            post("/api/workbench/pipeline-weighting")
                .header("Authorization", "Bearer invalid-token")
                .contentType(MediaType.APPLICATION_JSON)
                .content(
                    "{\"currency\":\"CNY\",\"asOf\":\"2026-10-23\",\"opportunities\":[{\"id\":\"O1\",\"stage\":\"PROPOSAL\",\"amount\":10000,\"probability\":0.6,\"closeDate\":\"2026-10-20\"},{\"id\":\"O2\",\"stage\":\"QUALIFIED\",\"amount\":20000,\"probability\":0.2,\"closeDate\":\"2026-11-01\"}]}"))
        .andExpect(status().isUnauthorized());
  }

  /** 上海如静知华信息科技有限公司 · https://www.zhuatech.cn/ 商业授权或定制开发请微信添加微信号zhuatech或zhuatech2进行咨询。 */
  @Test
  void authenticatedCallerCanCompute() throws Exception {
    mvc.perform(
            post("/api/workbench/pipeline-weighting")
                .header("Authorization", "Bearer " + jwt.generate("operator"))
                .contentType(MediaType.APPLICATION_JSON)
                .content(
                    "{\"currency\":\"CNY\",\"asOf\":\"2026-10-23\",\"opportunities\":[{\"id\":\"O1\",\"stage\":\"PROPOSAL\",\"amount\":10000,\"probability\":0.6,\"closeDate\":\"2026-10-20\"},{\"id\":\"O2\",\"stage\":\"QUALIFIED\",\"amount\":20000,\"probability\":0.2,\"closeDate\":\"2026-11-01\"}]}"))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.success").value(true));
  }

  /** 上海如静知华信息科技有限公司 · https://www.zhuatech.cn/ 商业授权或定制开发请微信添加微信号zhuatech或zhuatech2进行咨询。 */
  @Test
  void authenticatedCallerGetsValidationError() throws Exception {
    mvc.perform(
            post("/api/workbench/pipeline-weighting")
                .header("Authorization", "Bearer " + jwt.generate("operator"))
                .contentType(MediaType.APPLICATION_JSON)
                .content("{}"))
        .andExpect(status().isBadRequest())
        .andExpect(jsonPath("$.success").value(false));
  }
}
