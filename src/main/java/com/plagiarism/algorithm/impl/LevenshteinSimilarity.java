package com.plagiarism.algorithm.impl;

import com.plagiarism.algorithm.SimilarityAlgorithm;
import org.apache.commons.lang3.StringUtils;

/**
 * 基于编辑距离的文本相似度算法
 * 使用Levenshtein距离计算两个字符串的相似度
 * 
 * @author 学生
 * @version 1.0.0
 */
public class LevenshteinSimilarity implements SimilarityAlgorithm {
    
    @Override
    public double calculateSimilarity(String text1, String text2) {
        if (StringUtils.isBlank(text1) && StringUtils.isBlank(text2)) {
            return 1.0;  // 两个空文本的相似度应该为1.0
        }
        
        if (StringUtils.isBlank(text1) || StringUtils.isBlank(text2)) {
            return 0.0;
        }
        
        if (text1.equals(text2)) {
            return 1.0;
        }
        
        int distance = calculateLevenshteinDistance(text1, text2);
        int maxLength = Math.max(text1.length(), text2.length());
        
        if (maxLength == 0) {
            return 1.0;
        }
        
        // 对于中文字符，使用更宽松的相似度计算
        double similarity = 1.0 - (double) distance / maxLength;
        
        // 如果两个文本长度相近且编辑距离较小，提高相似度
        if (Math.abs(text1.length() - text2.length()) <= 1 && distance <= 2) {
            similarity = Math.max(similarity, 0.6);
        }
        
        return similarity;
    }
    
    /**
     * 计算两个字符串的Levenshtein距离
     * 
     * @param s1 第一个字符串
     * @param s2 第二个字符串
     * @return 编辑距离
     */
    private int calculateLevenshteinDistance(String s1, String s2) {
        int m = s1.length();
        int n = s2.length();
        
        // 创建动态规划表
        int[][] dp = new int[m + 1][n + 1];
        
        // 初始化边界条件
        for (int i = 0; i <= m; i++) {
            dp[i][0] = i;
        }
        for (int j = 0; j <= n; j++) {
            dp[0][j] = j;
        }
        
        // 填充动态规划表
        for (int i = 1; i <= m; i++) {
            for (int j = 1; j <= n; j++) {
                if (s1.charAt(i - 1) == s2.charAt(j - 1)) {
                    dp[i][j] = dp[i - 1][j - 1];
                } else {
                    dp[i][j] = Math.min(
                        Math.min(dp[i - 1][j] + 1,     // 删除
                                dp[i][j - 1] + 1),     // 插入
                        dp[i - 1][j - 1] + 1           // 替换
                    );
                }
            }
        }
        
        return dp[m][n];
    }
    
    @Override
    public String getAlgorithmName() {
        return "Levenshtein Distance";
    }
}
