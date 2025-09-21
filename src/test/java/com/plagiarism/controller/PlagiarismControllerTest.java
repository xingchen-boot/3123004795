package com.plagiarism.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.plagiarism.service.PlagiarismDetectionService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * 论文查重控制器测试类
 * 
 * @author 学生
 * @version 1.0.0
 */
@WebMvcTest(PlagiarismController.class)
@DisplayName("论文查重控制器测试")
class PlagiarismControllerTest {
    
    @Autowired
    private MockMvc mockMvc;
    
    @MockBean
    private PlagiarismDetectionService plagiarismDetectionService;
    
    @Autowired
    private ObjectMapper objectMapper;
    
    @BeforeEach
    void setUp() throws IOException {
        // 设置默认的模拟行为
        when(plagiarismDetectionService.calculateSimilarity(anyString(), anyString()))
            .thenReturn(0.85);
        when(plagiarismDetectionService.formatSimilarity(0.85))
            .thenReturn("0.85");
        when(plagiarismDetectionService.formatSimilarityAsPercentage(0.85))
            .thenReturn("85.00%");
    }
    
    @Test
    @DisplayName("测试文本相似度计算API")
    void testCalculateSimilarity() throws Exception {
        PlagiarismController.SimilarityRequest request = new PlagiarismController.SimilarityRequest();
        request.setOriginalText("这是原文");
        request.setPlagiarizedText("这是抄袭文本");
        
        mockMvc.perform(post("/api/similarity")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.similarity").value("0.85"))
                .andExpect(jsonPath("$.similarityPercentage").value("85.00%"))
                .andExpect(jsonPath("$.message").value("相似度计算成功"));
    }
    
    @Test
    @DisplayName("测试指定算法计算相似度API")
    void testCalculateSimilarityWithAlgorithm() throws Exception {
        PlagiarismController.AlgorithmSimilarityRequest request = new PlagiarismController.AlgorithmSimilarityRequest();
        request.setOriginalText("这是原文");
        request.setPlagiarizedText("这是抄袭文本");
        request.setAlgorithmName("Cosine Similarity");
        
        when(plagiarismDetectionService.calculateSimilarityWithAlgorithm(anyString(), anyString(), anyString()))
            .thenReturn(0.90);
        when(plagiarismDetectionService.formatSimilarity(0.90))
            .thenReturn("0.90");
        when(plagiarismDetectionService.formatSimilarityAsPercentage(0.90))
            .thenReturn("90.00%");
        
        mockMvc.perform(post("/api/similarity/algorithm")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.similarity").value("0.90"))
                .andExpect(jsonPath("$.similarityPercentage").value("90.00%"))
                .andExpect(jsonPath("$.algorithm").value("Cosine Similarity"))
                .andExpect(jsonPath("$.message").value("相似度计算成功"));
    }
    
    @Test
    @DisplayName("测试获取算法列表API")
    void testGetAvailableAlgorithms() throws Exception {
        List<String> algorithms = Arrays.asList("Cosine Similarity", "Levenshtein Distance", "Jaccard Similarity");
        when(plagiarismDetectionService.getAvailableAlgorithms()).thenReturn(algorithms);
        
        mockMvc.perform(get("/api/algorithms"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.algorithms").isArray())
                .andExpect(jsonPath("$.algorithms[0]").value("Cosine Similarity"))
                .andExpect(jsonPath("$.algorithms[1]").value("Levenshtein Distance"))
                .andExpect(jsonPath("$.algorithms[2]").value("Jaccard Similarity"))
                .andExpect(jsonPath("$.message").value("获取算法列表成功"));
    }
    
    @Test
    @DisplayName("测试健康检查API")
    void testHealthCheck() throws Exception {
        mockMvc.perform(get("/api/health"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value("UP"))
                .andExpect(jsonPath("$.service").value("Plagiarism Detection Service"))
                .andExpect(jsonPath("$.version").value("1.0.0"));
    }
    
    @Test
    @DisplayName("测试相似度计算异常处理")
    void testCalculateSimilarityException() throws Exception {
        when(plagiarismDetectionService.calculateSimilarity(anyString(), anyString()))
            .thenThrow(new RuntimeException("计算失败"));
        
        PlagiarismController.SimilarityRequest request = new PlagiarismController.SimilarityRequest();
        request.setOriginalText("这是原文");
        request.setPlagiarizedText("这是抄袭文本");
        
        mockMvc.perform(post("/api/similarity")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.success").value(false))
                .andExpect(jsonPath("$.error").value("计算失败"))
                .andExpect(jsonPath("$.message").value("相似度计算失败"));
    }
    
    @Test
    @DisplayName("测试不存在的算法异常处理")
    void testNonExistentAlgorithmException() throws Exception {
        when(plagiarismDetectionService.calculateSimilarityWithAlgorithm(anyString(), anyString(), anyString()))
            .thenThrow(new IllegalArgumentException("未找到算法: 不存在的算法"));
        
        PlagiarismController.AlgorithmSimilarityRequest request = new PlagiarismController.AlgorithmSimilarityRequest();
        request.setOriginalText("这是原文");
        request.setPlagiarizedText("这是抄袭文本");
        request.setAlgorithmName("不存在的算法");
        
        mockMvc.perform(post("/api/similarity/algorithm")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.success").value(false))
                .andExpect(jsonPath("$.error").value("未找到算法: 不存在的算法"))
                .andExpect(jsonPath("$.message").value("相似度计算失败"));
    }
    
    @Test
    @DisplayName("测试获取算法列表异常处理")
    void testGetAvailableAlgorithmsException() throws Exception {
        when(plagiarismDetectionService.getAvailableAlgorithms())
            .thenThrow(new RuntimeException("获取算法列表失败"));
        
        mockMvc.perform(get("/api/algorithms"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.success").value(false))
                .andExpect(jsonPath("$.error").value("获取算法列表失败"))
                .andExpect(jsonPath("$.message").value("获取算法列表失败"));
    }
}
