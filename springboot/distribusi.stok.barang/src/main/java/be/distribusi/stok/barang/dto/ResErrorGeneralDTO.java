package be.distribusi.stok.barang.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ResErrorGeneralDTO {
    @JsonProperty("status_msg")
    private String statusMsg;

    @JsonProperty("status_code")
    private String statusCode;

    @JsonProperty("error_message")
    private String errorMessage;
}
