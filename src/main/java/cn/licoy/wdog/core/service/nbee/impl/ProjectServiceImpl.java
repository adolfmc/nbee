package cn.licoy.wdog.core.service.nbee.impl;
import cn.licoy.wdog.common.exception.RequestException;
import cn.licoy.wdog.core.dto.nbee.FindProjectDTO;
import cn.licoy.wdog.core.dto.nbee.ProjectAddDTO;
import cn.licoy.wdog.core.dto.nbee.ProjectUpdateDTO;
import cn.licoy.wdog.core.entity.nbee.Project;
import cn.licoy.wdog.core.mapper.nbee.ProjectMapper;
import cn.licoy.wdog.core.service.nbee.ProjectService;
import cn.licoy.wdog.core.vo.nbee.ProjectVO;
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
public class ProjectServiceImpl extends ServiceImpl<ProjectMapper, Project> implements ProjectService {

    @Override
    public Project findProjectById(String id) {
        Project user = this.selectById(id);
        if(user == null){
            return null;
        }
        return user;
    }



    @Override
    public Page<ProjectVO> getAllProjectBySplitPage(FindProjectDTO findProjectDTO) {
        EntityWrapper<Project> wrapper = new EntityWrapper<>();
        wrapper.orderBy("create_date",findProjectDTO.getAsc());
        Page<Project> userPage = this.selectPage(new Page<>(findProjectDTO.getPage(),findProjectDTO.getPageSize()), wrapper);
        Page<ProjectVO> userVOPage = new Page<>();
            try {
               BeanUtils.copyProperties(userPage,userVOPage);
            } catch (Exception e) {
               e.printStackTrace();
            }
        List<ProjectVO> userVOS = new ArrayList<>();
        userPage.getRecords().forEach(v->{
            ProjectVO vo = new ProjectVO();
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
        Project user = this.selectById(userId);
        if(user==null){
            throw RequestException.fail("Project不存在");
        }
        Project Project = new Project();
    }

    @Override
    public void removeProject(String userId) {
        Project user = this.selectById(userId);
        if(user==null){
            throw RequestException.fail("Project不存在！");
        }
        Project Project = new Project();
        try {
            this.deleteById(userId);
        }catch (Exception e){
            throw RequestException.fail("删除失败",e);
        }
    }

    @Override
    public void add(ProjectAddDTO addDTO) {
        try {
            Project Project = new Project();

            String sb = "";
            for(String v :addDTO.getCheckBill() ){
                sb = sb + v +",";
            }

            Project.setCheckBill( sb.substring( 0, sb.length() - 1));
            Project.setCompanyAlipay( addDTO.getCompanyAlipay());
            Project.setCompanyCheckAccount(addDTO.getCompanyCheckAccount());
            Project.setCompanyId( addDTO.getCompanyId() );
            Project.setCompanyMakerAccount( addDTO.getCompanyMakerAccount());
            Project.setCompanySendAccount( addDTO.getCompanySendAccount());
            Project.setEmployer( addDTO.getEmployer());
            Project.setMakerBill( addDTO.getMakerBill());
            Project.setName( addDTO.getName());
            Project.setProductId( addDTO.getProductId() );
            Project.setPublicAdminAccount( addDTO.getPublicAdminAccount() );
            Project.setSendBill( addDTO.getSendBill() );

            Project.setCreateDate(new Date());
            this.insert(Project);
        }catch (Exception e){
            throw RequestException.fail("添加Project失败",e);
        }
    }

    @Override
    public void update(String id, ProjectUpdateDTO updateDTO) {
        Project Project = this.selectById(id);
        if(Project==null){
            throw RequestException.fail(
                    String.format("更新失败，不存在ID为 %s 的Project",id));
        }
        try {
            BeanUtils.copyProperties(updateDTO,Project);


            String sb = "";
            for(String v :updateDTO.getCheckBill() ){
                sb = sb + v +",";
            }

            Project.setCheckBill( sb.substring( 0, sb.length() - 1));
            Project.setCompanyAlipay( updateDTO.getCompanyAlipay());
            Project.setCompanyCheckAccount(updateDTO.getCompanyCheckAccount());
            Project.setCompanyId( updateDTO.getCompanyId() );
            Project.setCompanyMakerAccount( updateDTO.getCompanyMakerAccount());
            Project.setCompanySendAccount( updateDTO.getCompanySendAccount());
            Project.setEmployer( updateDTO.getEmployer());
            Project.setMakerBill( updateDTO.getMakerBill());
            Project.setName( updateDTO.getName());
            Project.setProductId( updateDTO.getProductId() );
            Project.setPublicAdminAccount( updateDTO.getPublicAdminAccount() );
            Project.setSendBill( updateDTO.getSendBill() );

            Project.setCreateDate(new Date());


            this.updateById(Project);
        }catch (Exception e){
            throw RequestException.fail("Project信息更新失败",e);
        }
    }
}
