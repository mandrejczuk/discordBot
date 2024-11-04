package mandrejczuk.audio;

import com.sedmelluq.discord.lavaplayer.player.AudioLoadResultHandler;
import com.sedmelluq.discord.lavaplayer.player.AudioPlayerManager;
import com.sedmelluq.discord.lavaplayer.player.DefaultAudioPlayerManager;
import com.sedmelluq.discord.lavaplayer.source.AudioSourceManagers;
import com.sedmelluq.discord.lavaplayer.tools.FriendlyException;
import com.sedmelluq.discord.lavaplayer.track.AudioItem;
import com.sedmelluq.discord.lavaplayer.track.AudioPlaylist;
import com.sedmelluq.discord.lavaplayer.track.AudioTrack;
import com.sedmelluq.discord.lavaplayer.track.AudioTrackInfo;
import mandrejczuk.configuration.Configuration;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Member;

import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.channel.concrete.VoiceChannel;

import java.util.*;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.CompletableFuture;


public class GuildPlayer {

    private final Guild guild;

    private final AudioPlayerWrapper audioPlayerWrapper;

    private final AudioPlayerManager audioPlayerManager;

    private Platform platform;

    private Radio radio = new Radio("maryja","http://51.68.135.155:80/stream");

    private VoiceChannel currentChannel;

    public GuildPlayer(Guild guild) {

        this.guild= guild;
        this.audioPlayerManager = new DefaultAudioPlayerManager();
        var player = audioPlayerManager.createPlayer();
        var trackScheduler = new TrackScheduler(player);
        player.addListener(trackScheduler);
        this.audioPlayerWrapper = new AudioPlayerWrapper(trackScheduler,guild);
        AudioSourceManagers.registerLocalSource(audioPlayerManager);
        AudioSourceManagers.registerRemoteSources(audioPlayerManager);
        this.platform = Platform.SOUNDCLOUD;
    }


    public Radio getRadio(){
        return radio;
    }
    public void setRadio(Radio radio)
    {
        this.radio = radio;
    }

    public Platform getPlatform() {
        return platform;
    }

    public void setPlatform(Platform platform) {
        this.platform = platform;
    }

    public void joinChannel(Member member)
    {
        for (var voiceChannel : member.getGuild().getVoiceChannels())
        {
            if(voiceChannel.getMembers().contains(member))
            {
                joinChannel(voiceChannel);
                return;
            }
        }
    }
    public void  joinChannel(VoiceChannel channel)
    {
            channel.getGuild().getAudioManager().setSendingHandler(audioPlayerWrapper);
            channel.getGuild().getAudioManager().openAudioConnection(channel);
            currentChannel = channel;

    }

    public void skipPlaying()
    {
        audioPlayerWrapper.getTrackScheduler().getPlayer().stopTrack();
    }

    public void unpause()
    {
        audioPlayerWrapper.getTrackScheduler().getPlayer().setPaused(false);
    }

    public void pause()
    {
        audioPlayerWrapper.getTrackScheduler().getPlayer().setPaused(true);
    }

    public CompletableFuture<AudioItem> play(String url)
    {
        CompletableFuture<AudioItem> loadedItem = new CompletableFuture<>();
        audioPlayerManager.loadItem(url,new AudioLoadResultHandlerImpl(audioPlayerWrapper,loadedItem));
        return loadedItem;
    }

    public int getQueueSize()
    {
       return audioPlayerWrapper.getTrackScheduler().getQueue().size();
    }


    public void disconnect()
    {
        if(currentChannel != null)
        {
            pause();
            currentChannel.getGuild().getAudioManager().closeAudioConnection();
            currentChannel = null;
        }
    }

    public VoiceChannel getCurrentChannel() {
        return currentChannel;
    }
    public AudioTrackInfo getPlayingTrackInfo()
    {
        return audioPlayerWrapper.getTrackScheduler().getPlayer().getPlayingTrack().getInfo();
    }
    public List<AudioTrack> getQueue()
    {
        return new LinkedList<>(audioPlayerWrapper.getTrackScheduler().getQueue());
    }
    public void moveTrackFromTo(int source, int destination)
    {
        if(destination >= audioPlayerWrapper.getTrackScheduler().getQueue().size() || source >= audioPlayerWrapper.getTrackScheduler().getQueue().size())
        {
            throw new  IllegalArgumentException("no tyle to nie");
        }
        else if(destination < 0 || source < 0)
        {
            throw new  IllegalArgumentException("no tak tez nie");
        }
        else {
            List<AudioTrack> list = new LinkedList<>(audioPlayerWrapper.getTrackScheduler().getQueue());
            list.forEach(e-> System.out.println(e.getInfo().title));
            list.add(destination,list.get(source));
            list.forEach(e-> System.out.println(e.getInfo().title));
           if(destination > source)
           {
               list.remove(source + 1);
           }
           else {
               list.remove(source);
           }
            audioPlayerWrapper.getTrackScheduler().getQueue().clear();
            list.forEach(el->audioPlayerWrapper.getTrackScheduler().getQueue().offer(el));
            audioPlayerWrapper.getTrackScheduler().getQueue().forEach(el-> System.out.println(el.getInfo().title));
        }
    }

    public void removeTrackAt(int index)
    {
        if(index < audioPlayerWrapper.getTrackScheduler().getQueue().size() && index >= 0)
        {
            List<AudioTrack> list = new LinkedList<>(audioPlayerWrapper.getTrackScheduler().getQueue());
            list.remove(index);
            audioPlayerWrapper.getTrackScheduler().getQueue().clear();
            list.forEach(el->audioPlayerWrapper.getTrackScheduler().getQueue().offer(el));

        }
        else
        {
            throw  new IllegalArgumentException("podaj dobry index ok");
        }
    }


}
