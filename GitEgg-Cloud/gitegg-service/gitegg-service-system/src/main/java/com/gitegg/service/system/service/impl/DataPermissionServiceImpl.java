package com.gitegg.service.system.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.gitegg.service.system.dto.UpdateDataPermissionDTO;
import com.gitegg.service.system.entity.DataPermission;
import com.gitegg.service.system.mapper.DataPermissionMapper;
import com.gitegg.service.system.service.IDataPermissionService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * <p>
 *  数据权限服务实现类
 * </p>
 *
 * @author gitegg
 * @since 2019-02-27
 */
@Slf4j
@Service
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class DataPermissionServiceImpl extends ServiceImpl<DataPermissionMapper, DataPermission> implements IDataPermissionService {

    @Override
    public boolean updateUserDataPermission(UpdateDataPermissionDTO updateDataPermission){

        boolean result = false;
        Long userId = updateDataPermission.getUserId();
        List<Long> removeDataPermissions = updateDataPermission.getRemoveDataPermissions();
        if (!CollectionUtils.isEmpty(removeDataPermissions) && null != userId)
        {
            QueryWrapper<DataPermission> removeWrapper = new QueryWrapper<>();
            removeWrapper.eq("user_id", userId).in("organization_id", removeDataPermissions);
            result = this.remove(removeWrapper);
        }

        List<Long> addDataPermissions = updateDataPermission.getAddDataPermissions();
        if (!CollectionUtils.isEmpty(addDataPermissions) && null != userId)
        {
            List<DataPermission> dataPermissionList = new ArrayList<>();
            for (Long dataId: addDataPermissions)
            {
                DataPermission dataPermission = new DataPermission();
                dataPermission.setOrganizationId(dataId);
                dataPermission.setUserId(userId);
                dataPermissionList.add(dataPermission);
            }
            result = this.saveBatch(dataPermissionList);
        }
        return result;
    }

}
