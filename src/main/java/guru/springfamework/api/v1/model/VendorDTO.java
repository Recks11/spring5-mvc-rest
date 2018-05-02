package guru.springfamework.api.v1.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class VendorDTO {
    //    private Long id;
    @ApiModelProperty(value = "Name of the vendor")
    private String name;

    @ApiModelProperty(value = "Vendor URL")
    @JsonProperty("vendor_url")
    private String url;
}
