package com.dobudobu.resto.Repository;

import com.dobudobu.resto.Entity.Payment;
import jakarta.websocket.server.PathParam;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PaymentRepository extends JpaRepository<Payment, String> {

    @Query("SELECT p FROM Payment p WHERE p.users.id = :users")
    List<Payment> findUser(@PathParam("users") String users);
}
