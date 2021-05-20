package cn.licoy.wdog.core.service.nbee.impl;
import cn.licoy.wdog.common.exception.RequestException;
import cn.licoy.wdog.core.dto.nbee.FindSalaryListDTO;
import cn.licoy.wdog.core.dto.nbee.SalaryListAddDTO;
import cn.licoy.wdog.core.dto.nbee.SalaryListUpdateDTO;
import cn.licoy.wdog.core.entity.nbee.SalaryList;
import cn.licoy.wdog.core.mapper.nbee.SalaryListMapper;
import cn.licoy.wdog.core.service.nbee.SalaryListService;
import cn.licoy.wdog.core.vo.nbee.SalaryListVO;
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
public class SalaryListServiceImpl extends ServiceImpl<SalaryListMapper, SalaryList> implements SalaryListService {

    @Override
    public SalaryList findSalaryListById(String id) {
        SalaryList user = this.selectById(id);
        if(user == null){
            return null;
        }
        return user;
    }



    @Override
    public Page<SalaryListVO> getAllSalaryListBySplitPage(FindSalaryListDTO findSalaryListDTO) {
        EntityWrapper<SalaryList> wrapper = new EntityWrapper<>();
        if(findSalaryListDTO.getPayStatus()!=null && !"".equals(findSalaryListDTO.getPayStatus())){
            wrapper.eq("pay_status",findSalaryListDTO.getPayStatus()) ;
        }
        wrapper.orderBy("create_date",findSalaryListDTO.getAsc());

        Page<SalaryList> userPage = this.selectPage(new Page<>(findSalaryListDTO.getPage(),findSalaryListDTO.getPageSize()), wrapper);
        Page<SalaryListVO> userVOPage = new Page<>();
            try {
               BeanUtils.copyProperties(userPage,userVOPage);
            } catch (Exception e) {
               e.printStackTrace();
            }
        List<SalaryListVO> userVOS = new ArrayList<>();
        userPage.getRecords().forEach(v->{
            SalaryListVO vo = new SalaryListVO();
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
        SalaryList user = this.selectById(userId);
        if(user==null){
            throw RequestException.fail("SalaryList不存在");
        }
        SalaryList SalaryList = new SalaryList();
    }

    @Override
    public void removeSalaryList(String userId) {


        SalaryList user = this.selectById(userId);
        if(user==null){
            throw RequestException.fail("SalaryList不存在！");
        }
        SalaryList SalaryList = new SalaryList();
        try {
            this.deleteById(userId);
        }catch (Exception e){
            throw RequestException.fail("删除失败",e);
        }
    }

    @Override
    public void add(SalaryListAddDTO addDTO) {
        try {
            SalaryList SalaryList = new SalaryList();
            BeanUtils.copyProperties(addDTO ,SalaryList);
            SalaryList.setCreateDate(new Date());
            this.insert(SalaryList);
        }catch (Exception e){
            throw RequestException.fail("添加SalaryList失败",e);
        }
    }

    @Override
    public void update(String id, SalaryListUpdateDTO updateDTO) {
        SalaryList user = this.selectById(id);
        if(user==null){
            throw RequestException.fail(
                    String.format("更新失败，不存在ID为 %s 的SalaryList",id));
        }
        try {
            BeanUtils.copyProperties(updateDTO ,user);
            this.updateById(user);
        }catch (Exception e){
            throw RequestException.fail("SalaryList信息更新失败",e);
        }
    }
}
