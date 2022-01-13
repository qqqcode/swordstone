package com.qqq.swordstone.util;

import org.lwjgl.BufferUtils;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.*;
import java.net.URL;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.util.ArrayList;

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

    public static BufferedReader getBufferedReader(String resource) {
        InputStream resourceAsStream = Thread.currentThread().getContextClassLoader().getResourceAsStream(resource);
        return new BufferedReader(new InputStreamReader(resourceAsStream));
    }

    public static BufferedImage getPic(String path){
        BufferedImage bufferImage = null;
        try {
            bufferImage = ImageIO.read(new File(path));
        } catch (IOException e) {
            e.printStackTrace();
        }
        return bufferImage;
    }

    public static boolean combinePic(BufferedImage bgimage,BufferedImage hdimage,int x,int y,int width,int height) {
        //创建画板
        Graphics2D g2d = bgimage.createGraphics();
        //画图
        g2d.drawImage(hdimage, x, y, width, height, null);
        //释放资源
        g2d.dispose();
        return true;
    }

    public static boolean saveImage(BufferedImage image,String savePath) {
        try {
            ImageIO.write(image, "jpg",new FileOutputStream(savePath) );
        } catch (IOException e) {
            e.printStackTrace();
        }
        return true;
    }

    public static void main(String[] args) {
        //加载图片
        BufferedImage backgroupImage = getPic("D:\\workspace\\blender\\a.png");

        for (int i = 0; i < 60; i++) {
            int tem = i / 10;
            int row = i %10;
            BufferedImage pic = null;
            if (tem == 0) {
                pic = getPic("D:\\workspace\\blender\\000" + i + ".png");
            } else {
                pic = getPic("D:\\workspace\\blender\\00" + i + ".png");
            }
            combinePic(backgroupImage,pic,100 * row,100 * tem,100,100);
        }

        //保存图片
        saveImage(backgroupImage, "newImage.png");
    }
}
