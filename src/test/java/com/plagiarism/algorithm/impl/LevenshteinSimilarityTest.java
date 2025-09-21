package com.plagiarism.algorithm.impl;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import static org.junit.jupiter.api.Assertions.*;

/**
 * 编辑距离相似度算法测试类
 * 
 * @author 学生
 * @version 1.0.0
 */
@DisplayName("编辑距离相似度算法测试")
class LevenshteinSimilarityTest {
    
    private LevenshteinSimilarity levenshteinSimilarity;
    
    @BeforeEach
    void setUp() {
        levenshteinSimilarity = new LevenshteinSimilarity();
    }
    
    @Test
    @DisplayName("测试相同文本的相似度")
    void testIdenticalTexts() {
        String text = "这是一个测试文本";
        double similarity = levenshteinSimilarity.calculateSimilarity(text, text);
        assertEquals(1.0, similarity, 0.001, "相同文本的相似度应该为1.0");
    }
    
    @Test
    @DisplayName("测试完全不同文本的相似度")
    void testCompletelyDifferentTexts() {
        String text1 = "这是第一个文本";
        String text2 = "那是第二个文本";
        double similarity = levenshteinSimilarity.calculateSimilarity(text1, text2);
        assertTrue(similarity >= 0.0 && similarity <= 1.0, "相似度应该在[0,1]范围内");
    }
    
    @Test
    @DisplayName("测试空文本的相似度")
    void testEmptyTexts() {
        double similarity1 = levenshteinSimilarity.calculateSimilarity("", "");
        assertEquals(1.0, similarity1, 0.001, "两个空文本的相似度应该为1.0");
        
        double similarity2 = levenshteinSimilarity.calculateSimilarity("有内容", "");
        assertEquals(0.0, similarity2, 0.001, "一个空文本的相似度应该为0.0");
        
        double similarity3 = levenshteinSimilarity.calculateSimilarity("", "有内容");
        assertEquals(0.0, similarity3, 0.001, "一个空文本的相似度应该为0.0");
    }
    
    @Test
    @DisplayName("测试null文本的相似度")
    void testNullTexts() {
        double similarity1 = levenshteinSimilarity.calculateSimilarity(null, null);
        assertEquals(0.0, similarity1, 0.001, "两个null文本的相似度应该为0.0");
        
        double similarity2 = levenshteinSimilarity.calculateSimilarity("有内容", null);
        assertEquals(0.0, similarity2, 0.001, "一个null文本的相似度应该为0.0");
        
        double similarity3 = levenshteinSimilarity.calculateSimilarity(null, "有内容");
        assertEquals(0.0, similarity3, 0.001, "一个null文本的相似度应该为0.0");
    }
    
    @Test
    @DisplayName("测试单字符差异")
    void testSingleCharacterDifference() {
        String text1 = "Hello World";
        String text2 = "Hello World!";
        double similarity = levenshteinSimilarity.calculateSimilarity(text1, text2);
        assertTrue(similarity > 0.9, "单字符差异应该有很高的相似度");
    }
    
    @Test
    @DisplayName("测试字符顺序差异")
    void testCharacterOrderDifference() {
        String text1 = "Hello World";
        String text2 = "World Hello";
        double similarity = levenshteinSimilarity.calculateSimilarity(text1, text2);
        assertTrue(similarity < 0.5, "字符顺序差异应该有较低的相似度");
    }
    
    @Test
    @DisplayName("测试算法名称")
    void testAlgorithmName() {
        assertEquals("Levenshtein Distance", levenshteinSimilarity.getAlgorithmName());
    }
    
    @Test
    @DisplayName("测试中文字符")
    void testChineseCharacters() {
        String text1 = "你好世界";
        String text2 = "你好地球";
        double similarity = levenshteinSimilarity.calculateSimilarity(text1, text2);
        assertTrue(similarity > 0.5, "中文字符的相似度应该合理");
    }
    
    @Test
    @DisplayName("测试长文本性能")
    void testLongTextPerformance() {
        StringBuilder sb1 = new StringBuilder();
        StringBuilder sb2 = new StringBuilder();
        
        // 构建两个相似的长文本
        for (int i = 0; i < 50; i++) {
            sb1.append("这是第").append(i).append("个句子。");
            sb2.append("这是第").append(i).append("个句子。");
        }
        
        // 在第二个文本中修改一些字符
        sb2.setCharAt(10, '那');
        
        long startTime = System.currentTimeMillis();
        double similarity = levenshteinSimilarity.calculateSimilarity(sb1.toString(), sb2.toString());
        long endTime = System.currentTimeMillis();
        
        assertTrue(similarity > 0.9, "长文本的相似度应该很高");
        assertTrue(endTime - startTime < 1000, "长文本计算应该在1秒内完成");
    }
}
