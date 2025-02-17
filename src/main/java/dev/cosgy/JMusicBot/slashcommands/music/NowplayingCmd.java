/*
 * Copyright 2018 John Grosh (jagrosh).
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package dev.cosgy.JMusicBot.slashcommands.music;

import com.jagrosh.jdautilities.command.CommandEvent;
import com.jagrosh.jmusicbot.Bot;
import com.jagrosh.jmusicbot.audio.AudioHandler;
import dev.cosgy.JMusicBot.slashcommands.MusicCommand;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.events.interaction.SlashCommandEvent;
import net.dv8tion.jda.api.interactions.InteractionHook;

/**
 * @author John Grosh <john.a.grosh@gmail.com>
 */
public class NowplayingCmd extends MusicCommand {
    public NowplayingCmd(Bot bot) {
        super(bot);
        this.name = "nowplaying";
        this.help = "現在再生中の曲を表示します";
        this.aliases = bot.getConfig().getAliases(this.name);
        this.botPermissions = new Permission[]{Permission.MESSAGE_EMBED_LINKS};
    }

    @Override
    public void doCommand(CommandEvent event) {
        AudioHandler handler = (AudioHandler) event.getGuild().getAudioManager().getSendingHandler();
        Message m = handler.getNowPlaying(event.getJDA());
        if (m == null) {
            event.reply(handler.getNoMusicPlaying(event.getJDA()));
            bot.getNowplayingHandler().clearLastNPMessage(event.getGuild());
        } else {
            event.reply(m, bot.getNowplayingHandler()::setLastNPMessage);
        }
    }

    @Override
    public void doCommand(SlashCommandEvent event) {
        AudioHandler handler = (AudioHandler) event.getGuild().getAudioManager().getSendingHandler();
        Message m = handler.getNowPlaying(event.getJDA());
        event.reply("現在再生中の楽曲を表示します...").queue(h -> h.deleteOriginal().queue());

        if (m == null) {
            event.getTextChannel().sendMessage(handler.getNoMusicPlaying(event.getJDA())).queue();
            bot.getNowplayingHandler().clearLastNPMessage(event.getGuild());
        } else {
            event.getTextChannel().sendMessage(m).queue(bot.getNowplayingHandler()::setLastNPMessage);
        }
    }
}
