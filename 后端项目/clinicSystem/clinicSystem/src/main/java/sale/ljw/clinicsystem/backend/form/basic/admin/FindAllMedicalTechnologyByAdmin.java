package sale.ljw.clinicsystem.backend.form.basic.admin;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;

@Data
@ApiModel(value = "查找全部医技列表")
public class FindAllMedicalTechnologyByAdmin {
    @ApiModelProperty(value = "页码")
    @NotNull(message = "页码不得为空")
    private Integer page;

    @ApiModelProperty(value = "医技名称")
    private String medicalName;

    @ApiModelProperty(value = "医技分类")
    private String medicalId;

}
