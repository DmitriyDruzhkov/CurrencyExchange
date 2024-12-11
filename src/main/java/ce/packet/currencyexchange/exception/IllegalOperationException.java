package ce.packet.currencyexchange.exception;

import lombok.Getter;

@Getter
public class IllegalOperationException extends RuntimeException {
    private final String code;

    public IllegalOperationException(String code, String message) {
        super(message);
        this.code = code;
    }
}
