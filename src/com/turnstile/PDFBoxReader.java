package com.turnstile;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.List;

public class PDFBoxReader {

    static PDFBoxReader singleton = null;

    public static PDFBoxReader SingleInstance() {
        if (singleton == null) singleton = new PDFBoxReader();
        return singleton;
    }

    private static final String ERR_FILE_OPEN = "Error while trying to open the input file. Please make sure the " +
            "PDF file is readable!";
    private static final String ERR_RENDER_PDF = "Error while processing the input file. Please make sure you " +
            "have provided a proper PDF file!";

    private static final String ERR_SAVE_JPEG = "Error while saving temporary results! Make sure you are running " +
            "the program in the right directory!";

    public String[] Import(String pdfPath, String exportPath) throws Exception{

        PDDocument document = null;
        try {
            document = PDDocument.load(pdfPath);
        } catch (IOException e) {
            throw new Exception(ERR_FILE_OPEN);
        }
        List<PDPage> pages = document.getDocumentCatalog().getAllPages();

        String[] paths = new String[pages.size()];

        for (int i = 0; i < pages.size(); i++) {
            PDPage page = pages.get(i);
            BufferedImage image = null;
            try {
                image = page.convertToImage(BufferedImage.TYPE_INT_RGB, 72);
            } catch (IOException e) {
                throw new Exception(ERR_RENDER_PDF);
            }
            paths[i] = exportPath + i + ".jpg";
            try {
                ImageIO.write(image, "jpg", new File(paths[i]));
            } catch (IOException e) {
                throw new Exception(ERR_SAVE_JPEG);
            }
        }

        return paths;
    }
}
