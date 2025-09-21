package com.plagiarism.controller;

import com.plagiarism.service.PlagiarismDetectionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 论文查重控制器
 * 提供Web API接口
 * 
 * @author 学生
 * @version 1.0.0
 */
@RestController
@RequestMapping("/api")
@CrossOrigin(origins = "*")
public class PlagiarismController {
    
    @Autowired
    private PlagiarismDetectionService plagiarismDetectionService;
    
    /**
     * 计算两个文本的相似度
     * 
     * @param request 包含两个文本的请求对象
     * @return 相似度结果
     */
    @PostMapping("/similarity")
    public ResponseEntity<Map<String, Object>> calculateSimilarity(@RequestBody SimilarityRequest request) {
        Map<String, Object> response = new HashMap<>();
        
        try {
            double similarity = plagiarismDetectionService.calculateSimilarity(
                request.getOriginalText(), 
                request.getPlagiarizedText()
            );
            
            response.put("success", true);
            response.put("similarity", plagiarismDetectionService.formatSimilarity(similarity));
            response.put("similarityPercentage", plagiarismDetectionService.formatSimilarityAsPercentage(similarity));
            response.put("message", "相似度计算成功");
            
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            response.put("success", false);
            response.put("error", e.getMessage());
            response.put("message", "相似度计算失败");
            
            return ResponseEntity.badRequest().body(response);
        }
    }
    
    /**
     * 使用指定算法计算相似度
     * 
     * @param request 包含两个文本和算法名称的请求对象
     * @return 相似度结果
     */
    @PostMapping("/similarity/algorithm")
    public ResponseEntity<Map<String, Object>> calculateSimilarityWithAlgorithm(@RequestBody AlgorithmSimilarityRequest request) {
        Map<String, Object> response = new HashMap<>();
        
        try {
            double similarity = plagiarismDetectionService.calculateSimilarityWithAlgorithm(
                request.getOriginalText(), 
                request.getPlagiarizedText(),
                request.getAlgorithmName()
            );
            
            response.put("success", true);
            response.put("similarity", plagiarismDetectionService.formatSimilarity(similarity));
            response.put("similarityPercentage", plagiarismDetectionService.formatSimilarityAsPercentage(similarity));
            response.put("algorithm", request.getAlgorithmName());
            response.put("message", "相似度计算成功");
            
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            response.put("success", false);
            response.put("error", e.getMessage());
            response.put("message", "相似度计算失败");
            
            return ResponseEntity.badRequest().body(response);
        }
    }
    
    /**
     * 上传文件并计算相似度
     * 
     * @param originalFile 原文文件
     * @param plagiarizedFile 抄袭文件
     * @return 相似度结果
     */
    @PostMapping("/similarity/upload")
    public ResponseEntity<Map<String, Object>> calculateSimilarityFromFiles(
            @RequestParam("originalFile") MultipartFile originalFile,
            @RequestParam("plagiarizedFile") MultipartFile plagiarizedFile) {
        
        Map<String, Object> response = new HashMap<>();
        
        try {
            // 检查文件是否为空
            if (originalFile.isEmpty() || plagiarizedFile.isEmpty()) {
                response.put("success", false);
                response.put("error", "文件不能为空");
                return ResponseEntity.badRequest().body(response);
            }
            
            // 读取文件内容
            String originalText = new String(originalFile.getBytes(), "UTF-8");
            String plagiarizedText = new String(plagiarizedFile.getBytes(), "UTF-8");
            
            // 计算相似度
            double similarity = plagiarismDetectionService.calculateSimilarity(originalText, plagiarizedText);
            
            response.put("success", true);
            response.put("similarity", plagiarismDetectionService.formatSimilarity(similarity));
            response.put("similarityPercentage", plagiarismDetectionService.formatSimilarityAsPercentage(similarity));
            response.put("originalFileName", originalFile.getOriginalFilename());
            response.put("plagiarizedFileName", plagiarizedFile.getOriginalFilename());
            response.put("message", "相似度计算成功");
            
            return ResponseEntity.ok(response);
        } catch (IOException e) {
            response.put("success", false);
            response.put("error", "文件读取失败: " + e.getMessage());
            response.put("message", "相似度计算失败");
            
            return ResponseEntity.badRequest().body(response);
        } catch (Exception e) {
            response.put("success", false);
            response.put("error", e.getMessage());
            response.put("message", "相似度计算失败");
            
            return ResponseEntity.badRequest().body(response);
        }
    }
    
    /**
     * 获取可用的算法列表
     * 
     * @return 算法列表
     */
    @GetMapping("/algorithms")
    public ResponseEntity<Map<String, Object>> getAvailableAlgorithms() {
        Map<String, Object> response = new HashMap<>();
        
        try {
            List<String> algorithms = plagiarismDetectionService.getAvailableAlgorithms();
            
            response.put("success", true);
            response.put("algorithms", algorithms);
            response.put("message", "获取算法列表成功");
            
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            response.put("success", false);
            response.put("error", e.getMessage());
            response.put("message", "获取算法列表失败");
            
            return ResponseEntity.badRequest().body(response);
        }
    }
    
    /**
     * 健康检查接口
     * 
     * @return 服务状态
     */
    @GetMapping("/health")
    public ResponseEntity<Map<String, Object>> healthCheck() {
        Map<String, Object> response = new HashMap<>();
        response.put("status", "UP");
        response.put("service", "Plagiarism Detection Service");
        response.put("version", "1.0.0");
        
        return ResponseEntity.ok(response);
    }
    
    /**
     * 相似度计算请求对象
     */
    public static class SimilarityRequest {
        private String originalText;
        private String plagiarizedText;
        
        public String getOriginalText() {
            return originalText;
        }
        
        public void setOriginalText(String originalText) {
            this.originalText = originalText;
        }
        
        public String getPlagiarizedText() {
            return plagiarizedText;
        }
        
        public void setPlagiarizedText(String plagiarizedText) {
            this.plagiarizedText = plagiarizedText;
        }
    }
    
    /**
     * 算法相似度计算请求对象
     */
    public static class AlgorithmSimilarityRequest {
        private String originalText;
        private String plagiarizedText;
        private String algorithmName;
        
        public String getOriginalText() {
            return originalText;
        }
        
        public void setOriginalText(String originalText) {
            this.originalText = originalText;
        }
        
        public String getPlagiarizedText() {
            return plagiarizedText;
        }
        
        public void setPlagiarizedText(String plagiarizedText) {
            this.plagiarizedText = plagiarizedText;
        }
        
        public String getAlgorithmName() {
            return algorithmName;
        }
        
        public void setAlgorithmName(String algorithmName) {
            this.algorithmName = algorithmName;
        }
    }
}
