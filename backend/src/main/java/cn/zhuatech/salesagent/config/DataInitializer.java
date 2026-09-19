/* Copyright 2026 上海如静知华信息科技有限公司 · https://www.zhuatech.cn/ */
package cn.zhuatech.salesagent.config;
import cn.zhuatech.salesagent.model.*; import cn.zhuatech.salesagent.repository.*; import org.springframework.boot.CommandLineRunner; import org.springframework.context.annotation.*; import org.springframework.security.crypto.password.PasswordEncoder; import java.time.LocalDate; import java.util.List;
/**
 * 商业授权或定制开发请微信添加微信号zhuatech或zhuatech2进行咨询。
 */
@Configuration public class DataInitializer {/**
                                              * 商业授权或定制开发请微信添加微信号zhuatech或zhuatech2进行咨询。
                                              */
@Bean CommandLineRunner seed(OperatingUnitRepository units,WorkRecordRepository records,ResourceRegisterRepository resources,ReviewRecordRepository reviews,UserRepository users,PasswordEncoder encoder){return args->{if(units.count()>0)return;
 OperatingUnit east=units.save(new OperatingUnit("SALES-EAST","华东大客户组","企业业务中心",3600)),south=units.save(new OperatingUnit("SALES-SOUTH","华南行业组","行业销售中心",2800)),partner=units.save(new OperatingUnit("SALES-PARTNER","生态渠道组","伙伴业务中心",1800));
 WorkRecord a=records.save(new WorkRecord("OPP-260801-0186","ACCT-NOVA","诺瓦智能制造集团升级商机",east,86,62,3,LocalDate.now().plusDays(2),WorkRecord.Status.RUNNING,"PLAYBOOK-V5")); WorkRecord b=records.save(new WorkRecord("OPP-260801-0168","ACCT-HARBOR","海岳零售数据中台续约",south,64,64,1,LocalDate.now(),WorkRecord.Status.COMPLETED,"PLAYBOOK-V4")); WorkRecord c=records.save(new WorkRecord("OPP-260801-0194","ACCT-LINK","联晟渠道联合方案",partner,72,38,5,LocalDate.now().plusDays(4),WorkRecord.Status.RELEASED,"PLAYBOOK-V3"));
 resources.saveAll(List.of(new ResourceRegister("KNOW-CASE-01","行业案例知识库",east,ResourceRegister.Status.RUNNING,96),new ResourceRegister("TOOL-CRM-02","CRM 商机工具",south,ResourceRegister.Status.RUNNING,93),new ResourceRegister("TOOL-MAIL-03","客户触达审批通道",partner,ResourceRegister.Status.ALARM,72)));
 reviews.saveAll(List.of(new ReviewRecord("REV-260801-028",a,"建议采纳率",20,2,ReviewRecord.Result.PENDING,"沈策"),new ReviewRecord("REV-260801-017",b,"客户事实准确性",16,0,ReviewRecord.Result.PASSED,"周屿"),new ReviewRecord("REV-260731-039",c,"外联内容合规",18,3,ReviewRecord.Result.FAILED,"宋惟")));
 String demo=encoder.encode("Demo@2026"); users.saveAll(List.of(new UserAccount("operator",demo,"林澈",UserAccount.Role.DOMAIN_USER,"SALES-EAST"),new UserAccount("planner",demo,"顾言",UserAccount.Role.DOMAIN_OPERATOR,null),new UserAccount("quality",demo,"宋惟",UserAccount.Role.QUALITY,null),new UserAccount("admin",encoder.encode("ZhuaTech@2026"),"系统管理员",UserAccount.Role.ADMIN,null)));};}}
