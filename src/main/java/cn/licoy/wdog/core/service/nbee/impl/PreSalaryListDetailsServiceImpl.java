package cn.licoy.wdog.core.service.nbee.impl;
import cn.licoy.wdog.common.exception.RequestException;
import cn.licoy.wdog.core.dto.nbee.FindPreSalaryListDetailsDTO;
import cn.licoy.wdog.core.dto.nbee.PreSalaryListDetailsAddDTO;
import cn.licoy.wdog.core.dto.nbee.PreSalaryListDetailsUpdateDTO;
import cn.licoy.wdog.core.entity.nbee.PreSalaryListDetails;
import cn.licoy.wdog.core.mapper.nbee.PreSalaryListDetailsMapper;
import cn.licoy.wdog.core.service.nbee.PreSalaryListDetailsService;
import cn.licoy.wdog.core.vo.nbee.PreSalaryListDetailsVO;
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
public class PreSalaryListDetailsServiceImpl extends ServiceImpl<PreSalaryListDetailsMapper, PreSalaryListDetails> implements PreSalaryListDetailsService {

    @Override
    public PreSalaryListDetails findPreSalaryListDetailsById(String id) {
        PreSalaryListDetails user = this.selectById(id);
        if(user == null){
            return null;
        }
        return user;
    }



    @Override
    public Page<PreSalaryListDetailsVO> getAllPreSalaryListDetailsBySplitPage(FindPreSalaryListDetailsDTO findPreSalaryListDetailsDTO) {
        EntityWrapper<PreSalaryListDetails> wrapper = new EntityWrapper<>();
        wrapper.orderBy("create_date",findPreSalaryListDetailsDTO.getAsc());
        Page<PreSalaryListDetails> userPage = this.selectPage(new Page<>(findPreSalaryListDetailsDTO.getPage(),findPreSalaryListDetailsDTO.getPageSize()), wrapper);
        Page<PreSalaryListDetailsVO> userVOPage = new Page<>();
            try {
               BeanUtils.copyProperties(userPage,userVOPage);
            } catch (Exception e) {
               e.printStackTrace();
            }
        List<PreSalaryListDetailsVO> userVOS = new ArrayList<>();
        userPage.getRecords().forEach(v->{
            PreSalaryListDetailsVO vo = new PreSalaryListDetailsVO();
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
        PreSalaryListDetails user = this.selectById(userId);
        if(user==null){
            throw RequestException.fail("PreSalaryListDetails不存在");
        }
        PreSalaryListDetails PreSalaryListDetails = new PreSalaryListDetails();
    }

    @Override
    public void removePreSalaryListDetails(String userId) {
        PreSalaryListDetails user = this.selectById(userId);
        if(user==null){
            throw RequestException.fail("PreSalaryListDetails不存在！");
        }
        PreSalaryListDetails PreSalaryListDetails = new PreSalaryListDetails();
        try {
            this.deleteById(userId);
        }catch (Exception e){
            throw RequestException.fail("删除失败",e);
        }
    }

    @Override
    public void add(PreSalaryListDetailsAddDTO addDTO) {
        try {
            PreSalaryListDetails PreSalaryListDetails = new PreSalaryListDetails();
            BeanUtils.copyProperties(addDTO,PreSalaryListDetails);
            PreSalaryListDetails.setCreateDate(new Date());
            this.insert(PreSalaryListDetails);
        }catch (Exception e){
            throw RequestException.fail("添加PreSalaryListDetails失败",e);
        }
    }

    @Override
    public void update(String id, PreSalaryListDetailsUpdateDTO updateDTO) {
        PreSalaryListDetails user = this.selectById(id);
        if(user==null){
            throw RequestException.fail(
                    String.format("更新失败，不存在ID为 %s 的PreSalaryListDetails",id));
        }
        try {
            BeanUtils.copyProperties(updateDTO ,user);
            this.updateById(user);
        }catch (Exception e){
            throw RequestException.fail("PreSalaryListDetails信息更新失败",e);
        }
    }
}
