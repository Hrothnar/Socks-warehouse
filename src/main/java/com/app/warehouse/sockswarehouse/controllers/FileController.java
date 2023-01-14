package com.app.warehouse.sockswarehouse.controllers;

import com.app.warehouse.sockswarehouse.exceptions.NotFoundException;
import com.app.warehouse.sockswarehouse.exceptions.RuntimeIOException;
import com.app.warehouse.sockswarehouse.models.SockPair;
import com.app.warehouse.sockswarehouse.services.SockService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;

@RestController
@RequestMapping("/api/socks")
@Tag(name = "Загрузка и выгрузка файла с движением товара, а так же получение остатков")
public class FileController {
    @Value("${app.exportWarehouse.filepath}")
    private String warehouseFilePath;
    private SockService sockService;

    @Operation(summary = "Получить журнал операций")
    @GetMapping("/export/journal")
    public ResponseEntity<InputStreamResource> exportJournal() {
        File file = new File("src/main/resources/data/SocksJournal.json");
        try {
            InputStreamResource isr = new InputStreamResource(new FileInputStream(file));
            return ResponseEntity.ok()
                    .contentLength(file.length())
                    .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"Journal.json\"")
                    .contentType(MediaType.APPLICATION_JSON)
                    .body(isr);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            throw new NotFoundException();
        } catch (IOException e) {
            e.printStackTrace();
            throw new RuntimeIOException();
        }
    }

    @Operation(summary = "Получить складские остатки")
    @ApiResponses(value = {@ApiResponse(responseCode = "200", description = "Успех",
            content = {@Content(mediaType = "application/json",
                    array = @ArraySchema(schema = @Schema(implementation = SockPair.class)))})})
    @GetMapping("/export/warehouse")
    public ResponseEntity<InputStreamResource> exportWarehouse() {
        File file = new File(warehouseFilePath);
        try {
            InputStreamResource isr = new InputStreamResource(new FileInputStream(file));
            return ResponseEntity.ok()
                    .contentLength(file.length())
                    .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"Warehouse.json\"")
                    .contentType(MediaType.APPLICATION_JSON)
                    .body(isr);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            throw new NotFoundException(e);
        } catch (IOException e) {
            e.printStackTrace();
            throw new RuntimeIOException(e);
        }
    }

    @Operation(summary = "Загрузить движение товара из файла", description = "Операции распределяются согласно их типу")
    @ApiResponses(value = {@ApiResponse(responseCode = "200", description = "Успех",
            content = {@Content(mediaType = "application/json",
                    array = @ArraySchema(schema = @Schema(implementation = SockPair.class)))})})
    @PostMapping(value = "import/journal", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<String> importJournal(@RequestParam MultipartFile multipartFile) {
        try {
            sockService.addFromFile(multipartFile.getInputStream());
            return ResponseEntity.ok().build();
        } catch (IOException e) {
            e.printStackTrace();
            throw new RuntimeIOException(e);
        }
    }


}
