package com.service.impl;

import com.entity.Admin;
import com.entity.AdminExample;
import com.mapper.AdminMapper;
import com.service.AdminService;
import com.utils.MD5Util;
import org.apache.dubbo.config.annotation.DubboService;

import javax.annotation.Resource;
import java.util.List;

@DubboService(interfaceClass = AdminService.class)
public class AdminServiceImpl implements AdminService {
    @Resource
    private AdminMapper mapper ;
    @Override
    public Admin login(String name, String pwd) {

        AdminExample example = new AdminExample();
        example.createCriteria().andANameEqualTo(name);

        List<Admin> adminList = mapper.selectByExample(example);

        // 当List的长度大于0时说明查询到用户名为name的用户，再进行密码比对
        if(adminList.size()>0){
            String password = MD5Util.getMD5(pwd);

            // 断言password不等于null
            assert password != null;
            if(password.equals(adminList.get(0).getaPass())){
                Admin admin = adminList.get(0);
                // 只返回一个不包含敏感信息（密码）的对象
                return new Admin(admin.getaId(),admin.getaName());
            }
        }

        return null;
    }
}
