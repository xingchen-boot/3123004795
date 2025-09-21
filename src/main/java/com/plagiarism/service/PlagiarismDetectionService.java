package com.plagiarism.service;

import com.plagiarism.algorithm.SimilarityAlgorithm;
import com.plagiarism.algorithm.impl.CosineSimilarity;
import com.plagiarism.algorithm.impl.JaccardSimilarity;
import com.plagiarism.algorithm.impl.LevenshteinSimilarity;
import org.apache.commons.io.FileUtils;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

/**
 * 论文查重服务类
 * 提供多种相似度算法进行文本查重
 * 
 * @author 学生
 * @version 1.0.0
 */
@Service
public class PlagiarismDetectionService {
    
    private final List<SimilarityAlgorithm> algorithms;
    
    public PlagiarismDetectionService() {
        this.algorithms = new ArrayList<>();
        // 初始化多种相似度算法
        this.algorithms.add(new CosineSimilarity());
        this.algorithms.add(new LevenshteinSimilarity());
        this.algorithms.add(new JaccardSimilarity());
    }
    
    /**
     * 计算两个文件的相似度
     * 
     * @param originalFilePath 原文文件路径
     * @param plagiarizedFilePath 抄袭文件路径
     * @return 相似度值，范围[0, 1]
     * @throws IOException 文件读取异常
     */
    public double calculateSimilarityFromFiles(String originalFilePath, String plagiarizedFilePath) throws IOException {
        String originalText = readFileContent(originalFilePath);
        String plagiarizedText = readFileContent(plagiarizedFilePath);
        
        return calculateSimilarity(originalText, plagiarizedText);
    }
    
    
    /**
     * 计算两个文本的相似度
     * 使用多种算法计算并取平均值
     * 
     * @param originalText 原文
     * @param plagiarizedText 抄袭文本
     * @return 相似度值，范围[0, 1]
     */
    public double calculateSimilarity(String originalText, String plagiarizedText) {
        if (originalText == null || plagiarizedText == null) {
            return 0.0;
        }
        
        if (originalText.trim().isEmpty() || plagiarizedText.trim().isEmpty()) {
            return 0.0;
        }
        
        if (originalText.equals(plagiarizedText)) {
            return 1.0;
        }
        
        // 使用多种算法计算相似度
        double totalSimilarity = 0.0;
        int validAlgorithms = 0;
        
        for (SimilarityAlgorithm algorithm : algorithms) {
            try {
                double similarity = algorithm.calculateSimilarity(originalText, plagiarizedText);
                totalSimilarity += similarity;
                validAlgorithms++;
            } catch (Exception e) {
                // 如果某个算法计算失败，跳过该算法
                System.err.println("算法 " + algorithm.getAlgorithmName() + " 计算失败: " + e.getMessage());
            }
        }
        
        if (validAlgorithms == 0) {
            return 0.0;
        }
        
        return totalSimilarity / validAlgorithms;
    }
    
    /**
     * 使用指定算法计算相似度
     * 
     * @param originalText 原文
     * @param plagiarizedText 抄袭文本
     * @param algorithmName 算法名称
     * @return 相似度值，范围[0, 1]
     */
    public double calculateSimilarityWithAlgorithm(String originalText, String plagiarizedText, String algorithmName) {
        for (SimilarityAlgorithm algorithm : algorithms) {
            if (algorithm.getAlgorithmName().equals(algorithmName)) {
                return algorithm.calculateSimilarity(originalText, plagiarizedText);
            }
        }
        
        throw new IllegalArgumentException("未找到算法: " + algorithmName);
    }
    
    /**
     * 获取所有可用的算法名称
     * 
     * @return 算法名称列表
     */
    public List<String> getAvailableAlgorithms() {
        List<String> algorithmNames = new ArrayList<>();
        for (SimilarityAlgorithm algorithm : algorithms) {
            algorithmNames.add(algorithm.getAlgorithmName());
        }
        return algorithmNames;
    }
    
    /**
     * 读取文件内容
     * 
     * @param filePath 文件路径
     * @return 文件内容
     * @throws IOException 文件读取异常
     */
    private String readFileContent(String filePath) throws IOException {
        File file = new File(filePath);
        if (!file.exists()) {
            throw new IOException("文件不存在: " + filePath);
        }
        
        if (!file.isFile()) {
            throw new IOException("路径不是文件: " + filePath);
        }
        
        return FileUtils.readFileToString(file, StandardCharsets.UTF_8);
    }
    
    /**
     * 将相似度值格式化为百分比字符串
     * 
     * @param similarity 相似度值
     * @return 格式化的百分比字符串
     */
    public String formatSimilarityAsPercentage(double similarity) {
        return String.format("%.2f", similarity * 100) + "%";
    }
    
    /**
     * 将相似度值格式化为两位小数的字符串
     * 
     * @param similarity 相似度值
     * @return 格式化的字符串
     */
    public String formatSimilarity(double similarity) {
        return String.format("%.2f", similarity);
    }
}
