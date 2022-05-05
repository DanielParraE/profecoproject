package com.profeco.consumer.controller;

import com.profeco.consumer.dto.FileUploadResponse;
import com.profeco.consumer.entities.Consumer;
import com.profeco.consumer.service.StorageService;
import com.profeco.consumer.service.consumer.ConsumerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

import javax.validation.Valid;
import java.io.File;
import java.util.List;

import static org.springframework.web.bind.annotation.RequestMethod.POST;

@RestController
@RequestMapping(value = "/consumers")
public class ConsumerController {

    @Autowired
    private ConsumerService consumerService;

    @Autowired
    private StorageService storageService;

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
        Consumer consumerDB = consumerService.getConsumer(id);
        if (consumerDB == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(consumerDB);
    }

    @PostMapping(consumes = { MediaType.MULTIPART_FORM_DATA_VALUE })
    public ResponseEntity<Consumer> createConsumer(@Valid @RequestPart Consumer consumer,
                                                   @RequestPart(required = false) MultipartFile file,
                                                   BindingResult result) {
        if (result.hasErrors())
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, ErrorMessage.formatMessage(result));

        String url =  (file == null)?
                "http://localhost:8091/files/abc-person.png"
                : storageService.store(file);

        consumer.setImage(url);
        Consumer consumerCreated = consumerService.createConsumer(consumer);
        return ResponseEntity.status(HttpStatus.CREATED).body(consumerCreated);
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
