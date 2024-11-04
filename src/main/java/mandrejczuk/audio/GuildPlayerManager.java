package mandrejczuk.audio;

import com.sedmelluq.discord.lavaplayer.player.AudioPlayer;
import net.dv8tion.jda.api.entities.Guild;

import javax.swing.plaf.PanelUI;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class GuildPlayerManager {

    private final Map<Long, GuildPlayer> audioPlayerMap = new ConcurrentHashMap<>();


    private static class SingletonHolder{
        private static final GuildPlayerManager guildPlayerManager = new GuildPlayerManager();
    }
    public static GuildPlayerManager getInstance()
    {
        return SingletonHolder.guildPlayerManager;
    }

    public GuildPlayer getOrCreate(Guild guild)
    {
      return   audioPlayerMap.computeIfAbsent(guild.getIdLong(),s-> new GuildPlayer(guild));
    }

    public boolean existsById(Long id)
    {
        return audioPlayerMap.containsKey(id);
    }
    public void remove(Guild guild)
    {
        audioPlayerMap.remove(guild.getIdLong());
    }
    public int playingCount()
    {
        return audioPlayerMap.size();
    }
}
