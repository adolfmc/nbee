package cn.licoy.wdog.core.dto.nbee;import com.fasterxml.jackson.annotation.JsonFormat;import lombok.Data;import java.util.Date;/** * @author mc * @version Sun May 02 11:04:52 2021 */@Datapublic class ProjectAddDTO{   private String id;   private String name;   private String companyId;   private String companyAlipay;   private String productId;   private String publicAdminAccount;   private String makerBill;   private String[] checkBill;   private String sendBill;   private String companyMakerAccount;   private String companyCheckAccount;   private String companySendAccount;   private String employer;   @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss", timezone="GMT+8")   private Date createDate;}