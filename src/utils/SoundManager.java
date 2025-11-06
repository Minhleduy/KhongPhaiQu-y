package utils;

import javafx.scene.media.AudioClip;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

/**
 * Lớp Singleton để quản lý nhạc nền và hiệu ứng âm thanh (SFX).
 */
public class SoundManager {

    private static SoundManager instance;
    private MediaPlayer backgroundMusicPlayer;
    private final Map<String, AudioClip> soundEffectsCache = new HashMap<>();
    private double musicVolume = 0.5; // Âm lượng mặc định
    private double sfxVolume = 0.8;

    private SoundManager() {}

    public static synchronized SoundManager getInstance() {
        if (instance == null) {
            instance = new SoundManager();
        }
        return instance;
    }

    /**
     * Phát một file nhạc nền và lặp lại liên tục.
     * @param path Đường dẫn đến file nhạc (ví dụ: "/sounds/music.mp3")
     */
    public void playBackgroundMusic(String path) {
        if (backgroundMusicPlayer != null) {
            backgroundMusicPlayer.stop();
        }
        try {
            URL resource = getClass().getResource(path);
            Media media = new Media(resource.toString());
            backgroundMusicPlayer = new MediaPlayer(media);
            backgroundMusicPlayer.setVolume(musicVolume);
            backgroundMusicPlayer.setCycleCount(MediaPlayer.INDEFINITE); // Lặp lại vô hạn
            backgroundMusicPlayer.play();
        } catch (Exception e) {
            System.err.println("Không thể phát nhạc nền: " + path);
        }
    }

    /**
     * Dừng nhạc nền đang phát.
     */
    public void stopBackgroundMusic() {
        if (backgroundMusicPlayer != null) {
            backgroundMusicPlayer.stop();
        }
    }

    /**
     * Phát một hiệu ứng âm thanh ngắn.
     * @param path Đường dẫn đến file âm thanh (ví dụ: "/sounds/brick_hit.wav")
     */
    public void playSoundEffect(String path) {
        try {
            AudioClip clip;
            if (soundEffectsCache.containsKey(path)) {
                clip = soundEffectsCache.get(path);
            } else {
                URL resource = getClass().getResource(path);
                clip = new AudioClip(resource.toString());
                soundEffectsCache.put(path, clip);
            }
            clip.setVolume(sfxVolume);
            clip.play();
        } catch (Exception e) {
            System.err.println("Không thể phát hiệu ứng âm thanh: " + path);
        }
    }

    // --- Các phương thức điều chỉnh âm lượng ---
    public void setMusicVolume(double volume) {
        this.musicVolume = Math.max(0.0, Math.min(1.0, volume));
        if (backgroundMusicPlayer != null) {
            backgroundMusicPlayer.setVolume(this.musicVolume);
        }
    }

    public void setSfxVolume(double volume) {
        this.sfxVolume = Math.max(0.0, Math.min(1.0, volume));
    }
}
