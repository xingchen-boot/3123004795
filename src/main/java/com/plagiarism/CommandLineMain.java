package com.plagiarism;

import com.plagiarism.algorithm.impl.CosineSimilarity;
import com.plagiarism.algorithm.impl.JaccardSimilarity;
import com.plagiarism.algorithm.impl.LevenshteinSimilarity;
import com.plagiarism.algorithm.SimilarityAlgorithm;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;

/**
 * 命令行入口类 - 独立版本
 * 支持命令行参数进行论文查重，不依赖Spring Boot
 * 
 * @author 学生
 * @version 1.0.0
 */
public class CommandLineMain {
    
    private static final SimilarityAlgorithm DEFAULT_ALGORITHM = new CosineSimilarity();
    
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
            
            // 计算相似度
            double similarity = DEFAULT_ALGORITHM.calculateSimilarity(originalText, plagiarizedText);
            
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
}
