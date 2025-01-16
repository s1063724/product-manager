package com.example.productmanagement.controller;

import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@RestController
@RequestMapping("/api/logs")
public class LogController {

    private static final String LOG_DIRECTORY = System.getProperty("user.dir") + "/logs/"; // Log 目錄路徑

    // 獲取 Log 文件列表
    @GetMapping("/files")
    public ResponseEntity<List<String>> getLogFiles() {
        try (Stream<Path> paths = Files.list(Paths.get(LOG_DIRECTORY))) {
            List<String> logFiles = paths
                    .filter(Files::isRegularFile) // 只處理文件
                    .filter(path -> path.toString().endsWith(".log")) // 只處理 .log 文件
                    .map(Path::getFileName) // 獲取文件名
                    .map(Path::toString) // 轉換為字符串
                    .collect(Collectors.toList());

            return ResponseEntity.ok(logFiles);
        } catch (IOException e) {
            return ResponseEntity.status(500).body(List.of("讀取 Log 目錄失敗"));
        }
    }

    // 下載單個 Log 文件
    @GetMapping("/download/{filename}")
    public ResponseEntity<Resource> downloadLogFile(@PathVariable String filename) {
        Path logFilePath = Paths.get(LOG_DIRECTORY + filename);

        if (!Files.exists(logFilePath)) {
            return ResponseEntity.status(404).body(null);
        }

        try {
            Resource resource = new UrlResource(logFilePath.toUri());
            return ResponseEntity.ok()
                    .contentType(MediaType.TEXT_PLAIN)
                    .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + resource.getFilename() + "\"")
                    .body(resource);
        } catch (IOException e) {
            return ResponseEntity.status(500).body(null);
        }
    }
}