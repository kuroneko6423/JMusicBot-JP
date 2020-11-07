package dev.cosgy.JMusicBot.commands.general;

import com.jagrosh.jdautilities.command.Command;
import com.jagrosh.jdautilities.command.CommandEvent;
import net.dv8tion.jda.api.Permission;

public class InviteCommand extends Command {
    public InviteCommand() {
        this.name = "invite";
        this.help = "Botの招待用URLを表示します。";
        this.guildOnly = false;
    }

    @Override
    protected void execute(CommandEvent event) {
        long botId = event.getSelfUser().getIdLong();
        Permission[] permissions = new Permission[] {Permission.MANAGE_CHANNEL, Permission.MANAGE_ROLES, Permission.MESSAGE_MANAGE, Permission.NICKNAME_CHANGE, Permission.MESSAGE_READ, Permission.MESSAGE_WRITE, Permission.VOICE_CONNECT, Permission.VOICE_SPEAK, Permission.VIEW_CHANNEL, Permission.MESSAGE_EXT_EMOJI};

        event.replyFormatted("https://discord.com/oauth2/authorize?client_id=%s&scope=bot&permissions=%s", botId, Permission.getRaw(permissions));
    }


}
