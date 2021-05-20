package cn.licoy.wdog.core.dto.nbee;import org.hibernate.validator.constraints.NotBlank;import com.fasterxml.jackson.annotation.JsonFormat;import lombok.Data;import java.util.Date;import java.math.BigDecimal;/** * @author mc * @version Sun May 02 11:04:52 2021 */@Datapublic class CustProjectAddDTO{   @NotBlank(message = "id")   private String id;   @NotBlank(message = "customerId")   private String customerId;   @NotBlank(message = "projectId")   private String projectId;   @NotBlank(message = "createDate")   @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss", timezone="GMT+8")   private Date createDate;}