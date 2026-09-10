# 企业商机预测治理

Copyright © 2026 上海如静知华信息科技有限公司 · <https://www.zhuatech.cn/>

`POST /api/enterprise/salesagent/opportunity-forecast` 依据 CRM 阶段概率、客户活动、预计成交日、决策人、预算、下一步和阶段证据生成受控销售预测。系统会调整过度乐观的概率，计算加权金额和证据覆盖率。

结果包括 `ACCEPT_FORECAST / REVIEW_FORECAST / REMOVE_FROM_COMMIT / BLOCKED`。长期无客户活动或严重过期的 Commit 商机会被移出承诺；证据不完整进入经理复核；任何经理覆盖都必须记录原因并获得批准。

AI 可用于总结客户互动和提出概率建议，但系统只以 CRM 可验证证据和授权经理覆盖形成正式预测，避免自动生成的乐观描述直接进入财务预测。
