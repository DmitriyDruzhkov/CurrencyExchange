package ce.packet.currencyexchange.controller;

import ce.packet.currencyexchange.exception.IllegalOperationException;
import ce.packet.currencyexchange.service.ExchangeService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ExchangeController { //вынести ("/exchange") в requestMapping
    private final ExchangeService exchangeService;

    public ExchangeController(ExchangeService exchangeService) {
        this.exchangeService = exchangeService;
    }

    @GetMapping("/exchange")
    public ResponseEntity<?> getExchange(@RequestParam String from,
                                         @RequestParam String to,
                                         @RequestParam Double amount) {
        try {
            return ResponseEntity.status(200).body(exchangeService.convert(from, to, amount));
        } catch (IllegalOperationException e) {
            return ResponseEntity.status(500).body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(400).build(); // для не обрабатываемых ошибок вызвращаем 500
        }
    }
}
