<!-- 通用表格 /*
* 使用指南
<common-table
    ref="tableSelection"
    :data="tableList"
    :tabName="tabName"
 >
</common-table>
---------------------------------------------
-->
<template>
  <div>
    <cmp-table-pagination
      ref="cmp_table"
      :table-data="tableData"
      :pagination="pagination"
      :border="false"
      :hide-on-single-page="false"
      :hide-pagination="true"
      :min-height="false"
      class="common-table"
      @row-click="rowClick"
    >
      <el-table-column
        v-for="item in tableColumns"
        :key="item.label"
        v-bind="item"
      >
        <template v-if="item.isPicture" #default="scope">
          <div class="url-wrap">
            <img
              :src="scope.row.coverPicture ? scope.row.coverPicture : noImage "
              alt=""
              class="goods-img"
            >
            {{ item.title }}
          </div>
        </template>
      </el-table-column>
    </cmp-table-pagination>
  </div>
</template>

<script>
import CmpTablePagination from '@/components/tablePagination/tablePagination.vue'
import noImage from '@/assets/images/no-img.png'
export default {
  name: 'CommonTable',
  components: {
    CmpTablePagination
  },
  props: {
    tabName: {
      type: String,
      default: 'move'
    },
    tableData: {
      type: Object,
      default: () => {
        return { totalCount: 1, list: [] }
      }
    },
    tableColumns: {
      type: Array,
      default: () => {
        return []
      }
    }
  },
  data() {
    return {
      noImage,
      pagination: {
        // 数据表格配置项
        currentPage: 1,
        pageSize: 10000
      }
    }
  },
  methods: {
    rowClick(data) {
      this.$emit('row-click', data)
    }
  }
}
</script>

<style lang="scss">
.common-table{
  @import "~@/assets/styles/common.scss";
  .el-table--medium .el-table__cell{
    padding: 3px 0 !important;
  }
  .el-table__empty-text {
    margin: 20px auto !important;
  }
  .url-wrap{
    display: inline-block;
    width:36px;
    height: 100%;
    display: flex;
    align-items: center;
    img{
      width: 36px;
      height: 36px;
    }
  }
}

</style>
