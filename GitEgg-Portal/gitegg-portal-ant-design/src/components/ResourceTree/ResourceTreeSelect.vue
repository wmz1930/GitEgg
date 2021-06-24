<template>
  <div>
    <a-input-search style="margin-bottom: 8px" placeholder="输入关键字进行过滤" @change="resourceTreeSearch" />
    <a-tree
      :ref="treeId"
      :default-expanded-keys="resourceTreeExpandedKeys"
      :expanded-keys="resourceTreeExpandedKeys"
      v-model="resourceTreeCheckedKeys"
      :selected-keys="resourceTreeSelectedKeys"
      :auto-expand-parent="autoExpandParent"
      :tree-data="resourceTreeList"
      :replace-fields="resourceTreeProps"
      :checkable="checkable"
      :check-strictly="checkStrictly"
      @expand="onResourceTreeExpand"
      @check="onResourceTreeCheck"
      @select="onResourceTreeSelect"
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
import { fetchResourceList } from '@/api/system/resource'
export default {
  name: 'ResourceTreeSelect',
  props: {
    // 唯一标识
    treeId: {
      type: String,
      default: 'resourceTree'
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
      this.resourceTreeExpandedKeys = keys
    },
    checkedKeys (keys) {
      this.resourceTreeCheckedKeys = keys
    },
    selectedKeys (keys) {
      this.resourceTreeSelectedKeys = keys
    }
  },
  data () {
    return {
      resourceTreeProps: {
        children: 'children', title: 'resourceName', key: 'id'
      },
      resourceList: [],
      resourceTreeList: [],
      resourceSearchList: [],
      searchValue: '',
      autoExpandParent: true,
      treeChecked: [],
      resourceTreeExpandedKeys: [],
      resourceTreeSelectedKeys: [],
      resourceTreeCheckedKeys: []
    }
  },
  created () {
     this.queryResourceList()
  },
  methods: {
    // 查询组织机构数据
    queryResourceList () {
      this.listLoading = true
      fetchResourceList({ parentId: 0 }).then(response => {
        this.resourceList = response.data
        if (this.resourceList) {
          const resourceListStr = JSON.stringify(this.resourceList)
          this.resourceTreeList = JSON.parse(resourceListStr.replace(/"isLeaf":1/g, '"isLeaf":true').replace(/"isLeaf":0/g, '"isLeaf":false'))
          this.generateResourceList(this.resourceTreeList)
          this.resourceTreeExpandedKeys = this.expandedKeys
          this.resourceTreeCheckedKeys = this.checkedKeys
          this.resourceTreeSelectedKeys = this.selectedKeys
        }
        this.listLoading = false
      })
    },
    // 准备左侧搜索树的数据
    generateResourceList (data) {
      for (let i = 0; i < data.length; i++) {
        const node = data[i]
        node.scopedSlots = { title: 'resourceName' }
        const key = node.id
        this.resourceSearchList.push({ key, title: node.resourceName })
        if (node.children) {
          this.generateResourceList(node.children)
        }
      }
    },
    getResourceParentKey (key, tree) {
      let parentKey
      for (let i = 0; i < tree.length; i++) {
        const node = tree[i]
        if (node.children) {
          if (node.children.some(item => item.id === key)) {
            parentKey = node.id
          } else if (this.getResourceParentKey(key, node.children)) {
            parentKey = this.getResourceParentKey(key, node.children)
          }
        }
      }
      return parentKey
    },
    onResourceTreeExpand (resourceTreeExpandedKeys) {
      this.resourceTreeExpandedKeys = resourceTreeExpandedKeys
      this.autoExpandParent = false
    },
    resourceTreeSearch (e) {
      const value = e.target.value
      const resourceTreeExpandedKeys = this.resourceSearchList
        .map(item => {
          if (item.title.indexOf(value) > -1) {
            return this.getResourceParentKey(item.key, this.resourceTreeList)
          }
          return null
        })
        .filter((item, i, self) => item && self.indexOf(item) === i)
      console.log(resourceTreeExpandedKeys)
      Object.assign(this, {
        resourceTreeExpandedKeys,
        searchValue: value,
        autoExpandParent: true
      })
    },
    onResourceTreeSelect (selectedKeys, info) {
      this.resourceTreeSelectedKeys = selectedKeys
      if (info && info.selectedNodes[0] && info.selectedNodes[0].data) {
        this.onSelect(info.selectedNodes[0].data.props)
      }
    },
    onResourceTreeCheck (item, e) {
      this.onCheck(item, e)
    }
  }
}
</script>

<style lang="less" scoped>

</style>
