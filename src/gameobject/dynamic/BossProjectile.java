package gameobject.dynamic;

import application.GameManager;
import gameobject.core.GameObject;
import gameobject.core.MovableObject;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import java.util.List;

/**
 * Lớp định nghĩa viên đạn do Boss bắn ra.
 * Nó di chuyển thẳng xuống và kiểm tra va chạm với Paddle.
 */
public class BossProjectile extends MovableObject {

    private Pane gameRoot;
    private double sceneHeight;
    private static final double PROJECTILE_SPEED = 200.0; // Tốc độ đạn (pixel/giây)

    public BossProjectile(Pane gameRoot, double x, double y) {
        super(x, y, 120, 120, null);

        this.gameRoot = gameRoot;
        this.sceneHeight = gameRoot.getHeight();

        try {
            Image image = new Image(getClass().getResourceAsStream("/images/npc/boss_shot.png"));
            this.imageView = new ImageView(image);
            this.imageView.setFitWidth(this.width);
            this.imageView.setFitHeight(this.height);
            this.imageView.setLayoutX(x);
            this.imageView.setLayoutY(y);

            gameRoot.getChildren().add(this.imageView);

        } catch (Exception e) {
            System.err.println("Không thể tải ảnh đạn của Boss!");
        }

        setDy(PROJECTILE_SPEED); // Bay xuống
    }

    @Override
    public void update(double deltaTime) {

        super.update(deltaTime);
        if (this.imageView != null) {
            this.imageView.setLayoutY(getY());
        }

        GameManager gm = GameManager.getInstance();
        if (gm == null) {
            remove();
            return;
        }

        List<GameObject> gameObjects = gm.getGameObjects();
        for (GameObject obj : gameObjects) {
            if (obj instanceof Paddle) {
                if (getBounds().intersects(obj.getBounds())) {
                    System.out.println("GAME: Paddle bị trúng đạn!");
                    gm.loseLife();
                    remove();
                    return;
                }
                break;
            }
        }

        if (getY() > this.sceneHeight) {
            remove(); // Tự hủy
        }
    }


    private void remove() {
        GameManager.getInstance().removeGameObject(this);
        if (gameRoot != null && this.imageView != null) {
            gameRoot.getChildren().remove(this.imageView);
        }
    }
}
