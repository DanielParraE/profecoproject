package com.profeco.entities;

import com.fasterxml.jackson.annotation.JsonBackReference;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.stereotype.Component;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

@Entity
@Table(name = "inconsistencies")
@Data
@AllArgsConstructor @NoArgsConstructor
@Builder
public class Inconsistency implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "real_price", nullable = false)
    private float realPrice;

    @Column(name = "published_price", nullable = false)
    private float publishedPrice;


    private String evidence;

    private String description;

    @Temporal(value = TemporalType.TIMESTAMP)
    @Column(name = "purchased_at")
    private Date purchasedAt;

    @Column(name = "consumer_id", nullable = false)
    private Long consumerId;

    @JsonBackReference
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "market_product_id", nullable = false)
    private MarketProduct marketProduct;
}
