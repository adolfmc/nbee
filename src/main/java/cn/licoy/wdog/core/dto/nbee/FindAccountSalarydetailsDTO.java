package cn.licoy.wdog.core.dto.nbee;import cn.licoy.wdog.core.dto.SplitPageDTO;import com.fasterxml.jackson.annotation.JsonFormat;import java.math.BigDecimal;import lombok.Data;import java.util.Date;/** * @author mc * @version Fri Jun 11 10:41:12 2021 */@Datapublic class FindAccountSalarydetailsDTO extends SplitPageDTO  {   private String id;   private String status;   private BigDecimal amount;   private String salaryDetailsId;   @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss", timezone="GMT+8")   private Date createDate;   private String tixId;   private String name;   private String idCard;   private String currentCompany;   private String accountId;}