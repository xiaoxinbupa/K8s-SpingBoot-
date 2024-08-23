<template>
  <!-- 订单详情 -->
  <div class="wrapper-box">
    <el-card shadow="hover" class="my-order">
      <div class="my-order">
        <div class="right-box">
          <!-- order-table -->
          <table class="order-table">
            <thead>
              <th>
                <span
                  v-if="[10].includes(orderObj.orderStatus)"
                  class="t-btn-del"
                  @click="$showNoAuthMsg"
                >取消订单</span>
                <span class="t-order-span t-order-time">下单时间：{{ orderObj.createTime }}</span>
                <span class="t-order-span t-order-id">订单号：{{ orderObj.orderNo }}</span>
              </th>
            </thead>
            <tbody>
              <td class="clearfix">
                <div class="f-left-box">
                  <dl class="cus-form-item">
                    <dt class="c-label">收件人：</dt>
                    <dd class="c-item">{{ orderObj.orderAddress.consigneeName }}</dd>
                  </dl>
                  <dl class="cus-form-item">
                    <dt class="c-label">联系方式：</dt>
                    <dd class="c-item">{{ orderObj.orderAddress.consigneeMobile }}</dd>
                  </dl>
                  <dl class="cus-form-item">
                    <dt class="c-label">收货地址：</dt>
                    <dd class="c-item">{{ orderObj.orderAddress.consigneeAddress }}</dd>
                  </dl>
                </div>
                <div class="f-right-box">
                  <dl class="cus-form-item">
                    <dt class="c-label">商品合计：</dt>
                    <dd class="c-item">{{ orderObj.goodsActualTotalAmounts / 100 }}</dd>
                  </dl>
                  <dl class="cus-form-item">
                    <dt class="c-label">邮费：</dt>
                    <dd class="c-item">{{ orderObj.freightAmounts / 100 }}</dd>
                  </dl>
                  <dl class="cus-form-item">
                    <dt class="c-label">活动优惠：</dt>
                    <dd class="c-item">0</dd>
                  </dl>
                  <div class="order-pay-box">
                    <dl class="cus-form-item">
                      <dt class="c-label">应付：</dt>
                      <dd class="c-item" style="color: #FF3C3E; font-size: 18px;">{{ orderObj.orderActualTotalAmount / 100 }}</dd>
                    </dl>
                    <div class="c-btn-wrap">
                      <span
                        v-if="orderObj.orderStatus == 10"
                        class="c-pay-btn"
                        @click="toPayFn"
                      >付款 {{ orderObj.orderActualTotalAmount / 100 }}</span>
                      <span v-if="orderObj.orderStatus < 0" class="c-pay-btn no-pay">付款 {{ orderObj.orderActualTotalAmount / 100 }}</span>
                    </div>
                  </div>
                </div>
              </td>
            </tbody>
          </table>
          <!-- goods-table -->
          <table class="goods-table">
            <colgroup>
              <col>
              <col style="width: 100px;">
              <col style="width: 80px;">
              <col style="width: 100px;">
              <col style="width: 80px;">
            </colgroup>
            <thead>
              <tr>
                <th colspan="5">
                  <span
                    v-if="orderObj.orderStatus < 0"
                    class="t-btn-del"
                    @click="$showNoAuthMsg"
                  >再次购买</span>
                  <span
                    v-if="orderObj.orderStatus ==20"
                    class="t-btn-default"
                    @click="$showNoAuthMsg"
                  >催促发货</span>
                  <span
                    v-if="orderObj.orderStatus == 30"
                    class="t-btn-del"
                    @click="$showNoAuthMsg"
                  >确认收货</span>
                  <span
                    v-if="orderObj.orderStatus == 40"
                    class="t-btn-default"
                    @click="$showNoAuthMsg"
                  >联系客服</span>
                  <span>商品清单</span>
                  <template v-if="[10].includes(orderObj.orderStatus )">
                    <span class="order-status">待付款</span>
                    <span>（请您尽快完成支付，该订单会为您保留2小时，如若到期未支付，该订单将会被系统自动取消）</span>
                  </template>
                  <template v-if="orderObj.orderStatus < 0">
                    <span class="order-status">已取消</span>
                    <span>（退款会原路返回您的交易账户，如订单进行拆分，优惠券将不会返回）</span>
                  </template>
                  <template v-if="[30].includes(orderObj.orderStatus )">
                    <span class="order-status">待收货</span>
                    <span>（您可在14天内对当前订单进行“确认收货”，届时如果您未确认订单也未发起售后申请，系统将在后台自动为您确认）</span>
                  </template>
                  <template v-if="[20].includes(orderObj.orderStatus )">
                    <span class="order-status">待发货</span>
                    <span>（付款后如果平台没有在24小时内发货，您可以催促发货）</span>
                  </template>
                  <template v-if="[40].includes(orderObj.orderStatus )">
                    <span class="order-status">已完成</span>
                    <span>（如果您对该笔交易有任何疑问，请您联系客服进行解决）</span>
                  </template>
                </th>
              </tr>
            </thead>
            <tbody class="t-c-thead">
              <tr>
                <td>商品信息</td>
                <td>单价</td>
                <td>数量</td>
                <td>实付</td>
                <td>会员积分</td>
              </tr>
            </tbody>
            <tbody class="t-c-tbody">
              <tr v-for="(goodsItem, index) in orderObj.orderGoodsList" :key="index">
                <td>
                  <img class="p-img" :src="goodsItem.picture">
                  <div class="p-sub">
                    <h3>{{ goodsItem.goodsTitle }}</h3>
                    <p class="p-attr">
                      <span v-for="(attrItem, ind) in goodsItem.attributePairs" :key="ind">
                        {{ (ind == goodsItem.attributePairs.length - 1 && attrItem.attributeValue) || attrItem.attributeValue + '； ' }}
                      </span>
                    </p>
                  </div>
                </td>
                <td>¥{{ goodsItem.saleUnitPrice / 100 }}</td>
                <td>{{ goodsItem.goodsNum }}</td>
                <td>¥{{ goodsItem.actualTotalAmount / 100 }}</td>
                <td>0</td>
              </tr>
            </tbody>
          </table>
        </div>
      </div>
    </el-card>
  </div>
