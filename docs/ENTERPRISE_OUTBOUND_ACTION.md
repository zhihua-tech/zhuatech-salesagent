# 企业级销售 Agent 对外动作授权

`POST /api/enterprise/salesagent/outbound-action-authorization` 检查客户偏好、禁联名单、数据授权、商业陈述、折扣承诺、人工批准、审计、频率和撤回能力，返回 `EXECUTE / REVIEW / BLOCKED`。

生产环境必须限制 Agent 可调用工具和商业权限，高影响承诺不得绕过人工审批。
