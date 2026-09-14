package com.cloudstorage.backend;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

@RestController
@RequestMapping("/api/files")
public class FileController {

    private final FileRepository fileRepository;

    private final Path uploadDir = Paths.get("uploads");

    public FileController(FileRepository fileRepository) {
        this.fileRepository = fileRepository;
    }

    @PostMapping("/upload")
    public ResponseEntity<?> uploadFile(
            @RequestParam("file") MultipartFile multipartFile,
            @RequestParam("userId") Long userId) {

        try {
            if (multipartFile.isEmpty()) {
                return ResponseEntity.badRequest()
                        .body("Please select a file");
            }

            Files.createDirectories(uploadDir);

            String fileName = multipartFile.getOriginalFilename();

            Path filePath = uploadDir.resolve(fileName);

            Files.write(filePath, multipartFile.getBytes());

            File file = new File();
            file.setFileName(fileName);
            file.setFileType(multipartFile.getContentType());
            file.setFileSize(multipartFile.getSize());
            file.setFilePath(filePath.toString());
            file.setUserId(userId);

            File savedFile = fileRepository.save(file);

            return ResponseEntity.ok(savedFile);

        } catch (IOException e) {
            return ResponseEntity.internalServerError()
                    .body("File upload failed");
        }
    }

    @GetMapping
    public ResponseEntity<List<File>> getAllFiles(
            @RequestParam Long userId) {

        return ResponseEntity.ok(
                fileRepository.findAll()
                        .stream()
                        .filter(file -> file.getUserId().equals(userId))
                        .toList()
        );
    }

    @GetMapping("/download/{id}")
    public ResponseEntity<?> downloadFile(
            @PathVariable Long id) {

        File file = fileRepository.findById(id).orElse(null);

        if (file == null) {
            return ResponseEntity.notFound().build();
        }

        try {
            Path path = Paths.get(file.getFilePath());

            if (!Files.exists(path)) {
                return ResponseEntity.notFound().build();
            }

            byte[] data = Files.readAllBytes(path);

            return ResponseEntity.ok()
                    .header("Content-Disposition",
                            "attachment; filename=\"" + file.getFileName() + "\"")
                    .header("Content-Type",
                            file.getFileType() != null
                                    ? file.getFileType()
                                    : "application/octet-stream")
                    .body(data);

        } catch (IOException e) {
            return ResponseEntity.internalServerError()
                    .body("File download failed");
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteFile(
            @PathVariable Long id) {

        File file = fileRepository.findById(id).orElse(null);

        if (file == null) {
            return ResponseEntity.notFound().build();
        }

        try {
            Files.deleteIfExists(Paths.get(file.getFilePath()));
        } catch (IOException e) {
            return ResponseEntity.internalServerError()
                    .body("Could not delete file");
        }

        fileRepository.deleteById(id);

        return ResponseEntity.ok("File deleted successfully");
    }
}