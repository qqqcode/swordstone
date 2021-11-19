package com.qqq.swordstone.util;

import org.lwjgl.BufferUtils;

import java.io.*;
import java.net.URL;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;

public class IOUtil {

    public static ByteBuffer resizeBuffer(ByteBuffer buffer, int newCapacity) {
        ByteBuffer newBuffer = BufferUtils.createByteBuffer(newCapacity);
        buffer.flip();
        newBuffer.put(buffer);
        return newBuffer;
    }

    public static ByteBuffer inputSteamToByteBuffer(InputStream inputStream,int bufferSize){
        ByteBuffer buffer = BufferUtils.createByteBuffer(bufferSize);
        try {
            byte[] buf = new byte[bufferSize];
            while (true) {
                int bytes = inputStream.read(buf, 0, buf.length);
                if (bytes == -1)
                    break;
                if (buffer.remaining() < bytes)
                    buffer = IOUtil.resizeBuffer(buffer, Math.max(buffer.capacity() * 2, buffer.capacity() - buffer.remaining() + bytes));
                buffer.put(buf, 0, bytes);
            }
            buffer.flip();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                inputStream.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return buffer;
    }

    public static ByteBuffer ioResourceToByteBuffer(String resource, int bufferSize) throws IOException {
        ByteBuffer buffer;
        URL url = Thread.currentThread().getContextClassLoader().getResource(resource);
        if (url == null)
            throw new IOException("Classpath resource not found: " + resource);
        File file = new File(url.getFile());
        if (file.isFile()) {
            FileInputStream fis = new FileInputStream(file);
            FileChannel fc = fis.getChannel();
            buffer = fc.map(FileChannel.MapMode.READ_ONLY, 0, fc.size());
            fc.close();
            fis.close();
        } else {
            buffer = BufferUtils.createByteBuffer(bufferSize);
            InputStream source = url.openStream();
            if (source == null)
                throw new FileNotFoundException(resource);
            try {
                byte[] buf = new byte[8192];
                while (true) {
                    int bytes = source.read(buf, 0, buf.length);
                    if (bytes == -1)
                        break;
                    if (buffer.remaining() < bytes)
                        buffer = resizeBuffer(buffer, Math.max(buffer.capacity() * 2, buffer.capacity() - buffer.remaining() + bytes));
                    buffer.put(buf, 0, bytes);
                }
                buffer.flip();
            } finally {
                source.close();
            }
        }
        return buffer;
    }
}
