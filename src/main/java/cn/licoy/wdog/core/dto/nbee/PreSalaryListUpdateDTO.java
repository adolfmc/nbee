package cn.licoy.wdog.core.dto.nbee;import lombok.Data;import org.hibernate.validator.constraints.NotBlank;import javax.validation.constraints.NotNull;import javax.validation.constraints.Pattern;import javax.validation.constraints.Size;import java.util.List;import com.fasterxml.jackson.annotation.JsonFormat;import java.math.BigDecimal;import lombok.Data;import java.util.Date;/** * @author mc * @version Tue May 04 18:50:13 2021 */@Datapublic class PreSalaryListUpdateDTO{   private String id;   @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss", timezone="GMT+8")   private Date kaoqingDate;   private BigDecimal onJobMunber;   private BigDecimal payNumber;   private BigDecimal payAmount;   @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss", timezone="GMT+8")   private Date preSalaryListCreatetime;   @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss", timezone="GMT+8")   private Date createDate;   private String createUser;   private String batchNo;   private String company;   private String projectName;   private String productName;   private String batchName;}