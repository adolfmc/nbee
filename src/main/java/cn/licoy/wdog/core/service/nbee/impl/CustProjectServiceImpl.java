package cn.licoy.wdog.core.service.nbee.impl;
import cn.licoy.wdog.common.exception.RequestException;
import cn.licoy.wdog.core.dto.nbee.CustProjectAddDTO;
import cn.licoy.wdog.core.dto.nbee.CustProjectUpdateDTO;
import cn.licoy.wdog.core.dto.nbee.FindCustProjectDTO;
import cn.licoy.wdog.core.entity.nbee.CustProject;
import cn.licoy.wdog.core.mapper.nbee.CustProjectMapper;
import cn.licoy.wdog.core.service.nbee.CustProjectService;
import cn.licoy.wdog.core.vo.nbee.CustProjectVO;
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
public class CustProjectServiceImpl extends ServiceImpl<CustProjectMapper, CustProject> implements CustProjectService {

    @Override
    public CustProject findCustProjectById(String id) {
        CustProject user = this.selectById(id);
        if(user == null){
            return null;
        }
        return user;
    }



    @Override
    public Page<CustProjectVO> getAllCustProjectBySplitPage(FindCustProjectDTO findCustProjectDTO) {
        EntityWrapper<CustProject> wrapper = new EntityWrapper<>();
        wrapper.orderBy("create_date",findCustProjectDTO.getAsc());
        Page<CustProject> userPage = this.selectPage(new Page<>(findCustProjectDTO.getPage(),findCustProjectDTO.getPageSize()), wrapper);
        Page<CustProjectVO> userVOPage = new Page<>();
            try {
               BeanUtils.copyProperties(userPage,userVOPage);
            } catch (Exception e) {
               e.printStackTrace();
            }
        List<CustProjectVO> userVOS = new ArrayList<>();
        userPage.getRecords().forEach(v->{
            CustProjectVO vo = new CustProjectVO();
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
        CustProject user = this.selectById(userId);
        if(user==null){
            throw RequestException.fail("CustProject不存在");
        }
        CustProject CustProject = new CustProject();
    }

    @Override
    public void removeCustProject(String userId) {
        CustProject user = this.selectById(userId);
        if(user==null){
            throw RequestException.fail("CustProject不存在！");
        }
        CustProject CustProject = new CustProject();
        try {
            this.deleteById(userId);
        }catch (Exception e){
            throw RequestException.fail("删除失败",e);
        }
    }

    @Override
    public void add(CustProjectAddDTO addDTO) {
        try {
            CustProject CustProject = new CustProject();
            BeanUtils.copyProperties(addDTO,CustProject);
            CustProject.setCreateDate(new Date());
            this.insert(CustProject);
        }catch (Exception e){
            throw RequestException.fail("添加CustProject失败",e);
        }
    }

    @Override
    public void update(String id, CustProjectUpdateDTO updateDTO) {
        CustProject user = this.selectById(id);
        if(user==null){
            throw RequestException.fail(
                    String.format("更新失败，不存在ID为 %s 的CustProject",id));
        }
        try {
            BeanUtils.copyProperties(updateDTO,user);
            this.updateById(user);
        }catch (Exception e){
            throw RequestException.fail("CustProject信息更新失败",e);
        }
    }
}
