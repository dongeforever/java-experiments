package fs;

import cmdline.BaseCommand;
import com.beust.jcommander.Parameter;
import fs.base.DefaultMmapFile;
import fs.base.MmapFile;
import fs.base.MmapFileList;
import java.io.File;
import utils.UtilAll;

public class FileWrite extends BaseCommand {


    @Parameter(names = {"--dir", "-d"}, description = "The directory to store the test data")
    private String dir =  System.getProperty("user.home") + File.separator + "mmapteststore";

    @Parameter(names = {"--size", "-s"}, description = "The file size")
    private int size = 1024;

    @Parameter(names = {"--count", "-c"}, description = "The file count")
    private int fileCount = 10;

    @Parameter(names = {"--file", "-f"}, description = "Whether to use the file channel")
    private boolean fileChannel = false;

    @Parameter(names = {"--random", "-r"}, description = "Whether to use MADV_RANDOM")
    private boolean random = false;



    private int fileSize = 1024 * 1024 * 1024;

    @Override public void doCommand() {

        MmapFileList mmapFileList = new MmapFileList(dir, fileSize);
        MmapFile last = null;
        long maxSize = (long) fileSize * fileCount;
        long start = System.currentTimeMillis();
        while (mmapFileList.getMaxWrotePosition() < maxSize) {
            mmapFileList.append(new byte[size]);
            MmapFile current =  mmapFileList.getLastMappedFile();
            if (current != last) {
                last = current;
                if (random) {
                    ((DefaultMmapFile) current).madvise(true);
                } else if (fileChannel) {
                    ((DefaultMmapFile) current).setWriteWithFileChannel(true);
                }
            }
        }
        log.info("[{}] maxSize={} cost={}", this.getClass().getSimpleName(), mmapFileList.getMaxWrotePosition(), UtilAll.elapsed(start));
    }
}
