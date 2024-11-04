package mandrejczuk.audio;

import com.sedmelluq.discord.lavaplayer.player.AudioPlayer;
import com.sedmelluq.discord.lavaplayer.track.playback.MutableAudioFrame;
import net.dv8tion.jda.api.audio.AudioSendHandler;
import net.dv8tion.jda.api.entities.Guild;

import java.nio.Buffer;
import java.nio.ByteBuffer;

/**
 * This is a wrapper around AudioPlayer which makes it behave as an AudioSendHandler for JDA.
 */
public class AudioPlayerWrapper implements AudioSendHandler {
    private final TrackScheduler trackScheduler;
    private final ByteBuffer buffer;
    private final MutableAudioFrame frame;

    private final Guild guild;

    private int afkTimer;


    public AudioPlayerWrapper(TrackScheduler trackScheduler, Guild guild) {
        this.trackScheduler = trackScheduler;
        this.buffer = ByteBuffer.allocate(2048);
        this.frame = new MutableAudioFrame();
        this.frame.setBuffer(buffer);
        this.guild = guild;
    }


    public TrackScheduler getTrackScheduler() {
        return trackScheduler;
    }

    @Override
    public boolean canProvide() {

        boolean canProvide = trackScheduler.getPlayer().provide(frame);
        if(!canProvide)
        {
            afkTimer += 20; //co 20ms ta metodka leci
            if(afkTimer > 3 * 60 * 1000)
            {
                afkTimer = 0;
                guild.getAudioManager().closeAudioConnection();
            }
            else afkTimer = 0;
        }
        return canProvide;
    }

    @Override
    public ByteBuffer provide20MsAudio() {
        // flip to make it a read buffer
        ((Buffer) buffer).flip();
        return buffer;
    }


    @Override
    public boolean isOpus() {
        return true;
    }

}