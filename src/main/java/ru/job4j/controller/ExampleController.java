package ru.job4j.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.util.MultiValueMapAdapter;
import org.springframework.web.bind.annotation.GetMapping;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Controller
public class ExampleController {
    @GetMapping("/example1")
    public ResponseEntity<?> example1() {
        return ResponseEntity.ok("Example1");
    }

    @GetMapping("/example2")
    public ResponseEntity<Map<String, String>> example2() {
        return ResponseEntity.of(Optional.of(Map.ofEntries(Map.entry("key","value"))));
    }

    @GetMapping("/example3")
    public ResponseEntity<?> example3() {
        Object body = Map.ofEntries(Map.entry("key", "value"));
        return new ResponseEntity(
                body,
                new MultiValueMapAdapter<>(Map.ofEntries(Map.entry("CustomHeader", List.of("job4j")))),
                HttpStatus.OK);
    }

    @GetMapping("/example4")
    public ResponseEntity<String> example4() {
        String body = Map.ofEntries(Map.entry("key", "value")).toString();
        return ResponseEntity.status(HttpStatus.CONFLICT)
                .header("CustomHeader", "job4j")
                .contentType(MediaType.TEXT_PLAIN)
                .contentLength(body.length())
                .body(body);
    }

    @GetMapping("/example5")
    public ResponseEntity<byte[]> example5() throws IOException {
        byte[] content = Files.readAllBytes(Path.of("C:\\Users\\Ёжик\\Downloads\\shop_property_file_171211_12242.pdf"));
        return ResponseEntity.ok()
                .contentType(MediaType.APPLICATION_PDF)
                .contentLength(content.length)
                .body(content);
    }

    @GetMapping("/example6")
    public byte[] example6() throws IOException {
        return Files.readAllBytes(Path.of("pom.xml"));
    }
}