</template>

<script>
import { orderDetail, orderCashDesk } from '@/apis/mall.js'
import { toCheckPayFn } from '@/utils/index.js'

export default {
  name: 'OrderInfo',
  components: {},
  data() {
    return {
      orderObj: {
        orderAddress: {},
        orderGoodsList: []
        // orderStatus 当前订单状态,10:待支付, 20:已支付,待发货, 30:已发货/待收货, 40:确认收货/已完成,-10: 用户关闭, -20:平台关闭(商家), -30:系统调度关闭
      },
      orderNo: ''
    }
  },
  computed: {},
  watch: {},
  created() {},
  mounted() {
    this.init()
  },
  methods: {
    init() {
      this.orderNo = this.$route.query.orderNo
      if (this.orderNo) {
        orderDetail({ orderNo: this.orderNo }).then(res => {
          console.log(res)
          this.orderObj = res || {}
        })
      }
    },
    // 去付款
    toPayFn() {
      const win = window.open()
      orderCashDesk({ orderNo: this.orderNo })
        .then(res => {
          // 失败情况TODO
          win.close()
        }).catch((err) => {
          // 成功情况
          if (win) {
            win.document.write(err.data)
          }
          toCheckPayFn(this.orderNo, win, () => {
            this.init()
          })
        })
    }
  }
}
</script>

