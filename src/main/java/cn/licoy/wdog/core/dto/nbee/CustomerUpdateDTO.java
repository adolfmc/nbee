package cn.licoy.wdog.core.dto.nbee;import lombok.Data;import org.hibernate.validator.constraints.NotBlank;import javax.validation.constraints.NotNull;import javax.validation.constraints.Pattern;import javax.validation.constraints.Size;import java.util.List;import com.fasterxml.jackson.annotation.JsonFormat;import java.math.BigDecimal;import lombok.Data;import java.util.Date;/** * @author mc * @version Thu May 20 14:09:47 2021 */@Datapublic class CustomerUpdateDTO{   private String id;   private String name;   private String sex;   private String idCard;   private String mobile;   private String alipay;   private String vxapy;   private String bank1;   private String bank2;   private String bank3;   @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss", timezone="GMT+8")   private Date createDate;   private String createUser;   private String bank4;   private String bank5;   private String currentCompany;}