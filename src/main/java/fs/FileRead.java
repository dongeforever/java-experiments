package fs;

import cmdline.BaseCommand;
import com.beust.jcommander.Parameter;
import fs.base.DefaultMmapFile;
import fs.base.MmapFile;
import fs.base.MmapFileList;
import fs.base.SelectMmapBufferResult;
import java.io.File;
import java.nio.ByteBuffer;
import utils.UtilAll;

public class FileRead extends BaseCommand {


    @Parameter(names = {"--dir", "-d"}, description = "The directory to store the test data")
    private String dir =  System.getProperty("user.home") + File.separator + "mmapteststore";

    @Parameter(names = {"--size", "-s"}, description = "The file size")
    private int size = 1024;


    @Parameter(names = {"--count", "-c"}, description = "The file count")
    private int fileCount = 10;

    @Parameter(names = {"--gap", "-g"}, description = "The read gap")
    private long gap = 100 * 1024;

    @Parameter(names = {"--file", "-f"}, description = "Whether to use the file channel")
    private boolean fileChannel = false;


    @Parameter(names = {"--random", "-r"}, description = "Whether to use MADV_RANDOM")
    private boolean random = false;


    private int fileSize = 1024 * 1024 * 1024;



    @Override public void doCommand() {

        MmapFileList mmapFileList = new MmapFileList(dir, fileSize);
        mmapFileList.load();
        long maxSize =  (long) fileSize * fileCount;
        long start = System.currentTimeMillis();
        int num = 0;
        if (random) {
            for (MmapFile mmapFile: mmapFileList.getMappedFiles()) {
                ((DefaultMmapFile) mmapFile).madvise(true);
            }
        }
        for (long i = 0; i < maxSize; i += gap) {
            MmapFile mmapFile = mmapFileList.findMappedFileByOffset(i);
            if (mmapFile == null) {
                break;
            }
            num++;
            if (fileChannel) {
                ByteBuffer buffer = ByteBuffer.allocate(size);
                mmapFile.getData((int) (i % fileSize), size, buffer);
                continue;
            }
            SelectMmapBufferResult sbr = mmapFile.selectMappedBuffer((int) (i % fileSize), size);
            sbr.getByteBuffer().get(new byte[size]);
        }
        log.info("[{}] size={} count={} cost={} tps={}", this.getClass().getSimpleName(), fileSize, fileCount, UtilAll.elapsed(start), num * 1000.0/UtilAll.elapsed(start));

    }
}
