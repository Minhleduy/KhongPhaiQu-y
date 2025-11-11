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
import utils.SoundManager;

public class Boss extends MovableObject {
    private int health;
    private int maxHealth;
    private ImageView imageView;
    private Pane gameRoot;
    private Random random;
    private AnimationTimer attackTimer;
    private boolean isAlive = true;
    private boolean isInvincible = false; // Trạng thái bất tử
    private static final double INVINCIBILITY_DURATION_MS = 500; // 0.5 giây
    private static final char[] BRICK_TYPES = {'N', 'S', 'T', 'H', 'Q', 'D', 'B'};

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

        imageView = new ImageView(image);
        imageView.setFitWidth(width);
        imageView.setFitHeight(height);
        imageView.setLayoutX(x);
        imageView.setLayoutY(y);
        gameRoot.getChildren().add(imageView);

        setDx(120.0);

    }

    @Override
    public void update(double deltaTime) {
        if (!isAlive) return;

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

        long now = System.nanoTime();

        if (now - lastAttackTime >= ATTACK_COOLDOWN * 1_000_000) {
            performRandomAttack();
            lastAttackTime = now; // Đặt lại mốc thời gian
        }
    }

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

    private void spawnBricks() {
        System.out.println("Boss spawning random bricks!");
        GameManager gm = GameManager.getInstance();
        Paddle paddle = null;
        for (GameObject obj : gm.getGameObjects()) {
            if (obj instanceof Paddle) {
                paddle = (Paddle) obj;
                break;
            }
        }

        if (paddle == null) {
            System.err.println("Boss không tìm thấy Paddle, không thể tạo gạch item.");
            return;
        }

        int brickCount = 3 + random.nextInt(3); // Tạo từ 3 đến 5 viên
        for (int i = 0; i < brickCount; i++) {

            // Lấy vị trí ngẫu nhiên
            double brickX = random.nextDouble() * (gameRoot.getWidth() - Config.BRICK_WIDTH);
            double brickY = getY() + getHeight() + 20 + random.nextDouble() * 100;

            // Lấy loại gạch ngẫu nhiên
            char brickType = BRICK_TYPES[random.nextInt(BRICK_TYPES.length)];

            // Tạo gạch (Dùng logic giống như BrickMapLoader)
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

            // Thêm gạch vào game
            if (brick != null) {
                gm.addGameObject(brick);
            }
        }
    }

    private void shootProjectile() {
        System.out.println("💥 Boss shooting projectile!");
        utils.SoundManager.getInstance().playSoundEffect("/sounds/bossroar.mp3");
        double projectileX = getX() + (getWidth() / 2) - 7.5;
        double projectileY = getY() + getHeight();

        BossProjectile projectile = new BossProjectile(this.gameRoot, projectileX, projectileY);
        GameManager.getInstance().addGameObject(projectile);
    }
    /**
     * Boss di chuyển nhanh
     */
    private void moveFast() {
        System.out.println("⚡ Boss moving fast!");

        setDx(getDx() * 2);

        javafx.animation.PauseTransition pause =
                new javafx.animation.PauseTransition(javafx.util.Duration.seconds(1.5));
        pause.setOnFinished(e -> setDx(getDx() / 2));
        pause.play();
    }

    /**
     * Boss nhận sát thương
     */
    public void takeDamage(int damage) {
        if (isInvincible || !isAlive) return;

        this.isInvincible = true;

        health -= damage;
        SoundManager.getInstance().playSoundEffect("/sounds/TouchBrick.mp3");
        imageView.setOpacity(0.5);
        javafx.animation.PauseTransition flash =
                new javafx.animation.PauseTransition(javafx.util.Duration.millis(100));
        flash.setOnFinished(e -> imageView.setOpacity(1.0));
        flash.play();

        System.out.println("💢 Boss took " + damage + " damage! Health: " + health + "/" + maxHealth);

        if (health <= 0) {
            die();
        }
        javafx.animation.PauseTransition cooldown =
                new javafx.animation.PauseTransition(javafx.util.Duration.millis(INVINCIBILITY_DURATION_MS));
        cooldown.setOnFinished(e -> this.isInvincible = false); // Tắt bất tử
        cooldown.play();
    }


    private void die() {
        isAlive = false;
        attackTimer.stop();

        System.out.println("🎉 Boss defeated!");
        gameRoot.getChildren().remove(imageView);
    }

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
