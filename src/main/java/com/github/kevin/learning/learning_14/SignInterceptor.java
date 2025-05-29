package com.github.kevin.learning.learning_14;

import feign.RequestInterceptor;
import feign.RequestTemplate;
import lombok.RequiredArgsConstructor;
import org.apache.commons.codec.digest.DigestUtils;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import java.nio.charset.StandardCharsets;
import java.util.*;

@RequiredArgsConstructor
public class SignInterceptor implements RequestInterceptor {

    // 签名密钥，可以从配置中心获取
    private final String secretKey;

    @Override
    public void apply(RequestTemplate template) {
        // 1. 获取所有请求参数
        Map<String, Collection<String>> queries = new LinkedHashMap<>(template.queries());

        // 2. 添加时间戳参数
        String timestamp = String.valueOf(System.currentTimeMillis());
        queries.put("timestamp", Collections.singletonList(timestamp));

        // 3. 生成随机字符串
        String nonce = UUID.randomUUID().toString().replace("-", "").substring(0, 16);
        queries.put("nonce", Collections.singletonList(nonce));

        // 4. 对参数进行排序并拼接
        String signContent = buildSignContent(queries);

        // 4. 如果存在请求体，将body纳入签名计算
        if (template.body() != null && template.body().length > 0) {
            String bodyContent = new String(template.body(), StandardCharsets.UTF_8);
            signContent += "&body=" + DigestUtils.sha256Hex(bodyContent);
        }

        // 5. 生成签名
        String sign = generateSign(signContent);

        // 6. 将签名添加到请求参数
        queries.put("sign", Collections.singletonList(sign));

        // 7. 更新请求模板
        template.queries(null); // 清空原有参数
        queries.forEach((key, values) ->
                values.forEach(value -> template.query(key, value))
        );
    }

    /**
     * 构建签名字符串
     */
    private String buildSignContent(Map<String, Collection<String>> params) {
        // 1. 过滤空值参数并排序
        List<String> paramPairs = new ArrayList<>();
        params.entrySet().stream()
                .filter(entry -> !CollectionUtils.isEmpty(entry.getValue()))
                .sorted(Map.Entry.comparingByKey())
                .forEach(entry -> {
                    String value = String.join(",", entry.getValue());
                    if (StringUtils.hasText(value)) {
                        paramPairs.add(entry.getKey() + "=" + value);
                    }
                });

        // 2. 拼接参数
        return String.join("&", paramPairs);
    }

    /**
     * 生成签名
     */
    private String generateSign(String content) {
        // 使用HMAC-SHA256算法
        String signStr = content + "&key=" + secretKey;
        return DigestUtils.sha256Hex(signStr).toUpperCase();
    }
}
