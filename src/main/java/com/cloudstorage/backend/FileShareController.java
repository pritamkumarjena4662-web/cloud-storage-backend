package com.cloudstorage.backend;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/shares")
public class FileShareController {

    private final FileShareRepository fileShareRepository;

    public FileShareController(FileShareRepository fileShareRepository) {
        this.fileShareRepository = fileShareRepository;
    }

    @PostMapping
    public ResponseEntity<?> shareFile(
            @RequestParam Long fileId,
            @RequestParam Long sharedWithUserId,
            @RequestParam String role) {

        if (!role.equalsIgnoreCase("VIEWER")
                && !role.equalsIgnoreCase("EDITOR")) {
            return ResponseEntity.badRequest()
                    .body("Role must be VIEWER or EDITOR");
        }

        FileShare share = new FileShare();
        share.setFileId(fileId);
        share.setSharedWithUserId(sharedWithUserId);
        share.setRole(role.toUpperCase());

        FileShare savedShare = fileShareRepository.save(share);

        return ResponseEntity.ok(savedShare);
    }

    @GetMapping("/file/{fileId}")
    public ResponseEntity<List<FileShare>> getFileShares(
            @PathVariable Long fileId) {

        return ResponseEntity.ok(
                fileShareRepository.findByFileId(fileId)
        );
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<List<FileShare>> getSharedFiles(
            @PathVariable Long userId) {

        return ResponseEntity.ok(
                fileShareRepository.findBySharedWithUserId(userId)
        );
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> removeShare(
            @PathVariable Long id) {

        if (!fileShareRepository.existsById(id)) {
            return ResponseEntity.notFound().build();
        }

        fileShareRepository.deleteById(id);

        return ResponseEntity.ok("File sharing removed successfully");
    }
}