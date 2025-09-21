package com.plagiarism.algorithm.impl;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Jaccard相似度算法测试类
 * 
 * @author 学生
 * @version 1.0.0
 */
@DisplayName("Jaccard相似度算法测试")
class JaccardSimilarityTest {
    
    private JaccardSimilarity jaccardSimilarity;
    
    @BeforeEach
    void setUp() {
        jaccardSimilarity = new JaccardSimilarity();
    }
    
    @Test
    @DisplayName("测试相同文本的相似度")
    void testIdenticalTexts() {
        String text = "这是一个测试文本";
        double similarity = jaccardSimilarity.calculateSimilarity(text, text);
        assertEquals(1.0, similarity, 0.001, "相同文本的相似度应该为1.0");
    }
    
    @Test
    @DisplayName("测试完全不同文本的相似度")
    void testCompletelyDifferentTexts() {
        String text1 = "这是第一个文本";
        String text2 = "那是第二个文本";
        double similarity = jaccardSimilarity.calculateSimilarity(text1, text2);
        assertTrue(similarity >= 0.0 && similarity <= 1.0, "相似度应该在[0,1]范围内");
    }
    
    @Test
    @DisplayName("测试空文本的相似度")
    void testEmptyTexts() {
        double similarity1 = jaccardSimilarity.calculateSimilarity("", "");
        assertEquals(1.0, similarity1, 0.001, "两个空文本的相似度应该为1.0");
        
        double similarity2 = jaccardSimilarity.calculateSimilarity("有内容", "");
        assertEquals(0.0, similarity2, 0.001, "一个空文本的相似度应该为0.0");
        
        double similarity3 = jaccardSimilarity.calculateSimilarity("", "有内容");
        assertEquals(0.0, similarity3, 0.001, "一个空文本的相似度应该为0.0");
    }
    
    @Test
    @DisplayName("测试null文本的相似度")
    void testNullTexts() {
        double similarity1 = jaccardSimilarity.calculateSimilarity(null, null);
        assertEquals(0.0, similarity1, 0.001, "两个null文本的相似度应该为0.0");
        
        double similarity2 = jaccardSimilarity.calculateSimilarity("有内容", null);
        assertEquals(0.0, similarity2, 0.001, "一个null文本的相似度应该为0.0");
        
        double similarity3 = jaccardSimilarity.calculateSimilarity(null, "有内容");
        assertEquals(0.0, similarity3, 0.001, "一个null文本的相似度应该为0.0");
    }
    
    @Test
    @DisplayName("测试部分重叠的字符集合")
    void testPartiallyOverlappingCharacterSets() {
        String text1 = "Hello World";
        String text2 = "Hello Earth";
        double similarity = jaccardSimilarity.calculateSimilarity(text1, text2);
        assertTrue(similarity > 0.0 && similarity < 1.0, "部分重叠的文本应该有中等相似度");
    }
    
    @Test
    @DisplayName("测试标点符号处理")
    void testPunctuationHandling() {
        String text1 = "Hello, World!";
        String text2 = "Hello World";
        double similarity = jaccardSimilarity.calculateSimilarity(text1, text2);
        assertEquals(1.0, similarity, 0.001, "去除标点符号后应该完全相同");
    }
    
    @Test
    @DisplayName("测试大小写处理")
    void testCaseHandling() {
        String text1 = "Hello World";
        String text2 = "hello world";
        double similarity = jaccardSimilarity.calculateSimilarity(text1, text2);
        assertEquals(1.0, similarity, 0.001, "大小写不同的相同文本相似度应该为1.0");
    }
    
    @Test
    @DisplayName("测试算法名称")
    void testAlgorithmName() {
        assertEquals("Jaccard Similarity", jaccardSimilarity.getAlgorithmName());
    }
    
    @Test
    @DisplayName("测试中文字符")
    void testChineseCharacters() {
        String text1 = "你好世界";
        String text2 = "你好地球";
        double similarity = jaccardSimilarity.calculateSimilarity(text1, text2);
        assertTrue(similarity > 0.0 && similarity < 1.0, "中文字符的相似度应该合理");
    }
    
    @Test
    @DisplayName("测试数字字符")
    void testNumericCharacters() {
        String text1 = "123456";
        String text2 = "123789";
        double similarity = jaccardSimilarity.calculateSimilarity(text1, text2);
        assertTrue(similarity > 0.0 && similarity < 1.0, "数字字符的相似度应该合理");
    }
    
    @Test
    @DisplayName("测试混合字符")
    void testMixedCharacters() {
        String text1 = "Hello123世界";
        String text2 = "Hello456世界";
        double similarity = jaccardSimilarity.calculateSimilarity(text1, text2);
        assertTrue(similarity > 0.0 && similarity < 1.0, "混合字符的相似度应该合理");
    }
}
