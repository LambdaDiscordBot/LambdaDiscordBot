package bot.java.lambda.command.commands.info;

import bot.java.lambda.command.CommandContext;
import bot.java.lambda.command.HelpCategory;
import bot.java.lambda.command.ICommand;
import bot.java.lambda.config.Config;
import bot.java.lambda.utils.Utils;
import com.jagrosh.jdautilities.commons.utils.FinderUtil;
import me.duncte123.botcommons.messaging.EmbedUtils;
import net.dv8tion.jda.api.entities.*;

import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Collectors;

public class UserInfoCommand implements ICommand {
    @Override
    public void handle(CommandContext ctx) {
        final List<String> args = ctx.getArgs();
        final TextChannel channel = ctx.getChannel();
        final Message message = ctx.getMessage();

        if (args.isEmpty()) {
            final User user = message.getAuthor();
            final Member member = ctx.getGuild().getMember(user);

            if (member == null) {
                channel.sendMessage("No users found for `" + String.join(" ", ctx.getArgs()) + "`").queue();
                return;
            }

            final String status = member.getOnlineStatus().name().toLowerCase();
            String utilStatus = "";
            if (status.startsWith("on")) utilStatus = "on";
            else if (status.startsWith("off")) utilStatus = "off";
            else if (status.startsWith("i")) utilStatus = "idle";
            else if (status.startsWith("d")) utilStatus = "dnd";

            final MessageEmbed embed = EmbedUtils.getDefaultEmbed()
                    .setAuthor(String.format("%#s", user), user.getEffectiveAvatarUrl(), user.getEffectiveAvatarUrl())
                    .setTitle(Utils.getStatusAsEmote(utilStatus) + " " + member.getEffectiveName())
                    .setThumbnail(user.getEffectiveAvatarUrl().replace("gif", "png"))
                    .setColor(member.getColor())
                    .addField("User Id + Mention", String.format("%s {%s}", user.getId(), member.getAsMention()), true)
                    .addField("Account Created", user.getTimeCreated().format(DateTimeFormatter.RFC_1123_DATE_TIME), true)
                    .addBlankField(true)
                    .addField("Guild Joined", member.getTimeJoined().format(DateTimeFormatter.RFC_1123_DATE_TIME), true)
                    .addField("Bot Account", user.isBot() ? "<:TickYes:755716208191602738> " : "<:TickNo:755716160472875079>", true)
                    .build();

            channel.sendMessage(embed).queue();
            return;
        }

        final List<Member> mentionedMembers = message.getMentionedMembers();
        ///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
        if (mentionedMembers.isEmpty()) {
            final String joined = String.join(" ", args);
            List<User> foundUsers = FinderUtil.findUsers(joined, ctx.getJDA());

            if (foundUsers.isEmpty()) {
                List<Member> foundMembers = FinderUtil.findMembers(joined, ctx.getGuild());

                if (foundMembers.isEmpty()) {
                    channel.sendMessage("No users found for `" + joined + "`").queue();
                    return;
                }

                foundUsers = foundMembers.stream().map(Member::getUser).collect(Collectors.toList());

            }

            User user = foundUsers.get(0);
            Member member = ctx.getGuild().getMember(user);

            if (member == null) {
                channel.sendMessage("No users found for `" + joined + "`").queue();
                return;
            }

            final String status = member.getOnlineStatus().name().toLowerCase();
            String utilStatus = "";
            if (status.startsWith("on")) utilStatus = "on";
            else if (status.startsWith("off")) utilStatus = "off";
            else if (status.startsWith("i")) utilStatus = "idle";
            else if (status.startsWith("d")) utilStatus = "dnd";

            final MessageEmbed embed = EmbedUtils.getDefaultEmbed()
                    .setAuthor(String.format("%#s", user), user.getEffectiveAvatarUrl(), user.getEffectiveAvatarUrl())
                    .setTitle(member.getEffectiveName())
                    .setThumbnail(Utils.getStatusAsEmote(utilStatus) + " " + user.getEffectiveAvatarUrl().replace("gif", "png"))
                    .setColor(member.getColor())
                    .addField("User Id + Mention", String.format("%s {%s}", user.getId(), member.getAsMention()), true)
                    .addField("Account Created", user.getTimeCreated().format(DateTimeFormatter.RFC_1123_DATE_TIME), true)
                    .addBlankField(true)
                    .addField("Guild Joined", member.getTimeJoined().format(DateTimeFormatter.RFC_1123_DATE_TIME), true)
                    .addField("Bot Account", user.isBot() ? "<:TickYes:755716208191602738> " : "<:TickNo:755716160472875079>", true)
                    .build();

            channel.sendMessage(embed).queue();
            return;
        }
        ///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
        final Member member = mentionedMembers.get(0);
        final User user = member.getUser();
        final String status = member.getOnlineStatus().name().toLowerCase();
        String utilStatus = "";
        if (status.startsWith("on")) utilStatus = "on";
        else if (status.startsWith("off")) utilStatus = "off";
        else if (status.startsWith("i")) utilStatus = "idle";
        else if (status.startsWith("d")) utilStatus = "dnd";
        final MessageEmbed embed = EmbedUtils.getDefaultEmbed()
                .setAuthor(String.format("%#s", user), user.getEffectiveAvatarUrl(), user.getEffectiveAvatarUrl())
                .setTitle(Utils.getStatusAsEmote(utilStatus) + " " + member.getEffectiveName())
                .setThumbnail(user.getEffectiveAvatarUrl().replace("gif", "png"))
                .setColor(member.getColor())
                .addField("User Id + Mention", String.format("%s {%s}", user.getId(), member.getAsMention()), true)
                .addField("Account Created", user.getTimeCreated().format(DateTimeFormatter.RFC_1123_DATE_TIME), true)
                .addBlankField(true)
                .addField("Guild Joined", member.getTimeJoined().format(DateTimeFormatter.RFC_1123_DATE_TIME), true)
                .addField("Bot Account", user.isBot() ? "<:TickYes:755716208191602738> " : "<:TickNo:755716160472875079>", true)
                .build();

        channel.sendMessage(embed).queue();

    }

    @Override
    public String getName() {
        return "userinfo";
    }

    @Override
    public String getHelp() {
        return "Displays information about a user\n" +
                "Usage : " + Config.get("prefix") + "userinfo < username / @user / user_id >";
    }

    @Override
    public HelpCategory getHelpCategory() {
        return HelpCategory.INFO;
    }
}
