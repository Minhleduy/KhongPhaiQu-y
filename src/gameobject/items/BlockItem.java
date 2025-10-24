package gameobject.items;

import gameobject.core.PowerUp;
import gameobject.dynamic.Paddle;
import javafx.scene.layout.Pane;
import javafx.animation.PauseTransition;
import javafx.util.Duration;

public class BlockItem extends PowerUp {

    private static boolean shieldActive = false; // Trạng thái bảo vệ

    public BlockItem(Pane gameRoot, Paddle paddle, double x, double y) {
        super(gameRoot, paddle, x, y, "/resources/images/items/Item6.png");
    }

    @Override
    protected void applyEffect(Paddle paddle) {
        // Kích hoạt shield bảo vệ
        shieldActive = true;

        System.out.println("Block Item activated! Next ball loss will not cost a life.");

        // Có thể thêm hiệu ứng visual cho paddle
        // paddle.activateShieldEffect();
    }

    public static boolean isShieldActive() {
        return shieldActive;
    }


    public static boolean useShield() {
        if (shieldActive) {
            shieldActive = false; // Chỉ dùng được 1 lần
            System.out.println("Shield used! Life saved.");
            return true;
        }
        return false;
    }

    /**
     * Reset shield (khi bắt đầu game mới)
     */
    public static void resetShield() {
        shieldActive = false;
    }
}