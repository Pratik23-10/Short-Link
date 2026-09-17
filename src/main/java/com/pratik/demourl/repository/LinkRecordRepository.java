package com.pratik.demourl.repository;

import com.pratik.demourl.model.LinkRecord;
import org.springframework.data.jpa.repository.JpaRepository;

public interface LinkRecordRepository extends JpaRepository<LinkRecord, String> {
}
