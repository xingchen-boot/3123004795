package com.plagiarism;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;

/**
 * 简化的命令行入口类 - 不依赖外部库
 * 支持命令行参数进行论文查重
 * 
 * @author 学生
 * @version 1.0.0
 */
public class SimpleCommandLineMain {
    
    public static void main(String[] args) {
        // 检查命令行参数
        if (args.length != 3) {
            System.err.println("使用方法: java -jar main.jar <原文文件> <抄袭文件> <输出文件>");
            System.err.println("示例: java -jar main.jar orig.txt orig_add.txt result.txt");
            System.exit(1);
        }
        
        String originalFilePath = args[0];
        String plagiarizedFilePath = args[1];
        String outputFilePath = args[2];
        
        try {
            // 读取文件内容
            String originalText = readFileContent(originalFilePath);
            String plagiarizedText = readFileContent(plagiarizedFilePath);
            
            // 计算相似度（使用简化的余弦相似度算法）
            double similarity = calculateCosineSimilarity(originalText, plagiarizedText);
            
            // 输出结果到文件
            writeResultToFile(outputFilePath, similarity);
            
            // 输出到控制台（可选，用于调试）
            System.out.println(String.format("%.2f", similarity));
            
        } catch (Exception e) {
            System.err.println("错误: " + e.getMessage());
            System.exit(1);
        }
    }
    
    /**
     * 读取文件内容
     * 
     * @param filePath 文件路径
     * @return 文件内容
     * @throws IOException 文件读取异常
     */
    private static String readFileContent(String filePath) throws IOException {
        try {
            return new String(Files.readAllBytes(Paths.get(filePath)), StandardCharsets.UTF_8);
        } catch (IOException e) {
            throw new IOException("无法读取文件: " + filePath + " - " + e.getMessage());
        }
    }
    
    /**
     * 将结果写入文件
     * 
     * @param outputFilePath 输出文件路径
     * @param similarity 相似度值
     * @throws IOException 文件写入异常
     */
    private static void writeResultToFile(String outputFilePath, double similarity) throws IOException {
        try {
            // 确保输出目录存在
            File outputFile = new File(outputFilePath);
            File parentDir = outputFile.getParentFile();
            if (parentDir != null && !parentDir.exists()) {
                parentDir.mkdirs();
            }
            
            // 写入结果
            try (FileWriter writer = new FileWriter(outputFile, StandardCharsets.UTF_8)) {
                writer.write(String.format("%.2f", similarity));
            }
        } catch (IOException e) {
            throw new IOException("无法写入文件: " + outputFilePath + " - " + e.getMessage());
        }
    }
    
    /**
     * 简化的余弦相似度计算（不依赖外部库）
     * 
     * @param text1 第一个文本
     * @param text2 第二个文本
     * @return 相似度值
     */
    private static double calculateCosineSimilarity(String text1, String text2) {
        // 处理null和空字符串
        if (text1 == null) text1 = "";
        if (text2 == null) text2 = "";
        
        if (text1.isEmpty() && text2.isEmpty()) {
            return 0.0;
        }
        
        if (text1.isEmpty() || text2.isEmpty()) {
            return 0.0;
        }
        
        // 预处理文本：转小写，去除标点符号
        text1 = preprocessText(text1);
        text2 = preprocessText(text2);
        
        // 分词
        List<String> words1 = tokenize(text1);
        List<String> words2 = tokenize(text2);
        
        if (words1.isEmpty() && words2.isEmpty()) {
            return 0.0;
        }
        
        if (words1.isEmpty() || words2.isEmpty()) {
            return 0.0;
        }
        
        // 构建词频向量
        Map<String, Integer> vector1 = buildWordVector(words1);
        Map<String, Integer> vector2 = buildWordVector(words2);
        
        // 计算余弦相似度
        return computeCosineSimilarity(vector1, vector2);
    }
    
    /**
     * 预处理文本
     */
    private static String preprocessText(String text) {
        return text.toLowerCase()
                .replaceAll("[\\p{Punct}\\p{IsPunctuation}]", " ")
                .replaceAll("\\s+", " ")
                .trim();
    }
    
    /**
     * 分词
     */
    private static List<String> tokenize(String text) {
        List<String> words = new ArrayList<>();
        String[] tokens = text.split("\\s+");
        for (String token : tokens) {
            if (!token.isEmpty()) {
                words.add(token);
            }
        }
        return words;
    }
    
    /**
     * 构建词频向量
     */
    private static Map<String, Integer> buildWordVector(List<String> words) {
        Map<String, Integer> vector = new HashMap<>();
        for (String word : words) {
            vector.put(word, vector.getOrDefault(word, 0) + 1);
        }
        return vector;
    }
    
    /**
     * 计算余弦相似度
     */
    private static double computeCosineSimilarity(Map<String, Integer> vector1, Map<String, Integer> vector2) {
        // 获取所有词汇
        Set<String> allWords = new HashSet<>();
        allWords.addAll(vector1.keySet());
        allWords.addAll(vector2.keySet());
        
        // 计算点积
        double dotProduct = 0.0;
        for (String word : allWords) {
            int freq1 = vector1.getOrDefault(word, 0);
            int freq2 = vector2.getOrDefault(word, 0);
            dotProduct += freq1 * freq2;
        }
        
        // 计算向量的模长
        double norm1 = 0.0;
        for (int freq : vector1.values()) {
            norm1 += freq * freq;
        }
        norm1 = Math.sqrt(norm1);
        
        double norm2 = 0.0;
        for (int freq : vector2.values()) {
            norm2 += freq * freq;
        }
        norm2 = Math.sqrt(norm2);
        
        // 避免除零
        if (norm1 == 0.0 || norm2 == 0.0) {
            return 0.0;
        }
        
        return dotProduct / (norm1 * norm2);
    }
}
