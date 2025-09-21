package com.plagiarism.algorithm.impl;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import static org.junit.jupiter.api.Assertions.*;

/**
 * 余弦相似度算法测试类
 * 
 * @author 学生
 * @version 1.0.0
 */
@DisplayName("余弦相似度算法测试")
class CosineSimilarityTest {
    
    private CosineSimilarity cosineSimilarity;
    
    @BeforeEach
    void setUp() {
        cosineSimilarity = new CosineSimilarity();
    }
    
    @Test
    @DisplayName("测试相同文本的相似度")
    void testIdenticalTexts() {
        String text = "这是一个测试文本";
        double similarity = cosineSimilarity.calculateSimilarity(text, text);
        assertEquals(1.0, similarity, 0.001, "相同文本的相似度应该为1.0");
    }
    
    @Test
    @DisplayName("测试完全不同文本的相似度")
    void testCompletelyDifferentTexts() {
        String text1 = "这是第一个文本";
        String text2 = "那是第二个文本";
        double similarity = cosineSimilarity.calculateSimilarity(text1, text2);
        assertTrue(similarity >= 0.0 && similarity <= 1.0, "相似度应该在[0,1]范围内");
    }
    
    @Test
    @DisplayName("测试空文本的相似度")
    void testEmptyTexts() {
        double similarity1 = cosineSimilarity.calculateSimilarity("", "");
        assertEquals(0.0, similarity1, 0.001, "两个空文本的相似度应该为0.0");
        
        double similarity2 = cosineSimilarity.calculateSimilarity("有内容", "");
        assertEquals(0.0, similarity2, 0.001, "一个空文本的相似度应该为0.0");
        
        double similarity3 = cosineSimilarity.calculateSimilarity("", "有内容");
        assertEquals(0.0, similarity3, 0.001, "一个空文本的相似度应该为0.0");
    }
    
    @Test
    @DisplayName("测试null文本的相似度")
    void testNullTexts() {
        double similarity1 = cosineSimilarity.calculateSimilarity(null, null);
        assertEquals(0.0, similarity1, 0.001, "两个null文本的相似度应该为0.0");
        
        double similarity2 = cosineSimilarity.calculateSimilarity("有内容", null);
        assertEquals(0.0, similarity2, 0.001, "一个null文本的相似度应该为0.0");
        
        double similarity3 = cosineSimilarity.calculateSimilarity(null, "有内容");
        assertEquals(0.0, similarity3, 0.001, "一个null文本的相似度应该为0.0");
    }
    
    @Test
    @DisplayName("测试中英文混合文本")
    void testMixedLanguageTexts() {
        String text1 = "Hello 世界 123";
        String text2 = "Hello 世界 456";
        double similarity = cosineSimilarity.calculateSimilarity(text1, text2);
        assertTrue(similarity > 0.5, "中英文混合文本应该有较高的相似度");
    }
    
    @Test
    @DisplayName("测试标点符号处理")
    void testPunctuationHandling() {
        String text1 = "这是一个测试文本！";
        String text2 = "这是一个测试文本？";
        double similarity = cosineSimilarity.calculateSimilarity(text1, text2);
        assertTrue(similarity > 0.8, "去除标点符号后应该有很高的相似度");
    }
    
    @Test
    @DisplayName("测试大小写处理")
    void testCaseHandling() {
        String text1 = "Hello World";
        String text2 = "hello world";
        double similarity = cosineSimilarity.calculateSimilarity(text1, text2);
        assertEquals(1.0, similarity, 0.001, "大小写不同的相同文本相似度应该为1.0");
    }
    
    @Test
    @DisplayName("测试算法名称")
    void testAlgorithmName() {
        assertEquals("Cosine Similarity", cosineSimilarity.getAlgorithmName());
    }
    
    @Test
    @DisplayName("测试长文本相似度")
    void testLongTextSimilarity() {
        StringBuilder sb1 = new StringBuilder();
        StringBuilder sb2 = new StringBuilder();
        
        // 构建两个相似的长文本
        for (int i = 0; i < 100; i++) {
            sb1.append("这是第").append(i).append("个句子。");
            sb2.append("这是第").append(i).append("个句子。");
        }
        
        // 在第二个文本中添加一些不同的内容
        sb2.append("这是额外的内容。");
        
        double similarity = cosineSimilarity.calculateSimilarity(sb1.toString(), sb2.toString());
        System.out.println("长文本相似度: " + similarity);
        assertTrue(similarity > 0.5, "长文本的相似度应该较高");
    }
}
