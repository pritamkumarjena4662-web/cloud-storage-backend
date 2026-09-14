package com.cloudstorage.backend;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface FolderRepository extends JpaRepository<Folder, Long> {

    List<Folder> findByUserId(Long userId);

    List<Folder> findByUserIdAndParentFolderId(Long userId, Long parentFolderId);
}