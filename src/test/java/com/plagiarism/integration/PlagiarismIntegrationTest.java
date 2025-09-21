package com.plagiarism.integration;

import com.plagiarism.service.PlagiarismDetectionService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.io.TempDir;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Path;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 * 论文查重系统集成测试
 * 测试整个系统的功能
 * 
 * @author 学生
 * @version 1.0.0
 */
@SpringBootTest
@ActiveProfiles("test")
@DisplayName("论文查重系统集成测试")
class PlagiarismIntegrationTest {
    
    @Autowired
    private PlagiarismDetectionService plagiarismDetectionService;
    
    @Test
    @DisplayName("测试完整查重流程")
    void testCompletePlagiarismDetectionFlow(@TempDir Path tempDir) throws IOException {
        // 创建测试文件
        File originalFile = tempDir.resolve("original.txt").toFile();
        File plagiarizedFile = tempDir.resolve("plagiarized.txt").toFile();
        
        // 写入测试内容
        try (FileWriter writer1 = new FileWriter(originalFile);
             FileWriter writer2 = new FileWriter(plagiarizedFile)) {
            writer1.write("这是一篇关于人工智能的论文。人工智能是计算机科学的一个分支，它企图了解智能的实质，并生产出一种新的能以人类智能相似的方式做出反应的智能机器。");
            writer2.write("这是一篇关于AI的论文。AI是计算机科学的一个分支，它企图了解智能的实质，并生产出一种新的能以人类智能相似的方式做出反应的智能机器。");
        }
        
        // 测试文件查重
        double similarity = plagiarismDetectionService.calculateSimilarity(
            originalFile.getAbsolutePath(), 
            plagiarizedFile.getAbsolutePath()
        );
        
        assertTrue(similarity > 0.7, "相似度应该较高");
        assertTrue(similarity < 1.0, "相似度不应该为1.0");
        
        // 测试文本查重
        String originalText = "这是原文内容";
        String plagiarizedText = "这是抄袭内容";
        
        double textSimilarity = plagiarismDetectionService.calculateSimilarity(originalText, plagiarizedText);
        assertTrue(textSimilarity >= 0.0 && textSimilarity <= 1.0, "相似度应该在[0,1]范围内");
    }
    
    @Test
    @DisplayName("测试不同算法的结果一致性")
    void testAlgorithmConsistency() {
        String text1 = "这是第一个测试文本，包含一些中文内容。";
        String text2 = "这是第二个测试文本，包含一些中文内容。";
        
        // 测试所有算法
        List<String> algorithms = plagiarismDetectionService.getAvailableAlgorithms();
        assertFalse(algorithms.isEmpty(), "应该有可用的算法");
        
        for (String algorithm : algorithms) {
            double similarity = plagiarismDetectionService.calculateSimilarityWithAlgorithm(text1, text2, algorithm);
            assertTrue(similarity >= 0.0 && similarity <= 1.0, 
                "算法 " + algorithm + " 的相似度应该在[0,1]范围内");
        }
    }
    
    @Test
    @DisplayName("测试性能要求")
    void testPerformanceRequirements() {
        StringBuilder sb1 = new StringBuilder();
        StringBuilder sb2 = new StringBuilder();
        
        // 构建长文本（约1000个字符）
        for (int i = 0; i < 50; i++) {
            sb1.append("这是第").append(i).append("个句子，用于测试系统性能。");
            sb2.append("这是第").append(i).append("个句子，用于测试系统性能。");
        }
        
        long startTime = System.currentTimeMillis();
        double similarity = plagiarismDetectionService.calculateSimilarity(sb1.toString(), sb2.toString());
        long endTime = System.currentTimeMillis();
        
        long executionTime = endTime - startTime;
        
        assertEquals(1.0, similarity, 0.001, "相同文本的相似度应该为1.0");
        assertTrue(executionTime < 1000, "计算时间应该在1秒内完成，实际用时: " + executionTime + "ms");
    }
    
    @Test
    @DisplayName("测试边界情况")
    void testBoundaryConditions() throws IOException {
        // 测试空文本
        double similarity1 = plagiarismDetectionService.calculateSimilarity("", "");
        assertEquals(0.0, similarity1, 0.001, "两个空文本的相似度应该为0.0");
        
        // 测试null文本
        double similarity2 = plagiarismDetectionService.calculateSimilarity(null, null);
        assertEquals(0.0, similarity2, 0.001, "两个null文本的相似度应该为0.0");
        
        // 测试单字符文本
        double similarity3 = plagiarismDetectionService.calculateSimilarity("a", "a");
        assertEquals(1.0, similarity3, 0.001, "相同单字符的相似度应该为1.0");
        
        // 测试完全不同字符
        double similarity4 = plagiarismDetectionService.calculateSimilarity("a", "b");
        assertTrue(similarity4 >= 0.0 && similarity4 <= 1.0, "不同字符的相似度应该在[0,1]范围内");
    }
    
    @Test
    @DisplayName("测试中英文混合文本")
    void testMixedLanguageText() throws IOException {
        String text1 = "Hello 世界 123";
        String text2 = "Hello 世界 456";
        
        double similarity = plagiarismDetectionService.calculateSimilarity(text1, text2);
        assertTrue(similarity > 0.5, "中英文混合文本应该有较高的相似度");
        assertTrue(similarity < 1.0, "中英文混合文本的相似度不应该为1.0");
    }
    
    @Test
    @DisplayName("测试标点符号处理")
    void testPunctuationHandling() throws IOException {
        String text1 = "这是一个测试文本！";
        String text2 = "这是一个测试文本？";
        
        double similarity = plagiarismDetectionService.calculateSimilarity(text1, text2);
        assertTrue(similarity > 0.8, "去除标点符号后应该有很高的相似度");
    }
    
    @Test
    @DisplayName("测试格式化功能")
    void testFormattingFunctions() {
        double similarity = 0.123456;
        
        String formatted = plagiarismDetectionService.formatSimilarity(similarity);
        assertEquals("0.12", formatted, "相似度应该格式化为两位小数");
        
        String percentage = plagiarismDetectionService.formatSimilarityAsPercentage(similarity);
        assertEquals("12.35%", percentage, "相似度应该格式化为百分比");
    }
}
