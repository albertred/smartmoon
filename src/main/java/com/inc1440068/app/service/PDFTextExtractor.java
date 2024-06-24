package com.inc1440068.app.service;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.text.PDFTextStripper;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.Base64;

@Service
public class PDFTextExtractor {
    public String extract(String base64Encoded) {
        byte[] content = Base64.getDecoder().decode(base64Encoded);
        try (PDDocument document = PDDocument.load(content)) {
            PDFTextStripper pdfStripper = new PDFTextStripper();
            String text = pdfStripper.getText(document);
            return text;
        } catch (IOException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }
}
