<template>
  <div class="category-management">
    <el-card shadow="never">
      <el-form ref="searchForm" :model="form" label-width="80px" label-position="right">
        <el-row :gutter="32">
          <el-col :md="6">
            <el-form-item label="名称">
              <el-input
                v-model="form.keywords"
                clearable
                placeholder="请输入文章名称名称"
              />
            </el-form-item>
          </el-col>
          <el-col :md="6">
            <el-form-item label="发布时间">
              <el-input
                v-model="form.keywords"
                clearable
                placeholder="请输入类目名称"
              />
            </el-form-item>
          </el-col>
          <el-col :md="12">
            <el-form-item align="right">
              <el-button type="primary" @click="search()">查 询</el-button>
              <el-button plain @click="resetSearch()">重 置</el-button>
            </el-form-item>
          </el-col>
        </el-row>
      </el-form>
      <div class="supplierBtn">
        <el-button type="warning" @click="createCategory()">批量屏蔽</el-button>
      </div>
      <el-table
        v-loading="loading"
        :data="tableData"
        stripe
        :header-cell-style="{background:'#f2f3f5',color:'#606266'}"
        class="table-data"
      >
        <el-table-column type="selection" align="center" width="55" />
        <el-table-column prop="name" label="文章" align="center" :show-overflow-tooltip="true" min-width="150" />
        <el-table-column prop="description" label="评论内容" align="center" :show-overflow-tooltip="true" min-width="150" />
        <el-table-column prop="description" label="评论时间" align="center" :show-overflow-tooltip="true" min-width="150" />
        <el-table-column prop="description" label="发布人" align="center" :show-overflow-tooltip="true" min-width="150" />
        <el-table-column prop="description" label="内评论" align="center" :show-overflow-tooltip="true" min-width="150" />
        <el-table-column
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
        </el-table-column>
        <el-table-column
          fixed="right"
          label="操作"
          width="150"
          align="center"
        >
          <template slot-scope="scope">
            <el-button type="text" size="small" @click="edit(scope.row)">屏蔽</el-button>
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
  </div>
</template>

<script>
export default {
  data() {
    return {
      form: {
        keywords: ''
      },
      tableData: [
        {
          name: 1111,
          description: 11111,
          enabled: 1
        }
      ],
      loading: false,
      page: {
        pageNo: 1,
        pageSize: 10,
        total: 0
      }
    }
  },
  methods: {
    // 查询
    search() {

    },
    // 重置
    resetSearch() {
      this.form.keywords = ''
    },
    // 新建
    createCategory() {
      this.$router.push(
        {
          name: 'buildManagement',
          query: {
            id: 1
          }
        }
      )
    },
    // 编辑
    edit() {
      this.addOrEditDialogVisible = true
      this.dialogTitle = '编辑类目'
    },
    // 添加下级
    addSublevel() {

    },
    // 删除
    deleteItem() {

    },
    handleSizeChange(val) {
      console.log(`每页 ${val} 条`)
    },
    handleCurrentChange(val) {
      console.log(`当前页: ${val}`)
    }
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
</style>
