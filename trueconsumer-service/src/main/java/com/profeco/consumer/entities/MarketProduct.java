package com.profeco.consumer.entities;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import javax.validation.constraints.Positive;
import java.util.Date;
import java.util.List;

@Entity
@Table(name = "rel_marketsproducts")
@Data
@Builder
@AllArgsConstructor @NoArgsConstructor
public class MarketProduct {
    @Id
    @Column(name = "marketproduct_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    //@JsonBackReference(value = "market-marketproduct")
    @JsonIgnoreProperties({"hibernateLazyInitializer","handler"})
    @ManyToOne(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @JoinColumn(name = "market_id", nullable = false)
    private Market market;

    @JsonBackReference(value = "product-marketproduct")
    //@JsonIgnoreProperties({"hibernateLazyInitializer","handler"})
    @ManyToOne(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @JoinColumn(name = "product_id", nullable = false)
    private Product product;

    @JsonManagedReference(value = "market-product-productreview")
    @OneToMany(mappedBy = "marketProduct", cascade = CascadeType.ALL)
    private List<ProductReview> reviews;

    @JsonManagedReference(value = "market-product-inconsistency")
    @OneToMany(mappedBy = "marketProduct", cascade = CascadeType.ALL)
    private List<Inconsistency> inconsistencies;

    @Positive(message = "price must be > 0")
    private Double price;

    @Column(name = "updated_at")
    @Temporal(TemporalType.TIMESTAMP)
    private Date updatedAt;
}
