package com.plagiarism.algorithm.impl;

import com.plagiarism.algorithm.SimilarityAlgorithm;
import org.apache.commons.lang3.StringUtils;

import java.util.*;
import java.util.regex.Pattern;

/**
 * 基于余弦相似度的文本相似度算法
 * 将文本转换为词频向量，然后计算余弦相似度
 * 
 * @author 学生
 * @version 1.0.0
 */
public class CosineSimilarity implements SimilarityAlgorithm {
    
    private static final Pattern CHINESE_PATTERN = Pattern.compile("[\\u4e00-\\u9fa5]");
    private static final Pattern ENGLISH_PATTERN = Pattern.compile("[a-zA-Z]");
    private static final Pattern DIGIT_PATTERN = Pattern.compile("[0-9]");
    
    @Override
    public double calculateSimilarity(String text1, String text2) {
        if (StringUtils.isBlank(text1) || StringUtils.isBlank(text2)) {
            return 0.0;
        }
        
        if (text1.equals(text2)) {
            return 1.0;
        }
        
        // 预处理文本
        String processedText1 = preprocessText(text1);
        String processedText2 = preprocessText(text2);
        
        // 分词
        Set<String> words1 = segmentText(processedText1);
        Set<String> words2 = segmentText(processedText2);
        
        // 构建词汇表
        Set<String> vocabulary = new HashSet<>(words1);
        vocabulary.addAll(words2);
        
        // 计算词频向量
        Map<String, Integer> vector1 = calculateWordFrequency(processedText1, vocabulary);
        Map<String, Integer> vector2 = calculateWordFrequency(processedText2, vocabulary);
        
        // 计算余弦相似度
        return calculateCosineSimilarity(vector1, vector2, vocabulary);
    }
    
    /**
     * 预处理文本，去除标点符号和多余空格
     */
    private String preprocessText(String text) {
        if (StringUtils.isBlank(text)) {
            return "";
        }
        
        // 去除标点符号，保留中文、英文、数字和空格
        StringBuilder sb = new StringBuilder();
        for (char c : text.toCharArray()) {
            if (CHINESE_PATTERN.matcher(String.valueOf(c)).matches() ||
                ENGLISH_PATTERN.matcher(String.valueOf(c)).matches() ||
                DIGIT_PATTERN.matcher(String.valueOf(c)).matches() ||
                Character.isWhitespace(c)) {
                sb.append(c);
            }
        }
        
        // 规范化空格
        return sb.toString().replaceAll("\\s+", " ").trim();
    }
    
    /**
     * 文本分词，支持中英文混合
     */
    private Set<String> segmentText(String text) {
        Set<String> words = new HashSet<>();
        if (StringUtils.isBlank(text)) {
            return words;
        }
        
        // 预处理文本，去除标点符号
        String cleanText = preprocessText(text);
        
        // 按空格分割
        String[] tokens = cleanText.split("\\s+");
        
        for (String token : tokens) {
            if (StringUtils.isNotBlank(token)) {
                // 对于中文，按字符分割，但也保留完整的词
                if (CHINESE_PATTERN.matcher(token).find()) {
                    // 添加完整的中文词
                    words.add(token);
                    // 也添加单个中文字符
                    for (char c : token.toCharArray()) {
                        if (CHINESE_PATTERN.matcher(String.valueOf(c)).matches()) {
                            words.add(String.valueOf(c));
                        }
                    }
                } else {
                    // 对于英文，按单词分割
                    words.add(token.toLowerCase());
                }
            }
        }
        
        return words;
    }
    
    
    /**
     * 计算词频向量
     */
    private Map<String, Integer> calculateWordFrequency(String text, Set<String> vocabulary) {
        Map<String, Integer> frequency = new HashMap<>();
        
        // 初始化所有词汇的频率为0
        for (String word : vocabulary) {
            frequency.put(word, 0);
        }
        
        // 计算实际频率
        Set<String> words = segmentText(text);
        for (String word : words) {
            frequency.put(word, frequency.getOrDefault(word, 0) + 1);
        }
        
        return frequency;
    }
    
    /**
     * 计算余弦相似度
     */
    private double calculateCosineSimilarity(Map<String, Integer> vector1, 
                                           Map<String, Integer> vector2, 
                                           Set<String> vocabulary) {
        double dotProduct = 0.0;
        double norm1 = 0.0;
        double norm2 = 0.0;
        
        for (String word : vocabulary) {
            int freq1 = vector1.getOrDefault(word, 0);
            int freq2 = vector2.getOrDefault(word, 0);
            
            dotProduct += freq1 * freq2;
            norm1 += freq1 * freq1;
            norm2 += freq2 * freq2;
        }
        
        if (norm1 == 0.0 || norm2 == 0.0) {
            return 0.0;
        }
        
        return dotProduct / (Math.sqrt(norm1) * Math.sqrt(norm2));
    }
    
    @Override
    public String getAlgorithmName() {
        return "Cosine Similarity";
    }
}
