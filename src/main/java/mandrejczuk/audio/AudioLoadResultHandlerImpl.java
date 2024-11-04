package mandrejczuk.audio;

import com.sedmelluq.discord.lavaplayer.player.AudioLoadResultHandler;
import com.sedmelluq.discord.lavaplayer.player.AudioPlayer;
import com.sedmelluq.discord.lavaplayer.tools.FriendlyException;
import com.sedmelluq.discord.lavaplayer.track.AudioItem;
import com.sedmelluq.discord.lavaplayer.track.AudioPlaylist;
import com.sedmelluq.discord.lavaplayer.track.AudioTrack;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class AudioLoadResultHandlerImpl implements AudioLoadResultHandler {

    private final AudioPlayerWrapper audioPlayerWrapper;

    private final CompletableFuture<AudioItem> loadedItem;

    public AudioLoadResultHandlerImpl(AudioPlayerWrapper guildPlayer, CompletableFuture<AudioItem> loadedItem) {
        this.audioPlayerWrapper = guildPlayer;
        this.loadedItem = loadedItem;
    }

    @Override
    public void trackLoaded(AudioTrack track) {
        audioPlayerWrapper.getTrackScheduler().queue(track);
        loadedItem.complete(track);
    }

    @Override
    public void playlistLoaded(AudioPlaylist playlist) {
        if(!playlist.getTracks().isEmpty()) {
            playlist.getTracks().forEach(track-> audioPlayerWrapper.getTrackScheduler().queue(track));
            loadedItem.complete(playlist);
        }
        else noMatches();
    }

    @Override
    public void noMatches() {
        loadedItem.completeExceptionally(new IllegalArgumentException("No matches found"));

    }

    @Override
    public void loadFailed(FriendlyException exception) {

        loadedItem.completeExceptionally(exception);

    }

}
