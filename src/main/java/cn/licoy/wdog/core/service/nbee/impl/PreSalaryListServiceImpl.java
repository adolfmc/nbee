package cn.licoy.wdog.core.service.nbee.impl;
import cn.licoy.wdog.common.exception.RequestException;
import cn.licoy.wdog.core.dto.nbee.FindPreSalaryListDTO;
import cn.licoy.wdog.core.dto.nbee.PreSalaryListAddDTO;
import cn.licoy.wdog.core.dto.nbee.PreSalaryListUpdateDTO;
import cn.licoy.wdog.core.entity.nbee.PreSalaryList;
import cn.licoy.wdog.core.mapper.nbee.PreSalaryListMapper;
import cn.licoy.wdog.core.service.nbee.PreSalaryListService;
import cn.licoy.wdog.core.vo.nbee.PreSalaryListVO;
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
public class PreSalaryListServiceImpl extends ServiceImpl<PreSalaryListMapper, PreSalaryList> implements PreSalaryListService {

    @Override
    public PreSalaryList findPreSalaryListById(String id) {
        PreSalaryList user = this.selectById(id);
        if(user == null){
            return null;
        }
        return user;
    }



    @Override
    public Page<PreSalaryListVO> getAllPreSalaryListBySplitPage(FindPreSalaryListDTO findPreSalaryListDTO) {
        EntityWrapper<PreSalaryList> wrapper = new EntityWrapper<>();
        wrapper.orderBy("create_date",findPreSalaryListDTO.getAsc());
        Page<PreSalaryList> userPage = this.selectPage(new Page<>(findPreSalaryListDTO.getPage(),findPreSalaryListDTO.getPageSize()), wrapper);
        Page<PreSalaryListVO> userVOPage = new Page<>();
            try {
               BeanUtils.copyProperties(userPage,userVOPage);
            } catch (Exception e) {
               e.printStackTrace();
            }
        List<PreSalaryListVO> userVOS = new ArrayList<>();
        userPage.getRecords().forEach(v->{
            PreSalaryListVO vo = new PreSalaryListVO();
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
        PreSalaryList user = this.selectById(userId);
        if(user==null){
            throw RequestException.fail("PreSalaryList不存在");
        }
        PreSalaryList PreSalaryList = new PreSalaryList();
    }

    @Override
    public void removePreSalaryList(String userId) {
        PreSalaryList user = this.selectById(userId);
        if(user==null){
            throw RequestException.fail("PreSalaryList不存在！");
        }
        PreSalaryList PreSalaryList = new PreSalaryList();
        try {
            this.deleteById(userId);
        }catch (Exception e){
            throw RequestException.fail("删除失败",e);
        }
    }

    @Override
    public void add(PreSalaryListAddDTO addDTO) {
        try {
            PreSalaryList PreSalaryList = new PreSalaryList();
            BeanUtils.copyProperties(addDTO ,PreSalaryList);
            PreSalaryList.setCreateDate(new Date());
            this.insert(PreSalaryList);
        }catch (Exception e){
            throw RequestException.fail("添加PreSalaryList失败",e);
        }
    }

    @Override
    public void update(String id, PreSalaryListUpdateDTO updateDTO) {
        PreSalaryList user = this.selectById(id);
        if(user==null){
            throw RequestException.fail(
                    String.format("更新失败，不存在ID为 %s 的PreSalaryList",id));
        }
        try {
            BeanUtils.copyProperties(updateDTO ,user);
            this.updateById(user);
        }catch (Exception e){
            throw RequestException.fail("PreSalaryList信息更新失败",e);
        }
    }
}
