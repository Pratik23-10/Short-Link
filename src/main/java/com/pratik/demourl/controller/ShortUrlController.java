package com.pratik.demourl.controller;

import com.pratik.demourl.model.LinkRecord;
import com.pratik.demourl.service.ShortUrlService;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ShortUrlController {

    private final ShortUrlService service;

    public ShortUrlController(ShortUrlService service) {
        this.service = service;
    }

    @PostMapping("/api/urls")
    public ResponseEntity<CreateResponse> create(@RequestBody CreateRequest request) {
        LinkRecord link = service.create(request.url());
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(new CreateResponse(link.getCode(), "/" + link.getCode(), link.getOriginalUrl()));
    }

    @GetMapping("/{code:[A-Za-z0-9]+}")
    public ResponseEntity<Void> redirect(@PathVariable String code) {
        LinkRecord link = service.find(code);
        if (link == null) {
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.status(HttpStatus.FOUND)
                .header(HttpHeaders.LOCATION, link.getOriginalUrl())
                .build();
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<ErrorResponse> handleBadRequest(IllegalArgumentException ex) {
        return ResponseEntity.badRequest().body(new ErrorResponse(ex.getMessage()));
    }

    public record CreateRequest(String url) {}
    public record CreateResponse(String code, String shortPath, String url) {}
    public record ErrorResponse(String error) {}
}
