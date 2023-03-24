package com.example.go4lunch.data.pojo;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.gson.annotations.SerializedName;

public class PlusCodeResponse {
    @SerializedName("global_code")
    String globalCode;
    @SerializedName("compound_code")
    String compoundCode;
}
