package cn.licoy.wdog.core.service.nbee.impl;
    public TppTixian findTppTixianById(String id) {
        TppTixian user = this.selectById(id);
        if(user == null){
            return null;
        }
        return user;
    }



    @Override
    public Page<TppTixianVO> getAllTppTixianBySplitPage(FindTppTixianDTO findTppTixianDTO) {
        EntityWrapper<TppTixian> wrapper = new EntityWrapper<>();
        wrapper.orderBy("create_date",findTppTixianDTO.getAsc());
        Page<TppTixian> userPage = this.selectPage(new Page<>(findTppTixianDTO.getPage(),findTppTixianDTO.getPageSize()), wrapper);
        Page<TppTixianVO> userVOPage = new Page<>();
            try {
               BeanUtils.copyProperties(userPage,userVOPage);
            } catch (Exception e) {
               e.printStackTrace();
            }
        List<TppTixianVO> userVOS = new ArrayList<>();
        userPage.getRecords().forEach(v->{
            TppTixianVO vo = new TppTixianVO();
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
        TppTixian user = this.selectById(userId);
        if(user==null){
            throw RequestException.fail("TppTixian不存在");
        }
        TppTixian TppTixian = new TppTixian();
    }

    @Override
    public void removeTppTixian(String userId) {
        TppTixian user = this.selectById(userId);
        if(user==null){
            throw RequestException.fail("TppTixian不存在！");
        }
        TppTixian TppTixian = new TppTixian();
        try {
            this.deleteById(userId);
        }catch (Exception e){
            throw RequestException.fail("删除失败",e);
        }
    }

    @Override
    public void add(TppTixianAddDTO addDTO) {
        try {
            TppTixian TppTixian = new TppTixian();
            BeanUtils.copyProperties(addDTO,TppTixian);
            TppTixian.setCreateDate(new Date());
            this.insert(TppTixian);
        }catch (Exception e){
            throw RequestException.fail("添加TppTixian失败",e);
        }
    }

    @Override
    public void update(String id, TppTixianUpdateDTO updateDTO) {
        TppTixian user = this.selectById(id);
        if(user==null){
            throw RequestException.fail(
                    String.format("更新失败，不存在ID为 %s 的TppTixian",id));
        }
        try {
            BeanUtils.copyProperties(updateDTO,user);
            this.updateById(user);
        }catch (Exception e){
            throw RequestException.fail("TppTixian信息更新失败",e);
        }
    }
}