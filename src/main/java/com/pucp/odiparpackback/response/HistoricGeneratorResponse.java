package com.pucp.odiparpackback.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class HistoricGeneratorResponse {
    @JsonProperty("orderId")
    private Integer orderId;

    @JsonProperty("date")
    private String date;

    @JsonProperty("startingNode")
    private String startingNode;

    @JsonProperty("destinationNode")
    private String destinationNode;

    @JsonProperty("clientId")
    private Integer clientId;
}
