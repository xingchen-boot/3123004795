import com.plagiarism.algorithm.impl.CosineSimilarity;

public class DebugTest {
    public static void main(String[] args) {
        CosineSimilarity cosineSimilarity = new CosineSimilarity();
        
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
        System.out.println("相似度: " + similarity);
        System.out.println("是否大于0.8: " + (similarity > 0.8));
    }
}
