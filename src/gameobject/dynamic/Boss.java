package gameobject.dynamic;

import gameobject.core.MovableObject;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.animation.AnimationTimer;
import java.util.Random;

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

    // Kỹ năng của boss
    private boolean canSpawnBricks = true;
    private boolean canShootProjectiles = true;
    private long lastAttackTime = 0;
    private static final long ATTACK_COOLDOWN = 2000; // 2 giây giữa các đợt tấn công

    public Boss(Pane gameRoot, double sceneWidth, double sceneHeight) {
        super(sceneWidth / 2 - 75, 50, 150, 80,
                new Image(Boss.class.getResourceAsStream("/resources/images/npc/Boss.png")));
        this.gameRoot = gameRoot;
        this.random = new Random();
        this.maxHealth = 10; // Boss có 10 máu
        this.health = maxHealth;

        // Tạo ImageView

        imageView = new ImageView(image);
        imageView.setFitWidth(width);
        imageView.setFitHeight(height);
        imageView.setLayoutX(x);
        imageView.setLayoutY(y);
        gameRoot.getChildren().add(imageView);

        // Di chuyển ngang qua lại
        setDx(2);

        // Bắt đầu tấn công
        startAttacking();
    }

    @Override
    public void update(double deltaTime) {
        if (!isAlive) return;

        // Di chuyển boss
        setX(getX() + getDx() * deltaTime);

        // Đổi hướng khi chạm biên
        if (getX() <= 0 || getX() + getWidth() >= gameRoot.getWidth()) {
            setDx(-getDx());
        }

        // Cập nhật ImageView
        imageView.setLayoutX(getX());
        imageView.setLayoutY(getY());
    }

    /**
     * Bắt đầu các đợt tấn công của boss
     */
    private void startAttacking() {
        attackTimer = new AnimationTimer() {
            @Override
            public void handle(long now) {
                if (!isAlive) {
                    stop();
                    return;
                }

                // Tấn công mỗi 2 giây
                if (now - lastAttackTime >= ATTACK_COOLDOWN * 1_000_000) {
                    performRandomAttack();
                    lastAttackTime = now;
                }
            }
        };
        attackTimer.start();
    }

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
        // Chỉ sinh gạch khi máu dưới 30%
        if ((double)health / maxHealth > 0.3) return;

        System.out.println("🔥 Boss spawning bricks!");

        // Tạo 3-5 viên gạch ngẫu nhiên
        int brickCount = 3 + random.nextInt(3);
        for (int i = 0; i < brickCount; i++) {
            // Tạo gạch ở vị trí ngẫu nhiên phía dưới boss
            double brickX = random.nextDouble() * (gameRoot.getWidth() - 50);
            double brickY = getY() + getHeight() + 20 + random.nextDouble() * 100;

            // Có thể tạo các loại gạch khác nhau
            // new NormalBrick(brickX, brickY, gameRoot, null); // Cần paddle reference
        }
    }

    /**
     * Boss bắn đạn
     */
    private void shootProjectile() {
        System.out.println("💥 Boss shooting projectile!");

        // Tạo đạn từ vị trí boss
        double projectileX = getX() + getWidth() / 2 - 10;
        double projectileY = getY() + getHeight();

        // Có thể tạo class BossProjectile
        // BossProjectile projectile = new BossProjectile(gameRoot, projectileX, projectileY);
        // projectile.setDy(4); // Rơi xuống
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
        if (!isAlive) return;

        health -= damage;

        // Hiệu ứng khi bị đánh
        imageView.setOpacity(0.5);
        javafx.animation.PauseTransition flash =
                new javafx.animation.PauseTransition(javafx.util.Duration.millis(100));
        flash.setOnFinished(e -> imageView.setOpacity(1.0));
        flash.play();

        System.out.println("💢 Boss took " + damage + " damage! Health: " + health + "/" + maxHealth);

        if (health <= 0) {
            die();
        }
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
    public int getHealth() { return health; }
    public int getMaxHealth() { return maxHealth; }
    public boolean isAlive() { return isAlive; }
    public ImageView getImageView() { return imageView; }


    public boolean checkBallCollision(Ball ball) {
        return imageView.getBoundsInParent().intersects(ball.getImageView().getBoundsInParent());
    }
}