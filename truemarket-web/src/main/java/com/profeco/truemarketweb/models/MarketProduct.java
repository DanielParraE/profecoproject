package com.profeco.truemarketweb.models;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class MarketProduct implements Serializable {
    private Long id;
    private Market market;
    private float price;
    private Date updatedAt;
    private List<MarketProductReview> reviews;
}
