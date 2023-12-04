package com.example.tofi.common.persistance.domain.accountservice.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AccessLevel;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;

@Data
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class TransferRequest {

    @JsonProperty("sender_id")
    Long senderId;

    @JsonProperty("receiver_id")
    Long receiverId;

    @JsonProperty("sum")
    Double sum;

    @JsonProperty("currency")
    String currency;

}
