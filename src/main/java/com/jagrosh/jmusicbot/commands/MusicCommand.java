/*
 * Copyright 2018-2020 Cosgy Dev
 *
 *   Licensed under the Apache License, Version 2.0 (the "License");
 *   you may not use this file except in compliance with the License.
 *   You may obtain a copy of the License at
 *
 *       http://www.apache.org/licenses/LICENSE-2.0
 *
 *   Unless required by applicable law or agreed to in writing, software
 *   distributed under the License is distributed on an "AS IS" BASIS,
 *   WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *   See the License for the specific language governing permissions and
 *   limitations under the License.
 */
package com.jagrosh.jmusicbot.commands;

import com.jagrosh.jdautilities.command.Command;
import com.jagrosh.jdautilities.command.CommandEvent;
import com.jagrosh.jmusicbot.Bot;
import com.jagrosh.jmusicbot.audio.AudioHandler;
import com.jagrosh.jmusicbot.audio.QueuedTrack;
import com.jagrosh.jmusicbot.settings.Settings;
import com.jagrosh.jmusicbot.utils.FormatUtil;
import dev.cosgy.JMusicBot.playlist.CacheLoader;
import dev.cosgy.JMusicBot.util.MaintenanceInfo;
import net.dv8tion.jda.api.entities.GuildVoiceState;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.entities.VoiceChannel;
import net.dv8tion.jda.api.exceptions.PermissionException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.text.ParseException;

/**
 * @author John Grosh <john.a.grosh@gmail.com>
 */
public abstract class MusicCommand extends Command {
    Logger log = LoggerFactory.getLogger("MusicCommand");

    protected final Bot bot;
    protected boolean bePlaying;
    protected boolean beListening;

    public MusicCommand(Bot bot) {
        this.bot = bot;
        this.guildOnly = true;
        this.category = new Category("Music");
    }

    @Override
    protected void execute(CommandEvent event) {
        Settings settings = event.getClient().getSettingsFor(event.getGuild());
        TextChannel tchannel = settings.getTextChannel(event.getGuild());
        if (bot.getConfig().getCosgyDevHost()) {
            try {
                MaintenanceInfo.CommandInfo(event);
            } catch (IOException | ParseException e) {
                e.printStackTrace();
            }
        }
        if (tchannel != null && !event.getTextChannel().equals(tchannel)) {
            try {
                event.getMessage().delete().queue();
            } catch (PermissionException ignore) {
            }
            event.replyInDm(event.getClient().getError() + String.format("コマンドは%sでのみ実行できます", tchannel.getAsMention()));
            return;
        }
        bot.getPlayerManager().setUpHandler(event.getGuild()); // no point constantly checking for this later
        if (bePlaying && !((AudioHandler) event.getGuild().getAudioManager().getSendingHandler()).isMusicPlaying(event.getJDA())) {
            event.reply(event.getClient().getError() + "コマンドを使用するには、再生中である必要があります。");
            return;
        }
        if (beListening) {
            VoiceChannel current = event.getGuild().getSelfMember().getVoiceState().getChannel();
            if (current == null)
                current = settings.getVoiceChannel(event.getGuild());
            GuildVoiceState userState = event.getMember().getVoiceState();
            if (!userState.inVoiceChannel() || userState.isDeafened() || (current != null && !userState.getChannel().equals(current))) {
                event.replyError(String.format("このコマンドを使用するには、%sに参加している必要があります！", (current == null ? "音声チャンネル" : "**" + current.getName() + "**")));
                return;
            }
            if (!event.getGuild().getSelfMember().getVoiceState().inVoiceChannel()) {
                try {
                    event.getGuild().getAudioManager().openAudioConnection(userState.getChannel());
                    // キャッシュファイルがあった場合は自動的に読み込んで再生リストに追加します。
                    // Javaができる人に怒られそうなコードを書いたような気がする...
                    if(bot.getCacheLoader().cacheExists(event.getGuild().toString())) {
                        CacheLoader.Cache cache;
                        cache = bot.getCacheLoader().GetCache(event.getGuild().toString());
                        CacheLoader.Cache finalCache = cache;
                        event.getChannel().sendMessage(":calling: キャッシュファイルを読み込んでいます... (" + cache.getItems().size() + "曲)").queue(m ->
                        {
                            AudioHandler handler = (AudioHandler) event.getGuild().getAudioManager().getSendingHandler();
                            finalCache.loadTracks(bot.getPlayerManager(), (at) -> handler.addTrack(new QueuedTrack(at, event.getAuthor())), () -> {
                                StringBuilder builder = new StringBuilder(finalCache.getTracks().isEmpty()
                                        ? event.getClient().getWarning() + " 楽曲がロードされていません。"
                                        : event.getClient().getSuccess() + " キャッシュファイルから," + "**" + finalCache.getTracks().size() + "**曲読み込みました。");
                                if (!finalCache.getErrors().isEmpty())
                                    builder.append("\n以下の楽曲をロードできませんでした:");
                                finalCache.getErrors().forEach(err -> builder.append("\n`[").append(err.getIndex() + 1).append("]` **").append(err.getItem()).append("**: ").append(err.getReason()));
                                String str = builder.toString();
                                if (str.length() > 2000)
                                    str = str.substring(0, 1994) + " (以下略)";
                                m.editMessage(FormatUtil.filter(str)).queue();
                            });
                        });
                    }
                } catch (PermissionException ex) {
                    event.reply(event.getClient().getError() + String.format("**%s**に接続できません!", userState.getChannel().getName()));
                    return;
                }
            }
        }

        doCommand(event);
    }

    public abstract void doCommand(CommandEvent event);
}
