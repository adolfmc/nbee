package cn.licoy.wdog.core.service.nbee.impl;
import cn.licoy.wdog.common.exception.RequestException;
import cn.licoy.wdog.core.dto.nbee.CompanyAddDTO;
import cn.licoy.wdog.core.dto.nbee.CompanyUpdateDTO;
import cn.licoy.wdog.core.dto.nbee.FindCompanyDTO;
import cn.licoy.wdog.core.entity.nbee.Company;
import cn.licoy.wdog.core.mapper.nbee.CompanyMapper;
import cn.licoy.wdog.core.service.nbee.CompanyService;
import cn.licoy.wdog.core.vo.nbee.CompanyVO;
import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * @author mc
 * @version Sat Apr 17 19:14:07 2021
 */
@Service
public class CompanyServiceImpl extends ServiceImpl<CompanyMapper, Company> implements CompanyService {

    @Override
    public Company findCompanyById(String id) {
        Company user = this.selectById(id);
        if(user == null){
            return null;
        }
        return user;
    }



    @Override
    public Page<CompanyVO> getAllCompanyBySplitPage(FindCompanyDTO findCompanyDTO) {
        EntityWrapper<Company> wrapper = new EntityWrapper<>();
        wrapper.orderBy("create_date",findCompanyDTO.getAsc());
        Page<Company> userPage = this.selectPage(new Page<>(findCompanyDTO.getPage(),findCompanyDTO.getPageSize()), wrapper);
        Page<CompanyVO> userVOPage = new Page<>();
            try {
               org.springframework.beans.BeanUtils.copyProperties(userPage,userVOPage);
            } catch (Exception e) {
               e.printStackTrace();
            }
        List<CompanyVO> userVOS = new ArrayList<>();
        userPage.getRecords().forEach(v->{
            CompanyVO vo = new CompanyVO();
            try {
                org.springframework.beans.BeanUtils.copyProperties(v,vo);
            } catch (Exception e) {
               e.printStackTrace();
            }
            userVOS.add(vo);
        });
        userVOPage.setRecords(userVOS);
        return userVOPage;
    }

    @Override
    public void statusChange(String userId, Integer status) {
        Company user = this.selectById(userId);
        if(user==null){
            throw RequestException.fail("Company不存在");
        }
        Company Company = new Company();
    }

    @Override
    public void removeCompany(String userId) {
        Company user = this.selectById(userId);
        if(user==null){
            throw RequestException.fail("Company不存在！");
        }
        Company Company = new Company();
        try {
            this.deleteById(userId);
        }catch (Exception e){
            throw RequestException.fail("删除失败",e);
        }
    }

    @Override
    public void add(CompanyAddDTO addDTO) {
        try {
            Company Company = new Company();
            BeanUtils.copyProperties(addDTO,Company);
            Company.setCreateDate(new Date());
            this.insert(Company);
        }catch (Exception e){
            throw RequestException.fail("添加Company失败",e);
        }
    }

    @Override
    public void update(String id, CompanyUpdateDTO updateDTO) {
        Company user = this.selectById(id);
        if(user==null){
            throw RequestException.fail(
                    String.format("更新失败，不存在ID为 %s 的Company",id));
        }
        try {
            BeanUtils.copyProperties(updateDTO,user);
            this.updateById(user);
        }catch (Exception e){
            throw RequestException.fail("Company信息更新失败",e);
        }
    }
}
