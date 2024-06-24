package com.inc1440068.app.service;

import com.inc1440068.app.model.FileInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
public class FilesService {
    private static final Set<String> IMAGE_EXTENSIONS = new HashSet<>(Arrays.asList(
            "jpg", "jpeg", "png", "gif", "bmp", "tiff", "tif", "webp"
    ));

    @Autowired
    private WordTextExtractor wordTextExtractor;

    @Autowired
    private PDFTextExtractor pdfTextExtractor;

    public boolean isImage(FileInfo info) {
        String filename = info.getFilename().toLowerCase();
        return IMAGE_EXTENSIONS.stream().allMatch(e -> filename.endsWith("." + e));
    }

    public String generateAdditionalPrompt(List<FileInfo> files) {
        StringBuilder sb = new StringBuilder();

        files.forEach(info -> {
            sb.append(info.getFilename() + ":").append("\n");
            if (info.getFilename().toLowerCase().endsWith(".pdf")) {
                sb.append(pdfTextExtractor.extract(info.getContent())).append("\n");
            } else if (info.getFilename().toLowerCase().endsWith(".docx")) {
                sb.append(wordTextExtractor.extractDocx(info.getContent())).append("\n");
            } else if (info.getFilename().toLowerCase().endsWith(".doc")) {
                sb.append(wordTextExtractor.extractDoc(info.getContent())).append("\n");
            } else if (isImage(info)) {
                throw new IllegalStateException("Image file is not supported yet.");
            } else {
                sb.append(info.getContent()).append("\n");
            }
        });
        return sb.toString();
    }
}
