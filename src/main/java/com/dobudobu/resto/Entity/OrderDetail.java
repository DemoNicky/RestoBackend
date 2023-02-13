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

    @Column(name = "order_date")
    @JsonFormat(pattern = "dd-MM-yyyy HH:mm:ss")
    private LocalDateTime orderDate;

    @OneToMany
    private List<Order> order;

    @ManyToOne
    @JoinColumn(name = "tables_id")
    private Tables tables;

    @ManyToOne
    @JoinColumn(name = "users_id")
    private User users;
}
