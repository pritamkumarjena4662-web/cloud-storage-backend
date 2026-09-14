package com.cloudstorage.backend;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/folders")
public class FolderController {

    private final FolderRepository folderRepository;

    public FolderController(FolderRepository folderRepository) {
        this.folderRepository = folderRepository;
    }

    @PostMapping
    public ResponseEntity<?> createFolder(
            @RequestParam String folderName,
            @RequestParam Long userId,
            @RequestParam(required = false) Long parentFolderId) {

        if (folderName == null || folderName.trim().isEmpty()) {
            return ResponseEntity.badRequest()
                    .body("Folder name is required");
        }

        Folder folder = new Folder();
        folder.setFolderName(folderName);
        folder.setUserId(userId);
        folder.setParentFolderId(parentFolderId);

        Folder savedFolder = folderRepository.save(folder);

        return ResponseEntity.ok(savedFolder);
    }

    @GetMapping
    public ResponseEntity<List<Folder>> getFolders(
            @RequestParam Long userId) {

        return ResponseEntity.ok(
                folderRepository.findByUserId(userId)
        );
    }

    @GetMapping("/inside")
    public ResponseEntity<List<Folder>> getInsideFolders(
            @RequestParam Long userId,
            @RequestParam Long parentFolderId) {

        return ResponseEntity.ok(
                folderRepository.findByUserIdAndParentFolderId(
                        userId,
                        parentFolderId
                )
        );
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteFolder(
            @PathVariable Long id) {

        if (!folderRepository.existsById(id)) {
            return ResponseEntity.notFound().build();
        }

        folderRepository.deleteById(id);

        return ResponseEntity.ok("Folder deleted successfully");
    }
}