package Arkanoid.util;

import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import java.net.URL;

public class SoundManager {

    private MediaPlayer backgroundMusicPlayer;

    public SoundManager() {
        String musicFileName = "/sounds/musicgame1.mp3";

        try {
            URL musicFileUrl = getClass().getResource(musicFileName);
            if (musicFileUrl == null) {
                System.err.println("Không tìm thấy file nhạc nền tại: " + musicFileName);
                return;
            }

            String musicFilePath = musicFileUrl.toExternalForm();
            Media media = new Media(musicFilePath);
            backgroundMusicPlayer = new MediaPlayer(media);

            backgroundMusicPlayer.setCycleCount(MediaPlayer.INDEFINITE);

        } catch (Exception e) {
            System.err.println("Lỗi khi tải nhạc nền:");
            e.printStackTrace();
            backgroundMusicPlayer = null;
        }
    }

    /**
     *GameManager sẽ gọi hàm này khi game bắt đầu.
     */
    public void playMusic() {
        if (backgroundMusicPlayer != null) {
            backgroundMusicPlayer.play();
        }
    }

    /**
     * Dừng phát nhạc.
     * GameManager sẽ gọi hàm này khi Game Over hoặc Win.
     */
    public void stopMusic() {
        if (backgroundMusicPlayer != null) {
            backgroundMusicPlayer.stop();
        }
    }
}
