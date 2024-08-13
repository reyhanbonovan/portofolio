package be.distribusi.stok.barang.dto.insert;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ResErrorExceptionDTO {
    @JsonProperty("logger_id")
    private String loggerId;

    @JsonProperty("status_msg")
    private String statusMsg;

    @JsonProperty("status_code")
    private String statusCode;

    @JsonProperty("error_message")
    private String errorMessage;
}
