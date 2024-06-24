package com.inc1440068.app.service;

import org.apache.poi.hwpf.HWPFDocument;
import org.apache.poi.hwpf.extractor.WordExtractor;
import org.apache.poi.xwpf.extractor.XWPFWordExtractor;
import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.springframework.stereotype.Service;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Base64;

@Service
public class WordTextExtractor {
    public String extractDocx(String base64Encoded) {
        byte[] content = Base64.getDecoder().decode(base64Encoded);
        try (InputStream inputStream = new ByteArrayInputStream(content)) {
            XWPFDocument document = new XWPFDocument(inputStream);
            XWPFWordExtractor extractor = new XWPFWordExtractor(document);
            String text = extractor.getText();
            return text;
        } catch (IOException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }

    public String extractDoc(String base64Encoded) {
        byte[] content = Base64.getDecoder().decode(base64Encoded);
        try (InputStream inputStream = new ByteArrayInputStream(content)) {
            HWPFDocument document = new HWPFDocument(inputStream);
            WordExtractor extractor = new WordExtractor(document);
            String text = extractor.getText();
            return text;
        } catch (IOException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }
}
