package cmdline;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public abstract class BaseCommand {
    public static Logger log = LoggerFactory.getLogger("java-experiments");


    public abstract void doCommand();
}
