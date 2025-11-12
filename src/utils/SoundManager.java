package utils;

import javafx.scene.media.AudioClip;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

public class SoundManager {

    private static SoundManager instance;
    private MediaPlayer backgroundMusicPlayer;
    private final Map<String, AudioClip> soundEffectsCache = new HashMap<>();
    private double musicVolume = 0.5;
    private double sfxVolume = 0.8;

    private SoundManager() {}

    public static synchronized SoundManager getInstance() {
        if (instance == null) {
            instance = new SoundManager();
        }
        return instance;
    }

    public void playBackgroundMusic(String path) {
        if (backgroundMusicPlayer != null) {
            backgroundMusicPlayer.stop();
        }
        try {
            URL resource = getClass().getResource(path);
            Media media = new Media(resource.toString());
            backgroundMusicPlayer = new MediaPlayer(media);
            backgroundMusicPlayer.setVolume(musicVolume);
            backgroundMusicPlayer.setCycleCount(MediaPlayer.INDEFINITE);
            backgroundMusicPlayer.play();
        } catch (Exception e) {
            System.err.println("Không thể phát nhạc nền: " + path);
        }
    }

    public void stopBackgroundMusic() {
        if (backgroundMusicPlayer != null) {
            backgroundMusicPlayer.stop();
        }
    }

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
