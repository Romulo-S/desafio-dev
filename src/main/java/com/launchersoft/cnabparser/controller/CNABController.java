package com.launchersoft.cnabparser.controller;

import java.util.Arrays;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import javax.validation.Valid;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.launchersoft.cnabparser.model.CNABFile;
import com.launchersoft.cnabparser.model.UploadFileResponse;
import com.launchersoft.cnabparser.service.CNABFileParserService;

import io.swagger.v3.oas.annotations.Operation;

@RestController
@RequestMapping(value = "/api")
@CrossOrigin(origins = "*")
public class CNABController {

    private static final Logger logger = LoggerFactory.getLogger(CNABController.class);

    @Autowired
    private CNABFileParserService cnabFileParserService;

    @PostMapping("/uploadCNABFile")
    @Operation(summary = "Upload a cnab file")
    public UploadFileResponse parseCNABFile(@RequestParam("file") @Valid MultipartFile file) {
        Set<CNABFile> cnabFile = cnabFileParserService.storeCNABData(file);

//        String fileDownloadUri = ServletUriComponentsBuilder.fromCurrentContextPath()
//            .path("api/downloadFile/")
//            .path(cnabFile.getId())
//            .toUriString();

//        return new UploadFileResponse(null, fileDownloadUri,
//            file.getContentType(), file.getSize());
        return null;
    }

    @PostMapping("/uploadMultipleFiles")
    @Operation(summary = "Upload multiple cnab files", description = "Create a clone with the information of the body.")
    public List<UploadFileResponse> uploadMultipleFiles(@RequestParam("files") MultipartFile[] files) {
        return Arrays.asList(files)
            .stream()
            .map(file -> parseCNABFile(file))
            .collect(Collectors.toList());
    }

    @GetMapping("/downloadFile/{fileId}")
    @Operation(summary = "Download cnab file")
    public ResponseEntity<Resource> downloadFile(@PathVariable String fileId) {
        // Load file from database
        CNABFile dbFile = cnabFileParserService.getFile(fileId);

        return ResponseEntity.ok()
            .contentType(MediaType.parseMediaType(null))
            .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + dbFile.getTransactionType() + "\"")
            .body(new ByteArrayResource(null));
    }
}
