package com.example.go4lunch.data.pojo;

import com.fasterxml.jackson.annotation.JsonProperty;

public class PlusCodeResponse {
    @JsonProperty("global_code")
    String globalCode;
    @JsonProperty("compound_code")
    String compoundCode;
}
