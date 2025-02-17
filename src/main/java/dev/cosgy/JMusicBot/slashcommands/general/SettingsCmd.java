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
package dev.cosgy.JMusicBot.slashcommands.general;

import com.jagrosh.jdautilities.command.CommandEvent;
import com.jagrosh.jdautilities.command.SlashCommand;
import com.jagrosh.jmusicbot.Bot;
import com.jagrosh.jmusicbot.settings.Settings;
import com.jagrosh.jmusicbot.utils.FormatUtil;
import dev.cosgy.JMusicBot.settings.RepeatMode;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.MessageBuilder;
import net.dv8tion.jda.api.entities.Role;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.entities.VoiceChannel;
import net.dv8tion.jda.api.events.interaction.SlashCommandEvent;

import java.util.Objects;

/**
 * @author John Grosh <john.a.grosh@gmail.com>
 */
public class SettingsCmd extends SlashCommand {
    private final static String EMOJI = "\uD83C\uDFA7"; // 🎧

    public SettingsCmd(Bot bot) {
        this.name = "settings";
        this.help = "Botの設定を表示します";
        this.aliases = bot.getConfig().getAliases(this.name);
        this.guildOnly = true;
    }

    @Override
    protected void execute(SlashCommandEvent event) {
        Settings s = client.getSettingsFor(event.getGuild());
        MessageBuilder builder = new MessageBuilder()
                .append(EMOJI + " **")
                .append(FormatUtil.filter(event.getJDA().getSelfUser().getName()))
                .append("** の設定:");
        TextChannel tChan = s.getTextChannel(event.getGuild());
        VoiceChannel vChan = s.getVoiceChannel(event.getGuild());
        Role role = s.getRole(event.getGuild());
        EmbedBuilder ebuilder = new EmbedBuilder()
                .setDescription("コマンド実行用チャンネル: " + (tChan == null ? "なし" : "**#" + tChan.getName() + "**")
                        + "\n専用ボイスチャンネル: " + (vChan == null ? "なし" : "**" + vChan.getName() + "**")
                        + "\nDJ 権限: " + (role == null ? "未設定" : "**" + role.getName() + "**")
                        + "\nリピート: **" + (s.getRepeatMode() == RepeatMode.ALL ? "有効(全曲リピート)" : (s.getRepeatMode() == RepeatMode.SINGLE ? "有効(1曲リピート)" : "無効")) + "**"
                        + "\nデフォルトプレイリスト: " + (s.getDefaultPlaylist() == null ? "なし" : "**" + s.getDefaultPlaylist() + "**")
                )
                .setFooter(String.format(
                                "%s 個のサーバーに参加 | %s 個のボイスチャンネルに接続",
                                event.getJDA().getGuilds().size(),
                                event.getJDA().getGuilds().stream().filter(g -> Objects.requireNonNull(g.getSelfMember().getVoiceState()).inVoiceChannel()).count()),
                        null);
        event.replyEmbeds(ebuilder.build()).queue();
    }

    @Override
    protected void execute(CommandEvent event) {
        Settings s = event.getClient().getSettingsFor(event.getGuild());
        MessageBuilder builder = new MessageBuilder()
                .append(EMOJI + " **")
                .append(FormatUtil.filter(event.getSelfUser().getName()))
                .append("** の設定:");
        TextChannel tChan = s.getTextChannel(event.getGuild());
        VoiceChannel vChan = s.getVoiceChannel(event.getGuild());
        Role role = s.getRole(event.getGuild());
        EmbedBuilder ebuilder = new EmbedBuilder()
                .setColor(event.getSelfMember().getColor())
                .setDescription("コマンド実行用チャンネル: " + (tChan == null ? "なし" : "**#" + tChan.getName() + "**")
                        + "\n専用ボイスチャンネル: " + (vChan == null ? "なし" : "**" + vChan.getName() + "**")
                        + "\nDJ 権限: " + (role == null ? "未設定" : "**" + role.getName() + "**")
                        + "\nリピート: **" + (s.getRepeatMode() == RepeatMode.ALL ? "有効(全曲リピート)" : (s.getRepeatMode() == RepeatMode.SINGLE ? "有効(1曲リピート)" : "無効")) + "**"
                        + "\nデフォルトプレイリスト: " + (s.getDefaultPlaylist() == null ? "なし" : "**" + s.getDefaultPlaylist() + "**")
                )
                .setFooter(String.format(
                                "%s 個のサーバーに参加 | %s 個のボイスチャンネルに接続",
                                event.getJDA().getGuilds().size(),
                                event.getJDA().getGuilds().stream().filter(g -> Objects.requireNonNull(g.getSelfMember().getVoiceState()).inVoiceChannel()).count()),
                        null);
        event.getChannel().sendMessage(builder.setEmbed(ebuilder.build()).build()).queue();
    }

}
