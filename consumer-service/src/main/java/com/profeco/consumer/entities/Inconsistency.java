package com.profeco.consumer.entities;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.profeco.consumer.models.Market;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Positive;
import java.util.Date;

@Entity
@Table(name = "inconsistencies")
@Data @AllArgsConstructor  @NoArgsConstructor  @Builder
public class Inconsistency {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Positive(message = "price must be > 0")
    @Column(name = "real_price")
    private float realPrice;

    @Positive(message = "price must be > 0")
    @Column(name = "published_price")
    private float publishedPrice;

    @NotEmpty(message = "Description must not be empty!!")
    private String description;

    private String evidence;

    @Column(name = "purchased_at")
    @Temporal(TemporalType.TIMESTAMP)
    private Date purchasedAt;

    @JsonBackReference(value = "consumer-author")
    @ManyToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JsonIgnoreProperties({"hibernateLazyInitializer","handler"})
    @JoinColumn(name = "consumer_id", nullable = false)
    private Consumer author;

    @JsonBackReference(value = "market-product-inconsistency")
    @ManyToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JoinColumn(name = "market_product_id", nullable = false)
    private MarketProduct product;
}
