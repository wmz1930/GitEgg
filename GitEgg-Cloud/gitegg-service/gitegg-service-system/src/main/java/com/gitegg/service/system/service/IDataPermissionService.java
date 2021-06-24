package com.gitegg.service.system.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.gitegg.service.system.dto.UpdateDataPermissionDTO;
import com.gitegg.service.system.entity.DataPermission;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author gitegg
 * @since 2019-02-27
 */
public interface IDataPermissionService extends IService<DataPermission> {

    /**
     * 修改用户数据权限
     * @param updateDataPermission
     * @return
     */
    boolean updateUserDataPermission(UpdateDataPermissionDTO updateDataPermission);
}
