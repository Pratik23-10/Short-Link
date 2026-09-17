package com.pratik.demourl.model;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

import java.time.Instant;

@Entity
@Table(name = "short_links")
public class LinkRecord {

    @Id
    private String code;

    private String originalUrl;

    private Instant createdAt;

    protected LinkRecord() {
    }

    public LinkRecord(String code, String originalUrl, Instant createdAt) {
        this.code = code;
        this.originalUrl = originalUrl;
        this.createdAt = createdAt;
    }

    public String getCode() {
        return code;
    }

    public String getOriginalUrl() {
        return originalUrl;
    }

    public Instant getCreatedAt() {
        return createdAt;
    }
}
