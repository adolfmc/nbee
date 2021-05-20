package cn.licoy.wdog.core.service.nbee.impl;
import cn.licoy.wdog.common.exception.RequestException;
import cn.licoy.wdog.core.dto.nbee.AttendanceAddDTO;
import cn.licoy.wdog.core.dto.nbee.AttendanceUpdateDTO;
import cn.licoy.wdog.core.dto.nbee.FindAttendanceDTO;
import cn.licoy.wdog.core.entity.nbee.Attendance;
import cn.licoy.wdog.core.mapper.nbee.AttendanceMapper;
import cn.licoy.wdog.core.service.nbee.AttendanceService;
import cn.licoy.wdog.core.vo.nbee.AttendanceVO;
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
public class AttendanceServiceImpl extends ServiceImpl<AttendanceMapper, Attendance> implements AttendanceService {

    @Override
    public Attendance findAttendanceById(String id) {
        Attendance user = this.selectById(id);
        if(user == null){
            return null;
        }
        return user;
    }



    @Override
    public Page<AttendanceVO> getAllAttendanceBySplitPage(FindAttendanceDTO findAttendanceDTO) {
        EntityWrapper<Attendance> wrapper = new EntityWrapper<>();
        wrapper.orderBy("create_date",findAttendanceDTO.getAsc());
        Page<Attendance> userPage = this.selectPage(new Page<>(findAttendanceDTO.getPage(),findAttendanceDTO.getPageSize()), wrapper);
        Page<AttendanceVO> userVOPage = new Page<>();
            try {
               BeanUtils.copyProperties(userPage,userVOPage);
            } catch (Exception e) {
               e.printStackTrace();
            }
        List<AttendanceVO> userVOS = new ArrayList<>();
        userPage.getRecords().forEach(v->{
            AttendanceVO vo = new AttendanceVO();
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
        Attendance user = this.selectById(userId);
        if(user==null){
            throw RequestException.fail("Attendance不存在");
        }
        Attendance Attendance = new Attendance();
    }

    @Override
    public void removeAttendance(String userId) {
        Attendance user = this.selectById(userId);
        if(user==null){
            throw RequestException.fail("Attendance不存在！");
        }
        Attendance Attendance = new Attendance();
        try {
            this.deleteById(userId);
        }catch (Exception e){
            throw RequestException.fail("删除失败",e);
        }
    }

    @Override
    public void add(AttendanceAddDTO addDTO) {
        try {
            Attendance Attendance = new Attendance();
            BeanUtils.copyProperties(addDTO,Attendance);
            Attendance.setCreateDate(new Date());
            this.insert(Attendance);
        }catch (Exception e){
            throw RequestException.fail("添加Attendance失败",e);
        }
    }

    @Override
    public void update(String id, AttendanceUpdateDTO updateDTO) {
        Attendance user = this.selectById(id);
        if(user==null){
            throw RequestException.fail(
                    String.format("更新失败，不存在ID为 %s 的Attendance",id));
        }
        try {
            BeanUtils.copyProperties(updateDTO,user);
            this.updateById(user);
        }catch (Exception e){
            throw RequestException.fail("Attendance信息更新失败",e);
        }
    }
}
