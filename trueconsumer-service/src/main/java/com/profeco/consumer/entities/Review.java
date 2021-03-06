package com.profeco.consumer.entities;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.*;

import javax.persistence.*;
import javax.validation.constraints.DecimalMax;
import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.NotEmpty;
import java.util.Date;

@Entity
@Inheritance(strategy = InheritanceType.TABLE_PER_CLASS)
@Getter
public class Review {
    @Id
    //@GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @NotEmpty(message = "Comment must not be empty!!")
    private String comment;

    @DecimalMin(value = "0.0", message = "rating must not be less than 0")
    @DecimalMax(value = "5.0", message = "rating must not be greater than 5")
    private float rating;

    //@JsonBackReference(value = "consumer-review")
    @JsonIgnoreProperties({"hibernateLazyInitializer","handler"})
    @ManyToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JoinColumn(name = "consumer_id", nullable = false)
    private Consumer consumer;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "created_at")
    private Date createdAt;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "updated_at")
    private Date updatedAt;

    private String status;
}
