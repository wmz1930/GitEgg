<template>
  <div>
    <a-input-search style="margin-bottom: 8px" placeholder="输入关键字进行过滤" @change="organizationTreeSearch" />
    <a-tree
      :ref="treeId"
      :default-expanded-keys="organizationTreeExpandedKeys"
      :expanded-keys="organizationTreeExpandedKeys"
      v-model="organizationTreeCheckedKeys"
      :selected-keys="organizationTreeSelectedKeys"
      :auto-expand-parent="autoExpandParent"
      :tree-data="organizationTreeList"
      :replace-fields="organizationTreeProps"
      :checkable="checkable"
      :check-strictly="checkStrictly"
      @expand="onOrganizationTreeExpand"
      @check="onOrganizationTreeCheck"
      @select="onOrganizationTreeSelect"
    >
      <template slot="organizationName" slot-scope="{ organizationName }">
        <span v-if="organizationName.indexOf(searchValue) > -1">
          {{ organizationName.substr(0, organizationName.indexOf(searchValue)) }}
          <span style="color: #f50">{{ searchValue }}</span>
          {{ organizationName.substr(organizationName.indexOf(searchValue) + searchValue.length) }}
        </span>
        <span v-else>{{ organizationName }}</span>
      </template>
    </a-tree>
  </div>
</template>

<script>
import { fetchOrgList } from '@/api/system/organization'
export default {
  name: 'OrganizationTreeSelect',
  props: {
    // 唯一标识
    treeId: {
      type: String,
      default: 'organizationTree'
    },
    // 父级id
    parentId: {
      type: String,
      default: '0'
    },
    checkStrictly: {
      type: Boolean,
      default: false
    },
    // 是否显示选择框
    checkable: {
      type: Boolean,
      default: false
    },
    // onCheck事件
    onCheck: {
      type: Function,
      default: () => {
      }
    },
    // onSelect事件
    onSelect: {
      type: Function,
      default: () => {
      }
    },
    expandedKeys: {
      type: Array,
      default: () => {
        return []
      }
    },
    checkedKeys: {
      type: Array,
      default: () => {
        return []
      }
    },
    selectedKeys: {
      type: Array,
      default: () => {
        return []
      }
    }
  },
  computed: {

  },
  watch: {
    expandedKeys (keys) {
      this.organizationTreeExpandedKeys = keys
    },
    checkedKeys (keys) {
      this.organizationTreeCheckedKeys = keys
    },
    selectedKeys (keys) {
      this.organizationTreeSelectedKeys = keys
    }
  },
  data () {
    return {
      organizationTreeProps: {
        children: 'children', title: 'organizationName', key: 'id'
      },
      organizationList: [],
      organizationTreeList: [],
      organizationSearchList: [],
      searchValue: '',
      autoExpandParent: true,
      treeChecked: [],
      organizationTreeExpandedKeys: [],
      organizationTreeSelectedKeys: [],
      organizationTreeCheckedKeys: []
    }
  },
  created () {
     this.queryOrganizationList()
  },
  methods: {
    // 查询组织机构数据
    queryOrganizationList () {
      this.listLoading = true
      fetchOrgList({ parentId: this.parentId }).then(response => {
        this.organizationList = response.data
        if (this.organizationList) {
          const organizationListStr = JSON.stringify(this.organizationList)
          this.organizationTreeList = JSON.parse(organizationListStr.replace(/"isLeaf":1/g, '"isLeaf":true').replace(/"isLeaf":0/g, '"isLeaf":false'))
          this.generateOrganizationList(this.organizationTreeList)
          this.organizationTreeExpandedKeys = this.expandedKeys
          this.organizationTreeCheckedKeys = this.checkedKeys
          this.organizationTreeSelectedKeys = this.selectedKeys
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
      this.autoExpandParent = false
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
      Object.assign(this, {
        organizationTreeExpandedKeys,
        searchValue: value,
        autoExpandParent: true
      })
    },
    onOrganizationTreeSelect (selectedKeys, info) {
      this.organizationTreeSelectedKeys = selectedKeys
      if (info && info.selectedNodes[0] && info.selectedNodes[0].data) {
        this.onSelect(info.selectedNodes[0].data.props)
      }
    },
    onOrganizationTreeCheck (item, e) {
      this.onCheck(item, e)
    }
  }
}
</script>

<style lang="less" scoped>

</style>
