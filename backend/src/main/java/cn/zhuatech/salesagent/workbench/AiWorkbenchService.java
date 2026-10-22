/* Copyright 2026 上海如静知华信息科技有限公司 · https://www.zhuatech.cn/ */
package cn.zhuatech.salesagent.workbench;

import static cn.zhuatech.salesagent.workbench.WorkbenchInput.*;

import java.math.*;
import java.util.*;
import org.springframework.stereotype.Service;

/** 上海如静知华信息科技有限公司 · https://www.zhuatech.cn/ 商业授权或定制开发请微信添加微信号zhuatech或zhuatech2进行咨询。 */
@Service
public class AiWorkbenchService {
  /** 上海如静知华信息科技有限公司 · https://www.zhuatech.cn/ 商业授权或定制开发请微信添加微信号zhuatech或zhuatech2进行咨询。 */
  public Map<String, Object> execute(Map<String, Object> r) {
    validate(r);
    keys(r, "currency", "asOf", "opportunities");
    String currency = text(r, "currency", 3);
    if (!currency.matches("[A-Z]{3}")) throw new IllegalArgumentException("币种格式错误");
    var asOf = date(text(r, "asOf", 10));
    Set<String> ids = new HashSet<>();
    Map<String, BigDecimal> stages = new TreeMap<>();
    List<Map<String, Object>> out = new ArrayList<>();
    BigDecimal total = BigDecimal.ZERO, weighted = BigDecimal.ZERO;
    for (var o : rows(r, "opportunities", 1, 500)) {
      keys(o, "id", "stage", "amount", "probability", "closeDate");
      String id = id(o, "id");
      unique(ids, id);
      String stage = id(o, "stage");
      BigDecimal amount = money(o, "amount"),
          p = decimal(o, "probability", 0, 1),
          w = amount.multiply(p).setScale(2, RoundingMode.HALF_UP);
      var close = date(text(o, "closeDate", 10));
      total = total.add(amount);
      weighted = weighted.add(w);
      stages.merge(stage, w, BigDecimal::add);
      out.add(
          Map.of("id", id, "stage", stage, "weightedAmount", w, "overdue", close.isBefore(asOf)));
    }
    List<Map<String, Object>> groups = new ArrayList<>();
    stages.forEach((stage, v) -> groups.add(Map.of("stage", stage, "weightedAmount", v)));
    return Map.of(
        "currency",
        currency,
        "grossAmount",
        total,
        "weightedAmount",
        weighted,
        "stages",
        groups,
        "opportunities",
        out);
  }
}
