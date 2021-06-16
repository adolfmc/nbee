package cn.licoy.wdog.core.service.nbee.impl;
import cn.licoy.wdog.common.exception.RequestException;
import cn.licoy.wdog.core.dto.nbee.FindTppAlipayDTO;
import cn.licoy.wdog.core.dto.nbee.TppAlipayAddDTO;
import cn.licoy.wdog.core.dto.nbee.TppAlipayUpdateDTO;
import cn.licoy.wdog.core.entity.nbee.TppAlipay;
import cn.licoy.wdog.core.mapper.nbee.TppAlipayMapper;
import cn.licoy.wdog.core.service.nbee.TppAlipayService;
import cn.licoy.wdog.core.vo.nbee.TppAlipayVO;
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
 * @version Thu Jun 03 11:04:40 2021
 */
@Service
public class TppAlipayServiceImpl extends ServiceImpl<TppAlipayMapper, TppAlipay> implements TppAlipayService {

    @Override
    public TppAlipay findTppAlipayById(String id) {
        TppAlipay user = this.selectById(id);
        if(user == null){
            return null;
        }
        return user;
    }



    @Override
    public Page<TppAlipayVO> getAllTppAlipayBySplitPage(FindTppAlipayDTO findTppAlipayDTO) {
        EntityWrapper<TppAlipay> wrapper = new EntityWrapper<>();
        wrapper.orderBy("create_date",findTppAlipayDTO.getAsc());
        Page<TppAlipay> userPage = this.selectPage(new Page<>(findTppAlipayDTO.getPage(),findTppAlipayDTO.getPageSize()), wrapper);
        Page<TppAlipayVO> userVOPage = new Page<>();
            try {
               BeanUtils.copyProperties(userPage,userVOPage);
            } catch (Exception e) {
               e.printStackTrace();
            }
        List<TppAlipayVO> userVOS = new ArrayList<>();
        userPage.getRecords().forEach(v->{
            TppAlipayVO vo = new TppAlipayVO();
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
        TppAlipay user = this.selectById(userId);
        if(user==null){
            throw RequestException.fail("TppAlipay不存在");
        }
        TppAlipay TppAlipay = new TppAlipay();
    }

    @Override
    public void removeTppAlipay(String userId) {
        TppAlipay user = this.selectById(userId);
        if(user==null){
            throw RequestException.fail("TppAlipay不存在！");
        }
        TppAlipay TppAlipay = new TppAlipay();
        try {
            this.deleteById(userId);
        }catch (Exception e){
            throw RequestException.fail("删除失败",e);
        }
    }

    @Override
    public void add(TppAlipayAddDTO addDTO) {
        try {
            TppAlipay TppAlipay = new TppAlipay();
            BeanUtils.copyProperties(addDTO,TppAlipay);
            TppAlipay.setCreateDate(new Date());
            this.insert(TppAlipay);
        }catch (Exception e){
            throw RequestException.fail("添加TppAlipay失败",e);
        }
    }

    @Override
    public void update(String id, TppAlipayUpdateDTO updateDTO) {
        TppAlipay user = this.selectById(id);
        if(user==null){
            throw RequestException.fail(
                    String.format("更新失败，不存在ID为 %s 的TppAlipay",id));
        }
        try {
            BeanUtils.copyProperties(updateDTO,user);
            this.updateById(user);
        }catch (Exception e){
            throw RequestException.fail("TppAlipay信息更新失败",e);
        }
    }
}
