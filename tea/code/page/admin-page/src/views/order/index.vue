<template>
  <div class="stock">
    <el-card shadow="never">
      <el-form ref="searchForm" :model="form" label-width="80px" label-position="right">
        <el-row>
          <el-col :md="8">
            <el-form-item label="订单编号">
              <el-input
                v-model="form.orderNo"
                clearable
                placeholder="请输入订单编号"
              />
            </el-form-item>
          </el-col>
          <el-col :md="8">
            <el-form-item label="用户">
              <el-input
                v-model="form.userName"
                clearable
                placeholder="请输入用户名称"
              />
            </el-form-item>
          </el-col>
          <!-- <el-col :md="8">
            <el-form-item label="支付方式">
              <el-select v-model="form.payMethod" placeholder="请选择支付方式">
                <el-option v-for="item in payMethodOpts" :key="item.value" :label="item.label" :value="item.value" />
              </el-select>
            </el-form-item>
          </el-col> -->
          <!-- <el-col :md="8">
            <el-form-item label="支付渠道">
              <el-select v-model="form.payChannel" placeholder="请选择支付渠道">
                <el-option v-for="item in payChannelOpts" :key="item.value" :label="item.label" :value="item.value" />
              </el-select>
            </el-form-item>
          </el-col> -->
          <el-col :md="8">
            <el-form-item label="订单类型">
              <el-select v-model="form.orderType" placeholder="请选择订单类型">
                <el-option v-for="item in orderTypeOpts" :key="item.value" :label="item.label" :value="item.value" />
              </el-select>
            </el-form-item>
          </el-col>

          <el-col :md="8">
            <el-form-item label="订单渠道">
              <el-select v-model="form.orderChannel" placeholder="请选择订单渠道">
                <el-option v-for="item in orderChannelOpts" :key="item.value" :label="item.label" :value="item.value" />
              </el-select>
            </el-form-item>
          </el-col>
          <el-col :md="8">
            <el-form-item label="订单状态">
              <el-select v-model="form.orderStatus" placeholder="请选择订单状态">
                <el-option v-for="item in orderStatusOpts" :key="item.value" :label="item.label" :value="item.value" />
              </el-select>
            </el-form-item>
          </el-col>
          <el-col :md="8">
            <el-form-item label="下单时间">
              <el-date-picker
                v-model="time"
                format="yyyy-MM-dd"
                value-format="yyyy-MM-dd"
                type="daterange"
                range-separator="至"
                start-placeholder="开始日期"
                end-placeholder="结束日期"
              />
            </el-form-item>
          </el-col>
          <el-col :md="24">
            <el-form-item align="right">
              <el-button type="primary" @click="getOrderList()">查 询</el-button>
              <el-button plain @click="resetSearch()">重 置</el-button>
            </el-form-item>
          </el-col>
        </el-row>
      </el-form>
      <el-table
        v-loading="loading"
        :data="tableData"
        stripe
        :header-cell-style="{background:'#f2f3f5',color:'#606266'}"
        class="table-data"
      >
        <el-table-column prop="orderNo" label="订单标号" align="center" :show-overflow-tooltip="true" min-width="180" />
        <el-table-column prop="createTime" label="下单时间" align="center" :show-overflow-tooltip="true" min-width="180">
          <template slot-scope="scope">
            {{ formatTimes(new Date(scope.row.createTime),'YYYY-MM-DD h:m:s') }}
          </template>
        </el-table-column>
        <el-table-column prop="userName" label="用户昵称" align="center" :show-overflow-tooltip="true" min-width="150" />
        <el-table-column prop="payChannel" label="支付渠道" align="center" :show-overflow-tooltip="true" min-width="150">
          <template slot-scope="scope">
            {{ scope.row.payChannel === 1 ? '微信' : '支付宝' }}
          </template>
        </el-table-column>
        <el-table-column prop="orderActualTotalAmount" label="订单总金额" align="center" :show-overflow-tooltip="true" min-width="150">
          <template slot-scope="scope">
            {{ (scope.row.orderActualTotalAmount / 100).toFixed(2) }}
          </template>
        </el-table-column>
        <el-table-column prop="orderChannel" label="订单渠道" align="center" :show-overflow-tooltip="true" min-width="150">
          <template slot-scope="scope">
            {{ scope.row.orderChannel === 1 ? 'PC' : 'H5' }}
          </template>
        </el-table-column>
        <el-table-column prop="payMethod" label="支付方式" align="center" :show-overflow-tooltip="true" min-width="150">
          <template slot-scope="scope">
            {{ scope.row.payMethod === 1 ? '在线支付' : '货到付款' }}
          </template>
        </el-table-column>
        <el-table-column prop="orderStatus" label="订单状态" align="center" :show-overflow-tooltip="true" min-width="150">
          <template slot-scope="scope">
            {{ orderStatusOpts.find(item=> item.value == scope.row.orderStatus) ? orderStatusOpts.find(item=> item.value == scope.row.orderStatus).label : '' }}
          </template>
        </el-table-column>
        <el-table-column
          fixed="right"
          label="操作"
          width="150"
          align="center"
        >
          <template slot-scope="scope">
            <el-button type="text" size="small" @click="orderDetail(scope.row)">订单详情</el-button>
            <el-button type="text" size="small" @click="orderCancel(scope.row)">取消订单</el-button>
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
import { getOrderList, orderCancel } from '@/api/order'
import { orderChannelOpts, orderStatusOpts, payChannelOpts, payMethodOpts, orderTypeOpts } from './orderConfig'
import { formatTimes } from '@/utils'
export default {
  data() {
    return {
      formatTimes,
      orderChannelOpts,
      orderStatusOpts,
      payChannelOpts,
      payMethodOpts,
      orderTypeOpts,
      form: {},
      time: [],
      tableData: [],
      loading: false,
      page: {
        pageNo: 1,
        pageSize: 10,
        total: 0
      }
    }
  },
  mounted() {
    this.getOrderList()
  },
  methods: {
    // 查询
    async getOrderList() {
      try {
        this.loading = true
        const parmas = {
          ...this.form,
          startTime: this.time && this.time.length > 0 ? this.time[0] : null,
          endTime: this.time && this.time.length > 0 ? this.time[1] : null,
          pageNo: this.page.pageNo,
          pageSize: this.page.pageSize
        }
        const res = await getOrderList(parmas)
        this.loading = false
        this.tableData = res.objects
        this.page.total = res.total
      } catch (error) {
        this.loading = false
        console.log(error)
      }
    },
    // 取消订单
    async orderCancel(row) {
      if (process.env.VUE_APP_PACKAGE === 'externalization') {
        try {
          await this.$confirm('亲，这是演示版，你没有权限!,想获得所有权限，请联系咨询老师，获取代码', '系统提示', { confirmButtonText: '确定', cancelButtonText: '取消', type: 'warning' })
        } catch (error) {
          console.log(error)
        }
        return
      }
      try {
        await orderCancel(row.orderNo)
        this.getOrderList()
      } catch (error) {
        console.log(error)
      }
    },
    // 重置
    resetSearch() {
      this.form = {}
    },
    // 跳转订单详情
    orderDetail(row) {
      this.$router.push({
        name: 'orderDetail',
        query: {
          orderNo: row.orderNo
        }
      })
    },
    // 切换条
    handleSizeChange(val) {
      this.page.pageSize = val
      this.getOrderList()
    },
    // 切换页
    handleCurrentChange(val) {
      this.page.pageNo = val
      this.getOrderList()
    }
  }
}
</script>
<style lang="scss" scoped>
.table-data{
  width: 100%;
  margin: 30px 0px;
}
.el-select{
  width: 100%;
}
.pagination{
  text-align: right;
}
</style>
