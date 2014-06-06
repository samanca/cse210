package com.turnstile;

import java.io.*;
import java.nio.channels.FileChannel;
import java.nio.ByteBuffer;
import com.sun.pdfview.*;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.awt.Image;
import java.awt.Graphics2D;
import javax.imageio.ImageIO;

public class PDFReader {

    static PDFReader singleton = null;

    public static PDFReader SingleInstance() {
        if (singleton == null) singleton = new PDFReader();
        return singleton;
    }

    public String[] Import(String pdfPath, String exportPath) throws Exception{

        File pdfFile = new File(pdfPath);
        RandomAccessFile raf;
        try {
            raf = new RandomAccessFile(pdfFile, "r");
        }
        catch (FileNotFoundException ex) {
            throw new Exception("Unable to read input PDF file!");
        }
        FileChannel channel = raf.getChannel();
        ByteBuffer buf;
        try {
            buf = channel.map(FileChannel.MapMode.READ_ONLY, 0, channel.size());
        }
        catch (IOException ex) {
            throw new Exception("Unable to read input PDF file!");
        }

        PDFFile pdf;
        try {
            pdf = new PDFFile(buf);
        }
        catch (IOException ex) {
            throw new Exception("Unable to open input PDF file!");
        }

        String[] images = new String[pdf.getNumPages()];
        for (int i = 0; i < pdf.getNumPages(); i++) {

            PDFPage page = pdf.getPage(i + 1);

            // create the image
            Rectangle rect = new Rectangle(0, 0, (int) page.getBBox().getWidth(),
                    (int) page.getBBox().getHeight());
            BufferedImage bufferedImage = new BufferedImage(rect.width, rect.height,
                    BufferedImage.TYPE_INT_RGB);

            Image image = page.getImage(rect.width,
                    rect.height,                // width & height
                    rect,                       // clip rect
                    null,                       // null for the ImageObserver
                    true,                       // fill background with white
                    true                        // block until drawing is done
            );

            Graphics2D bufImageGraphics = bufferedImage.createGraphics();
            bufImageGraphics.drawImage(image, 0, 0, null);

            if (rect.width > rect.height)
                throw new Exception("Invalid input file! The file must be in a landscape orientation!");

            try {
                images[i] = exportPath + i + ".jpg";
                ImageIO.write(bufferedImage, "JPEG", new File(images[i]));
            }
            catch (IOException ex) {
                throw new Exception("Unable to save data on disk!");
            }
        }

        return images;
    }
}
