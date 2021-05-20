package cn.licoy.wdog.core.service.nbee.impl;
import cn.licoy.wdog.common.exception.RequestException;
import cn.licoy.wdog.core.dto.nbee.FindProductDTO;
import cn.licoy.wdog.core.dto.nbee.ProductAddDTO;
import cn.licoy.wdog.core.dto.nbee.ProductUpdateDTO;
import cn.licoy.wdog.core.entity.nbee.Product;
import cn.licoy.wdog.core.mapper.nbee.ProductMapper;
import cn.licoy.wdog.core.service.nbee.ProductService;
import cn.licoy.wdog.core.vo.nbee.ProductVO;
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
public class ProductServiceImpl extends ServiceImpl<ProductMapper, Product> implements ProductService {

    @Override
    public Product findProductById(String id) {
        Product user = this.selectById(id);
        if(user == null){
            return null;
        }
        return user;
    }



    @Override
    public Page<ProductVO> getAllProductBySplitPage(FindProductDTO findProductDTO) {
        EntityWrapper<Product> wrapper = new EntityWrapper<>();
        wrapper.orderBy("create_date",findProductDTO.getAsc());
        Page<Product> userPage = this.selectPage(new Page<>(findProductDTO.getPage(),findProductDTO.getPageSize()), wrapper);
        Page<ProductVO> userVOPage = new Page<>();
            try {
               BeanUtils.copyProperties(userPage,userVOPage);
            } catch (Exception e) {
               e.printStackTrace();
            }
        List<ProductVO> userVOS = new ArrayList<>();
        userPage.getRecords().forEach(v->{
            ProductVO vo = new ProductVO();
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
        Product user = this.selectById(userId);
        if(user==null){
            throw RequestException.fail("Product不存在");
        }
        Product Product = new Product();
    }

    @Override
    public void removeProduct(String userId) {
        Product user = this.selectById(userId);
        if(user==null){
            throw RequestException.fail("Product不存在！");
        }
        Product Product = new Product();
        try {
            this.deleteById(userId);
        }catch (Exception e){
            throw RequestException.fail("删除失败",e);
        }
    }

    @Override
    public void add(ProductAddDTO addDTO) {
        try {
            Product Product = new Product();
            BeanUtils.copyProperties(addDTO,Product);
            Product.setCreateDate(new Date());
            this.insert(Product);
        }catch (Exception e){
            throw RequestException.fail("添加Product失败",e);
        }
    }

    @Override
    public void update(String id, ProductUpdateDTO updateDTO) {
        Product user = this.selectById(id);
        if(user==null){
            throw RequestException.fail(
                    String.format("更新失败，不存在ID为 %s 的Product",id));
        }
        try {
            BeanUtils.copyProperties(updateDTO,user);
            this.updateById(user);
        }catch (Exception e){
            throw RequestException.fail("Product信息更新失败",e);
        }
    }
}
