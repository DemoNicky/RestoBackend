package com.dobudobu.resto.Controller;

import com.dobudobu.resto.Dto.TableDto;
import com.dobudobu.resto.Entity.Tables;
import com.dobudobu.resto.Service.TableService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/api/v1/tables")
public class TableController {

    @Autowired
    private TableService tableService;

    @PostMapping
    public ResponseEntity<Void> createTableData(@RequestBody TableDto tableDto){
        tableService.createTableData(tableDto);
        return ResponseEntity.created(URI.create("/tables")).build();
    }

    @GetMapping
    public List<Tables> getDataTables(){
        return tableService.getAllData();
    }

}
