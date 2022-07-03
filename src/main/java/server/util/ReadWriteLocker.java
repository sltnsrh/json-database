package server.util;

import java.util.concurrent.locks.ReentrantReadWriteLock;

public final class ReadWriteLocker {
    public static final ReentrantReadWriteLock readWriteLock;
    public static final ReentrantReadWriteLock.WriteLock writeLock;
    public static final ReentrantReadWriteLock.ReadLock readLock;

    private ReadWriteLocker() {
    }

    static {
        readWriteLock = new ReentrantReadWriteLock(true);
        writeLock = readWriteLock.writeLock();
        readLock = readWriteLock.readLock();
    }
}
