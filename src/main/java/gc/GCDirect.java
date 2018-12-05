package gc;

import cmdline.BaseCommand;
import com.beust.jcommander.Parameter;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.List;
import sun.nio.ch.DirectBuffer;
import utils.UtilAll;

/**
 * java -Xmx100m -Xms100m -verbose:gc -XX:+PrintGCDetails -XX:MaxDirectMemorySize=15g  -jar java-experiments.jar dgc
 */

public class GCDirect extends BaseCommand {

    @Parameter(names = {"-hold", "-h"}, description = "Whether to hold the reference")
    private boolean hold = false;


    @Parameter(names = {"--count", "-c"}, description = "The memory count")
    private int fileCount = 10 * 1000;




    @Parameter(names = {"-gc", "-g"}, description = "Whether to call System.gc()")
    private boolean gc = false;


    @Override public void doCommand() {
        ByteBuffer.allocate(1024 * 1024 * 1024);
        long start = System.currentTimeMillis();
        List<ByteBuffer> buffers = new ArrayList<>(fileCount);
        for (int i = 0; i < fileCount; i++) {
            ByteBuffer buffer = ByteBuffer.allocateDirect(1024 * 1024);
            //((DirectBuffer) buffer).cleaner().clean();
            buffer.put(new byte[1024 * 1024]);
            if (hold) {
                buffers.add(buffer);
            }
        }
        if (gc) {
            System.gc();
        }
        log.info("size={} cost={} gc={}", buffers.size(), UtilAll.elapsed(start), gc);
        UtilAll.sleep(10000000);
    }
}
