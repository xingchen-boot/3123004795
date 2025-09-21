package com.plagiarism.util;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;
import java.util.Map;

/**
 * 性能监控工具类
 * 用于监控算法执行时间和内存使用情况
 * 
 * @author 学生
 * @version 1.0.0
 */
public class PerformanceMonitor {
    
    private static final Map<String, AtomicLong> executionTimes = new ConcurrentHashMap<>();
    private static final Map<String, AtomicLong> executionCounts = new ConcurrentHashMap<>();
    
    /**
     * 记录算法执行时间
     * 
     * @param algorithmName 算法名称
     * @param executionTime 执行时间（毫秒）
     */
    public static void recordExecutionTime(String algorithmName, long executionTime) {
        executionTimes.computeIfAbsent(algorithmName, k -> new AtomicLong(0))
                     .addAndGet(executionTime);
        executionCounts.computeIfAbsent(algorithmName, k -> new AtomicLong(0))
                      .incrementAndGet();
    }
    
    /**
     * 获取平均执行时间
     * 
     * @param algorithmName 算法名称
     * @return 平均执行时间（毫秒）
     */
    public static double getAverageExecutionTime(String algorithmName) {
        AtomicLong totalTime = executionTimes.get(algorithmName);
        AtomicLong count = executionCounts.get(algorithmName);
        
        if (totalTime == null || count == null || count.get() == 0) {
            return 0.0;
        }
        
        return (double) totalTime.get() / count.get();
    }
    
    /**
     * 获取执行次数
     * 
     * @param algorithmName 算法名称
     * @return 执行次数
     */
    public static long getExecutionCount(String algorithmName) {
        AtomicLong count = executionCounts.get(algorithmName);
        return count != null ? count.get() : 0;
    }
    
    /**
     * 获取总执行时间
     * 
     * @param algorithmName 算法名称
     * @return 总执行时间（毫秒）
     */
    public static long getTotalExecutionTime(String algorithmName) {
        AtomicLong totalTime = executionTimes.get(algorithmName);
        return totalTime != null ? totalTime.get() : 0;
    }
    
    /**
     * 获取所有算法的性能统计
     * 
     * @return 性能统计信息
     */
    public static Map<String, Map<String, Object>> getAllStats() {
        Map<String, Map<String, Object>> stats = new ConcurrentHashMap<>();
        
        for (String algorithmName : executionTimes.keySet()) {
            Map<String, Object> algorithmStats = new ConcurrentHashMap<>();
            algorithmStats.put("totalTime", getTotalExecutionTime(algorithmName));
            algorithmStats.put("executionCount", getExecutionCount(algorithmName));
            algorithmStats.put("averageTime", getAverageExecutionTime(algorithmName));
            stats.put(algorithmName, algorithmStats);
        }
        
        return stats;
    }
    
    /**
     * 清理统计信息
     */
    public static void clearStats() {
        executionTimes.clear();
        executionCounts.clear();
    }
    
    /**
     * 获取当前内存使用情况
     * 
     * @return 内存使用信息
     */
    public static Map<String, Long> getMemoryUsage() {
        Map<String, Long> memoryInfo = new ConcurrentHashMap<>();
        
        Runtime runtime = Runtime.getRuntime();
        memoryInfo.put("totalMemory", runtime.totalMemory());
        memoryInfo.put("freeMemory", runtime.freeMemory());
        memoryInfo.put("usedMemory", runtime.totalMemory() - runtime.freeMemory());
        memoryInfo.put("maxMemory", runtime.maxMemory());
        
        return memoryInfo;
    }
    
    /**
     * 格式化内存大小
     * 
     * @param bytes 字节数
     * @return 格式化后的字符串
     */
    public static String formatMemorySize(long bytes) {
        if (bytes < 1024) {
            return bytes + " B";
        } else if (bytes < 1024 * 1024) {
            return String.format("%.2f KB", bytes / 1024.0);
        } else if (bytes < 1024 * 1024 * 1024) {
            return String.format("%.2f MB", bytes / (1024.0 * 1024.0));
        } else {
            return String.format("%.2f GB", bytes / (1024.0 * 1024.0 * 1024.0));
        }
    }
}
