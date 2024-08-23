<template>
  <div class="menu-list">
    <el-card shadow="never">
      <!-- <el-form ref="searchForm" :model="form" label-width="80px" label-position="right">
        <el-row>
          <el-col :md="6">
            <el-form-item label="标题">
              <el-input
                v-model="form.keywords"
                clearable
                placeholder="请输入类型名称"
              />
            </el-form-item>
          </el-col>
          <el-col :md="6">
            <el-form-item label="绑定类型">
              <el-input
                v-model="form.keywords"
                clearable
                placeholder="请输入类型名称"
              />
            </el-form-item>
          </el-col>
          <el-col :md="6">
            <el-form-item label="状态">
              <el-input
                v-model="form.keywords"
                clearable
                placeholder="请输入类型名称"
              />
            </el-form-item>
          </el-col>
          <el-col :md="6">
            <el-form-item align="right">
              <el-button type="primary" @click="search()">查 询</el-button>
              <el-button plain @click="resetSearch()">重 置</el-button>
            </el-form-item>
          </el-col>
        </el-row>
      </el-form> -->
      <div class="supplierBtn">
        <el-button type="primary" @click="createCategory()">新建菜单</el-button>
      </div>
      <el-table
        v-loading="loading"
        :data="tableData"
        stripe
        :header-cell-style="{background:'#f2f3f5',color:'#606266'}"
        class="table-data"
      >
        <el-table-column prop="title" label="标题" align="center" :show-overflow-tooltip="true" min-width="150" />
        <el-table-column prop="description" label="绑定类型" align="center" :show-overflow-tooltip="true" min-width="150">
          <template slot-scope="scope">
            {{ scope.row.type === 1 ? '内部链接' : '外部链接' }}
          </template>
        </el-table-column>
        <el-table-column prop="status" label="状态" align="center" :show-overflow-tooltip="true" min-width="150">
          <template slot-scope="scope">
            <span :class="[scope.row.status === 1 ? 'status-active' : 'status-enable']">{{ scope.row.status === 1 ? '启用' : '禁用' }}</span>
          </template>
        </el-table-column>
        <el-table-column prop="url" label="绑定详情" align="center" :show-overflow-tooltip="true" min-width="150" />
        <el-table-column prop="modifiedUserName" label="更新人" align="center" :show-overflow-tooltip="true" min-width="150" />
        <el-table-column prop="gmtModified" label="更新时间" align="center" :show-overflow-tooltip="true" min-width="150">
          <template slot-scope="scope">
            {{ scope.row.gmtModified ? formatTime(scope.row.gmtModified, 'YYYY-MM-DD h:m:s') : '' }}
          </template>
        </el-table-column>
        <!-- <el-table-column
          label="状态"
          align="center"
        >
          <template slot-scope="scope">
            <el-switch
              v-model="scope.row.enabled"
              :active-value="true"
              :inactive-value="false"
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
    <AddOrEditDialog
      v-if="addOrEditDialogVisible"
      :add-or-edit-dialog-visible="addOrEditDialogVisible"
      :dialog-title="dialogTitle"
      :menu-id="menuId"
      @closeDialog="closeDialog"
      @loadList="loadList"
    />
  </div>
</template>

<script>
import { getMenuList } from '@/api/menu'
import AddOrEditDialog from './addOrEditDialog'
import { formatTimes } from '@/utils'
export default {
  components: {
    AddOrEditDialog
  },
  data() {
    return {
      form: {
        keywords: ''
      },
      tableData: [],
      loading: false,
      page: {
        pageNo: 1,
        pageSize: 10,
        total: 0
      },
      addOrEditDialogVisible: false,
      dialogTitle: '',
      formatTime: formatTimes,
      menuId: ''
    }
  },
  mounted() {
    this.getMenuList()
  },
  methods: {
    // 获取菜单列表
    async getMenuList() {
      try {
        this.loading = true
        const res = await getMenuList({
          pageNo: this.page.pageNo,
          pageSize: this.page.pageSize
        })
        this.loading = false
        this.tableData = res.objects
        this.page.total = res.total
      } catch (error) {
        this.loading = false
        console.log(error)
      }
    },
    // 新建
    createCategory() {
      this.addOrEditDialogVisible = true
      this.dialogTitle = '新建菜单'
      this.menuId = ''
    },
    // 编辑
    edit(row) {
      this.addOrEditDialogVisible = true
      this.dialogTitle = '编辑菜单'
      this.menuId = row.id
    },
    // 关闭弹窗
    closeDialog() {
      this.addOrEditDialogVisible = false
    },
    // 切换条
    handleSizeChange(val) {
      this.page.pageSize = val
      this.getMenuList()
      console.log(`每页 ${val} 条`)
    },
    // 切换页
    handleCurrentChange(val) {
      this.page.pageNo = val
      this.getMenuList()
      console.log(`当前页: ${val}`)
    },
    loadList() {
      this.getMenuList()
    }
  }
}
</script>
<style lang="scss" scoped>
  .table-data{
    margin: 30px 0px;
    .status-active{
      color: #67c23a;
    }
    .status-enable{
      color: #f56c6c;
    }
  }
  .pagination{
    text-align: right;
  }
</style>
