<template>
  <div>
    <a-select
      :value="searchValue"
      :placeholder="optPlaceholder"
      allow-clear
      show-search
      :filter-option="false"
      @popupScroll="handlePopupScroll"
      @search="filterOpts"
      @change="onChange"
      :not-found-content="undefined">
      <a-select-option v-for="item in dataList" :key="item[optKey]" :value="item[optValue]">
        {{ item[optLable] }}
      </a-select-option>
    </a-select>
  </div>
</template>

<script>
export default {
  name: 'SelectAsyn',
  props: {
    // 默认值搜索框
    defaultValue: {
      type: String,
      default: undefined
    },
    // select的key
    optKey: {
      type: String,
      default: undefined
    },
    // select的value
    optValue: {
      type: String,
      default: undefined
    },
    // select的lable
    optLable: {
      type: String,
      default: undefined
    },
    // select的placeholder
    optPlaceholder: {
      type: String,
      default: undefined
    },
    // 当前加载的页数
    pageNo: {
      type: Number,
      default: 1
    },
    // 每页加载的数据个数
    pageSize: {
      type: Number,
      default: 10
    },
    // 查询的条件信息
    queryKey: {
      type: String,
      default: undefined
    },
    // 执行数据查询的方法
    queryDataList: {
      type: Function,
      default: () => {
        return []
      }
    }
  },
  computed: {

  },
  watch: {
    defaultValue (val) {
       this.searchValue = val
    }
  },
  data () {
    return {
      // 服务端返回的数据总条数
      dataTotalCount: 0,
      // 定时器，控制请求频率
      timer: undefined,
      searchValue: undefined,
      // 服务器返回的响应信息中的搜索匹配项数据
      dataList: []
    }
  },
  created () {
     this.current = this.pageNo
     if (this.defaultValue && this.defaultValue !== '') {
       this.searchValue = this.defaultValue
     }
     this.getDataList()
  },
  methods: {
      getDataList () {
        const that = this
        that.listLoading = true
        const queryPamars = {
          current: that.current,
          size: that.pageSize
        }
        queryPamars[that.queryKey] = that.searchValue
        this.queryDataList(queryPamars).then(response => {
            that.dataTotalCount = response.data.total
            if (that.dataList.length <= that.dataTotalCount) {
              that.dataList = that.dataList.concat(response.data.records)
            }
            that.listLoading = false
        })
      },
      // 文本框值变化时回调
      filterOpts (val) {
        this.searchValue = val
        clearTimeout(this.timer)
        this.dataList = []
        this.current = 1
        this.pageSize = 10
        this.timer = setTimeout(() => {
          this.getDataList()
        }, 500)
    },
    // 列表滚动时加载数据
    handlePopupScroll (e) {
      const target = e.target
        // 判断滚动条滚动到底部时才加载
        if ((target.scrollTop + target.offsetHeight === target.scrollHeight) && (this.current * this.pageSize < this.dataTotalCount)) {
        this.current += 1
        this.getDataList()
      }
    },
    // 选中 option 调用
    onChange (val) {
        this.searchValue = val
        this.$emit('holderBack', val)
        // 点击清除之后的回调
        if (val === undefined) {
          this.filterOpts(val)
        }
    }
  }
}
</script>

<style lang="less" scoped>

</style>
