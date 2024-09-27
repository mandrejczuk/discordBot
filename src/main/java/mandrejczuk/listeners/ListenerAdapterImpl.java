package mandrejczuk.listeners;

import com.sedmelluq.discord.lavaplayer.player.AudioPlayer;
import com.sedmelluq.discord.lavaplayer.player.AudioPlayerManager;
import com.sedmelluq.discord.lavaplayer.player.DefaultAudioPlayerManager;
import com.sedmelluq.discord.lavaplayer.source.AudioSourceManagers;
import com.sedmelluq.discord.lavaplayer.track.AudioTrack;
import mandrejczuk.AudioLoadResultHandlerImpl;
import mandrejczuk.AudioPlayerSendHandler;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.channel.concrete.VoiceChannel;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.managers.AudioManager;
import org.jetbrains.annotations.NotNull;

public class ListenerAdapterImpl extends ListenerAdapter {

    private final AudioPlayerManager audioManager;
    private final AudioPlayer audioPlayer;

    private String lastLink;

    private AudioTrack stoppedTrack;

    public ListenerAdapterImpl() {
        audioManager = new DefaultAudioPlayerManager();
        audioPlayer = audioManager.createPlayer();
        AudioSourceManagers.registerLocalSource(audioManager);
        AudioSourceManagers.registerRemoteSources(audioManager);

    }


    @Override
    public void onMessageReceived(@NotNull MessageReceivedEvent event) {

        if (event.getAuthor().isBot()) return;

        var rawMessage = event.getMessage().getContentRaw();

        switch (rawMessage) {
            case "join" -> {
                AudioManager audioManager = event.getGuild().getAudioManager();
                Member member = event.getMember();
                for (VoiceChannel voiceChannel : event.getGuild().getVoiceChannels()) {
                    var optMem = voiceChannel.getMembers().stream()
                            .filter(m -> m.getUser().equals(member.getUser()))
                            .findAny();
                    if (optMem.isPresent()) {
                        audioManager.setSendingHandler(new AudioPlayerSendHandler(audioPlayer)); // Ustawienie adaptera
                        audioManager.openAudioConnection(voiceChannel);
                        return;
                    }
                }
                event.getChannel().sendMessage(event.getMember().getAsMention() + " ty huncwocie doloncz na kanal zberezniku").queue();

            }
            case "playing" -> {
                System.out.println(audioPlayer.getPlayingTrack().getPosition());
                System.out.println(audioPlayer);
            }
            case "dziendobry" -> {
                var filepath = "src/main/resources/dziendobry.mp3";
                audioManager.loadItem(filepath, new AudioLoadResultHandlerImpl(audioPlayer));
            }
            case "play" -> {
                var path = "http://51.68.135.155:80/stream";
                // var filepath = "src/main/resources/dziendobry.mp3";
                audioManager.loadItem(path, new AudioLoadResultHandlerImpl(audioPlayer));
            }
            case "playlastlink" -> {
                audioManager.loadItem(lastLink, new AudioLoadResultHandlerImpl(audioPlayer));
            }
            case "dc" -> {
                AudioManager audioManager = event.getGuild().getAudioManager();
                if (audioManager.isConnected()) {
                    audioManager.closeAudioConnection();
                }
            }
            case "drop" -> {
                audioPlayer.stopTrack();
            }
            case "stop" -> {
                stoppedTrack = audioPlayer.getPlayingTrack().makeClone();
                audioPlayer.stopTrack();
            }
            case "resume" -> {
                audioPlayer.playTrack(stoppedTrack);
            }
            default -> {
                // throw new DiscordMessageException("Inappropriate message");
                if (rawMessage.startsWith("https://")) {
                    lastLink = rawMessage;
                    System.out.println("ddd");
                    System.out.println(lastLink);
                }
            }

        }
    }
}


