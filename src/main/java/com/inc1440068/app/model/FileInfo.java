package com.inc1440068.app.model;

public class FileInfo {
    private String filename;
    private String content;

    public String getFilename() {
        return filename;
    }

    public FileInfo setFilename(String filename) {
        this.filename = filename;
        return this;
    }

    public String getContent() {
        return content;
    }

    public FileInfo setContent(String content) {
        this.content = content;
        return this;
    }
}
