package cn.licoy.wdog.core.service.nbee.impl;
    public AccountSalarydetails findAccountSalarydetailsById(String id) {
        AccountSalarydetails user = this.selectById(id);
        if(user == null){
            return null;
        }
        return user;
    }



    @Override
    public Page<AccountSalarydetailsVO> getAllAccountSalarydetailsBySplitPage(FindAccountSalarydetailsDTO findAccountSalarydetailsDTO) {
        EntityWrapper<AccountSalarydetails> wrapper = new EntityWrapper<>();
        wrapper.orderBy("create_date",findAccountSalarydetailsDTO.getAsc());
        Page<AccountSalarydetails> userPage = this.selectPage(new Page<>(findAccountSalarydetailsDTO.getPage(),findAccountSalarydetailsDTO.getPageSize()), wrapper);
        Page<AccountSalarydetailsVO> userVOPage = new Page<>();
            try {
               BeanUtils.copyProperties(userPage,userVOPage);
            } catch (Exception e) {
               e.printStackTrace();
            }
        List<AccountSalarydetailsVO> userVOS = new ArrayList<>();
        userPage.getRecords().forEach(v->{
            AccountSalarydetailsVO vo = new AccountSalarydetailsVO();
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
        AccountSalarydetails user = this.selectById(userId);
        if(user==null){
            throw RequestException.fail("AccountSalarydetails不存在");
        }
        AccountSalarydetails AccountSalarydetails = new AccountSalarydetails();
    }

    @Override
    public void removeAccountSalarydetails(String userId) {
        AccountSalarydetails user = this.selectById(userId);
        if(user==null){
            throw RequestException.fail("AccountSalarydetails不存在！");
        }
        AccountSalarydetails AccountSalarydetails = new AccountSalarydetails();
        try {
            this.deleteById(userId);
        }catch (Exception e){
            throw RequestException.fail("删除失败",e);
        }
    }

    @Override
    public void add(AccountSalarydetailsAddDTO addDTO) {
        try {
            AccountSalarydetails AccountSalarydetails = new AccountSalarydetails();
            BeanUtils.copyProperties(addDTO,AccountSalarydetails);
            AccountSalarydetails.setCreateDate(new Date());
            this.insert(AccountSalarydetails);
        }catch (Exception e){
            throw RequestException.fail("添加AccountSalarydetails失败",e);
        }
    }

    @Override
    public void update(String id, AccountSalarydetailsUpdateDTO updateDTO) {
        AccountSalarydetails user = this.selectById(id);
        if(user==null){
            throw RequestException.fail(
                    String.format("更新失败，不存在ID为 %s 的AccountSalarydetails",id));
        }
        try {
            BeanUtils.copyProperties(updateDTO,user);
            this.updateById(user);
        }catch (Exception e){
            throw RequestException.fail("AccountSalarydetails信息更新失败",e);
        }
    }
}