<style lang="scss" scoped>
.my-order {
  width: 1200px;
  margin: 20px auto 50px;
  .right-box {
    width: 1160px;
  }
  .order-table {
    width: 100%;
    border-collapse: collapse;
    font-size: 12px;
    color: #333333;
    border: 1px solid #dddddd;
    thead {
      th {
        height: 40px;
        background: #f5f5f5;
        border: 1px solid #dddddd;
        color: #666666;
        font-weight: normal;
        text-align: left;
        padding: 0 15px;
        line-height: 24px;
      }
      .t-order-span ~ .t-order-span{
        margin-left: 25px;
      }
    }
    tbody td {
      padding: 15px 20px 15px 15px;
    }
    margin-bottom: 10px;
    .t-btn-del {
      display: inline-block;
      line-height: 16px;
      float: right;
      cursor: pointer;
      background: #f5f5f5;
      border-radius: 3px;
      border: 1px solid #cccccc;
      padding: 3px 10px;
    }
    dl,
    dt,
    dd {
      margin: 0;
      padding: 0;
    }
    .cus-form-item {
      display: flex;
      line-height: 22px;
      & ~ .cus-form-item {
        margin-top: 8px;
      }
      & ~ .c-btn-wrap {
        margin-top: 25px;
      }
      .c-label {
        color: #9e9e9e;
        font-size: 14px;
        width: 90px;
      }
      .c-item {
        flex: 1;
        font-size: 14px;
        color: #333333;
        font-weight: bold;
      }
    }
    .f-left-box {
      float: left;
      width: 600px;
    }
    .f-right-box {
      float: right;
      width: 200px;
      .c-label,
      .c-item {
        text-align: right;
      }
      .order-pay-box {
        margin-top: 50px;
        border-top: 1px solid #d8d8d8;
        padding: 25px 0 10px 0;
        .c-btn-wrap {
          text-align: right;
        }
        .c-pay-btn {
          display: inline-block;
          background: #ff3c3e;
          border-radius: 4px;
          line-height: 20px;
          font-size: 14px;
          color: #ffffff;
          padding: 10px 20px;
          cursor: pointer;
          &.no-pay {
            background: #cccccc;
            cursor: not-allowed;
          }
        }
      }
    }
  }
  .goods-table {
    width: 100%;
    border-collapse: collapse;
    font-size: 12px;
    color: #333333;
    border: none;
    .order-status {
      color: #333333;
      font-size: 14px;
      margin: 0 5px 0 25px;
    }
    thead {
      th {
        height: 40px;
        background: #f5f5f5;
        border: 1px solid #dddddd;
        color: #666666;
        font-weight: normal;
        text-align: left;
        padding: 0 15px;
        line-height: 24px;
      }
      .t-btn-del {
        display: inline-block;
        line-height: 16px;
        float: right;
        cursor: pointer;
        color: #FF3C3E;
        border-radius: 3px;
        border: 1px solid #FF3C3E;
        padding: 3px 10px;
      }
      .t-btn-default{
        display: inline-block;
        line-height: 16px;
        float: right;
        cursor: pointer;
        background: #f5f5f5;
        border-radius: 3px;
        border: 1px solid #cccccc;
        padding: 3px 10px;
      }
    }

    tbody {
      td {
        text-align: center;
        padding: 20px 0;
        border: 1px solid #dddddd;
        vertical-align: top;
        &:first-child {
          text-align: left;
        }
        &:nth-child(1) {
          border-right: none;
          padding-left: 15px;
          padding-right: 15px;
        }

        &:nth-child(2),
        &:nth-child(3),
        &:nth-child(4) {
          border-left: none;
          border-right: none;
        }
        &:nth-child(5) {
          border-left: none;
        }
      }
      .p-img {
        float: left;
        width: 80px;
        height: 80px;
        border: 1px solid #dddddd;
        object-fit: fill;
      }
      .p-sub {
        margin-left: 90px;
        h3 {
          margin: 5px 0 13px 0;
          font-size: 12px;
          font-weight: normal;
          color: #333333;
        }
        .p-attr {
          color: #9e9e9e;
          margin: 0;
        }
      }
    }
    .t-c-thead {
      td {
        padding: 12px 0;
      }
    }
  }
}
</style>
