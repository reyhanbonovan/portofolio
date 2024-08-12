package be.distribusi.stok.barang.dto.delete;

import be.distribusi.stok.barang.dto.add.ResAddDTO;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ResDeleteDTO {
    @JsonProperty("data")
    private ResDeleteDTO.deleteDataDTO data;

    @JsonProperty("status_code")
    private String statusCode;

    @JsonProperty("status_msg")
    private String statusMsg;

    @JsonProperty("error_message")
    private String errorMessage;

    @Getter
    @Setter
    @AllArgsConstructor
    @NoArgsConstructor
    public static class deleteDataDTO {
        @JsonProperty("logger_id")
        private String loggerId;

        @JsonProperty("status_data")
        private String statusData;
        }

}
