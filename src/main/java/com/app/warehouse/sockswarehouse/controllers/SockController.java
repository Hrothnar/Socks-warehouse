package com.app.warehouse.sockswarehouse.controllers;

import com.app.warehouse.sockswarehouse.exceptions.InvalidArgumentsException;
import com.app.warehouse.sockswarehouse.exceptions.NotFoundException;
import com.app.warehouse.sockswarehouse.models.SockPair;
import com.app.warehouse.sockswarehouse.services.SockService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/socks")
@Tag(name = "Склад носков")
public class SockController {
    private SockService sockService;

    public SockController(SockService sockService) {
        this.sockService = sockService;
    }

    @Operation(summary = "Добавить носки на склад")
    @ApiResponses(value = {@ApiResponse(responseCode = "200", description = "Успех",
            content = {@Content(mediaType = "application/json",
                    array = @ArraySchema(schema = @Schema(implementation = SockPair.class)))})})
    @PostMapping()
    public ResponseEntity<String> addSocks(@RequestBody SockPair sockPair) {
        if (sockService.addSocks(sockPair)) {
            return ResponseEntity.ok().build();
        } else {
            throw new InvalidArgumentsException();
        }
    }

    @Operation(summary = "Выписать носки со склада")
    @ApiResponses(value = {@ApiResponse(responseCode = "200", description = "Успех",
            content = {@Content(mediaType = "application/json",
                    array = @ArraySchema(schema = @Schema(implementation = SockPair.class)))})})
    @PutMapping()
    public ResponseEntity<String> editRemainingSocks(@RequestBody SockPair sockPair) {
        if (sockService.realizeGoods(sockPair)) {
            return ResponseEntity.ok().build();
        } else {
            throw new NotFoundException();
        }
    }

    @Operation(summary = "Получить остатки на складе", description = "Можно указать min, max или оба диапазона")
    @ApiResponses(value = {@ApiResponse(responseCode = "200", description = "Успех",
            content = {@Content(mediaType = "application/json",
                    array = @ArraySchema(schema = @Schema(implementation = SockPair.class)))})})
    @GetMapping()
    public ResponseEntity<Integer> showWarehouse(@RequestParam String color, @RequestParam int size, @RequestParam(required = false) Integer cottonMin, @RequestParam(required = false) Integer cottonMax) {
        int quantity = sockService.getQuantityOfSocks(color, size, cottonMin, cottonMax);
        if (quantity != 0) {
            return ResponseEntity.ok(quantity);
        } else {
            throw new NotFoundException();
        }
    }

    @Operation(summary = "Списать бракованные носки")
    @ApiResponses(value = {@ApiResponse(responseCode = "200", description = "Успех",
            content = {@Content(mediaType = "application/json",
                    array = @ArraySchema(schema = @Schema(implementation = SockPair.class)))})})
    @DeleteMapping()
    public ResponseEntity<String> writeOffDefectiveGoods(@RequestBody SockPair sockPair) {
        if (sockService.removeDefectiveGoods(sockPair)) {
            return ResponseEntity.ok().build();
        } else {
            throw new NotFoundException();
        }
    }


}