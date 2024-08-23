<template>
  <div class="order-detail">
    <el-card shadow="never">
      <el-steps :active="active" align-center>
        <el-step v-for="item in stepArray" :key="item.value" :title="item.name" :description="item.time" />
      </el-steps>
      <div class="order-status">
        <i class="el-icon-question" /><span>订单状态：</span><span>订单取消</span>
      </div>
      <h3>订单信息</h3>
      <div class="order-info">
        <div class="info-title">
          基础信息
        </div>
        <div class="infor-container">
          <el-row :gutter="20">
            <el-col :span="8"><div class="grid-content">订单编号：{{ orderDetail.orderNo }}</div></el-col>
            <el-col :span="8"><div class="grid-content">订单来源：PC</div></el-col>
            <el-col :span="8"><div class="grid-content">订单类型： {{ orderDetail.orderType == 1 ? '普通订单' : '秒杀订单' }}</div></el-col>
            <el-col :span="8"><div class="grid-content">下单时间： {{ formatTimes(new Date(orderDetail.createTime),'YYYY-MM-DD h:m:s') }}</div></el-col>
          </el-row>
        </div>
        <div class="info-title">
          收货人信息
        </div>
        <div class="infor-container">
          <el-row :gutter="20">
            <el-col :span="8"><div class="grid-content">收货人姓名： {{ orderDetail.orderAddress ? orderDetail.orderAddress.consigneeName : '' }}</div></el-col>
            <el-col :span="16"><div class="grid-content">收货地址： {{ orderDetail.orderAddress ? orderDetail.orderAddress.consigneeAddress : '' }}</div></el-col>
            <el-col :span="8"><div class="grid-content">联系方式： {{ orderDetail.orderAddress ? orderDetail.orderAddress.consigneeMobile : '' }}</div></el-col>
          </el-row>
        </div>
        <div class="info-title">
          商品信息
        </div>
        <el-table
          :data="orderDetail.orderGoodsList"
          stripe
          :header-cell-style="{background:'#f2f3f5',color:'#606266'}"
          class="table-data"
        >
          <el-table-column prop="title" label="商品图片" align="center" :show-overflow-tooltip="true" min-width="150">
            <template slot-scope="scope">
              <el-image
                style="width: 100px; height: 100px"
                :src="scope.row.picture"
                fit="fill'"
              />
            </template>
          </el-table-column>
          <el-table-column prop="title" label="商品名称" align="center" :show-overflow-tooltip="true" min-width="150" />
          <!-- <el-table-column prop="goodsCode" label="货号" align="center" :show-overflow-tooltip="true" min-width="150" /> -->
          <el-table-column prop="saleUnitPrice" label="价格" align="center" :show-overflow-tooltip="true" min-width="150">
            <template slot-scope="scope">
              {{ (scope.row.saleUnitPrice/100).toFixed(2) }}
            </template>
          </el-table-column>
          <!-- <el-table-column prop="title" label="商品规格" align="center" :show-overflow-tooltip="true" min-width="150" /> -->
          <el-table-column prop="goodsNum" label="数量" align="center" :show-overflow-tooltip="true" min-width="150" />
          <el-table-column prop="goodsTotalAmounts" label="小计" align="center" :show-overflow-tooltip="true" min-width="150">
            <template slot-scope="scope">
              {{ (scope.row.goodsTotalAmounts/100).toFixed(2) }}
            </template>
          </el-table-column>
        </el-table>
        <div class="info-title">
          费用信息
        </div>
        <div class="infor-container">
          <el-row :gutter="20">
            <el-col :span="8"><div class="grid-content">商品总价： {{ (orderDetail.goodsTotalAmounts/100).toFixed(2) }}</div></el-col>
            <el-col :span="8"><div class="grid-content">运费： {{ (orderDetail.freightAmounts/100).toFixed(2) }}</div></el-col>
            <el-col :span="8"><div class="grid-content">优惠金额： {{ (orderDetail.discountTotalAmounts/100).toFixed(2) }}</div></el-col>
            <el-col :span="8"><div class="grid-content">实付金额： {{ (orderDetail.orderActualTotalAmount/100).toFixed(2) }}</div></el-col>
          </el-row>
        </div>
        <div class="info-title">
          操作日志
        </div>
        <el-table
          :data="orderDetail.orderLogs"
          stripe

          :header-cell-style="{background:'#f2f3f5',color:'#606266'}"
          class="table-data"
        >
          <el-table-column prop="userName" label="操作人" align="center" :show-overflow-tooltip="true" min-width="150" />
          <el-table-column prop="afterStatus" label="订单状态" align="center" :show-overflow-tooltip="true" min-width="150">
            <template slot-scope="scope">
              {{ afterStatusObj[scope.row.afterStatus] }}
            </template>
          </el-table-column>
          <el-table-column prop="remark" label="备注" align="center" :show-overflow-tooltip="true" min-width="150" />
          <el-table-column prop="operatedTime" label="操作时间" align="center" :show-overflow-tooltip="true" min-width="150">
            <template slot-scope="scope">
              {{ formatTimes(new Date(scope.row.operatedTime),'YYYY-MM-DD h:m:s') }}
            </template>
          </el-table-column>
        </el-table>
      </div>
    </el-card>
  </div>
</template>

<script>
import { stepArray, orderStatusOpts, afterStatusObj } from './orderConfig'
import { getOrderDetail } from '@/api/order'
import { formatTimes } from '@/utils'
export default {
  data() {
    return {
      formatTimes,
      stepArray,
      orderStatusOpts,
      afterStatusObj,
      active: 1,
      orderDetail: {},
      tableData: []
    }
  },
  mounted() {
    this.getOrderDetail()
  },
  methods: {
    async getOrderDetail() {
      try {
        const res = await getOrderDetail({ orderNo: this.$route.query.orderNo })
        this.orderDetail = res
      } catch (error) {
        console.log(error)
      }
    }
  }
}
</script>
<style lang="scss" scoped>
.order-detail{
  .order-status{
    margin-top: 40px;
    padding: 10px 30px;
    font-size: 14px;
    font-weight: normal;
    color: rgba(0, 0, 0, 0.65);
    background: #F2F3F6;
    .el-icon-question{
      color: #1890FF;
    }
  }
  .order-info{
    background: #FFFFFF;
    border: 1px solid #D9D9D9;
    padding-bottom: 30px;
    .info-title{
      background: #F2F3F6;
      padding: 10px 30px;
      font-weight: 600;
    }
    .infor-container{
      padding: 10px 40px;
      color: rgba(0, 0, 0, 0.65);
      font-size: 14px;
      font-weight: normal;
      .grid-content{
        padding: 15px 0px;
      }
    }
    .table-data{
      padding: 15px 40px;
    }
  }
}
</style>
