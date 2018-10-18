import com.jagrosh.jdautilities.command.CommandClient;
import com.jagrosh.jdautilities.command.CommandClientBuilder;
import net.dv8tion.jda.core.AccountType;
import net.dv8tion.jda.core.JDA;
import net.dv8tion.jda.core.JDABuilder;
import net.dv8tion.jda.core.entities.ChannelType;
import net.dv8tion.jda.core.entities.Message;
import net.dv8tion.jda.core.entities.MessageChannel;
import net.dv8tion.jda.core.entities.User;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;
import net.dv8tion.jda.core.hooks.ListenerAdapter;

import javax.security.auth.login.LoginException;

public class Main extends ListenerAdapter {
    private static final String TOKEN = "TOKEN";
    public static final String PREFIX = "!";
    public static void main(String[] args) {
        try {
            CommandClientBuilder builder = new CommandClientBuilder();

            builder.setPrefix(PREFIX);
            builder.setOwnerId("132830963653804032");

            builder.addCommands(new RankCommand(),
                                new TestCommand());

            CommandClient client = builder.build();

            JDA jda = new JDABuilder(AccountType.BOT).setToken(TOKEN).build();
            jda.addEventListener(new Main());
            jda.addEventListener(client);
        } catch (LoginException e) {
            System.out.println(e.getMessage());
        }
    }

    /**
     *
     * Override onMessageReceived from the ListenerAdapter class to ensure it is actually overriding the
     * super class properly.
     *
     */
    @Override
    public void onMessageReceived(MessageReceivedEvent e) {
        // Retrieve message information such as author, message contents, and channel type
        User author = e.getAuthor();
        Message message = e.getMessage();
        MessageChannel channel = e.getChannel();

        // Returns a human readable version of the message
        String msg = message.getContentDisplay();

        // Ignore DMs and other bots
        if (!e.isFromType(ChannelType.PRIVATE) || !author.isBot()) {
            int exp = 1+msg.length()/5;
            String id = author.getId();

            if (Levels.userExists(id)) {
                exp += Levels.getCurrentExp(id);
                Levels.updateUser(id, exp);
            } else {
                Levels.addUser(id, exp);
            }

        }
    }
}
