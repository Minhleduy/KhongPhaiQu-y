package gameobject.dynamic;

import application.GameManager;
import gameobject.bricks.NormalBrick;
import gameobject.core.MovableObject;
import application.Config;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.animation.AnimationTimer;
import java.util.Random;
import gameobject.core.Brick;
import gameobject.core.GameObject;
import gameobject.dynamic.Paddle;
import gameobject.bricks.*;

/**
 * Boss - kẻ địch cuối cùng, có nhiều máu và kỹ năng đặc biệt
 */
public class Boss extends MovableObject {
    private int health;
    private int maxHealth;
    private ImageView imageView;
    private Pane gameRoot;
    private Random random;
    private AnimationTimer attackTimer;
    private boolean isAlive = true;
    private boolean isInvincible = false; // Trạng thái bất tử
    private static final double INVINCIBILITY_DURATION_MS = 500; // 0.2 giây
    private static final char[] BRICK_TYPES = {'N', 'S', 'T', 'H', 'Q', 'D', 'B'};

    // Kỹ năng của boss
    private boolean canSpawnBricks = true;
    private boolean canShootProjectiles = true;
    private long lastAttackTime = 0;
    private static final long ATTACK_COOLDOWN = 2000; // 2 giây giữa các đợt tấn công

    public Boss(Pane gameRoot, double sceneWidth, double sceneHeight) {
        super(sceneWidth / 2 - 10, 0, 250, 250,
                new Image(Boss.class.getResourceAsStream("/images/npc/Boss1.png")));
        this.gameRoot = gameRoot;
        this.random = new Random();
        this.maxHealth = 25;
        this.health = maxHealth;

        // Tạo ImageView

        imageView = new ImageView(image);
        imageView.setFitWidth(width);
        imageView.setFitHeight(height);
        imageView.setLayoutX(x);
        imageView.setLayoutY(y);
        gameRoot.getChildren().add(imageView);

        // Di chuyển ngang qua lại
        setDx(120.0);

        // Bắt đầu tấn công
        //startAttacking();
    }

    @Override
    public void update(double deltaTime) {
        if (!isAlive) return;

        // --- PHẦN 1: LOGIC DI CHUYỂN (Code cũ của bạn, đã đúng) ---
        double newX = getX() + getDx() * deltaTime;
        if (newX <= 0) {
            newX = 0;
            setDx(-getDx());
        } else if (newX + getWidth() >= gameRoot.getWidth()) {
            newX = gameRoot.getWidth() - getWidth();
            setDx(-getDx());
        }
        setX(newX);
        imageView.setLayoutX(getX());
        imageView.setLayoutY(getY());

        // --- PHẦN 2: LOGIC TẤN CÔNG (MỚI) ---
        // (Chuyển từ AnimationTimer vào đây)

        // 1. Lấy thời gian hiện tại (tính bằng nano giây)
        long now = System.nanoTime();

        // 2. Kiểm tra thời gian hồi chiêu
        // (LƯU Ý: ATTACK_COOLDOWN của bạn là mili giây, phải nhân 1_000_000)
        if (now - lastAttackTime >= ATTACK_COOLDOWN * 1_000_000) {
            performRandomAttack();
            lastAttackTime = now; // Đặt lại mốc thời gian
        }
    }


    /**
     * Bắt đầu các đợt tấn công của boss
     */
//    private void startAttacking() {
//        attackTimer = new AnimationTimer() {
//            @Override
//            public void handle(long now) {
//                if (!isAlive) {
//                    stop();
//                    return;
//                }
//
//                // Tấn công mỗi 2 giây
//                if (now - lastAttackTime >= ATTACK_COOLDOWN * 1_000_000) {
//                    performRandomAttack();
//                    lastAttackTime = now;
//                }
//            }
//        };
//        attackTimer.start();
//    }

    /**
     Thực hiện tấn công ngẫu nhiên
     */
    private void performRandomAttack() {
        int attackType = random.nextInt(3); // 3 loại tấn công

        switch (attackType) {
            case 0:
                if (canSpawnBricks) spawnBricks();
                break;
            case 1:
                if (canShootProjectiles) shootProjectile();
                break;
            case 2:
                moveFast(); // Di chuyển nhanh
                break;
        }
    }

