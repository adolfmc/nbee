package cn.licoy.wdog.core.service.nbee.impl;
import cn.licoy.wdog.common.exception.RequestException;
import cn.licoy.wdog.core.dto.nbee.AttendanceDetailsAddDTO;
import cn.licoy.wdog.core.dto.nbee.AttendanceDetailsUpdateDTO;
import cn.licoy.wdog.core.dto.nbee.FindAttendanceDetailsDTO;
import cn.licoy.wdog.core.entity.nbee.AttendanceDetails;
import cn.licoy.wdog.core.mapper.nbee.AttendanceDetailsMapper;
import cn.licoy.wdog.core.service.nbee.AttendanceDetailsService;
import cn.licoy.wdog.core.vo.nbee.AttendanceDetailsVO;
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
public class AttendanceDetailsServiceImpl extends ServiceImpl<AttendanceDetailsMapper, AttendanceDetails> implements AttendanceDetailsService {

    @Override
    public AttendanceDetails findAttendanceDetailsById(String id) {
        AttendanceDetails user = this.selectById(id);
        if(user == null){
            return null;
        }
        return user;
    }



    @Override
    public Page<AttendanceDetailsVO> getAllAttendanceDetailsBySplitPage(FindAttendanceDetailsDTO findAttendanceDetailsDTO) {
        EntityWrapper<AttendanceDetails> wrapper = new EntityWrapper<>();
        wrapper.orderBy("create_date",findAttendanceDetailsDTO.getAsc());
        Page<AttendanceDetails> userPage = this.selectPage(new Page<>(findAttendanceDetailsDTO.getPage(),findAttendanceDetailsDTO.getPageSize()), wrapper);
        Page<AttendanceDetailsVO> userVOPage = new Page<>();
            try {
               BeanUtils.copyProperties(userPage,userVOPage);
            } catch (Exception e) {
               e.printStackTrace();
            }
        List<AttendanceDetailsVO> userVOS = new ArrayList<>();
        userPage.getRecords().forEach(v->{
            AttendanceDetailsVO vo = new AttendanceDetailsVO();
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
        AttendanceDetails user = this.selectById(userId);
        if(user==null){
            throw RequestException.fail("AttendanceDetails不存在");
        }
        AttendanceDetails AttendanceDetails = new AttendanceDetails();
    }

    @Override
    public void removeAttendanceDetails(String userId) {
        AttendanceDetails user = this.selectById(userId);
        if(user==null){
            throw RequestException.fail("AttendanceDetails不存在！");
        }
        AttendanceDetails AttendanceDetails = new AttendanceDetails();
        try {
            this.deleteById(userId);
        }catch (Exception e){
            throw RequestException.fail("删除失败",e);
        }
    }

    @Override
    public void add(AttendanceDetailsAddDTO addDTO) {
        try {
            AttendanceDetails AttendanceDetails = new AttendanceDetails();
            BeanUtils.copyProperties(addDTO,AttendanceDetails);
            AttendanceDetails.setCreateDate(new Date());
            this.insert(AttendanceDetails);
        }catch (Exception e){
            throw RequestException.fail("添加AttendanceDetails失败",e);
        }
    }

    @Override
    public void update(String id, AttendanceDetailsUpdateDTO updateDTO) {
        AttendanceDetails user = this.selectById(id);
        if(user==null){
            throw RequestException.fail(
                    String.format("更新失败，不存在ID为 %s 的AttendanceDetails",id));
        }
        try {
            BeanUtils.copyProperties(updateDTO,user);
            this.updateById(user);
        }catch (Exception e){
            throw RequestException.fail("AttendanceDetails信息更新失败",e);
        }
    }
}
