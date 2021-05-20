package cn.licoy.wdog.core.service.nbee.impl;
import cn.licoy.wdog.common.exception.RequestException;
import cn.licoy.wdog.core.dto.nbee.AccountCoreAddDTO;
import cn.licoy.wdog.core.dto.nbee.AccountCoreUpdateDTO;
import cn.licoy.wdog.core.dto.nbee.FindAccountCoreDTO;
import cn.licoy.wdog.core.entity.nbee.AccountCore;
import cn.licoy.wdog.core.mapper.nbee.AccountCoreMapper;
import cn.licoy.wdog.core.service.nbee.AccountCoreService;
import cn.licoy.wdog.core.vo.nbee.AccountCoreVO;
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
public class AccountCoreServiceImpl extends ServiceImpl<AccountCoreMapper, AccountCore> implements AccountCoreService {

    @Override
    public AccountCore findAccountCoreById(String id) {
        AccountCore user = this.selectById(id);
        if(user == null){
            return null;
        }
        return user;
    }



    @Override
    public Page<AccountCoreVO> getAllAccountCoreBySplitPage(FindAccountCoreDTO findAccountCoreDTO) {
        EntityWrapper<AccountCore> wrapper = new EntityWrapper<>();
        wrapper.orderBy("create_date",findAccountCoreDTO.getAsc());
        wrapper.eq("account_type","用人单位");
        Page<AccountCore> userPage = this.selectPage(new Page<>(findAccountCoreDTO.getPage(),findAccountCoreDTO.getPageSize()), wrapper);
        Page<AccountCoreVO> userVOPage = new Page<>();
            try {
               BeanUtils.copyProperties(userPage,userVOPage);
            } catch (Exception e) {
               e.printStackTrace();
            }
        List<AccountCoreVO> userVOS = new ArrayList<>();
        userPage.getRecords().forEach(v->{
            AccountCoreVO vo = new AccountCoreVO();
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
        AccountCore user = this.selectById(userId);
        if(user==null){
            throw RequestException.fail("AccountCore不存在");
        }
        AccountCore AccountCore = new AccountCore();
    }

    @Override
    public void removeAccountCore(String userId) {
        AccountCore user = this.selectById(userId);
        if(user==null){
            throw RequestException.fail("AccountCore不存在！");
        }
        AccountCore AccountCore = new AccountCore();
        try {
            this.deleteById(userId);
        }catch (Exception e){
            throw RequestException.fail("删除失败",e);
        }
    }

    @Override
    public void add(AccountCoreAddDTO addDTO) {
        try {
            AccountCore AccountCore = new AccountCore();
            BeanUtils.copyProperties(addDTO,AccountCore);
            AccountCore.setCreateDate(new Date());
            this.insert(AccountCore);
        }catch (Exception e){
            throw RequestException.fail("添加AccountCore失败",e);
        }
    }

    @Override
    public void update(String id, AccountCoreUpdateDTO updateDTO) {
        AccountCore user = this.selectById(id);
        if(user==null){
            throw RequestException.fail(
                    String.format("更新失败，不存在ID为 %s 的AccountCore",id));
        }
        try {
            BeanUtils.copyProperties(updateDTO,user);
            this.updateById(user);
        }catch (Exception e){
            throw RequestException.fail("AccountCore信息更新失败",e);
        }
    }
}
