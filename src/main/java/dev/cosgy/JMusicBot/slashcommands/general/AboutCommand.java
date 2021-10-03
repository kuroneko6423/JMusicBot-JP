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
package dev.cosgy.JMusicBot.slashcommands.general;

import com.jagrosh.jdautilities.command.CommandEvent;
import com.jagrosh.jdautilities.command.SlashCommand;
import com.jagrosh.jdautilities.commons.JDAUtilitiesInfo;
import com.jagrosh.jdautilities.doc.standard.CommandInfo;
import com.jagrosh.jdautilities.examples.doc.Author;
import com.jagrosh.jmusicbot.Bot;
import com.jagrosh.jmusicbot.JMusicBot;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.JDAInfo;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.ApplicationInfo;
import net.dv8tion.jda.api.entities.ChannelType;
import net.dv8tion.jda.api.events.interaction.SlashCommandEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.awt.*;
import java.util.Collections;
import java.util.Objects;

/**
 * @author Cosgy Dev
 */
@CommandInfo(
        name = "About",
        description = "ボットに関する情報を表示します"
)
@Author("Cosgy Dev")
public class AboutCommand extends SlashCommand {
    private final Color color;
    private final Permission[] perms;
    private boolean IS_AUTHOR = true;
    private String REPLACEMENT_ICON = "+";
    private String oauthLink;

    private final Bot bot;

    public AboutCommand(Bot bot) {
        this.color = Color.BLUE.brighter();
        this.bot = bot;
        this.name = "about";
        this.help = "ボットに関する情報を表示します";
        this.guildOnly = false;
        this.perms = JMusicBot.RECOMMENDED_PERMS;
    }

    public void setIsAuthor(boolean value) {
        this.IS_AUTHOR = value;
    }

    public void setReplacementCharacter(String value) {
        this.REPLACEMENT_ICON = value;
    }

    @Override
    protected void execute(SlashCommandEvent event) {
        if (oauthLink == null) {
            try {
                ApplicationInfo info = event.getJDA().retrieveApplicationInfo().complete();
                oauthLink = info.isBotPublic() ? info.getInviteUrl(0L, perms) : "";
            } catch (Exception e) {
                Logger log = LoggerFactory.getLogger("OAuth2");
                log.error("Could not generate invite link ", e);
                oauthLink = "";
            }
        }
        EmbedBuilder builder = new EmbedBuilder();

        builder.setColor(color);

        builder.setAuthor("About " + event.getJDA().getSelfUser().getName() + " !", null, event.getJDA().getSelfUser().getAvatarUrl());

        StringBuilder description = new StringBuilder()
                .append("[招待リンク](").append(oauthLink).append(")").append("\n");

        builder.setDescription(description);

        StringBuilder field;

        // Memory
        long totalMB = Runtime.getRuntime().totalMemory() / 1024 / 1024;
        long freeMB = Runtime.getRuntime().freeMemory() / 1024 / 1024;
        long usedMB = totalMB-freeMB;
        long maxMB = Runtime.getRuntime().maxMemory() / 1024 / 1024;

        // used / 40
        double magnif40 = 40.0 / totalMB;
        int used40 = Math.toIntExact(Math.round(usedMB * magnif40));
        int free40 = 40 - used40;

        // used / 100
        double magnif1000 = 1000.0 / totalMB;
        double used1000 = Math.round(usedMB * magnif1000);

        field = new StringBuilder()
                .append("```fix").append("\n")
                .append("[").append(usedMB).append(" MB / ").append(totalMB).append(" MB] ( MAX ").append(maxMB/1024).append(" GB )").append("\n")
                .append("[")
                .append(
                        String.join("", Collections.nCopies(used40, "#"))
                ).append(
                        String.join("", Collections.nCopies(free40, " "))
                ).append("] [").append(used1000/10).append("%]");

        field.append("```");

        builder.addField("Memory",field.toString(),false);

        field = new StringBuilder()
                .append(
                        event.getJDA().getGuilds().size()
                )
                .append("サーバーで作動中 | ")
                .append(
                        event.getJDA().getGuilds().stream().filter(g -> g.getSelfMember().getVoiceState().inVoiceChannel()).count()
                )
                .append("サーバーに接続中");

        builder.addField("Status",field.toString(),false);

        builder.setFooter("About Command is developed by cron#0001", null);


        event.replyEmbeds(builder.build()).queue();
    }

    @Override
    protected void execute(CommandEvent event) {
        if (oauthLink == null) {
            try {
                ApplicationInfo info = event.getJDA().retrieveApplicationInfo().complete();
                oauthLink = info.isBotPublic() ? info.getInviteUrl(0L, perms) : "";
            } catch (Exception e) {
                Logger log = LoggerFactory.getLogger("OAuth2");
                log.error("Could not generate invite link ", e);
                oauthLink = "";
            }
        }
        EmbedBuilder builder = new EmbedBuilder();

        builder.setColor(color);

        builder.setAuthor("About " + event.getJDA().getSelfUser().getName() + " !", null, event.getJDA().getSelfUser().getAvatarUrl());

        StringBuilder description = new StringBuilder()
                .append("[招待リンク](").append(oauthLink).append(")").append("\n");

        builder.setDescription(description);

        StringBuilder field;

        // Memory
        long totalMB = Runtime.getRuntime().totalMemory() / 1024 / 1024;
        long freeMB = Runtime.getRuntime().freeMemory() / 1024 / 1024;
        long usedMB = totalMB-freeMB;
        long maxMB = Runtime.getRuntime().maxMemory() / 1024 / 1024;

        // used / 40
        double magnif40 = 40.0 / totalMB;
        int used40 = Math.toIntExact(Math.round(usedMB * magnif40));
        int free40 = 40 - used40;

        // used / 100
        double magnif1000 = 1000.0 / totalMB;
        double used1000 = Math.round(usedMB * magnif1000);

        field = new StringBuilder()
                .append("```fix").append("\n")
                .append("[").append(usedMB).append(" MB / ").append(totalMB).append(" MB] ( MAX ").append(maxMB/1024).append(" GB )").append("\n")
                .append("[")
                .append(
                        String.join("", Collections.nCopies(used40, "#"))
                ).append(
                        String.join("", Collections.nCopies(free40, " "))
                ).append("] [").append(used1000/10).append("%]");

        field.append("```");

        builder.addField("Memory",field.toString(),false);

        field = new StringBuilder()
                .append(
                        event.getJDA().getGuilds().size()
                )
                .append("サーバーで作動中 | ")
                .append(
                        event.getJDA().getGuilds().stream().filter(g -> g.getSelfMember().getVoiceState().inVoiceChannel()).count()
                )
                .append("サーバーに接続中");

        builder.addField("Status",field.toString(),false);

        builder.setFooter("About Command is developed by cron#0001", null);


        event.reply(builder.build());
    }

}
