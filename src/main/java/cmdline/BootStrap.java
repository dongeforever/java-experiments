package cmdline;

import com.beust.jcommander.JCommander;
import fs.FileRead;
import fs.FileWrite;
import java.util.HashMap;
import java.util.Map;

public class BootStrap {
    public static void main(String args[]) {
        Map<String, BaseCommand> commands = new HashMap<>();
        commands.put("fw", new FileWrite());
        commands.put("fr", new FileRead());

        JCommander.Builder builder = JCommander.newBuilder();
        for (String cmd : commands.keySet()) {
            builder.addCommand(cmd, commands.get(cmd));
        }
        JCommander jc = builder.build();
        jc.parse(args);

        if (jc.getParsedCommand() == null) {
            jc.usage();
        } else {
            BaseCommand command = commands.get(jc.getParsedCommand());
            if (command != null) {
                command.doCommand();
            } else {
                jc.usage();
            }
        }
    }
}
