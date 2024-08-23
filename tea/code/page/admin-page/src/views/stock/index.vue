<template>
  <div class="stock">
    <el-card shadow="never">
      <el-form ref="searchForm" :model="form" label-width="80px" label-position="right">
        <el-row>
          <el-col :md="8">
            <el-form-item label="商品编码">
              <el-input
                v-model="form.code"
                clearable
                placeholder="请输入商品编码"
              />
            </el-form-item>
          </el-col>
          <el-col :md="8">
            <el-form-item label="商品类别">
              <el-cascader
                v-model="form.categoryId"
                clearable
                :props="{children: 'child', value: 'id',label: 'categoryName',checkStrictly: true, emitPath:false }"
                :options="categoryList"
              />
            </el-form-item>
          </el-col>
          <el-col :md="8">
            <el-form-item align="right">
              <el-button type="primary" @click="getStockList()">查 询</el-button>
              <el-button plain @click="resetSearch()">重 置</el-button>
            </el-form-item>
          </el-col>
        </el-row>
      </el-form>
      <!-- <div class="supplierBtn">
        <el-button type="success" @click="batchInitSkuStock()">设置库存</el-button>
      </div> -->
      <el-table
        v-loading="loading"
        :data="tableData"
        stripe
        :header-cell-style="{background:'#f2f3f5',color:'#606266'}"
        class="table-data"
        @selection-change="handleSelectionChange"
      >
        <el-table-column type="selection" width="55" align="center" />
        <el-table-column type="index" width="50" align="center" label="编号" />
        <el-table-column prop="title" label="标题" align="center" :show-overflow-tooltip="true" min-width="150" />
        <el-table-column prop="categoryName" label="商品类别" align="center" :show-overflow-tooltip="true" min-width="150" />
        <el-table-column prop="num" label="可用库存" align="center" :show-overflow-tooltip="true" min-width="150" />
        <!-- <el-table-column prop="salePrice" label="锁定库存" align="center" :show-overflow-tooltip="true" min-width="150" /> -->
        <el-table-column
          fixed="right"
          label="操作"
          width="150"
          align="center"
        >
          <template slot-scope="scope">
            <el-button type="text" size="small" :disabled="scope.row.num != 0" @click="setUpStock(scope.row,'setUpStock')">设置库存</el-button>
            <el-button type="text" size="small" :disabled="scope.row.num == 0" @click="manualReplenishment(scope.row,'manualReplenishment')">人工补货</el-button>
          </template>
        </el-table-column>
      </el-table>
      <div class="pagination">
        <el-pagination
          layout="total, sizes, prev, pager, next, jumper"
          :total="page.total"
          :page-size="page.pageSize"
          :page-sizes="[10, 20, 30, 40, 50, 100]"
          :current-page.sync="page.pageNo"
          @size-change="handleSizeChange"
          @current-change="handleCurrentChange"
        />
      </div>
    </el-card>
    <SetUpStockDialog
      v-if="setUpStockDialog"
      :set-up-stock-dialog="setUpStockDialog"
      :dialog-title="dialogTitle"
      :type="type"
      :row-data="rowData"
      :ids="ids"
      @closeDialog="closeDialog"
      @loadList="loadList"
    />
  </div>
</template>

<script>
import SetUpStockDialog from './setUpStockDialog'
import { mapState, mapActions } from 'vuex'
import { getStockList, batchInitSkuStock } from '@/api/stock'
export default {
  components: {
    SetUpStockDialog
  },
  data() {
    return {
      form: {},
      tableData: [],
      loading: false,
      page: {
        pageNo: 1,
        pageSize: 10,
        total: 0
      },
      setUpStockDialog: false,
      dialogTitle: '',
      rowData: {},
      type: '',
      multipleSelection: [],
      ids: []
    }
  },
  computed: {
    ...mapState('category', ['categoryList'])
  },
  mounted() {
    this.getCategoryList()
    this.getStockList()
  },
  methods: {
    // 获取商品类别
    ...mapActions('category', ['getCategoryList']),
    // 获取库存列表
    async getStockList() {
      try {
        this.loading = true
        const parmas = {
          ...this.form,
          pageNo: this.page.pageNo,
          pageSize: this.page.pageSize
        }
        const res = await getStockList(parmas)
        this.loading = false
        this.tableData = res.objects
        this.page.total = res.total
      } catch (error) {
        this.loading = false
        console.log(error)
      }
    },
    // 选中的数据
    handleSelectionChange(val) {
      this.multipleSelection = val
    },
    // 批量设置库存
    batchInitSkuStock() {
      if (this.multipleSelection && this.multipleSelection.length <= 0) {
        return this.$message.warning('请先选择要设置的库存！')
      }
      const ids = []
      this.multipleSelection.forEach(item => {
        ids.push(item.id)
      })
      this.ids = ids
      this.setUpStockDialog = true
      this.dialogTitle = '批量设置库存'
      this.type = 'batchInitSkuStock'
    },
    // 重置
    resetSearch() {
      this.form = {}
    },
    // 设置库存
    setUpStock(row, type) {
      this.setUpStockDialog = true
      this.dialogTitle = '设置库存'
      this.rowData = row
      this.type = type
    },
    // 人工补货
    manualReplenishment(row, type) {
      this.setUpStockDialog = true
      this.dialogTitle = '人工补货'
      this.rowData = row
      this.type = type
    },
    // 切换条
    handleSizeChange(val) {
      this.page.pageSize = val
    },
    // 切换页
    handleCurrentChange(val) {
      this.page.pageNo = val
    },
    closeDialog() {
      this.setUpStockDialog = false
    },
    loadList() {
      this.page.pageNo = 1
      this.getStockList()
    }
  }
}
</script>
<style lang="scss" scoped>
.table-data{
  width: 100%;
  margin: 30px 0px;
}
.el-cascader{
  width: 100%;
}
.pagination{
  text-align: right;
}
</style>
