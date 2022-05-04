package com.launchersoft.cnabparser.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.launchersoft.cnabparser.model.CNABFile;

@Repository
public interface CNABFileRepository extends JpaRepository<CNABFile, String> {

}
