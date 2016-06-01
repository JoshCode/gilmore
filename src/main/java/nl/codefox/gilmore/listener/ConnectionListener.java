package nl.codefox.gilmore.listener;

import net.dv8tion.jda.events.guild.member.GuildMemberJoinEvent;
import net.dv8tion.jda.hooks.ListenerAdapter;

public class ConnectionListener extends ListenerAdapter {

    @Override
    public void onGuildMemberJoin(GuildMemberJoinEvent event) {
        StringBuilder builder = new StringBuilder();

        builder.append(String.format("**Critical Role Discord Chat Rules**\n", event.getUser().getAsMention()));
        builder.append("```#1 - Each channel says at the top if Critical Role spoilers are allowed or not. NO SPOILERS IN GENERAL CHAT.\n");
        builder.append("#2 - Don't Be a Dick. Otherwise, WELCOME!!!\n");
        builder.append("#3 - As long as you follow the above rules, we have chat channels for everything.  Playing games (Overwatch, Tabletop Simulatior, Rocket League, etc.), organizing DND/RPG games (LFG), discussing Critical Role or Game of Thrones or artwork or programming. We do it all in this community!\n");
        builder.append("#4 - If it is Thursday, we discuss the show #live in the channel #live, come join us there!```");
        builder.append("*I am a bot, and this message was automatic. Is it Thursday yet?*");

        event.getUser().getPrivateChannel().sendMessage(builder.toString());
    }

}
