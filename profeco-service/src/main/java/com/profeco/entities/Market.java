package com.profeco.entities;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Table(name = "markets")
@Data  @AllArgsConstructor  @NoArgsConstructor @Builder
public class Market implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    private String rfc;
    @Column(name = "web_page")
    private String webPage;
    private String image;
}
