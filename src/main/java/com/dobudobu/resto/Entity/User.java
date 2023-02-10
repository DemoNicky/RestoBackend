package com.dobudobu.resto.Entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.GenericGenerator;

@Entity
@Data
@Table(name = "tbl_user")
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class User {

    @Id
    @GenericGenerator(name = "uuid", strategy = "uuid2")
    @GeneratedValue(generator = "uuid")
    private String id;

    @Column(name = "name", length = 100)
    private String name;

    @Column(name = "email", unique = true)
    private String email;

}
