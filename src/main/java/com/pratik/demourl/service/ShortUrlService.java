package com.pratik.demourl.service;

import com.pratik.demourl.model.LinkRecord;
import com.pratik.demourl.repository.LinkRecordRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.net.URI;
import java.security.SecureRandom;
import java.time.Instant;
import java.util.concurrent.atomic.AtomicLong;

@Service
public class ShortUrlService {

    private static final String BASE62 =
            "0123456789abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ";

    private static final int CODE_LENGTH = 6;

    private final SecureRandom random = new SecureRandom();
    private final LinkRecordRepository linkRecordRepository;

    public ShortUrlService(LinkRecordRepository linkRecordRepository) {
        this.linkRecordRepository = linkRecordRepository;
    }

    private String generateCode() {

        StringBuilder code = new StringBuilder(CODE_LENGTH);

        for (int i = 0; i < CODE_LENGTH; i++) {
            int index = random.nextInt(BASE62.length());
            code.append(BASE62.charAt(index));
        }

        return code.toString();
    }

    @Transactional
    public LinkRecord create(String originalUrl) {
        validateUrl(originalUrl);

        String code;
        do {
            code = generateCode();
        } while (linkRecordRepository.existsById(code));

        LinkRecord link = new LinkRecord(code, originalUrl.trim(), Instant.now());
        return linkRecordRepository.save(link);
    }

    @Transactional(readOnly = true)
    public LinkRecord find(String code) {
        return linkRecordRepository.findById(code).orElse(null);
    }

    private void validateUrl(String url) {
        if (url == null || url.isBlank()) {
            throw new IllegalArgumentException("URL is required");
        }

        try {
            URI uri = URI.create(url.trim());
            String scheme = uri.getScheme();
            if (!("http".equalsIgnoreCase(scheme) || "https".equalsIgnoreCase(scheme))
                    || uri.getHost() == null) {
                throw new IllegalArgumentException("URL must be a valid http or https URL");
            }
        } catch (IllegalArgumentException ex) {
            throw new IllegalArgumentException("URL must be a valid http or https URL");
        }
    }

    private String generateCode1() {

        StringBuilder code = new StringBuilder(CODE_LENGTH);

        for (int i = 0; i < CODE_LENGTH; i++) {
            int index = random.nextInt(BASE62.length());
            code.append(BASE62.charAt(index));
        }

        return code.toString();
    }
}
