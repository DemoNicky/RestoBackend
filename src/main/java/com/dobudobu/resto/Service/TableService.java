package com.dobudobu.resto.Service;

import com.dobudobu.resto.Dto.TableDto;
import com.dobudobu.resto.Entity.Tables;
import com.dobudobu.resto.Repository.TableRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TableService {

    @Autowired
    private TableRepository tableRepository;

    public void createTableData(TableDto tableDto) {
        Tables tables = Tables.builder()
                .tableNumber(tableDto.getTableNumber())
                .status(false)
                .build();
        tableRepository.save(tables);
    }

    public List<Tables> getAllData() {
        return tableRepository.findAll();
    }
}
