package edu.tarleton.drdup2.index;

import java.io.IOException;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;

/**
 * The class that represents the memory mapped file.
 * 
 * @author Zdenek Tronicek, tronicek@tarleton.edu
 */
public class MappedFile implements AutoCloseable {

    private final FileChannel.MapMode mode;
    private final int pageSize;
    private final FileChannel channel;
    private MappedByteBuffer[] buffers;
    private long position;

    public static MappedFile open(String fileName, FileChannel.MapMode mode, int pageSize) throws IOException {
        MappedFile mf = new MappedFile(fileName, mode, pageSize);
        mf.mapFile();
        return mf;
    }
    
    public static MappedFile initialize(String fileName, FileChannel.MapMode mode, int pageSize) throws IOException {
        MappedFile mf = new MappedFile(fileName, mode, pageSize);
        mf.truncate();
        mf.mapFile();
        return mf;
    }

    private MappedFile(String fileName, FileChannel.MapMode mode, int pageSize) throws IOException {
        this.mode = mode;
        this.pageSize = pageSize;
        Path path = Paths.get(fileName);
        channel = FileChannel.open(path, StandardOpenOption.READ, StandardOpenOption.WRITE, StandardOpenOption.CREATE);
    }
    
    private void truncate() throws IOException {
        channel.truncate(0L);
    }

    private void mapFile() throws IOException {
        int n = (int) (channel.size() / pageSize) + 1;
        buffers = new MappedByteBuffer[n];
        for (int i = 0; i < n; i++) {
            buffers[i] = channel.map(mode, (long) i * pageSize, pageSize);
        }
    }

    private void mapIfNeeded(int page) throws IOException {
        if (page < buffers.length) {
            return;
        }
        MappedByteBuffer[] nb = new MappedByteBuffer[page + 1];
        System.arraycopy(buffers, 0, nb, 0, buffers.length);
        for (int i = buffers.length; i <= page; i++) {
            nb[i] = channel.map(mode, (long) i * pageSize, pageSize);
        }
        buffers = nb;
    }

    @Override
    public void close() throws IOException {
        channel.close();
    }

    public void seek(long position) {
        this.position = position;
    }
    
    public int readShort() throws IOException {
        int page = (int) (position / pageSize);
        int index = (int) (position % pageSize);
        mapIfNeeded(page);
        position += 2;
        return buffers[page].getShort(index);
    }

    public int readInt() throws IOException {
        int page = (int) (position / pageSize);
        int index = (int) (position % pageSize);
        mapIfNeeded(page);
        position += 4;
        return buffers[page].getInt(index);
    }

    public long readLong() throws IOException {
        int page = (int) (position / pageSize);
        int index = (int) (position % pageSize);
        mapIfNeeded(page);
        position += 8;
        return buffers[page].getLong(index);
    }

    public void writeShort(short value) throws IOException {
        int page = (int) (position / pageSize);
        int index = (int) (position % pageSize);
        mapIfNeeded(page);
        position += 2;
        buffers[page].putShort(index, value);
    }

    public void writeInt(int value) throws IOException {
        int page = (int) (position / pageSize);
        int index = (int) (position % pageSize);
        mapIfNeeded(page);
        position += 4;
        buffers[page].putInt(index, value);
    }

    public void writeLong(long value) throws IOException {
        int page = (int) (position / pageSize);
        int index = (int) (position % pageSize);
        mapIfNeeded(page);
        position += 8;
        buffers[page].putLong(index, value);
    }
}
