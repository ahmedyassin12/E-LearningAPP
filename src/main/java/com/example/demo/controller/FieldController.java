package com.example.demo.controller;


import com.example.demo.entity.Field;
import com.example.demo.service.FieldService;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/fields")
@RequiredArgsConstructor
public class FieldController {

    private final FieldService fieldService;

    @PostMapping("/createField")
    public ResponseEntity<Field> createField(@RequestBody Field field) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(fieldService.createField(field));
    }

    @GetMapping("/getAllFields")
    public ResponseEntity<List<Field>> getAllFields() {
        return ResponseEntity.ok(fieldService.getAllFields());
    }

    @GetMapping("/getFieldById/{id}")
    public ResponseEntity<Field> getFieldById(@PathVariable Long id) {

        return ResponseEntity.ok(fieldService.getFieldById(id));

    }

    @PutMapping("/updateField")
    public ResponseEntity<Field> updateField(
          @RequestBody Field field) {
        return ResponseEntity.ok(fieldService.updateField(field));
    }

    @DeleteMapping("/deleteField/{id}")
    public ResponseEntity<String> deleteField(@PathVariable Long id) {
        return new ResponseEntity<>(fieldService.deleteField(id),HttpStatus.OK);
    }

    @PostConstruct
    public void initializeFields() {

        fieldService.initializeFields();


    }
}