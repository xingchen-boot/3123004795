package com.plagiarism.controller;

import com.plagiarism.util.PerformanceMonitor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

/**
 * 性能监控控制器
 * 提供性能统计信息的API接口
 * 
 * @author 学生
 * @version 1.0.0
 */
@RestController
@RequestMapping("/api/performance")
@CrossOrigin(origins = "*")
public class PerformanceController {
    
    /**
     * 获取性能统计信息
     * 
     * @return 性能统计信息
     */
    @GetMapping("/stats")
    public ResponseEntity<Map<String, Object>> getPerformanceStats() {
        Map<String, Object> response = new HashMap<>();
        
        try {
            Map<String, Map<String, Object>> algorithmStats = PerformanceMonitor.getAllStats();
            Map<String, Long> memoryUsage = PerformanceMonitor.getMemoryUsage();
            
            response.put("success", true);
            response.put("algorithmStats", algorithmStats);
            response.put("memoryUsage", memoryUsage);
            response.put("formattedMemoryUsage", formatMemoryUsage(memoryUsage));
            response.put("message", "获取性能统计信息成功");
            
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            response.put("success", false);
            response.put("error", e.getMessage());
            response.put("message", "获取性能统计信息失败");
            
            return ResponseEntity.badRequest().body(response);
        }
    }
    
    /**
     * 清理性能统计信息
     * 
     * @return 操作结果
     */
    @PostMapping("/clear")
    public ResponseEntity<Map<String, Object>> clearPerformanceStats() {
        Map<String, Object> response = new HashMap<>();
        
        try {
            PerformanceMonitor.clearStats();
            
            response.put("success", true);
            response.put("message", "性能统计信息已清理");
            
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            response.put("success", false);
            response.put("error", e.getMessage());
            response.put("message", "清理性能统计信息失败");
            
            return ResponseEntity.badRequest().body(response);
        }
    }
    
    /**
     * 获取内存使用情况
     * 
     * @return 内存使用情况
     */
    @GetMapping("/memory")
    public ResponseEntity<Map<String, Object>> getMemoryUsage() {
        Map<String, Object> response = new HashMap<>();
        
        try {
            Map<String, Long> memoryUsage = PerformanceMonitor.getMemoryUsage();
            
            response.put("success", true);
            response.put("memoryUsage", memoryUsage);
            response.put("formattedMemoryUsage", formatMemoryUsage(memoryUsage));
            response.put("message", "获取内存使用情况成功");
            
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            response.put("success", false);
            response.put("error", e.getMessage());
            response.put("message", "获取内存使用情况失败");
            
            return ResponseEntity.badRequest().body(response);
        }
    }
    
    /**
     * 格式化内存使用情况
     * 
     * @param memoryUsage 内存使用情况
     * @return 格式化后的内存使用情况
     */
    private Map<String, String> formatMemoryUsage(Map<String, Long> memoryUsage) {
        Map<String, String> formatted = new HashMap<>();
        
        for (Map.Entry<String, Long> entry : memoryUsage.entrySet()) {
            formatted.put(entry.getKey(), PerformanceMonitor.formatMemorySize(entry.getValue()));
        }
        
        return formatted;
    }
}
