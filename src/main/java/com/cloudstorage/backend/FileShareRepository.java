package com.cloudstorage.backend;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface FileShareRepository extends JpaRepository<FileShare, Long> {

    List<FileShare> findByFileId(Long fileId);

    List<FileShare> findBySharedWithUserId(Long sharedWithUserId);
}