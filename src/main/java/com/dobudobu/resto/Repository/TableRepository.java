package com.dobudobu.resto.Repository;

import com.dobudobu.resto.Entity.Tables;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TableRepository extends JpaRepository<Tables, Long> {

    List<Tables> findByStatusFalse();

}
