package ce.packet.currencyexchange.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "currencies")
@Getter
@Setter
public class Currency {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "code", nullable = false, unique = true)
    private String code;

    @Column(name = "full_name", nullable = false, unique = true)
    private String fullName;

    @Column(name = "sign", nullable = false, unique = true)
    private String sign;
}
