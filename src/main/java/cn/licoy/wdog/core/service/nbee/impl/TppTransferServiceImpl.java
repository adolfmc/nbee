package cn.licoy.wdog.core.service.nbee.impl;import cn.licoy.wdog.core.dto.nbee.FindTppTransferDTO;import cn.licoy.wdog.core.dto.nbee.TppTransferAddDTO;import cn.licoy.wdog.core.dto.nbee.TppTransferUpdateDTO;import cn.licoy.wdog.core.vo.nbee.TppTransferVO;import org.springframework.beans.BeanUtils;import cn.licoy.wdog.common.exception.RequestException;import java.util.ArrayList;import java.util.List;import cn.licoy.wdog.core.entity.nbee.TppTransfer;import cn.licoy.wdog.core.mapper.nbee.TppTransferMapper;import cn.licoy.wdog.core.service.nbee.TppTransferService;import com.alibaba.fastjson.JSONObject;import com.baomidou.mybatisplus.mapper.EntityWrapper;import com.baomidou.mybatisplus.plugins.Page;import com.baomidou.mybatisplus.service.impl.ServiceImpl;import org.springframework.stereotype.Service;import java.util.Date;import java.util.List;/** * @author mc * @version Thu Jun 03 20:15:10 2021 */@Servicepublic class TppTransferServiceImpl extends ServiceImpl<TppTransferMapper, TppTransfer> implements TppTransferService {    @Override
    public TppTransfer findTppTransferById(String id) {
        TppTransfer user = this.selectById(id);
        if(user == null){
            return null;
        }
        return user;
    }



    @Override
    public Page<TppTransferVO> getAllTppTransferBySplitPage(FindTppTransferDTO findTppTransferDTO) {
        EntityWrapper<TppTransfer> wrapper = new EntityWrapper<>();
        wrapper.orderBy("create_date",findTppTransferDTO.getAsc());
        Page<TppTransfer> userPage = this.selectPage(new Page<>(findTppTransferDTO.getPage(),findTppTransferDTO.getPageSize()), wrapper);
        Page<TppTransferVO> userVOPage = new Page<>();
            try {
               BeanUtils.copyProperties(userPage,userVOPage);
            } catch (Exception e) {
               e.printStackTrace();
            }
        List<TppTransferVO> userVOS = new ArrayList<>();
        userPage.getRecords().forEach(v->{
            TppTransferVO vo = new TppTransferVO();
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
        TppTransfer user = this.selectById(userId);
        if(user==null){
            throw RequestException.fail("TppTransfer不存在");
        }
        TppTransfer TppTransfer = new TppTransfer();
    }

    @Override
    public void removeTppTransfer(String userId) {
        TppTransfer user = this.selectById(userId);
        if(user==null){
            throw RequestException.fail("TppTransfer不存在！");
        }
        TppTransfer TppTransfer = new TppTransfer();
        try {
            this.deleteById(userId);
        }catch (Exception e){
            throw RequestException.fail("删除失败",e);
        }
    }

    @Override
    public void add(TppTransferAddDTO addDTO) {
        try {
            TppTransfer TppTransfer = new TppTransfer();
            BeanUtils.copyProperties(addDTO,TppTransfer);
            TppTransfer.setCreateDate(new Date());
            this.insert(TppTransfer);
        }catch (Exception e){
            throw RequestException.fail("添加TppTransfer失败",e);
        }
    }

    @Override
    public void update(String id, TppTransferUpdateDTO updateDTO) {
        TppTransfer user = this.selectById(id);
        if(user==null){
            throw RequestException.fail(
                    String.format("更新失败，不存在ID为 %s 的TppTransfer",id));
        }
        try {
            BeanUtils.copyProperties(updateDTO,user);
            this.updateById(user);
        }catch (Exception e){
            throw RequestException.fail("TppTransfer信息更新失败",e);
        }
    }
}