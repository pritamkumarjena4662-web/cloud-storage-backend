package com.cloudstorage.backend;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface FileRepository extends JpaRepository<File, Long> {

    List<File> findByFileNameContainingIgnoreCase(String fileName);

    List<File> findByUserId(Long userId);

    List<File> findByUserIdAndFileNameContainingIgnoreCase(
            Long userId,
            String fileName
    );
}