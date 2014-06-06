package com.turnstile;

import org.ghost4j.document.PDFDocument;
import org.ghost4j.renderer.SimpleRenderer;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.RenderedImage;
import java.io.File;
import java.io.IOException;
import java.util.List;

public class GhostPDFReader {

    static GhostPDFReader singleton = null;

    public static GhostPDFReader SingleInstance() {
        if (singleton == null) singleton = new GhostPDFReader();
        return singleton;
    }

    private static final String ERR_FILE_OPEN = "Error while trying to open the input file. Please make sure the " +
            "PDF file is readable!";
    private static final String ERR_RENDER_PDF = "Error while processing the input file. Please make sure you " +
            "have provided a proper PDF file!";

    private static final String ERR_SAVE_JPEG = "Error while saving temporary results! Make sure you are running " +
            "the program in the right directory!";

    public String[] Import(String pdfPath, String exportPath) throws Exception{

        File pdfFile = new File(pdfPath);
        PDFDocument document = new PDFDocument();

        try {
            document.load(pdfFile);
        } catch (IOException e) {
            throw new Exception(ERR_FILE_OPEN);
        }

        SimpleRenderer renderer = new SimpleRenderer();
        renderer.setResolution(300);

        List<Image> images = null;
        try {
            images = renderer.render(document);
        } catch (Exception e) {
            throw new Exception(ERR_RENDER_PDF);
        }

        String[] paths = new String[images.size()];

        for (int i = 0; i < images.size(); i++) {
            Image img= images.get(i);
            paths[i] = exportPath + i + ".jpg";
            try {
                ImageIO.write((RenderedImage)img, "jpg",  new File(paths[i]));
            } catch (IOException e) {
                throw new Exception(ERR_SAVE_JPEG);
            }
        }

        return paths;
    }
}
