<template>
  <div class="goods-list">
    <el-card shadow="never">
      <el-form ref="searchForm" :model="form" label-width="80px" label-position="right">
        <el-row>
          <el-col :md="8">
            <el-form-item label="商品名称">
              <el-input
                v-model="form.title"
                clearable
                placeholder="请输入商品名称"
              />
            </el-form-item>
          </el-col>
          <el-col :md="8">
            <el-form-item label="商品编号">
              <el-input
                v-model="form.barCode"
                clearable
                placeholder="请输入商品编号"
              />
            </el-form-item>
          </el-col>
          <el-col :md="8">
            <el-form-item label="商品类别">
              <el-cascader
                v-model="form.categoryId"
                clearable
                placeholder="请选择商品类别"
                :props="{children: 'child', value: 'id',label: 'categoryName',checkStrictly: true, emitPath:false }"
                :options="categoryList"
              />
            </el-form-item>
          </el-col>
          <el-col :md="8">
            <el-form-item label="上架状态">
              <el-select v-model="form.status" clearable placeholder="请选择上架状态">
                <el-option v-for="item in statusData" :key="item.value" :label="item.name" :value="item.value" />
              </el-select>
            </el-form-item>
          </el-col>
          <!-- <el-col :md="8">
            <el-form-item label="售价区间" class="price-container">
              <el-input
                v-model="form.input1"
                placeholder="最低"
              />
              <div class="tip"> — </div>
              <el-input
                v-model="form.input2"
                placeholder="最高"
              />
            </el-form-item>
          </el-col> -->
          <el-col :md="16">
            <el-form-item align="right">
              <el-button type="primary" @click="getGoodsList()">查 询</el-button>
              <el-button plain @click="resetForm()">重 置</el-button>
            </el-form-item>
          </el-col>
        </el-row>
      </el-form>
      <div class="supplierBtn">
        <el-button type="primary" @click="createProduct()">新建商品</el-button>
        <el-button type="success" @click="$showNoAuthMsg()">数据下载</el-button>
        <el-button type="warning" @click="$showNoAuthMsg()">数据导入</el-button>
        <el-button type="danger" @click="$showNoAuthMsg()">操作记录</el-button>
      </div>
      <el-table
        v-loading="loading"
        :data="tableData"
        stripe
        :header-cell-style="{background:'#f2f3f5',color:'#606266'}"
        class="table-data"
      >
        <el-table-column type="selection" align="center" width="55" />
        <el-table-column prop="id" label="商品ID" align="center" :show-overflow-tooltip="true" min-width="150" />
        <el-table-column prop="attachList" label="封面图片" align="center" :show-overflow-tooltip="true" min-width="150">
          <template slot-scope="scope">
            <el-image
              :src="scope.row.attachList && scope.row.attachList.length > 0 ? scope.row.attachList[0].url : ''"
              :preview-src-list="scope.row.attachList && scope.row.attachList.length > 0 ? [scope.row.attachList[0].url] : ''"
              fit="fill'"
            />
          </template>
        </el-table-column>
        <el-table-column prop="title" label="商品名称" align="center" :show-overflow-tooltip="true" min-width="150">
          <template slot-scope="scope">
            <div class="goods-name" @click="linkDetail(scope.row)"> {{ scope.row.title }}</div>
          </template>
        </el-table-column>
        <el-table-column prop="categoryName" label="商品类别" align="center" :show-overflow-tooltip="true" min-width="150" />
        <el-table-column prop="barCode" label="编码" align="center" :show-overflow-tooltip="true" min-width="150" />
        <el-table-column prop="salePrice" label="价格" align="center" :show-overflow-tooltip="true" min-width="150">
          <template slot-scope="scope">
            {{ (scope.row.salePrice / 100).toFixed(2) }}
          </template>
        </el-table-column>
        <el-table-column prop="status" label="状态管理" align="center" :show-overflow-tooltip="true" min-width="150">
          <template slot-scope="scope">
            <div class="status">
              <span class="title">上架</span>
              <el-switch
                v-model="scope.row.status"
                active-color="#13ce66"
                :active-value="1"
                :inactive-value="0"
                @change="changeStatus(scope.row)"
              />
            </div>
            <div class="status">
              <span class="title">推荐</span>
              <el-switch
                v-model="scope.row.status1"
                active-color="#13ce66"
                active-value="1"
                inactive-value="0"
              />
            </div>
            <div class="status">
              <span class="title">新品</span>
              <el-switch
                v-model="scope.row.status2"
                active-color="#13ce66"
                active-value="1"
                inactive-value="0"
              />
            </div>
          </template>
        </el-table-column>
        <el-table-column prop="sort" label="排序" align="center" :show-overflow-tooltip="true" min-width="150" />
        <el-table-column
          fixed="right"
          label="操作"
          width="150"
          align="center"
        >
          <template slot-scope="scope">
            <el-button type="text" size="small" @click="edit(scope.row)">编 辑</el-button>
            <el-button type="text" size="small" @click="goodsDelete(scope.row)">删 除</el-button>
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
import { getGoodsList, goodsDelete, goodsEnable, goodsDisable } from '@/api/goods'
import { statusData } from './goodsConfig'
import { mapState, mapActions } from 'vuex'
export default {
  data() {
    return {
      statusData,
      form: {},
      tableData: [],
      loading: false,
      page: {
        pageNo: 1,
        pageSize: 10,
        total: 0
      }
    }
  },
  computed: {
    ...mapState('category', ['categoryList'])
  },
  mounted() {
    this.getCategoryList()
    this.getGoodsList()
    console.log(process.env.VUE_APP_PACKAGE)
  },
  methods: {
    // 获取商品类别
    ...mapActions('category', ['getCategoryList']),
    // 获取商品列表
    async getGoodsList() {
      try {
        this.loading = true
        const parmas = {
          ...this.form,
          pageNo: this.page.pageNo,
          pageSize: this.page.pageSize
        }
        const res = await getGoodsList(parmas)
        this.tableData = res.objects
        this.page.total = res.total
        this.loading = false
      } catch (error) {
        this.loading = false
        console.log(error)
      }
    },
    // 商品删除
    async goodsDelete(row) {
      try {
        await this.$confirm('确定删除该商品, 是否继续?', '提示', { confirmButtonText: '确定', cancelButtonText: '取消', type: 'warning' })
        await goodsDelete({ id: row.id })
        this.getGoodsList()
      } catch (error) {
        console.log(error)
      }
    },
    // 重置搜索
    resetForm() {
      this.$nextTick(() => {
        this.form = {}
      })
    },
    // 商品上下架状态切换
    async changeStatus(row) {
      if (process.env.VUE_APP_PACKAGE === 'externalization') {
        return
      }
      try {
        if (row.status == 0) {
          await goodsDisable({ id: row.id })
        } else {
          await goodsEnable({ id: row.id })
        }
        this.getGoodsList()
      } catch (error) {
        console.log(error)
      }
    },
    // 创建商品跳转
    createProduct() {
      this.$router.push({ name: 'publishProduct' })
    },
    edit(row) {
      this.$router.push({ name: 'publishProduct', query: {
        id: row.id
      }})
    },
    linkDetail(row) {
      this.$router.push({ name: 'publishProduct', query: {
        id: row.id,
        view: true
      }})
    },
    // 切换条
    handleSizeChange(val) {
      this.page.pageSize = val
      this.getGoodsList()
    },
    // 切换页
    handleCurrentChange(val) {
      this.page.pageNo = val
      this.getGoodsList()
    }
  }
}
</script>
<style lang="scss" scoped>
.el-select{
  width: 100%;
}
.el-cascader{
  width: 100%;
}
.price-container{
  ::v-deep{
    .el-form-item__content{
      display: flex;
    }
  }
  .tip{
    margin: 0px 10px;
    color: #DCDFE6;
  }
}
  .table-data{
    margin: 30px 0px;
    .goods-name{
      color: #409EFF;
      cursor: pointer;
    }
    .status{
      margin: 5px;
      .title{
        margin-right: 10px;
      }
    }
  }
  .pagination{
    text-align: right;
  }
</style>
