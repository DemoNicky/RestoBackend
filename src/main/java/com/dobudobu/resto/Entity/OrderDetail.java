package com.dobudobu.resto.Entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.GenericGenerator;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@Data
@Table(name = "tbl_order_detail")
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class OrderDetail {

    @Id
    @GenericGenerator(name = "uuid", strategy = "uuid2")
    @GeneratedValue(generator = "uuid")
    private String id;

    @Column(name = "total_payment")
    private Double paymentTotal;

    @Column(name = "order_date")
    @JsonFormat(pattern = "dd-MM-yyyy HH:mm:ss")
    private LocalDateTime orderDate;

    @Column(name = "order_status")
    @Enumerated(EnumType.STRING)
    private OrderStatus paymentStatus;

    @OneToMany(cascade = CascadeType.ALL)
    @JoinTable(name = "order_and_detail_table",
            joinColumns = {@JoinColumn(name = "order_detail_id")},
            inverseJoinColumns = {@JoinColumn(name = "order_id")}
    )
    private List<Order> order;


}
