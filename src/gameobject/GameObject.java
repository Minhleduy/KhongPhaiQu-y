package gameobject;

public class GameObject {
    protected int x, y;        // Toa do cua vat
    protected int width, height; // Kich thuoc

    public GameObject(int x, int y, int width, int height) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
    }

    public void update() {
        // Hàm cập nhật trạng thái vật thể
    }

    public void render() {
        // Hàm vẽ vật thể
    }
}
