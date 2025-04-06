package com.uptheant.demo.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.uptheant.demo.dto.auction.AuctionCreateDTO;
import com.uptheant.demo.dto.auction.AuctionResponseDTO;
import com.uptheant.demo.exception.BusinessRuleException;
import com.uptheant.demo.exception.EntityNotFoundException;
import com.uptheant.demo.service.auction.AuctionService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;

import java.util.List;

@RestController
@RequestMapping("/auctions")
@Tag(name = "Auction API", description = "Операции с аукционами")
public class AuctionController {

    @Autowired
    private AuctionService auctionService;

    @Operation(
        summary = "Получить все аукционы",
        description = "Возвращает список всех активных аукционов"
    )
    @ApiResponse(
        responseCode = "200",
        description = "Список аукционов успешно получен"
    )
    @GetMapping
    public List<AuctionResponseDTO> getAllAuctions() {
        return auctionService.getAllAuctions();
    }

    @Operation(
        summary = "Получить аукцион по ID",
        description = "Возвращает полную информацию об аукционе по его идентификатору"
    )
    @ApiResponses({
        @ApiResponse(
            responseCode = "200",
            description = "Аукцион найден"
        ),
        @ApiResponse(
            responseCode = "404",
            description = "Аукцион не найден"
        ),
        @ApiResponse(responseCode = "400", description = "Некорректный запрос")
    })
    @GetMapping("/{id}")
    public ResponseEntity<?> getAuctionById(
        @Parameter(description = "ID аукциона", example = "1")
        @PathVariable Integer id) {
        try {
            AuctionResponseDTO auction = auctionService.getAuctionById(id);
            return ResponseEntity.ok(auction);
        } catch (EntityNotFoundException ex) {
            return ResponseEntity.badRequest().body(ex.getMessage());
        } catch (RuntimeException ex) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }

    @Operation(
        summary = "Создать новый аукцион",
        description = "Создает новый аукцион с указанными параметрами"
    )
    @ApiResponses({
        @ApiResponse(
            responseCode = "201",
            description = "Аукцион успешно создан"
        ),
        @ApiResponse(
            responseCode = "400",
            description = "Невалидные данные аукциона"),
        @ApiResponse(responseCode = "409", description = "Конфликт данных")
    })
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<?> createAuction(
        @io.swagger.v3.oas.annotations.parameters.RequestBody(
            description = "Данные для создания аукциона",
            required = true,
            content = @Content(
                schema = @Schema(implementation = AuctionCreateDTO.class),
                examples = @ExampleObject(value = """
                    {
                      "name": "Новый аукцион",
                      "description": "Описание аукциона",
                      "startPrice": 500,
                      "minBidStep": 50,
                      "startTime": "2025-04-03T23:59:59",
                      "endTime": "2025-04-09T23:59:59"
                    }
                    """)
            )
        )
        @Valid @RequestBody AuctionCreateDTO auctionCreateDTO,
        @Parameter(description = "ID продавца", example = "1", required = true)
        @RequestParam Integer sellerId) {
        try {
            AuctionResponseDTO createdAuction = auctionService.createAuction(auctionCreateDTO, sellerId);
            return ResponseEntity.status(HttpStatus.CREATED).body(createdAuction);
        } catch (BusinessRuleException ex) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body(ex.getMessage());
        }
    }

    @Operation(
        summary = "Удалить аукцион",
        description = "Удаляет аукцион по его идентификатору. Нельзя удалить аукцион со ставками."
    )
    @ApiResponses({
        @ApiResponse(
            responseCode = "200",
            description = "Аукцион успешно удален"
        ),
        @ApiResponse(
            responseCode = "404",
            description = "Аукцион не найден"
        ),
        @ApiResponse(
            responseCode = "400",
            description = "Невозможно удалить аукцион")
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteAuction(
        @Parameter(description = "ID аукциона для удаления", example = "1")
        @PathVariable Integer id) {
        try {
            auctionService.deleteAuction(id);
            return ResponseEntity.ok().build();
        } catch (EntityNotFoundException e) {
            return ResponseEntity.notFound().build();
        } catch (BusinessRuleException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
}