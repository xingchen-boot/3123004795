package com.plagiarism.algorithm;

/**
 * 相似度算法接口
 * 定义了计算两个文本相似度的基本方法
 * 
 * @author 学生
 * @version 1.0.0
 */
public interface SimilarityAlgorithm {
    
    /**
     * 计算两个文本的相似度
     * 
     * @param text1 第一个文本
     * @param text2 第二个文本
     * @return 相似度值，范围[0, 1]，1表示完全相同，0表示完全不同
     */
    double calculateSimilarity(String text1, String text2);
    
    /**
     * 获取算法名称
     * 
     * @return 算法名称
     */
    String getAlgorithmName();
}
