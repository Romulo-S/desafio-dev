package com.launchersoft.cnabparser.service;

import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import com.launchersoft.cnabparser.exception.CNABNotFoundException;
import com.launchersoft.cnabparser.exception.CNABParseException;
import com.launchersoft.cnabparser.model.CNABFile;
import com.launchersoft.cnabparser.repository.CNABFileRepository;

@Service
public class CNABFileParserService {

    @Autowired
    private CNABFileRepository cnabFileRepository;

    public Set<CNABFile> storeCNABData(MultipartFile file) {

        String fileName = getFilename(file);

        try {
            FileCNABParser fileCNABParser = new FileCNABParser(fileName);
            Set<CNABFile> cnabFiles = fileCNABParser.parseCNABFile();

            cnabFileRepository.saveAllAndFlush(cnabFiles);
            return cnabFiles;
        } catch (Exception e) {
            throw new CNABParseException("Could not store CNAB data " + fileName + ". Please try again!", e.getCause());
        }
    }

    private String getFilename(MultipartFile file) {
        String fileName = StringUtils.cleanPath(file.getOriginalFilename());

        // Check if the file's name contains invalid characters
        if (fileName.contains("/^[a-z0-9_.@()-]+\\.txt$/i")) {
            throw new CNABParseException("Sorry! Filename contains invalid path sequence " + fileName);
        }

        return fileName;
    }

    public CNABFile getFile(String fileId) {
        return cnabFileRepository.findById(fileId)
            .orElseThrow(() -> new CNABNotFoundException("File not found with id " + fileId));
    }

    public static class NegocioVendaEmitenteObrigatorioNaoInformadoException extends RuntimeException {
        private static final long serialVersionUID = -3067945661928976409L;

    }
}
