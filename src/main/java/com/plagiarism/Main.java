package com.plagiarism;

import com.plagiarism.service.PlagiarismDetectionService;
import org.springframework.boot.SpringApplication;
import org.springframework.context.ConfigurableApplicationContext;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

/**
 * 命令行入口类
 * 支持命令行参数进行论文查重
 * 
 * @author 学生
 * @version 1.0.0
 */
public class Main {
    
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
            // 启动Spring Boot应用
            ConfigurableApplicationContext context = SpringApplication.run(PlagiarismDetectorApplication.class);
            PlagiarismDetectionService service = context.getBean(PlagiarismDetectionService.class);
            
            // 计算相似度
            double similarity = service.calculateSimilarityFromFiles(originalFilePath, plagiarizedFilePath);
            
            // 输出结果到文件
            writeResultToFile(outputFilePath, similarity);
            
            // 输出到控制台
            System.out.println("相似度计算完成:");
            System.out.println("原文文件: " + originalFilePath);
            System.out.println("抄袭文件: " + plagiarizedFilePath);
            System.out.println("相似度: " + service.formatSimilarity(similarity));
            System.out.println("结果已保存到: " + outputFilePath);
            
            // 关闭应用
            context.close();
            
        } catch (Exception e) {
            System.err.println("错误: " + e.getMessage());
            e.printStackTrace();
            System.exit(1);
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
        File outputFile = new File(outputFilePath);
        
        // 确保输出目录存在
        File parentDir = outputFile.getParentFile();
        if (parentDir != null && !parentDir.exists()) {
            parentDir.mkdirs();
        }
        
        // 写入结果
        try (FileWriter writer = new FileWriter(outputFile, false)) {
            writer.write(String.format("%.2f", similarity));
        }
    }
}
