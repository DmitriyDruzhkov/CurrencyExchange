package ce.packet.currencyexchange.controller;

import ce.packet.currencyexchange.dto.CurrencyDto;
import ce.packet.currencyexchange.exception.IllegalOperationException;
import ce.packet.currencyexchange.service.CurrencyService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
public class CurrencyController {
    private final CurrencyService currencyService;

    public CurrencyController(CurrencyService currencyService) {
        this.currencyService = currencyService;
    }

    @PostMapping("/currencies")
    public ResponseEntity<?> addNewCurrency(@RequestBody CurrencyDto currencyDto) {
        try {
            return ResponseEntity.status(201).body(currencyService.addNewCurrency(currencyDto));
        } catch (IllegalOperationException e) {
            switch (e.getCode()) {
                case "error.field.not.filed" -> {
                    return ResponseEntity.status(400).build();
                }
                case "error.currency.already.exists" -> {
                    return ResponseEntity.status(409).build();
                }
                default -> {
                    return ResponseEntity.status(500).build();
                }
            }
        } catch (Exception e) {
            return ResponseEntity.status(500).build();
        }
    }

    @GetMapping("/currencies")
    public ResponseEntity<?> getAllCurrencies() {
        try {
            return ResponseEntity.ok(currencyService.findAll());
        } catch (Exception e) {
            return ResponseEntity.status(500).build();
        }
    }

    @GetMapping("/currencies/{code}")
    public ResponseEntity<?> getCurrency(@PathVariable String code) {
        if (code == null) {
            return ResponseEntity.status(400).build();
        }
        try {
            var maybeCurrency = currencyService.findByCode(code);
            if (maybeCurrency.isPresent()) {
                return ResponseEntity.ok(maybeCurrency.get());
            } else {
                return ResponseEntity.status(404).build();
            }
        } catch (Exception e) {
            return ResponseEntity.status(500).build();
        }
    }
}
