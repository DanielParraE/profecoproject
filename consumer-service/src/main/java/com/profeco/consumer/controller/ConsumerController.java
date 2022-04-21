package com.profeco.consumer.controller;

import com.profeco.consumer.entities.Consumer;
import com.profeco.consumer.service.ConsumerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(value = "/consumers")
public class ConsumerController {

    @Autowired
    private ConsumerService consumerService;

    @GetMapping
    public ResponseEntity<List<Consumer>> listConsumer() {
        List<Consumer> consumers = consumerService.listAllConsumer();
        if (consumers.isEmpty()) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(consumers);
    }

    @GetMapping(value = "/{id}")
    public ResponseEntity<Consumer> consumerById(@PathVariable(value = "id", required = true) Long id) {
        List<Consumer> consumers = consumerService.listAllConsumer();
        if (consumers.isEmpty()) {
            return ResponseEntity.noContent().build();
        }
        Consumer cs = consumerService.getConsumer(id);
        return ResponseEntity.ok(cs);
    }

    @PostMapping
    public ResponseEntity<Consumer> createConsumer(@RequestBody Consumer consumer) {
        Consumer productCreate = consumerService.createConsumer(consumer);
        return ResponseEntity.status(HttpStatus.CREATED).body(productCreate);
    }

    @PutMapping(value = "/{id}")
    public ResponseEntity<Consumer> updateConsumer(@PathVariable(value = "id") Long id, @RequestBody Consumer consumer) {
        consumer.setId(id);
        Consumer consumerDB = consumerService.updateConsumer(consumer);
        if (consumerDB == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(consumerDB);
    }

    @DeleteMapping(value = "/{id}")
    public ResponseEntity<String> deleteConsumer(@PathVariable(value = "id") Long id) {
        Consumer consumerDeleted = consumerService.deleteConsumer(id);
        if (consumerDeleted == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok("[ DELETED ]");
    }

}
