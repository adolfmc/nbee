package cn.licoy.wdog.core.dto.nbee;import org.hibernate.validator.constraints.NotBlank;import com.fasterxml.jackson.annotation.JsonFormat;import lombok.Data;import java.util.Date;import java.math.BigDecimal;/** * @author mc * @version Mon May 03 13:27:22 2021 */@Datapublic class AttendanceDetailsAddDTO{   private String id;   private String attendanceId;   private String no;   private String customerName;   @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss", timezone="GMT+8")   private Date workstartTime;   @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss", timezone="GMT+8")   private Date workendTime;   @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss", timezone="GMT+8")   private Date importtime;   @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss", timezone="GMT+8")   private Date createDate;   private String idcard;   private String mobile;   private BigDecimal workdays;   private String batchNo;   private String company;   private String projectName;   @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss", timezone="GMT+8")   private Date kaoqingDate;   private String createUser;   private String productName;   private String batchName;}