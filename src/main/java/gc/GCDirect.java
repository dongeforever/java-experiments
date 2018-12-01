package gc;

import cmdline.BaseCommand;
import com.beust.jcommander.Parameter;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.List;
import utils.UtilAll;

public class GCDirect extends BaseCommand {

    @Parameter(names = {"-hold", "-h"}, description = "Whether to hold the reference")
    private boolean hold = false;



    @Parameter(names = {"-gc", "-g"}, description = "Whether to call System.gc()")
    private boolean gc = false;


    @Override public void doCommand() {
        long start = System.currentTimeMillis();
        List<ByteBuffer> buffers = new ArrayList<>(10 * 1024);
        for (int i = 0; i < 10 * 1024; i++) {
            ByteBuffer buffer = ByteBuffer.allocateDirect(1024 * 1024);
            buffer.put(new byte[1024]);
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
