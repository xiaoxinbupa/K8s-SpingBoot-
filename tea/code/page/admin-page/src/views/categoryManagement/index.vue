<template>
  <div class="category-management">
    <el-card shadow="never">
      <el-form ref="searchForm" :model="form" label-width="80px" label-position="right">
        <el-row :gutter="32">
          <el-col :md="8">
            <el-form-item label="类目名称">
              <el-input
                v-model="form.name"
                clearable
                placeholder="请输入类型名称"
              />
            </el-form-item>
          </el-col>
          <el-col :md="16">
            <el-form-item align="right">
              <el-button type="primary" @click="$showNoAuthMsg()">查 询</el-button>
              <el-button plain @click="$showNoAuthMsg()">重 置</el-button>
            </el-form-item>
          </el-col>
        </el-row>
      </el-form>
      <div class="supplierBtn">
        <el-button type="primary" @click="createCategory()">新建</el-button>
      </div>
      <el-table
        ref="treeTable"
        :data="tableData"
        row-key="id"
        :expand-row-keys="expandID"
        stripe
        :tree-props="{children: 'child'}"
        :header-cell-style="{background:'#eef1f6',color:'#606266'}"
        class="table-data"
      >
        <el-table-column prop="name" label="类目名称" align="center" :show-overflow-tooltip="true" min-width="150" />
        <el-table-column prop="code" label="类目编码" align="center" :show-overflow-tooltip="true" min-width="150" />
        <el-table-column prop="createUserName" label="更新人" align="center" :show-overflow-tooltip="true" min-width="150" />
        <el-table-column prop="gmtCreate" label="更新时间" align="center" :show-overflow-tooltip="true" min-width="150">
          <template slot-scope="scope">
            <div>
              {{ scope.row.gmtCreate ? formatTime(scope.row.gmtCreate, 'YYYY-MM-DD h:m:s') : '' }}
            </div>
          </template>
        </el-table-column>
        <!-- <el-table-column
          label="状态"
          align="center"
        >
          <template slot-scope="scope">
            <el-switch
              v-model="scope.row.status"
              :active-value="1"
              :inactive-value="0"
              active-color="#13ce66"
              @change="changeStatus(scope.row)"
            />
          </template>
        </el-table-column> -->
        <el-table-column
          fixed="right"
          label="操作"
          width="150"
          align="center"
        >
          <template slot-scope="scope">
            <el-button type="text" size="small" @click="edit(scope.row)">编辑</el-button>
            <el-button type="text" size="small" @click="addSublevel(scope.row)">添加子级</el-button>
          </template>
        </el-table-column>
      </el-table>
      <!-- <div class="pagination">
        <el-pagination
          layout="total, sizes, prev, pager, next, jumper"
          :total="page.total"
          :page-size="page.pageSize"
          :page-sizes="[10, 20, 30, 40, 50, 100]"
          :current-page.sync="page.pageNo"
          @size-change="handleSizeChange"
          @current-change="handleCurrentChange"
        />
      </div> -->
    </el-card>
    <AddOrEditDialog
      v-if="addOrEditDialogVisible"
      :add-or-edit-dialog-visible="addOrEditDialogVisible"
      :dialog-title="dialogTitle"
      :tree-data="treeData"
      :category-id="categoryId"
      @closeDialog="closeDialog"
      @loadList="loadList"
    />
  </div>
</template>

<script>
import AddOrEditDialog from './addOrEditDialog'
import { getCategoryTree } from '@/api/categoryManagement'
import { formatTimes } from '@/utils'
export default {
  components: {
    AddOrEditDialog
  },
  data() {
    return {
      form: {
        name: ''
      },
      formatTime: formatTimes,
      expandID: [],
      tableData: [],
      treeData: [],
      loading: false,
      // page: {
      //   pageNo: 1,
      //   pageSize: 10,
      //   total: 0
      // },
      addOrEditDialogVisible: false,
      dialogTitle: '',
      categoryId: ''
    }
  },
  mounted() {
    this.getCategoryTree()
  },
  methods: {
    // 获取列表
    async getCategoryTree() {
      try {
        const res = await getCategoryTree()
        this.tableData = res
        const data = JSON.parse(JSON.stringify(this.tableData))
        data.unshift({
          name: '无上级类别',
          id: 0
        })
        this.treeData = data
      } catch (error) {
        this.treeData = [{
          name: '无上级类别',
          id: 0
        }]
        console.log(error)
      }
    },
    // changeStatus(row) {
    //   console.log(row.status)
    // },
    // 新建
    createCategory() {
      this.addOrEditDialogVisible = true
      this.dialogTitle = '新建类目'
      this.categoryId = ''
    },
    // 编辑
    edit(row) {
      this.addOrEditDialogVisible = true
      this.dialogTitle = '编辑类目'
      this.categoryId = row.id
    },
    // 添加下级
    addSublevel(row) {
      this.addOrEditDialogVisible = true
      this.dialogTitle = '添加子集'
      this.categoryId = row.id
    },
    // 关闭弹窗
    closeDialog() {
      this.addOrEditDialogVisible = false
    },
    loadList() {
      this.getCategoryTree()
    }
    // handleSizeChange(val) {
    //   this.page.pageSize = val
    //   this.getCategoryList()
    // },
    // handleCurrentChange(val) {
    //   this.page.pageNo = val
    //   this.getCategoryList()
    // }
  }
}
</script>
<style lang="scss" scoped>
  .table-data{
    margin: 30px 0px;
  }
  .pagination{
    text-align: right;
  }
  .el-cascader{
    width: 100%;
  }
</style>
