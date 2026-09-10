# SalesAgent API

版权所有 © 2026 上海如静知华信息科技有限公司。

| 方法 | 路径 | 说明 |
| --- | --- | --- |
| POST | `/api/auth/login` | 获取 JWT |
| GET | `/api/admin/dashboard` | 销售智能体运营总览 |
| GET | `/api/admin/work-orders` | 商机协同任务 |
| GET | `/api/shopfloor/dashboard` | 客户经理工作台 |
| POST | `/api/shopfloor/work-orders/{id}/reports` | 确认销售行动 |
| POST | `/api/shopfloor/agent-preview` | 生成本地演示销售建议 |
| POST | `/api/shopfloor/follow-up-cadence` | 按阶段、响应和干系人覆盖生成多触点跟进节奏 |
| POST | `/api/shopfloor/lead-action` | 评估商机热度并生成下一最佳行动与升级建议 |
| POST | `/api/enterprise/salesagent/outbound-action-authorization` | 校验 Agent 对外销售动作授权条件 |
| POST | `/api/enterprise/salesagent/opportunity-forecast` | 计算受证据约束的销售预测和 Commit 决策 |
