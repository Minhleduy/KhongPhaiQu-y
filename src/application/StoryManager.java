package application;

import java.util.HashMap;
import java.util.Map;

public class StoryManager {
    private static StoryManager instance;
    private final Map<Integer, String> levelStartDialogues;

    private StoryManager() {
        levelStartDialogues = new HashMap<>();
        // Tải trước nội dung cốt truyện
        levelStartDialogues.put(1, "Chào Thuyền trưởng. Chúng ta đang ở vành đai phòng thủ đầu tiên. Cảm biến phát hiện các 'Lá Chắn Phân Tách' (gạch DoubleBrick). Chúng sẽ nhân đôi quả cầu của chúng ta khi va chạm. Đây là một cơ hội tốt, hãy tận dụng hỏa lực nhân đôi!");
        levelStartDialogues.put(2, "Địch đang tăng cường phòng thủ, Thuyền trưởng. Chúng dùng 'Lá Chắn Bọc Thép', cần nhiều cú va chạm để phá vỡ. Tôi cũng quét thấy các 'Lõi Năng Lượng Không Ổn Định'. Phá hủy chúng sẽ 'cường hóa' quả cầu, giúp nó bay xuyên qua mọi thứ!");
        levelStartDialogues.put(3, "Chúng ta đã vào sâu trong hạm đội địch. Chúng đang dùng chiến thuật hỗn hợp: cả 'Lá Chắn Bọc Thép' và 'Lá Chắn Phân Tách'. Mật độ phòng thủ dày đặc hơn, nhưng cơ hội nâng cấp cũng nhiều hơn. Hãy giữ vững tay lái!");
        levelStartDialogues.put(4, "Cảnh báo, Thuyền trưởng! Đây là tuyến phòng thủ cuối cùng. Chúng đã dựng lên một 'Bức Tường Bất Khả Xâm Phạm' với mật độ lá chắn cực kỳ dày đặc. Hãy tập trung cao độ. Chúng ta cần xuyên qua hàng rào này bằng mọi giá để đối mặt với tên chỉ huy!");
        levelStartDialogues.put(5, "Mục tiêu đã lộ diện! Đó là một ROBOT BẢO VỆ khổng lồ, có vẻ nó là kẻ chỉ huy. CẢNH BÁO,Thuyền trưởng! Nó đang sạc vũ khí! Phân tích cho thấy đạn của nó sẽ trừ thẳng vào mạng sống của tàu! Xin hãy né đạn của nó bằng mọi giá và tập trung hỏa lực!");
    }

    public static synchronized StoryManager getInstance() {
        if (instance == null) {
            instance = new StoryManager();
        }
        return instance;
    }

    public String getStoryForLevel(int levelNumber) {
        return levelStartDialogues.get(levelNumber);
    }
}
