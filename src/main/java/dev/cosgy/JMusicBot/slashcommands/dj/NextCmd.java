/*
 *  Copyright 2021 Cosgy Dev (info@cosgy.dev).
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *        http://www.apache.org/licenses/LICENSE-2.0
 *
 *   Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */

package dev.cosgy.JMusicBot.slashcommands.dj;

import com.jagrosh.jdautilities.command.CommandEvent;
import com.jagrosh.jmusicbot.Bot;
import com.jagrosh.jmusicbot.audio.AudioHandler;
import com.sedmelluq.discord.lavaplayer.track.AudioTrack;
import dev.cosgy.JMusicBot.slashcommands.DJCommand;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.events.interaction.SlashCommandEvent;

public class NextCmd extends DJCommand {
    public NextCmd(Bot bot)
    {
        super(bot);
        this.name = "next";
        this.help = "リピートモードが有効な場合、再生待ちから削除せずに現在の曲をスキップします";
        this.aliases = bot.getConfig().getAliases(this.name);
        this.bePlaying = true;
    }

    @Override
    public void doCommand(CommandEvent event) {
        AudioHandler handler = (AudioHandler)event.getGuild().getAudioManager().getSendingHandler();
        User u = event.getJDA().getUserById(handler.getRequester());

        AudioTrack track = handler.getPlayer().getPlayingTrack();
        handler.addTrackIfRepeat(track);

        event.reply(event.getClient().getSuccess()+" **"+handler.getPlayer().getPlayingTrack().getInfo().title
                +"**をスキップしました。 ("+(u==null ? "誰か" : "**"+u.getName()+"**")+"がリクエストしました。)");
        handler.getPlayer().stopTrack();
    }

    @Override
    public void doCommand(SlashCommandEvent event) {
        if(!checkDJPermission(client, event)){
            event.reply(client.getWarning()+"権限がないため実行できません。").queue();
            return;
        }
        AudioHandler handler = (AudioHandler)event.getGuild().getAudioManager().getSendingHandler();
        User u = event.getJDA().getUserById(handler.getRequester());

        AudioTrack track = handler.getPlayer().getPlayingTrack();
        handler.addTrackIfRepeat(track);

        event.reply(client.getSuccess()+" **"+handler.getPlayer().getPlayingTrack().getInfo().title
                +"**をスキップしました。 ("+(u==null ? "誰か" : "**"+u.getName()+"**")+"がリクエストしました。)").queue();
        handler.getPlayer().stopTrack();
    }
}
