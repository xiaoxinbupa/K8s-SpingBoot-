<template>
  <!-- 表格 分页 组件 -->
  <div class="tmp-table-pagination">
    <el-table
      ref="table_ref"
      v-loading="loading.isLoad"
      v-bind="$attrs"
      element-loading-spinner="el-icon-loading"
      :element-loading-text="loading.loadText"
      :border="border"
      :data="columData"
      :row-key="rowKey"
      :max-height="maxHeight"
      @row-dblclick="rowDblclick"
      @row-click="rowClick"
    >
      <slot />
    </el-table>
  </div>
</template>

<script>
export default {
  name: 'TmpTablePagination',
  inheritAttrs: false,
  props: {
    tableData: {
      type: Object,
      default() {
        return {}
      }
    },
    pagination: {
      type: Object,
      default() {
        return {}
      }
    },
    maxHeight: {
      type: [String, Number],
      default: undefined
    },
    rowKey: {
      type: [String, Function],
      default: undefined
    },
    border: {
      type: Boolean,
      default: false
    },
    hideOnSinglePage: {
      type: Boolean,
      default: false
    },
    // 初始化选中的列.ID数组
    tableSelData: {
      type: Array,
      default() {
        return []
      }
    },
    pageSizes: {
      type: Array,
      default() {
        return [10, 20, 50]
      }
    },
    showSize: {
      type: Boolean,
      default: false
    }

  },
  data() {
    return {
      loading: {
        isLoad: false,
        loadText: '加载中...'
      },
      columData: [],
      curTotal: 0,
      curSize: this.pagination.pageSize
    }
  },
  watch: {
    tableData: {
      immediate: true,
      deep: true,
      handler(val) {
        this.curTotal = Number(val.totalCount) || 0
        this.columData = this.tableData.list || []
        if (this.tableSelData.length && this.columData.length) {
          this.$nextTick(() => {
            this.initSelectedRowsByRowIDs(this.tableSelData)
          })
        }
      }
    }
  },
  methods: {
    // 表格实例
    instance() {
      return this.$refs.table_ref
    },
    // loading 弹窗
    loadingState(type) {
      this.loading.isLoad = type || false
    },
    rowDblclick(row) {
      this.$emit('rowDblclick', row)
    },
    // 改变页码
    onChangePage(curPage) {
      // console.log("change-", curPage);
      this.pagination.pageNo = curPage
      this.$emit('callback')
    },
    // 改变分页数
    onChangeSize(curSize) {
      // console.log("sizes-", curSize);
      this.pagination.pageSize = curSize
      this.curSize = curSize
      this.pagination.pageNo = 1
      this.$emit('callback')
    },
    /**
     * @param {Object} RowIDs  id数组
     *
     */
    initSelectedRowsByRowIDs(RowIDs) {
      const r_ = this.columData.filter(item => {
        return RowIDs.indexOf(item.id) !== -1
      })
      r_.length && this.onToggleRowSelection(r_, true)
    },
    // 用于多选表格，切换某一行的选中状态，如果使用了第二个参数，则是设置这一行选中与否（selected 为 true 则选中）
    onToggleRowSelection(rows, selected) {
      if (rows) {
        rows.forEach(row => {
          this.$refs.table_ref.toggleRowSelection(row, selected)
        })
      } else {
        this.$refs.table_ref.clearSelection()
      }
    },
    rowClick(data) {
      this.$emit('row-click', data)
    }
  }
}
</script>

<style lang="scss" scoped>

.section-pagination {
  padding: 15px 10px;
  display: flex;
  justify-content: flex-end;
}
</style>
