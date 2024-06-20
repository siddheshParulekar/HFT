package com.thrift.hft.utils;

import com.thrift.hft.entity.UploadDocument;
import com.thrift.hft.exceptions.FileException;
import com.thrift.hft.exceptions.NotFoundException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.Part;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

@Component
public class UploadDocumentsUtils {
    private static final Logger logger = LogManager.getLogger(UploadDocumentsUtils.class);

    public String uploadDocuments(MultipartFile file, String folderPath, String folderName) {
        logger.info("UploadDocumentServiceUtils - Inside uploadDocuments method");
        List<String> attachments = new ArrayList<>();
        if (!Files.exists(Paths.get(folderPath)))
            throw new NotFoundException("ERROR_FILE_PATH");
        UploadDocument document = new UploadDocument();
        if (!Files.exists(Paths.get(folderPath + folderName))) {
            File newFolder = new File(folderPath + folderName);
            if (!newFolder.mkdir())
                throw new NotFoundException("ERROR_FOLDER_CREATION");
        }
        if (file == null)
            throw new NotFoundException("ERROR_MISSING_FILE");


        try {
            int dotIndex = file.getOriginalFilename().lastIndexOf(".");
            String fileExtension = file.getOriginalFilename().substring(dotIndex);
            String fileName = file.getOriginalFilename().substring(0, dotIndex);
            document.setName(fileName + "-" + DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.SSS").format(LocalDateTime.now()) + fileExtension);
            document.setData(file.getBytes());
            saveDocument(folderPath, folderName, document, file);
        } catch (Exception ex) {
            logger.error("exception",ex);
            throw new FileException();
        }
        String attach = folderPath + folderName + "/" + document.getName();
        attachments.add(attach);
        return attach;
    }

    private void saveDocument(String folderPath, String folderName, UploadDocument document, MultipartFile file) throws IOException {
        logger.info("UploadDocumentServiceUtils - Inside saveDocument method");
        String taskDocumentLatestPath = folderPath + folderName + "/";
        Path path = Paths.get(taskDocumentLatestPath + document.getName());
        Files.write(path, file.getBytes());
    }


    public String uploadDocuments(Part file, String folderPath, String folderName) {
        logger.info("UploadDocumentServiceUtils - Inside uploadDocuments method");
        List<String> attachments = new ArrayList<>();
        if (!Files.exists(Paths.get(folderPath))) {
            throw new NotFoundException("ERROR_FILE_PATH");
        }
        UploadDocument document = new UploadDocument();
        if (!Files.exists(Paths.get(folderPath + folderName))) {
            File newFolder = new File(folderPath + folderName);
            if (!newFolder.mkdir()) {
                throw new NotFoundException("ERROR_FOLDER_CREATION");
            }
        }
        if (file == null) {
            throw new NotFoundException("ERROR_MISSING_FILE");
        }

        try {
            String fileName = Paths.get(file.getSubmittedFileName()).getFileName().toString();
            int dotIndex = fileName.lastIndexOf(".");
            String fileExtension = fileName.substring(dotIndex);
            String baseName = fileName.substring(0, dotIndex);
            document.setName(baseName + "-" + DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.SSS").format(LocalDateTime.now()) + fileExtension);
            document.setData(file.getInputStream().readAllBytes());
            saveDocument(folderPath, folderName, document, file);
        } catch (Exception ex) {
            logger.error("exception", ex);
            throw new FileException();
        }
        String attach = folderPath + folderName + "/" + document.getName();
        attachments.add(attach);
        return attach;
    }

    private void saveDocument(String folderPath, String folderName, UploadDocument document, Part file) throws IOException {
        logger.info("UploadDocumentServiceUtils - Inside saveDocument method");
        String taskDocumentLatestPath = folderPath + folderName + "/";
        Path path = Paths.get(taskDocumentLatestPath + document.getName());
        Files.write(path, file.getInputStream().readAllBytes());
    }
}
