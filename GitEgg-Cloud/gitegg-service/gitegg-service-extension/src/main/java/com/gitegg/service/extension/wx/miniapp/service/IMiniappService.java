package com.gitegg.service.extension.wx.miniapp.service;

import java.util.List;

import com.gitegg.service.extension.wx.miniapp.entity.Miniapp;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;

import com.gitegg.service.extension.wx.miniapp.bo.MiniappExportBO;
import com.gitegg.service.extension.wx.miniapp.dto.MiniappDTO;
import com.gitegg.service.extension.wx.miniapp.dto.CreateMiniappDTO;
import com.gitegg.service.extension.wx.miniapp.dto.UpdateMiniappDTO;
import com.gitegg.service.extension.wx.miniapp.dto.QueryMiniappDTO;

import org.springframework.web.multipart.MultipartFile;

/**
 * <p>
 * 微信小程序配置 服务类
 * </p>
 *
 * @author GitEgg
 * @since 2023-07-16
 */
public interface IMiniappService extends IService<Miniapp> {

    /**
    * 分页查询微信小程序配置列表
    * @param page
    * @param queryMiniappDTO
    * @return
    */
    Page<MiniappDTO> queryMiniappList(Page<MiniappDTO> page, QueryMiniappDTO queryMiniappDTO);

    /**
    * 查询微信小程序配置列表
    * @param queryMiniappDTO
    * @return
    */
    List<MiniappDTO> queryMiniappList(QueryMiniappDTO queryMiniappDTO);

    /**
    * 查询微信小程序配置详情
    * @param queryMiniappDTO
    * @return
    */
    MiniappDTO queryMiniapp(QueryMiniappDTO queryMiniappDTO);

    /**
    * 创建微信小程序配置
    * @param miniapp
    * @return
    */
    boolean createMiniapp(CreateMiniappDTO miniapp);

    /**
    * 更新微信小程序配置
    * @param miniapp
    * @return
    */
    boolean updateMiniapp(UpdateMiniappDTO miniapp);

    /**
    * 删除微信小程序配置
    * @param miniappId
    * @return
    */
    boolean deleteMiniapp(Long miniappId);

    /**
    * 批量删除微信小程序配置
    * @param miniappIds
    * @return
    */
    boolean batchDeleteMiniapp(List<Long> miniappIds);

    /**
    * 导出微信小程序配置列表
    * @param queryMiniappDTO
    * @return
    */
    List<MiniappExportBO> exportMiniappList(QueryMiniappDTO queryMiniappDTO);

    /**
    * 导入微信小程序配置列表
    * @param file
    * @return
    */
    boolean importMiniappList(MultipartFile file);

    /**
     * 初始化微信小程序配置表列表
     * @return
     */
    void initMiniappList();

    /**
     * 排除多租户插件查询微信小程序配置信息
     * @param miniappDTO
     * @return
     */
    MiniappDTO getMiniapp(QueryMiniappDTO miniappDTO);

    /**
     * 通过appid获取appid，忽略租户插件
     * @param miniappId
     * @return
     */
    String getMiniappId(String miniappId);

    /**
     * 根据AppId切换当前ThreadLocal的微信小程序
     * @param miniappId
     * @return
     */
    boolean switchover(String miniappId);
}
