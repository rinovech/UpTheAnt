package com.uptheant.demo.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.uptheant.demo.dto.bid.BidCreateDTO;
import com.uptheant.demo.dto.bid.BidResponseDTO;
import com.uptheant.demo.exception.BusinessRuleException;
import com.uptheant.demo.exception.EntityNotFoundException;
import com.uptheant.demo.service.bid.BidService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;

import java.util.List;

@RestController
@RequestMapping("/bids")
@Tag(name = "Bid API", description = "Операции со ставками на аукционах")
public class BidController {

    private final BidService bidService;

    @Autowired
    public BidController(BidService bidService) {
        this.bidService = bidService;
    }

    @Operation(
        summary = "Получить все ставки",
        description = "Возвращает список всех ставок в системе"
    )
    @ApiResponse(
        responseCode = "200",
        description = "Список ставок успешно получен",
        content = @Content(
            mediaType = "application/json",
            schema = @Schema(implementation = BidResponseDTO.class, type = "array"),
            examples = @ExampleObject(value = """
                [{
                  "bidAmount": 1500.00,
                  "bidTime": "2025-04-03T23:59:59",
                  "userId": 1,
                  "auctionId": 1
                }]
                """)
        )
    )
    @GetMapping
    public List<BidResponseDTO> getAllBids() {
        return bidService.getAllBids();
    }

    @Operation(
        summary = "Получить ставку по ID",
        description = "Возвращает информацию о конкретной ставке по её идентификатору"
    )
    @ApiResponses({
        @ApiResponse(
            responseCode = "200",
            description = "Ставка найдена",
            content = @Content(
                schema = @Schema(implementation = BidResponseDTO.class),
                examples = @ExampleObject(value = """
                    {
                      "bidAmount": 1500.00,
                      "bidTime": "2025-04-03T23:59:59",
                      "userId": 1,
                      "auctionId": 1
                    }
                    """)
            )
        ),
        @ApiResponse(
            responseCode = "404",
            description = "Ставка не найдена",
            content = @Content(examples = @ExampleObject(value = "{}"))
        )
    })
    @GetMapping("/{id}")
    public ResponseEntity<BidResponseDTO> getBidById(
        @Parameter(description = "ID ставки", example = "1")
        @PathVariable Integer id) {
        try {
            BidResponseDTO bid = bidService.getBidById(id);
            return ResponseEntity.ok(bid);
        } catch (RuntimeException ex) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }

    @Operation(
        summary = "Создать новую ставку",
        description = "Создает новую ставку на указанном аукционе"
    )
    @ApiResponses({
        @ApiResponse(
            responseCode = "201",
            description = "Ставка успешно создана",
            content = @Content(
                schema = @Schema(implementation = BidResponseDTO.class),
                examples = @ExampleObject(value = """
                    {
                      "bidAmount": 1500.00,
                      "bidTime": "2025-04-03T23:59:59",
                      "userId": 1,
                      "auctionId": 1
                    }
                    """)
            )
        ),
        @ApiResponse(
            responseCode = "400",
            description = "Невалидные данные ставки",
            content = @Content(examples = @ExampleObject(value = """
                {
                  "error": "Bid amount must be higher than current price"
                }
                """))
        ),
        @ApiResponse(
            responseCode = "404",
            description = "Аукцион или пользователь не найдены",
            content = @Content(examples = @ExampleObject(value = """
                {
                  "error": "Auction not found"
                }
                """))
        )
    })
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public BidResponseDTO createBid(
        @io.swagger.v3.oas.annotations.parameters.RequestBody(
            description = "Данные для создания ставки",
            required = true,
            content = @Content(
                schema = @Schema(implementation = BidCreateDTO.class),
                examples = @ExampleObject(value = """
                    {
                      "bidAmount": 1500.00
                    }
                    """)
            )
        )
        @RequestBody BidCreateDTO bidCreateDTO,
        @Parameter(description = "ID пользователя", example = "1", required = true)
        @RequestParam Integer userId,
        @Parameter(description = "ID аукциона", example = "1", required = true)
        @RequestParam Integer auctionId) {
        return bidService.createBid(bidCreateDTO, userId, auctionId);
    }

    @Operation(
        summary = "Удалить ставку",
        description = "Удаляет ставку по её идентификатору"
    )
    @ApiResponses({
        @ApiResponse(
            responseCode = "200",
            description = "Ставка успешно удалена",
            content = @Content(examples = @ExampleObject(value = """
                "Bid with ID 1 was successfully deleted."
                """))
        ),
        @ApiResponse(
            responseCode = "404",
            description = "Ставка не найдена",
            content = @Content(examples = @ExampleObject(value = """
                "Bid not found with ID: 1"
                """))
        )
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteBid(
        @Parameter(description = "ID ставки для удаления", example = "1")
        @PathVariable Integer id) {
        try {
            bidService.deleteBid(id);
            return ResponseEntity.ok("Bid with ID " + id + " was successfully deleted.");
        } catch (EntityNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        } catch (BusinessRuleException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }
}