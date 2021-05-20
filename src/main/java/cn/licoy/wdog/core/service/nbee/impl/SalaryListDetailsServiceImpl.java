package cn.licoy.wdog.core.service.nbee.impl;
import cn.licoy.wdog.common.exception.RequestException;
import cn.licoy.wdog.core.dto.nbee.FindSalaryListDetailsDTO;
import cn.licoy.wdog.core.dto.nbee.SalaryListDetailsAddDTO;
import cn.licoy.wdog.core.dto.nbee.SalaryListDetailsUpdateDTO;
import cn.licoy.wdog.core.entity.nbee.SalaryListDetails;
import cn.licoy.wdog.core.mapper.nbee.SalaryListDetailsMapper;
import cn.licoy.wdog.core.service.nbee.SalaryListDetailsService;
import cn.licoy.wdog.core.vo.nbee.SalaryListDetailsVO;
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
public class SalaryListDetailsServiceImpl extends ServiceImpl<SalaryListDetailsMapper, SalaryListDetails> implements SalaryListDetailsService {

    @Override
    public SalaryListDetails findSalaryListDetailsById(String id) {
        SalaryListDetails user = this.selectById(id);
        if(user == null){
            return null;
        }
        return user;
    }



    @Override
    public Page<SalaryListDetailsVO> getAllSalaryListDetailsBySplitPage(FindSalaryListDetailsDTO findSalaryListDetailsDTO) {
        EntityWrapper<SalaryListDetails> wrapper = new EntityWrapper<>();
        wrapper.orderBy("create_date",findSalaryListDetailsDTO.getAsc());
        Page<SalaryListDetails> userPage = this.selectPage(new Page<>(findSalaryListDetailsDTO.getPage(),findSalaryListDetailsDTO.getPageSize()), wrapper);
        Page<SalaryListDetailsVO> userVOPage = new Page<>();
            try {
               BeanUtils.copyProperties(userPage,userVOPage);
            } catch (Exception e) {
               e.printStackTrace();
            }
        List<SalaryListDetailsVO> userVOS = new ArrayList<>();
        userPage.getRecords().forEach(v->{
            SalaryListDetailsVO vo = new SalaryListDetailsVO();
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
        SalaryListDetails user = this.selectById(userId);
        if(user==null){
            throw RequestException.fail("SalaryListDetails不存在");
        }
        SalaryListDetails SalaryListDetails = new SalaryListDetails();
    }

    @Override
    public void removeSalaryListDetails(String userId) {
        SalaryListDetails user = this.selectById(userId);
        if(user==null){
            throw RequestException.fail("SalaryListDetails不存在！");
        }
        SalaryListDetails SalaryListDetails = new SalaryListDetails();
        try {
            this.deleteById(userId);
        }catch (Exception e){
            throw RequestException.fail("删除失败",e);
        }
    }

    @Override
    public void add(SalaryListDetailsAddDTO addDTO) {
        try {
            SalaryListDetails SalaryListDetails = new SalaryListDetails();
            BeanUtils.copyProperties(addDTO ,SalaryListDetails);
            SalaryListDetails.setCreateDate(new Date());
            this.insert(SalaryListDetails);
        }catch (Exception e){
            throw RequestException.fail("添加SalaryListDetails失败",e);
        }
    }

    @Override
    public void update(String id, SalaryListDetailsUpdateDTO updateDTO) {
        SalaryListDetails user = this.selectById(id);
        if(user==null){
            throw RequestException.fail(
                    String.format("更新失败，不存在ID为 %s 的SalaryListDetails",id));
        }
        try {
            BeanUtils.copyProperties(updateDTO , user);
            this.updateById(user);
        }catch (Exception e){
            throw RequestException.fail("SalaryListDetails信息更新失败",e);
        }
    }
}
