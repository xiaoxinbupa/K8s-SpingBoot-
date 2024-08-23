<template>
  <div class="category-management">
    <el-card shadow="never">
      <el-form ref="searchForm" :model="form" label-width="80px" label-position="right">
        <el-row :gutter="32">
          <el-col :md="8">
            <el-form-item label="名称">
              <el-input
                v-model="form.title"
                clearable
                placeholder="请输入文章名称名称"
              />
            </el-form-item>
          </el-col>
          <!-- <el-col :md="6">
            <el-form-item label="发布时间">
              <el-input
                v-model="form.keywords"
                clearable
                placeholder="请输入类目名称"
              />
            </el-form-item>
          </el-col> -->
          <el-col :md="8">
            <el-form-item label="分类">
              <el-cascader
                v-model="form.categoryId"
                clearable
                :props="{children: 'child', value: 'id',label: 'name',checkStrictly: true, emitPath:false }"
                :options="treeData"
              />
            </el-form-item>
          </el-col>
          <el-col :md="8">
            <el-form-item align="right">
              <el-button type="primary" @click="getContentList()">查 询</el-button>
              <el-button plain @click="resetSearch()">重 置</el-button>
            </el-form-item>
          </el-col>
        </el-row>
      </el-form>
      <div class="supplierBtn">
        <el-button type="primary" @click="createCategory()">新建文章</el-button>
        <el-button type="success" @click="contentEnable(1)">批量上架</el-button>
        <el-button type="warning" @click="contentEnable(0)">批量下架</el-button>
      </div>
      <el-table
        v-loading="loading"
        :data="tableData"
        stripe
        :header-cell-style="{background:'#f2f3f5',color:'#606266'}"
        class="table-data"
        @selection-change="handleSelectionChange"
      >
        <el-table-column type="selection" align="center" width="55" />
        <el-table-column prop="title" label="标题" align="center" :show-overflow-tooltip="true" min-width="150" />
        <el-table-column prop="createUserName" label="作者" align="center" :show-overflow-tooltip="true" min-width="150" />
        <el-table-column prop="categoryName" label="分类" align="center" :show-overflow-tooltip="true" min-width="150" />
        <el-table-column prop="sort" label="排序" align="center" :show-overflow-tooltip="true" min-width="150" />
        <el-table-column prop="keywords" label="标签" align="center" :show-overflow-tooltip="true" min-width="150">
          <template slot-scope="scope">
            <div>
              {{ filterTags(JSON.parse(scope.row.keywords)).join(',') }}
            </div>
          </template>
        </el-table-column>
        <el-table-column prop="gmtCreate" label="发布时间" align="center" :show-overflow-tooltip="true" min-width="150">
          <template slot-scope="scope">
            <div>
              {{ scope.row.gmtCreate ? formatTime(scope.row.gmtCreate, 'YYYY-MM-DD h:m:s') : '' }}
            </div>
          </template>
        </el-table-column>
        <el-table-column prop="description" label="点赞/评论" align="center" :show-overflow-tooltip="true" min-width="150">
          <template slot-scope="scope">
            <div>{{ `${scope.row.clickTimes} / ${scope.row.commentTimes }` }}</div>
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
            <el-button type="text" size="small" @click="viewArticle(scope.row)">查看</el-button>
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
  </div>
</template>

<script>
import { getCategoryTree } from '@/api/categoryManagement'
import { getContentList, contentEnable } from '@/api/content'
import { formatTimes } from '@/utils'
export default {
  data() {
    return {
      form: {
        title: '',
        categoryId: ''
      },
      tableData: [],
      loading: false,
      treeData: [],
      formatTime: formatTimes,
      multipleSelection: [],
      selectIds: [],
      page: {
        pageNo: 1,
        pageSize: 10,
        total: 0
      }
    }
  },
  mounted() {
    this.getCategoryTree()
    this.getContentList()
  },
  methods: {
    // 获取类别树列表
    async getCategoryTree() {
      try {
        const res = await getCategoryTree()
        this.treeData = res
      } catch (error) {
        console.log(error)
      }
    },
    // 查询
    async getContentList() {
      try {
        const parmas = {
          categoryId: this.form.categoryId,
          title: this.form.title,
          pageNo: this.page.pageNo,
          pageSize: this.page.pageSize
        }
        const res = await getContentList(parmas)
        this.tableData = res.objects
        this.page.total = res.total
      } catch (error) {
        console.log(error)
      }
    },
    // 重置
    resetSearch() {
      this.form.title = ''
      this.form.categoryId = ''
    },
    // 新建
    createCategory() {
      this.$router.push({ name: 'buildManagement' })
    },
    // 编辑
    edit(row) {
      this.$router.push({ name: 'buildManagement', query: { id: row.id }})
    },
    // 查看文章
    viewArticle(row) {
      this.$router.push({ name: 'contentDetail', query: { id: row.id }})
    },
    handleSelectionChange(val) {
      this.multipleSelection = val
      const selectIds = []
      if (this.multipleSelection && this.multipleSelection.length > 0) {
        this.multipleSelection.forEach(item => {
          selectIds.push(item.id)
        })
        this.selectIds = selectIds
      }
    },
    // 内容批量上下架
    async contentEnable(status) {
      if (process.env.VUE_APP_PACKAGE === 'externalization') {
        try {
          await this.$confirm('亲，这是演示版，你没有权限!,想获得所有权限，请联系咨询老师，获取代码', '系统提示', { confirmButtonText: '确定', cancelButtonText: '取消', type: 'warning' })
        } catch (error) {
          console.log(error)
        }
        return
      }
      try {
        if (this.selectIds.length <= 0) {
          return this.$message.error('请先选中数据！')
        }
        await contentEnable({
          ids: this.selectIds,
          status
        })
        this.$message.success('success')
      } catch (error) {
        console.log(error)
      }
    },
    handleSizeChange(val) {
      this.page.pageSize = val
      this.getContentList()
    },
    handleCurrentChange(val) {
      this.page.pageNo = val
      this.getContentList()
    },
    filterTags(data) {
      if (data && data.length > 0) {
        const tags = []
        data.map(item => {
          tags.push(item.name)
        })
        return tags
      } else {
        return ''
      }
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
  ::v-deep{
  .el-cascader{
    width: 100% !important;
  }
}
</style>
