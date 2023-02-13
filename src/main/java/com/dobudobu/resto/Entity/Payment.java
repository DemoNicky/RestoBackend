package com.dobudobu.resto.Entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.GenericGenerator;

@Entity
@Data
@Table(name = "tbl_payment")
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Payment {

    @Id
    @GenericGenerator(name = "uuid", strategy = "uuid2")
    @GeneratedValue(generator = "uuid")
    private String id;

    @Column(name = "total_payment")
    private Double paymentTotal;

    @Column(name = "pay")
    private Double pay;

    @Column(name = "change_pay")
    private Double change;

}
