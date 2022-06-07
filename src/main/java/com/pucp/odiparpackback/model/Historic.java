package com.pucp.odiparpackback.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import java.util.Date;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class Historic {
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id @Column(name = "id")
    private Integer id;

    @Column(name = "order_date")
    private Date orderDate;

    @Column(name = "starting_node")
    private String startingNode;

    @Column(name = "destination_node")
    private String destinationNode;

    @Column(name = "packages")
    private Integer packages;

    @Column(name = "client_id")
    private Integer clientId;

}
