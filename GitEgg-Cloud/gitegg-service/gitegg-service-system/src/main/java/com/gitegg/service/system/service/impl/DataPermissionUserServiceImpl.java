package com.gitegg.service.system.service.impl;

import java.util.ArrayList;
import java.util.List;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.gitegg.service.system.dto.*;
import com.gitegg.service.system.entity.UserInfo;
import org.springframework.beans.factory.annotation.Autowired;
import com.gitegg.service.system.entity.DataPermissionUser;
import com.gitegg.service.system.mapper.DataPermissionUserMapper;
import com.gitegg.service.system.service.IDataPermissionUserService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;

import com.gitegg.platform.base.util.BeanCopierUtils;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.CollectionUtils;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author GitEgg
 * @since 2021-05-13
 */
@Slf4j
@Service
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class DataPermissionUserServiceImpl extends ServiceImpl<DataPermissionUserMapper, DataPermissionUser> implements IDataPermissionUserService {

    private final DataPermissionUserMapper dataPermissionUserMapper;

    /**
    * 分页查询列表
    * @param page
    * @param queryDataPermissionUserDTO
    * @return
    */
    @Override
    public Page<DataPermissionUserDTO> queryDataPermissionUserList(Page<DataPermissionUserDTO> page, QueryDataPermissionUserDTO queryDataPermissionUserDTO) {
        Page<DataPermissionUserDTO> dataPermissionUserInfoList = dataPermissionUserMapper.queryDataPermissionUserList(page, queryDataPermissionUserDTO);
        return dataPermissionUserInfoList;
    }

    /**
    * 查询详情
    * @param queryDataPermissionUserDTO
    * @return
    */
    @Override
    public DataPermissionUserDTO queryDataPermissionUser(QueryDataPermissionUserDTO queryDataPermissionUserDTO) {
        DataPermissionUserDTO dataPermissionUserDTO = dataPermissionUserMapper.queryDataPermissionUser(queryDataPermissionUserDTO);
        return dataPermissionUserDTO;
    }

    /**
    * 创建
    * @param dataPermissionUser
    * @return
    */
    @Override
    public boolean createDataPermissionUser(CreateDataPermissionUserDTO dataPermissionUser) {
        DataPermissionUser dataPermissionUserEntity = BeanCopierUtils.copyByClass(dataPermissionUser, DataPermissionUser.class);
        boolean result = this.save(dataPermissionUserEntity);
        return result;
    }

    /**
    * 更新
    * @param dataPermissionUser
    * @return
    */
    @Override
    public boolean updateDataPermissionUser(UpdateDataPermissionUserDTO dataPermissionUser) {
        DataPermissionUser dataPermissionUserEntity = BeanCopierUtils.copyByClass(dataPermissionUser, DataPermissionUser.class);
        QueryWrapper<DataPermissionUser> wrapper = new QueryWrapper<>();
        wrapper.eq("id", dataPermissionUserEntity.getId());
        boolean result = this.update(dataPermissionUserEntity, wrapper);
        return result;
    }

    /**
    * 删除
    * @param dataPermissionUserId
    * @return
    */
    @Override
    public boolean deleteDataPermissionUser(Long dataPermissionUserId) {
        boolean result = this.removeById(dataPermissionUserId);
        return result;
    }

    /**
    * 批量删除
    * @param dataPermissionUserIds
    * @return
    */
    @Override
    public boolean batchDeleteDataPermissionUser(List<Long> dataPermissionUserIds) {
        boolean result = this.removeByIds(dataPermissionUserIds);
        return result;
    }

    /**
     * 更新用户的机构权限
     * @param updateDataPermission
     * @return
     */
    @Override
    public boolean updateUserOrganizationDataPermission(UpdateDataPermissionUserDTO updateDataPermission){
        boolean result = false;
        Long userId = updateDataPermission.getUserId();

        List<Long> removeDataPermissions = updateDataPermission.getRemoveDataPermissions();
        if (!CollectionUtils.isEmpty(removeDataPermissions) && null != userId)
        {
            QueryWrapper<DataPermissionUser> removeWrapper = new QueryWrapper<>();
            removeWrapper.eq("user_id", userId).in("organization_id", removeDataPermissions);
            result = this.remove(removeWrapper);
        }

        List<Long> addDataPermissions = updateDataPermission.getAddDataPermissions();
        if (!CollectionUtils.isEmpty(addDataPermissions) && null != userId)
        {
            List<DataPermissionUser> dataPermissionList = new ArrayList<>();
            for (Long dataId: addDataPermissions)
            {
                DataPermissionUser dataPermission = new DataPermissionUser();
                dataPermission.setOrganizationId(dataId);
                dataPermission.setUserId(userId);
                dataPermissionList.add(dataPermission);
            }
            result = this.saveBatch(dataPermissionList);
        }
        return result;
    }

    @Override
    public Page<UserInfo> selectOrganizationUserList(Page<UserInfo> page, QueryUserDTO user) {
        Page<UserInfo> pageUserInfo = dataPermissionUserMapper.selectOrganizationUserList(page, user);
        return pageUserInfo;
    }
}
