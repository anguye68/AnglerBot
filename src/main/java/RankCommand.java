import com.jagrosh.jdautilities.command.Command;
import com.jagrosh.jdautilities.command.CommandEvent;
import com.jagrosh.jdautilities.doc.standard.CommandInfo;
import com.jagrosh.jdautilities.examples.doc.Author;
import net.dv8tion.jda.core.entities.User;

@CommandInfo(
        name = {"rank"},
        description = "Checks user's rank"
)
@Author("Dunkasaur")
public class RankCommand extends Command {


    public RankCommand() {
        this.name = "rank";
        this.help = "checks user's rank";
        this.guildOnly = true;
    }

    @Override
    protected void execute(CommandEvent e) {
        if (!e.getMessage().getMentionedUsers().isEmpty()) {
            String mentioned = e.getMessage().getMentionedUsers().get(0).getId();
            String username = e.getMessage().getMentionedUsers().get(0).getName();
            int currentExp = Levels.getCurrentExp(mentioned);
            int level = Levels.getLevel(currentExp);
            int rank = Levels.getRank(mentioned);

            e.reply(username + "\nTotal EXP: `" + currentExp + "`\nLevel: `" + level + "`\nRank: `" + rank + "/" + Levels.getTotalMembers() + "`");
        } else {
            if (e.getArgs().isEmpty()) {
                // Retrieve message information such as author, message contents, and channel type
                User author = e.getAuthor();
                String id = author.getId();

                int currentExp = Levels.getCurrentExp(id);
                int level = Levels.getLevel(currentExp);
                int rank = Levels.getRank(id);

                e.reply(e.getAuthor().getName() + "\nTotal EXP: `" + currentExp + "`\nLevel: `" + level + "`\nRank: `" + rank + "/" + Levels.getTotalMembers() + "`");
            } else {
                String user = e.getArgs();
                String name = e.getGuild().getMemberById(Long.parseLong(user)).getEffectiveName();
                int currentExp = Levels.getCurrentExp(user);
                int level = Levels.getLevel(currentExp);
                int rank = Levels.getRank(user);

                e.reply(name + "\nTotal EXP: `" + currentExp + "`\nLevel: `" + level + "`\nRank: `" + rank + "/" + Levels.getTotalMembers() + "`");
            }
        }
    }
}
