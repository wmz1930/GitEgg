<template>
  <div>
    <a-input-search style="margin-bottom: 8px" placeholder="输入关键字进行过滤" @change="onTreeChange" />
    <a-tree
      ref="resourceTree"
      checkable
      :check-strictly="true"
      v-model="resourceTreeCheckedKeys"
      :tree-data="resourceTreeData"
      :replace-fields="treeProps"
      :default-expanded-keys="resourceTreeExpandedKeys"
      :expanded-keys="expandedKeys"
      :auto-expand-parent="autoExpandParent"
      @expand="onTreeExpand"
      @check="computeRoleResources"
    >
      <template slot="resourceName" slot-scope="{ resourceName }">
        <span v-if="resourceName.indexOf(searchValue) > -1">
          {{ resourceName.substr(0, resourceName.indexOf(searchValue)) }}
          <span style="color: #f50">{{ searchValue }}</span>
          {{ resourceName.substr(resourceName.indexOf(searchValue) + searchValue.length) }}
        </span>
        <span v-else>{{ resourceName }}</span>
      </template>
    </a-tree>
  </div>
</template>

<script>
import { fetchOrgList } from '@/api/system/organization'
export default {
  name: 'OrganizationTreeSelect',
  props: {
    prefixCls: {
      type: String,
      default: 'antd-pro-components-standard-form-row-index-standardFormRow'
    }
  },
  computed: {

  },
  data () {
    return {
      resourceTreeExpandedKeys: [],
      resourceTreeCheckedKeys: [],
      resourceTree: [],
      resourceTreeData: [],
      resourceDataList: [],
      oldResourceList: [],
      treeProps: {
        children: 'children', title: 'resourceName', key: 'id'
      },
      searchValue: '',
      autoExpandParent: true
    }
  },
  created () {
     this.queryOrganizationList()
  },
  methods: {
    // 查询组织机构数据
    queryOrganizationList () {
      this.listLoading = true
      fetchOrgList({ parentId: 0 }).then(response => {
        this.organizationList = response.data
        if (this.organizationList) {
          const organizationListStr = JSON.stringify(this.organizationList)
          console.log(organizationListStr)
          this.organizationTreeList = JSON.parse(organizationListStr.replace(/"isLeaf":1/g, '"isLeaf":true').replace(/"isLeaf":0/g, '"isLeaf":false'))
          console.log(this.organizationTreeList)
          this.generateOrganizationList(this.organizationTreeList)
        }
        this.listLoading = false
      })
    },
    // 准备左侧搜索树的数据
    generateOrganizationList (data) {
      for (let i = 0; i < data.length; i++) {
        const node = data[i]
        node.scopedSlots = { title: 'organizationName' }
        const key = node.id
        this.organizationSearchList.push({ key, title: node.organizationName })
        if (node.children) {
          this.generateOrganizationList(node.children)
        }
      }
    },
    getOrganizationParentKey (key, tree) {
      let parentKey
      for (let i = 0; i < tree.length; i++) {
        const node = tree[i]
        if (node.children) {
          if (node.children.some(item => item.id === key)) {
            parentKey = node.id
          } else if (this.getOrganizationParentKey(key, node.children)) {
            parentKey = this.getOrganizationParentKey(key, node.children)
          }
        }
      }
      return parentKey
    },
    onOrganizationTreeExpand (organizationTreeExpandedKeys) {
      this.organizationTreeExpandedKeys = organizationTreeExpandedKeys
      this.autoExpandParent = true
    },
    organizationTreeSearch (e) {
      const value = e.target.value
      const organizationTreeExpandedKeys = this.organizationSearchList
        .map(item => {
          if (item.title.indexOf(value) > -1) {
            return this.getOrganizationParentKey(item.key, this.organizationTreeList)
          }
          return null
        })
        .filter((item, i, self) => item && self.indexOf(item) === i)
      console.log(organizationTreeExpandedKeys)
      Object.assign(this, {
        organizationTreeExpandedKeys,
        searchValue: value,
        autoExpandParent: true
      })
    }
  }
}
</script>

<style lang="less" scoped>

</style>
