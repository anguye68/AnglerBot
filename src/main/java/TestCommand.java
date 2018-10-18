import com.jagrosh.jdautilities.command.Command;
import com.jagrosh.jdautilities.command.CommandEvent;
import com.jagrosh.jdautilities.doc.standard.CommandInfo;
import com.jagrosh.jdautilities.examples.doc.Author;
import net.dv8tion.jda.core.entities.Member;

import java.util.List;

@CommandInfo(
        name = {"test"},
        description = "test"
)
@Author("Dunkasaur")
public class TestCommand extends Command {


    public TestCommand() {
        this.name = "test";
        this.help = "checks user's rank";
        this.guildOnly = true;
    }

    @Override
    protected void execute(CommandEvent e) {
        List<Member> members = e.getGuild().getMembers();
        String user = e.getArgs();
        String person = "";
        String id = "";

        for (int i = 0; i < members.size(); i++) {
            if (members.get(i).getEffectiveName().toLowerCase().contains(user.toLowerCase())) {
                person = members.get(i).getEffectiveName();
                id = members.get(i).getUser().getId();
            }
        }

        e.reply(person + "\n" + id);
    }
}
