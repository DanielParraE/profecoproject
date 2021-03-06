package com.profeco.consumer.controller;

import com.profeco.consumer.MQConfig;
import com.profeco.consumer.dto.InconsistencyDTO;
import com.profeco.consumer.entities.Market;
import com.profeco.consumer.entities.MarketProduct;
import com.profeco.consumer.entities.Product;
import com.profeco.consumer.service.StorageService;
import com.profeco.consumer.service.inconsistency.InconsistencyService;
import com.profeco.consumer.entities.Inconsistency;
import com.profeco.consumer.service.product.ProductService;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

import javax.validation.Valid;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping(value = "/inconsistencies")
public class InconsistencyController {

    @Autowired
    private InconsistencyService inconsistencyService;

    @Autowired
    private ProductService productService;

    @Autowired
    private StorageService storageService;

    @Autowired
    private RabbitTemplate template;

    @GetMapping
    public ResponseEntity<List<Inconsistency>> listComplain(@RequestParam(required = false) Long marketProduct) {
        List<Inconsistency> inconsistencies = marketProduct == null?
                inconsistencyService.listAllComplain():
                inconsistencyService.getComplainByMarket(marketProduct);

        if (inconsistencies.isEmpty()) {
            return ResponseEntity.noContent().build();
        }
//        inconsistencies.forEach(cm -> {
//            cm.setMarket(marketClient.marketById(cm.getMarketID()).getBody());
//        });
        return ResponseEntity.ok(inconsistencies);
    }

    @GetMapping(value = "/{id}")
    public ResponseEntity<Inconsistency> complainById(@PathVariable(value = "id", required = true) Long id) {

        List<Inconsistency> inconsistencies = inconsistencyService.getComplainByMarket(id);
        if (inconsistencies.isEmpty()) {
            return ResponseEntity.noContent().build();
        }
        Inconsistency cm = inconsistencyService.getComplain(id);
//        cm.setMarket(marketClient.marketById(cm.getMarketID()).getBody());
        return ResponseEntity.ok(cm);
    }

    @PostMapping(consumes = { MediaType.MULTIPART_FORM_DATA_VALUE })
    public ResponseEntity<Inconsistency> createComplain(@Valid @RequestPart Inconsistency inconsistency,
                                                        @RequestPart(required = false) MultipartFile file, BindingResult result) {
        if (result.hasErrors())
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, ErrorMessage.formatMessage(result));

        String url =  (file == null)?
                ""
                : storageService.store(file);
        inconsistency.setEvidence(url);
        Inconsistency inconsistencyCreate = inconsistencyService.createComplain(inconsistency);

//        InconsistencyDTO dto = InconsistencyDTO.builder()
//                                    .id(inconsistencyCreate.getId())
//                                    .realPrice(inconsistencyCreate.getRealPrice())
//                                    .publishedPrice(inconsistencyCreate.getPublishedPrice())
//                                    .description(inconsistencyCreate.getDescription())
//                                    .consumerId(inconsistencyCreate.getAuthor().getId())
//                                    .evidence(inconsistencyCreate.getEvidence())
//                                    .purchasedAt(inconsistencyCreate.getPurchasedAt())
//                                    .marketId(inconsistency.getProduct().get)
//                                    .productId(inconsistencyCreate.getProduct().getProduct().getId())
//                                    .build();
        Product product = productService.getProductByMarketProduct(inconsistencyCreate.getMarketProduct().getId());
        Optional<MarketProduct> market = product.getMarketProductList()
                .stream()
                .filter(marketProduct -> marketProduct.getId() == inconsistency.getMarketProduct().getId()).findFirst();
        inconsistencyCreate.setMarketProduct(market.get());

        // send to broker
        template.convertAndSend(MQConfig.EXCHANGE, MQConfig.NEW_INCONSISTENCY_ROUTING_KEY, inconsistencyCreate.convertToDTO());

        return ResponseEntity.status(HttpStatus.CREATED).body(inconsistencyCreate);
    }

    @PutMapping(value = "/{id}")
    public ResponseEntity<Inconsistency> updateComplain(@PathVariable(value = "id") Long id, @RequestBody Inconsistency inconsistency) {
        inconsistency.setId(id);
        Inconsistency inconsistencyDB = inconsistencyService.updateComplain(inconsistency);
        if (inconsistencyDB == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(inconsistencyDB);
    }

    @DeleteMapping(value = "/{id}")
    public ResponseEntity<String> deleteComplain(@PathVariable(value = "id") Long id) {
        boolean deleted = inconsistencyService.deleteComplain(id);
        if (!deleted) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok("[ DELETED ]");
    }
}
