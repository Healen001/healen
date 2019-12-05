package com.tencent.interact.service;

import com.sun.imageio.plugins.gif.GIFImageReader;
import com.sun.imageio.plugins.gif.GIFImageReaderSpi;
import com.sun.imageio.plugins.gif.GIFImageWriter;
import com.sun.imageio.plugins.gif.GIFImageWriterSpi;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.imageio.spi.ImageReaderSpi;
import javax.imageio.spi.ImageWriterSpi;
import javax.imageio.stream.FileImageInputStream;
import javax.imageio.stream.FileImageOutputStream;
import java.io.*;
import java.util.List;

/**
 * @Description TODO
 * @Author healen
 * @Date 2019/7/8 9:26
 */
@Service
public class UploadService {
    @Value("${file.location.ffmpegPath}")
    private String ffmpegPath;
    // 日志
    public final Logger logger = LoggerFactory.getLogger(getClass());
    /**
     * 获取指定视频的帧并保存为图片至指定目录
     *
     * @param filePath       视频存放的地址
     * @param targerFilePath 截图存放的地址
     * @return
     * @throws Exception
     */
    public boolean executeCodecs(String filePath, String targerFilePath) throws Exception {

        FileImageInputStream in = null;
        FileImageOutputStream out = null;
        try {
            in = new FileImageInputStream(new File(filePath));
            ImageReaderSpi readerSpi = new GIFImageReaderSpi();
            GIFImageReader gifReader = (GIFImageReader) readerSpi.createReaderInstance();
            gifReader.setInput(in);
            int num = gifReader.getNumImages(true);
            // 要取的帧数要小于总帧数
            if (num > 5) {
                ImageWriterSpi writerSpi = new GIFImageWriterSpi();
                GIFImageWriter writer = (GIFImageWriter) writerSpi.createWriterInstance();
                for (int i = 0; i < num; i++) {
                    if (i == 5) {
                        File newfile = new File(targerFilePath);
                        out = new FileImageOutputStream(newfile);
                        writer.setOutput(out);
                        //    读取读取帧的图片
                        writer.write(gifReader.read(i));
                        return true;
                    }
                }
            }
            return true;
        } catch (Exception e) {

            e.printStackTrace();
            return false;
        }
    }

    public boolean getThumb(String videoFilePath, String thumbFilePath) {
        File file = new File(videoFilePath);
        if (!file.exists()) {
            logger.error("路径[" + videoFilePath + "]对应的视频文件不存在!");
            return false;
        }
        File file1 = new File(thumbFilePath);
        if (file1.exists()){
            file1.delete();
        }
        List<String> commands = new java.util.ArrayList<String>();
        commands.add(ffmpegPath);
        commands.add("-i");
        commands.add(videoFilePath);
        commands.add("-f");
        commands.add("image2");
        commands.add("-ss");
        // 这个参数是设置截取视频多少秒时的画面
        commands.add("1");
        commands.add(thumbFilePath);
        try {
            ProcessBuilder builder = new ProcessBuilder();
            builder.command(commands);
            Process process = builder.start();
            InputStream stderr = process.getErrorStream();
            InputStreamReader isr = new InputStreamReader(stderr);
            BufferedReader br = new BufferedReader(isr);
            String line;
            while ((line = br.readLine()) != null);
            process.waitFor();
            if(br != null)
                br.close();
            if(isr != null)
                isr.close();
            if(stderr != null)
                stderr.close();
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
}
