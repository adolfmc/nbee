package cn.licoy.wdog.core.service.nbee.impl;
import cn.licoy.wdog.common.exception.RequestException;
import cn.licoy.wdog.core.dto.nbee.AccountingentriesAddDTO;
import cn.licoy.wdog.core.dto.nbee.AccountingentriesUpdateDTO;
import cn.licoy.wdog.core.dto.nbee.FindAccountingentriesDTO;
import cn.licoy.wdog.core.entity.nbee.Accountingentries;
import cn.licoy.wdog.core.mapper.nbee.AccountingentriesMapper;
import cn.licoy.wdog.core.service.nbee.AccountingentriesService;
import cn.licoy.wdog.core.vo.nbee.AccountingentriesVO;
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
public class AccountingentriesServiceImpl extends ServiceImpl<AccountingentriesMapper, Accountingentries> implements AccountingentriesService {

    @Override
    public Accountingentries findAccountingentriesById(String id) {
        Accountingentries user = this.selectById(id);
        if(user == null){
            return null;
        }
        return user;
    }



    @Override
    public Page<AccountingentriesVO> getAllAccountingentriesBySplitPage(FindAccountingentriesDTO findAccountingentriesDTO) {
        EntityWrapper<Accountingentries> wrapper = new EntityWrapper<>();
        wrapper.orderBy("create_date",findAccountingentriesDTO.getAsc());
        Page<Accountingentries> userPage = this.selectPage(new Page<>(findAccountingentriesDTO.getPage(),findAccountingentriesDTO.getPageSize()), wrapper);
        Page<AccountingentriesVO> userVOPage = new Page<>();
            try {
               BeanUtils.copyProperties(userPage,userVOPage);
            } catch (Exception e) {
               e.printStackTrace();
            }
        List<AccountingentriesVO> userVOS = new ArrayList<>();
        userPage.getRecords().forEach(v->{
            AccountingentriesVO vo = new AccountingentriesVO();
            try {
               BeanUtils.copyProperties(v,vo);
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
        Accountingentries user = this.selectById(userId);
        if(user==null){
            throw RequestException.fail("Accountingentries不存在");
        }
        Accountingentries Accountingentries = new Accountingentries();
    }

    @Override
    public void removeAccountingentries(String userId) {
        Accountingentries user = this.selectById(userId);
        if(user==null){
            throw RequestException.fail("Accountingentries不存在！");
        }
        Accountingentries Accountingentries = new Accountingentries();
        try {
            this.deleteById(userId);
        }catch (Exception e){
            throw RequestException.fail("删除失败",e);
        }
    }

    @Override
    public void add(AccountingentriesAddDTO addDTO) {
        try {
            Accountingentries Accountingentries = new Accountingentries();
            BeanUtils.copyProperties(addDTO,Accountingentries);
            Accountingentries.setCreateDate(new Date());
            this.insert(Accountingentries);
        }catch (Exception e){
            throw RequestException.fail("添加Accountingentries失败",e);
        }
    }

    @Override
    public void update(String id, AccountingentriesUpdateDTO updateDTO) {
        Accountingentries user = this.selectById(id);
        if(user==null){
            throw RequestException.fail(
                    String.format("更新失败，不存在ID为 %s 的Accountingentries",id));
        }
        try {
            BeanUtils.copyProperties(updateDTO,user);
            this.updateById(user);
        }catch (Exception e){
            throw RequestException.fail("Accountingentries信息更新失败",e);
        }
    }
}
