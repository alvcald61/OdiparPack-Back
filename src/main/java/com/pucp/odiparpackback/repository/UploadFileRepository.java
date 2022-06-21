package com.pucp.odiparpackback.repository;

import com.pucp.odiparpackback.enums.FileType;
import com.pucp.odiparpackback.model.UploadFile;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UploadFileRepository extends JpaRepository<UploadFile, Long> {
  UploadFile findOneByFileTypeOrderByUploadDateDesc(FileType fileType);
}
