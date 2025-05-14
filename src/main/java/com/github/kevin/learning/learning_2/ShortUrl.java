package com.github.kevin.learning.learning_2;

import java.util.Random;

public class ShortUrl {
    // 标准Base64字符表（索引0~63对应的字符）
    private static final char[] BASE64_CHARS = {
            'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I', 'J', 'K', 'L', 'M',
            'N', 'O', 'P', 'Q', 'R', 'S', 'T', 'U', 'V', 'W', 'X', 'Y', 'Z',
            'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'i', 'j', 'k', 'l', 'm',
            'n', 'o', 'p', 'q', 'r', 's', 't', 'u', 'v', 'w', 'x', 'y', 'z',
            '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', '-', '_'
    };

    /**
     * 生成6位URL安全的Base64随机字符串
     *
     * @return 例如 "AQg7aB" 或 "x-Y9fP"（已替换+/为-_）
     */
    public static String generateRandomBase64UrlSafeString() {
        Random random = new Random();
        StringBuilder sb = new StringBuilder(6);

        for (int i = 0; i < 6; i++) {
            int randomIndex = random.nextInt(64);
            char c = BASE64_CHARS[randomIndex];
            sb.append(c);
        }

        return sb.toString();
    }

    public static void main(String[] args) {
        for (int i = 0; i < 1000; i++) {
            System.out.println(generateRandomBase64UrlSafeString());
        }
    }
}
