package com.plagiarism.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.io.TempDir;
import static org.junit.jupiter.api.Assertions.*;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Path;
import java.util.List;

/**
 * 论文查重服务测试类
 * 
 * @author 学生
 * @version 1.0.0
 */
@DisplayName("论文查重服务测试")
class PlagiarismDetectionServiceTest {
    
    private PlagiarismDetectionService service;
    
    @BeforeEach
    void setUp() {
        service = new PlagiarismDetectionService();
    }
    
    @Test
    @DisplayName("测试文本相似度计算")
    void testCalculateSimilarity() throws IOException {
        String text1 = "这是一个测试文本";
        String text2 = "这是一个测试文本";
        
        double similarity = service.calculateSimilarity(text1, text2);
        assertEquals(1.0, similarity, 0.001, "相同文本的相似度应该为1.0");
    }
    
    @Test
    @DisplayName("测试文件相似度计算")
    void testCalculateSimilarityFromFiles(@TempDir Path tempDir) throws IOException {
        // 创建测试文件
        File file1 = tempDir.resolve("test1.txt").toFile();
        File file2 = tempDir.resolve("test2.txt").toFile();
        
        try (FileWriter writer1 = new FileWriter(file1);
             FileWriter writer2 = new FileWriter(file2)) {
            writer1.write("这是第一个测试文件的内容");
            writer2.write("这是第二个测试文件的内容");
        }
        
        double similarity = service.calculateSimilarity(file1.getAbsolutePath(), file2.getAbsolutePath());
        assertTrue(similarity >= 0.0 && similarity <= 1.0, "相似度应该在[0,1]范围内");
    }
    
    @Test
    @DisplayName("测试文件不存在异常")
    void testFileNotExists() {
        assertThrows(IOException.class, () -> {
            service.calculateSimilarityFromFiles("不存在的文件.txt", "另一个不存在的文件.txt");
        }, "文件不存在应该抛出IOException");
    }
    
    @Test
    @DisplayName("测试指定算法计算相似度")
    void testCalculateSimilarityWithAlgorithm() {
        String text1 = "Hello World";
        String text2 = "Hello World";
        
        double similarity = service.calculateSimilarityWithAlgorithm(text1, text2, "Cosine Similarity");
        assertEquals(1.0, similarity, 0.001, "使用指定算法计算相同文本的相似度应该为1.0");
    }
    
    @Test
    @DisplayName("测试不存在的算法")
    void testNonExistentAlgorithm() {
        String text1 = "Hello World";
        String text2 = "Hello World";
        
        assertThrows(IllegalArgumentException.class, () -> {
            service.calculateSimilarityWithAlgorithm(text1, text2, "不存在的算法");
        }, "不存在的算法应该抛出IllegalArgumentException");
    }
    
    @Test
    @DisplayName("测试获取可用算法列表")
    void testGetAvailableAlgorithms() {
        List<String> algorithms = service.getAvailableAlgorithms();
        assertNotNull(algorithms, "算法列表不应该为null");
        assertFalse(algorithms.isEmpty(), "算法列表不应该为空");
        assertTrue(algorithms.contains("Cosine Similarity"), "应该包含余弦相似度算法");
        assertTrue(algorithms.contains("Levenshtein Distance"), "应该包含编辑距离算法");
        assertTrue(algorithms.contains("Jaccard Similarity"), "应该包含Jaccard相似度算法");
    }
    
    @Test
    @DisplayName("测试相似度格式化")
    void testFormatSimilarity() {
        double similarity = 0.123456;
        String formatted = service.formatSimilarity(similarity);
        assertEquals("0.12", formatted, "相似度应该格式化为两位小数");
    }
    
    @Test
    @DisplayName("测试相似度百分比格式化")
    void testFormatSimilarityAsPercentage() {
        double similarity = 0.123456;
        String formatted = service.formatSimilarityAsPercentage(similarity);
        assertEquals("12.35%", formatted, "相似度应该格式化为百分比");
    }
    
    @Test
    @DisplayName("测试空文本处理")
    void testEmptyTextHandling() throws IOException {
        double similarity1 = service.calculateSimilarity("", "");
        assertEquals(0.0, similarity1, 0.001, "两个空文本的相似度应该为0.0");
        
        double similarity2 = service.calculateSimilarity("有内容", "");
        assertEquals(0.0, similarity2, 0.001, "一个空文本的相似度应该为0.0");
    }
    
    @Test
    @DisplayName("测试null文本处理")
    void testNullTextHandling() throws IOException {
        double similarity1 = service.calculateSimilarity(null, null);
        assertEquals(0.0, similarity1, 0.001, "两个null文本的相似度应该为0.0");
        
        double similarity2 = service.calculateSimilarity("有内容", null);
        assertEquals(0.0, similarity2, 0.001, "一个null文本的相似度应该为0.0");
    }
    
    @Test
    @DisplayName("测试长文本性能")
    void testLongTextPerformance() throws IOException {
        StringBuilder sb1 = new StringBuilder();
        StringBuilder sb2 = new StringBuilder();
        
        // 构建两个相似的长文本
        for (int i = 0; i < 100; i++) {
            sb1.append("这是第").append(i).append("个句子。");
            sb2.append("这是第").append(i).append("个句子。");
        }
        
        long startTime = System.currentTimeMillis();
        double similarity = service.calculateSimilarity(sb1.toString(), sb2.toString());
        long endTime = System.currentTimeMillis();
        
        assertEquals(1.0, similarity, 0.001, "长文本的相似度应该为1.0");
        assertTrue(endTime - startTime < 2000, "长文本计算应该在2秒内完成");
    }
}
