package com.plagiarism.algorithm.impl;

import com.plagiarism.algorithm.SimilarityAlgorithm;
import org.apache.commons.lang3.StringUtils;

import java.util.*;
import java.util.regex.Pattern;

/**
 * 基于Jaccard系数的文本相似度算法
 * 将文本转换为字符集合，计算两个集合的Jaccard相似度
 * 
 * @author 学生
 * @version 1.0.0
 */
public class JaccardSimilarity implements SimilarityAlgorithm {
    
    private static final Pattern CHINESE_PATTERN = Pattern.compile("[\\u4e00-\\u9fa5]");
    private static final Pattern ENGLISH_PATTERN = Pattern.compile("[a-zA-Z]");
    private static final Pattern DIGIT_PATTERN = Pattern.compile("[0-9]");
    
    @Override
    public double calculateSimilarity(String text1, String text2) {
        if (StringUtils.isBlank(text1) && StringUtils.isBlank(text2)) {
            return 1.0;
        }
        
        if (StringUtils.isBlank(text1) || StringUtils.isBlank(text2)) {
            return 0.0;
        }
        
        if (text1.equals(text2)) {
            return 1.0;
        }
        
        // 预处理文本
        String processedText1 = preprocessText(text1);
        String processedText2 = preprocessText(text2);
        
        // 转换为字符集合
        Set<Character> set1 = textToCharacterSet(processedText1);
        Set<Character> set2 = textToCharacterSet(processedText2);
        
        // 计算Jaccard相似度
        return calculateJaccardSimilarity(set1, set2);
    }
    
    /**
     * 预处理文本，去除标点符号和多余空格
     */
    private String preprocessText(String text) {
        if (StringUtils.isBlank(text)) {
            return "";
        }
        
        // 去除标点符号，保留中文、英文、数字
        StringBuilder sb = new StringBuilder();
        for (char c : text.toCharArray()) {
            if (CHINESE_PATTERN.matcher(String.valueOf(c)).matches() ||
                ENGLISH_PATTERN.matcher(String.valueOf(c)).matches() ||
                DIGIT_PATTERN.matcher(String.valueOf(c)).matches()) {
                sb.append(c);
            }
        }
        
        return sb.toString();
    }
    
    /**
     * 将文本转换为字符集合
     */
    private Set<Character> textToCharacterSet(String text) {
        Set<Character> characterSet = new HashSet<>();
        
        if (StringUtils.isNotBlank(text)) {
            for (char c : text.toCharArray()) {
                characterSet.add(c);
            }
        }
        
        return characterSet;
    }
    
    /**
     * 计算Jaccard相似度
     * Jaccard(A, B) = |A ∩ B| / |A ∪ B|
     */
    private double calculateJaccardSimilarity(Set<Character> set1, Set<Character> set2) {
        if (set1.isEmpty() && set2.isEmpty()) {
            return 1.0;
        }
        
        if (set1.isEmpty() || set2.isEmpty()) {
            return 0.0;
        }
        
        // 计算交集
        Set<Character> intersection = new HashSet<>(set1);
        intersection.retainAll(set2);
        
        // 计算并集
        Set<Character> union = new HashSet<>(set1);
        union.addAll(set2);
        
        return (double) intersection.size() / union.size();
    }
    
    @Override
    public String getAlgorithmName() {
        return "Jaccard Similarity";
    }
}