    /**
     * Boss sinh ra gạch khi máu thấp
     */
    private void spawnBricks() {
        // 1. XÓA BỎ KIỂM TRA MÁU
        // (Dòng code cũ "if ((double)health / maxHealth > 0.3) return;" đã bị xóa)

        System.out.println("Boss spawning random bricks!");

        // 2. TÌM PADDLE
        // Chúng ta cần 'paddle' để có thể tạo ra các loại gạch rơi item
        GameManager gm = GameManager.getInstance();
        Paddle paddle = null;
        for (GameObject obj : gm.getGameObjects()) {
            if (obj instanceof Paddle) {
                paddle = (Paddle) obj;
                break;
            }
        }

        // Nếu vì lý do nào đó không tìm thấy paddle, hủy kỹ năng
        if (paddle == null) {
            System.err.println("Boss không tìm thấy Paddle, không thể tạo gạch item.");
            return;
        }

        // 3. TẠO GẠCH NGẪU NHIÊN
        int brickCount = 3 + random.nextInt(3); // Tạo từ 3 đến 5 viên
        for (int i = 0; i < brickCount; i++) {

            // 3a. Lấy vị trí ngẫu nhiên
            double brickX = random.nextDouble() * (gameRoot.getWidth() - Config.BRICK_WIDTH);
            double brickY = getY() + getHeight() + 20 + random.nextDouble() * 100;

            // 3b. Lấy loại gạch ngẫu nhiên
            char brickType = BRICK_TYPES[random.nextInt(BRICK_TYPES.length)];

            // 3c. Tạo gạch (Dùng logic giống như BrickMapLoader)
            Brick brick = null;
            switch (brickType) {
                case 'N':
                    brick = new NormalBrick(brickX, brickY, gameRoot);
                    break;
                case 'S':
                    brick = new StrongBrick(brickX, brickY, gameRoot, paddle);
                    break;
                case 'D':
                    brick = new DoubleBallBrick(brickX, brickY, gameRoot, paddle);
                    break;
                case 'H':
                    brick = new HeartBrick(brickX, brickY, gameRoot, paddle);
                    break;
                case 'T': // Gạch 3 máu (hoặc 2 máu) của bạn
                    brick = new ToughBrick(brickX, brickY, gameRoot);
                    break;
            }

            // 3d. Thêm gạch vào game
            if (brick != null) {
                gm.addGameObject(brick);
            }
        }
    }


    /**
     * Boss bắn đạn
     */
    private void shootProjectile() {
        System.out.println("💥 Boss shooting projectile!");

        // 1. Tính toán vị trí đạn (ở giữa, bên dưới Boss)
        double projectileX = getX() + (getWidth() / 2) - 7.5; // (7.5 là một nửa chiều rộng đạn)
        double projectileY = getY() + getHeight();

        // 2. Tạo đối tượng đạn mới
        BossProjectile projectile = new BossProjectile(this.gameRoot, projectileX, projectileY);

        // 3. THÊM ĐẠN VÀO GAME
        // (Đây là bước quan trọng nhất)
        GameManager.getInstance().addGameObject(projectile);
    }
    /**
     * Boss di chuyển nhanh
     */
    private void moveFast() {
        System.out.println("⚡ Boss moving fast!");

        // Tăng tốc độ di chuyển
        setDx(getDx() * 2);

        // Trở lại tốc độ bình thường sau 1.5 giây
        javafx.animation.PauseTransition pause =
                new javafx.animation.PauseTransition(javafx.util.Duration.seconds(1.5));
        pause.setOnFinished(e -> setDx(getDx() / 2));
        pause.play();
    }

    /**
     * Boss nhận sát thương
     */
    public void takeDamage(int damage) {
        // 1. KIỂM TRA BẤT TỬ:
        // Nếu Boss đang bất tử (vừa bị đánh) hoặc đã chết, không làm gì cả.
        if (isInvincible || !isAlive) return;

        // 2. KÍCH HOẠT BẤT TỬ
        // (Ngăn chặn các cú đánh ở frame tiếp theo)
        this.isInvincible = true;

        // 3. Trừ máu (Code cũ của bạn)
        health -= damage;

        // 4. Hiệu ứng flash (Code cũ của bạn)
        imageView.setOpacity(0.5);
        javafx.animation.PauseTransition flash =
                new javafx.animation.PauseTransition(javafx.util.Duration.millis(100));
        flash.setOnFinished(e -> imageView.setOpacity(1.0));
        flash.play();

        System.out.println("💢 Boss took " + damage + " damage! Health: " + health + "/" + maxHealth);

        // 5. Kiểm tra chết (Code cũ của bạn)
        if (health <= 0) {
            die();
        }

        // 6. BỘ ĐẾM GIỜ HỒI PHỤC
        // Tạo một bộ đếm giờ để TẮT bất tử sau 0.2 giây
        javafx.animation.PauseTransition cooldown =
                new javafx.animation.PauseTransition(javafx.util.Duration.millis(INVINCIBILITY_DURATION_MS));
        cooldown.setOnFinished(e -> this.isInvincible = false); // Tắt bất tử
        cooldown.play();
    }

    /**
     * Boss chết
     */
    private void die() {
        isAlive = false;
        attackTimer.stop();

        System.out.println("🎉 Boss defeated!");

        // Hiệu ứng chết
        gameRoot.getChildren().remove(imageView);

        // Có thể thêm animation nổ, hiệu ứng particles, v.v.
    }

    // ======== GETTERS ========
    public int getHealth() {
        return health;
    }
    public int getMaxHealth() {
        return maxHealth;
    }
    public boolean isAlive() {
        return isAlive;
    }
    public ImageView getImageView() {
        return imageView;
    }


    public boolean checkBallCollision(Ball ball) {
        return imageView.getBoundsInParent().intersects(ball.getImageView().getBoundsInParent());
    }
}
