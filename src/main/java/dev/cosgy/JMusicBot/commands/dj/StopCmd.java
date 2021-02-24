package dev.cosgy.JMusicBot.commands.dj;

import com.jagrosh.jdautilities.command.CommandEvent;
import com.jagrosh.jmusicbot.Bot;
import com.jagrosh.jmusicbot.audio.AudioHandler;
import com.jagrosh.jmusicbot.audio.QueuedTrack;
import com.jagrosh.jmusicbot.commands.DJCommand;
import com.jagrosh.jmusicbot.queue.FairQueue;
import dev.cosgy.JMusicBot.playlist.CacheLoader;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class StopCmd {
    public static class SaveCmd extends DJCommand {
        Logger log = LoggerFactory.getLogger("Stop");
        public SaveCmd(Bot bot)
        {
            super(bot);
            this.name = "save";
            this.aliases = new String[] {"s"};
            this.help = "再生リストにある曲を保存します。";
            this.bePlaying = false;
        }

        @Override
        public void doCommand(CommandEvent event) {
            AudioHandler handler = (AudioHandler) event.getGuild().getAudioManager().getSendingHandler();
            CacheLoader cache = bot.getCacheLoader();
            FairQueue<QueuedTrack> queue = handler.getQueue();
            handler.stopAndClear();
            event.getGuild().getAudioManager().closeAudioConnection();

            if(!queue.isEmpty()) {
                cache.Save(event.getGuild().toString(), handler.getQueue());
                event.reply(event.getClient().getSuccess() + " 再生待ちの"+ queue.size() +"曲を保存して再生を停止しました。");
            }else{
                event.reply(event.getClient().getSuccess() + " 再生待ちを削除して、再生を停止しました。");
            }
            log.info(event.getGuild().getName()+"で再生待ちを保存してボイスチャンネルから切断しました。");
        }
    }
}
