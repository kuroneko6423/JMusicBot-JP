package dev.cosgy.JMusicBot.commands.general;

import com.jagrosh.jdautilities.command.Command;
import com.jagrosh.jdautilities.command.CommandEvent;
import com.jagrosh.jmusicbot.Bot;
import dev.cosgy.JMusicBot.util.MaintenanceInfo;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.MessageBuilder;
import net.dv8tion.jda.api.entities.Message;

import java.awt.*;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class InfoCommand extends Command {

    public InfoCommand(Bot bot) {
        this.name = "info";
        this.help = "メンテナンス情報をお知らせします。";
        //this.aliases = bot.getConfig().getAliases(this.name);
        this.guildOnly = false;
    }

    @Override
    protected void execute(CommandEvent event) {
        Calendar Now = Calendar.getInstance();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
        Date NowTime = Now.getTime();
        Message m = event.getChannel().sendMessage("お知らせを受信中...").complete();
        try {
            if (MaintenanceInfo.Verification()) {
                MaintenanceInfo InfoResult = MaintenanceInfo.GetInfo();

                MessageBuilder builder = new MessageBuilder()
                        .append("**" + InfoResult.Title + "**");
                EmbedBuilder ebuilder = new EmbedBuilder()
                        .setColor(Color.orange)
                        .setDescription(InfoResult.Content);
                if (InfoResult.StartTime != "") {
                    ebuilder.addField("開始時刻:", InfoResult.StartTime, false);
                }
                if (InfoResult.EndTime != "") {
                    ebuilder.addField("終了時刻:", InfoResult.EndTime, false);
                }
                ebuilder.addField("更新日時:", InfoResult.LastUpdate, false)
                        .addField("現在時刻", sdf.format(NowTime), false)
                        .setFooter("※メンテナンス期間は予定なく変更する場合があります。", null);
                m.editMessage(builder.setEmbed(ebuilder.build()).build()).queue();

            } else {
                m.editMessage("お知らせはありません。").queue();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}