package ce.packet.currencyexchange.controller;

import ce.packet.currencyexchange.dto.ExchangeRateDto;
import ce.packet.currencyexchange.dto.ExchangeRateChangeDto;
import ce.packet.currencyexchange.exception.IllegalOperationException;
import ce.packet.currencyexchange.service.ExchangeRatesService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
public class ExchangeRatesController { //вынести ("/exchangeRates") в requestMapping
    private final ExchangeRatesService exchangeRatesService;

    public ExchangeRatesController(ExchangeRatesService exchangeRatesService) {
        this.exchangeRatesService = exchangeRatesService;
    }

    @GetMapping("/exchangeRates")
    public ResponseEntity<?> getExchangeRates() {
        try {
            return ResponseEntity.ok(exchangeRatesService.findAllExchangeRates());
        } catch (Exception e) {
            return ResponseEntity.status(500).build();
        }
    }

    @GetMapping("/exchangeRates/{code}")
    public ResponseEntity<?> getExchangeRate(@PathVariable String code) {
        if (code == null) {
            return ResponseEntity.status(400).build();
        }
        try {
            exchangeRatesService.findExchangeRate(code);
            return ResponseEntity.status(200).build(); // возвращается пустой ответ
        } catch (IllegalOperationException e) { // сюда никогда не попадем
            return ResponseEntity.status(404).body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(500).build();
        }
    }


    @PostMapping("/exchangeRate")
    public ResponseEntity<?> addExchangeRates(@RequestBody ExchangeRateDto exchangeRateDto) {
        try {
            return ResponseEntity.status(201).body(exchangeRatesService.addNewExchangeRates(exchangeRateDto));
        } catch (Exception e) {
            return ResponseEntity.status(500).build();
        }
    }

    @PatchMapping("/exchangeRate/{code}")
    public ResponseEntity<?> patchExchangeRate(@PathVariable String code, //название patch поменять на update
                                               @RequestBody ExchangeRateChangeDto exchangeRateChangeDto) {
        if (code == null || exchangeRateChangeDto == null
                || exchangeRateChangeDto.getRate() == null) {
            return ResponseEntity.status(400).build();
        }
        try {
            exchangeRatesService.updateExchangeRate(code, exchangeRateChangeDto);
            return ResponseEntity.status(200).build();
        } catch (IllegalOperationException e) {
            return ResponseEntity.status(404).body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(500).build();
        }
    }

}
