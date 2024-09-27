package mandrejczuk;

import com.sedmelluq.discord.lavaplayer.player.AudioLoadResultHandler;
import com.sedmelluq.discord.lavaplayer.player.AudioPlayer;
import com.sedmelluq.discord.lavaplayer.tools.FriendlyException;
import com.sedmelluq.discord.lavaplayer.track.AudioPlaylist;
import com.sedmelluq.discord.lavaplayer.track.AudioTrack;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class AudioLoadResultHandlerImpl implements AudioLoadResultHandler {

    private final AudioPlayer audioPlayer;

    public AudioLoadResultHandlerImpl(AudioPlayer audioPlayer) {
        this.audioPlayer = audioPlayer;
    }

    @Override
    public void trackLoaded(AudioTrack track) {
        // Odtwarzanie załadowanego utworu
        audioPlayer.playTrack(track);
        System.out.println("Odtwarzam plik: " + track.getInfo().title);
        System.out.println("Czas trwania: " + track.getDuration());



        // Tworzymy scheduler, który będzie co sekundę sprawdzał postęp
        ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);
        scheduler.scheduleAtFixedRate(() -> {
            if (audioPlayer.getPlayingTrack() != null) {
                long currentPosition = audioPlayer.getPlayingTrack().getPosition();
                System.out.println("Aktualna pozycja: " + currentPosition);


                // Jeśli utwór się skończył, zatrzymujemy scheduler
                if (currentPosition >= audioPlayer.getPlayingTrack().getDuration()) {
                    scheduler.shutdown();
                    System.out.println("Utwór się skończył.");
                }
            } else {
                System.out.println("Brak utworu w odtwarzaczu.");
                scheduler.shutdown();
            }
        }, 0, 1, TimeUnit.SECONDS);
    }


    @Override
    public void noMatches() {
        System.out.println("Nie znaleziono pliku.");
    }

    @Override
    public void playlistLoaded(AudioPlaylist audioPlaylist) {
        System.out.println(audioPlaylist.getName());
    }

    @Override
    public void loadFailed(FriendlyException e) {
        // Obsługa błędu ładowania
        System.out.println("Błąd podczas ładowania pliku: " + e.getMessage());
    }

}
