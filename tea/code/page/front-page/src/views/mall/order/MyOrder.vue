<template>
  <div class="wrapper-box">
    <el-card shadow="hover" class="my-order">
      <div>
        <div class="right-box">
          <div class="tabs-wrap">
            <div
              class="tab-item"
              :class="{ cur: orderStatus == 0 }"
              @click="tabChangeFn(0)"
            >全部订单</div>
            <div class="c-line" />
            <div
              class="tab-item"
              :class="{ cur: orderStatus == 10 }"
              @click="tabChangeFn(10)"
            >
              待付款
              <i v-if="orderStatusCount.unPaidCount > 0">{{ orderStatusCount.unPaidCount }}</i>
            </div>
            <div class="c-line" />
            <div
              class="tab-item"
              :class="{ cur: orderStatus == 20 }"
              @click="tabChangeFn(20)"
            >
              待发货
              <i v-if="orderStatusCount.paidCount > 0">{{ orderStatusCount.paidCount }}</i>
            </div>
            <div class="c-line" />
            <div
              class="tab-item"
              :class="{ cur: orderStatus == 30 }"
              @click="tabChangeFn(30)"
            >
              待收货
              <i v-if="orderStatusCount.shippedCount > 0">{{ orderStatusCount.shippedCount }}</i>
            </div>
          </div>

          <!-- order-table -->
          <table class="order-table">
            <colgroup>
              <col>
              <col style="width: 80px;">
              <col style="width: 60px;">
              <col style="width: 80px;">
              <col style="width: 90px;">
              <col style="width: 100px;">
              <col style="width: 100px;">
            </colgroup>
            <thead>
              <tr>
                <th>商品信息</th>
                <th>单价</th>
                <th>数量</th>
                <th>操作</th>
                <th>实付金额</th>
                <th>订单状态</th>
                <th>可执行操作</th>
              </tr>
            </thead>
            <!-- 订单 start -->
            <tbody v-for="item in list" :key="item.id">
              <tr class="t-c-row"><td colspan="7" /></tr>
              <tr class="t-c-header">
                <td colspan="7">
                  <span class="t-btn-del el-icon-delete" @click="$showNoAuthMsg" />
                  <span class="t-order-time">下单时间：{{ item.createTime }}</span>
                  <span class="t-order-id">订单号：{{ item.orderNo }}</span>
                </td>
              </tr>
              <tr
                v-for="(goodsItem, index) in item.orderGoodsList"
                :key="index"
                class="t-c-info"
              >
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
                <td>
                  <span
                    v-if="item.orderStatus == 40"
                    class="btn-link"
                    @click="$showNoAuthMsg"
                  >申请售后</span>
                  <span
                    v-if="item.orderStatus < 0"
                    class="btn-link"
                    @click="$showNoAuthMsg"
                  >查看退款</span>
                </td>

                <template v-if="item.orderGoodsList.length > 1">
                  <template v-if="index == 0">
                    <td :rowspan="item.orderGoodsList.length">¥{{ item.orderActualTotalAmount / 100 }}</td>
                    <td :rowspan="item.orderGoodsList.length">
                      <p class="order-status">{{ orderStatusMap[item.orderStatus] }}</p>
                      <span class="btn-link"><router-link :to="{path:'orderinfor', query:{orderNo:item.orderNo}}">订单详情</router-link></span>
                    </td>
                    <td :rowspan="item.orderGoodsList.length">
                      <span
                        v-if="item.orderStatus == 10"
                        class="btn-block btn-danger"
                        @click="toPay(item.orderNo)"
                      >立即付款</span>
                      <span
                        v-if="item.orderStatus == 10"
                        class="btn-link"
                        @click="$showNoAuthMsg"
                      >取消订单</span>
                      <span
                        v-if="item.orderStatus == 30"
                        class="btn-block btn-primary"
                        @click="$showNoAuthMsg"
                      >确认收货</span>
                      <span
                        v-if="item.orderStatus == 20"
                        class="btn-block btn-primary"
                        @click="$showNoAuthMsg"
                      >催发货</span>
                      <span
                        v-if="item.orderStatus == 40"
                        class="btn-link"
                        @click="$showNoAuthMsg"
                      >再次购买</span>
                    </td>
                  </template>
                </template>

                <template v-else>
                  <td>¥{{ item.orderActualTotalAmount / 100 }}</td>
                  <td>
                    <p class="order-status">{{ orderStatusMap[item.orderStatus] }}</p>
                    <span class="btn-link"><router-link :to="{path:'orderinfor', query:{orderNo:item.orderNo}}">订单详情</router-link></span>
                  </td>
                  <td>
                    <span
                      v-if="item.orderStatus == 10"
                      class="btn-block btn-danger"
                      @click="toPay(item.orderNo)"
                    >立即付款</span>
                    <span
                      v-if="item.orderStatus == 10"
                      class="btn-link"
                      @click="$showNoAuthMsg"
                    >取消订单</span>
                    <span
                      v-if="item.orderStatus == 30"
                      class="btn-block btn-primary"
                      @click="$showNoAuthMsg"
                    >确认收货</span>
                    <span
                      v-if="item.orderStatus == 20"
                      class="btn-block btn-primary"
                      @click="$showNoAuthMsg"
                    >催发货</span>
                    <span
                      v-if="item.orderStatus == 40"
                      class="btn-link"
                      @click="$showNoAuthMsg"
                    >再次购买</span>
                  </td>
                </template>
              </tr>
            </tbody>
            <!-- 订单 end -->
            <tbody v-if="list.length == 0">
              <tr><td colspan="7" style="text-align: center; padding: 50px;">~~ 无订单 ~~</td></tr>
            </tbody>
          </table>

          <!-- 分页 -->
          <div class="pagination-wrap">
            <el-pagination
              background
              layout="prev, pager, next, jumper"
              :page-size="pagination.pageSize"
              :total="pagination.total"
              :current-page="pagination.pageNo"
              @current-change="onCurrentChangeFn"
            />
          </div>

        </div>
      </div>
    </el-card>
  </div>
</template>

<script>
import { orderPage, orderStatistics, orderCashDesk } from '@/apis/mall.js'
import { toCheckPayFn } from '@/utils/index.js'

export default {
  name: 'MyOrder',
  components: {},
  data() {
    return {
      // 订单列表
      list: [],
      orderStatus: 0,
      orderStatusCount: {
        totalCount: 0,
        unPaidCount: 0, // 待支付
        paidCount: 0, // 已支付/待发货
        shippedCount: 0 // 已发货/待收货
      },
      orderStatusMap: { // 当前订单状态,10:待支付, 20:已支付,待发货, 30:已发货/待收货, 40:确认收货/已完成,-10: 用户关闭, -20:平台关闭(商家), -30:系统调度关闭
        10: '待付款',
        20: '已付款',
        30: '待收货',
        40: '已完成',
        '-10': '已取消',
        '-20': '已取消',
        '-30': '已取消'
      },
      // 分页控制器
      pagination: {
        total: 0,
        pageSize: 20,
        pageNo: 1
      }
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
      const _data = {
        orderStatus: this.orderStatus,
        ...this.pagination
      }
      // 获取订单
      orderPage(_data)
        .then(res => {
          this.pagination.total = res.total
          this.list = res.objects || []
        })
        .catch(_ => {
          this.list = []
        })

      // 获取订单状态数量
      orderStatistics()
        .then(res => {
          this.orderStatusCount = res
          console.log('--orderStatistics', res)
        })
        .catch(_ => {
          this.orderStatusCount = this.$options.data().orderStatusCount
        })
    },
    // 订单状态切换
    tabChangeFn(tab) {
      if (this.orderStatus !== tab) {
        this.orderStatus = tab
        this.init()
      }
    },
    // 支付
    toPay(orderNo) {
      const win = window.open()
      orderCashDesk({ orderNo })
        .then(res => {
          // 失败情况TODO
          win.close()
        }).catch((err) => {
          // 成功情况
          if (win) {
            win.document.write(err.data)
          }
          toCheckPayFn(orderNo, win, () => {
            // this.init()
            this.$router.push({ path: 'orderinfor', query: { orderNo }})
          })
        })
    },
    // 分页
    onCurrentChangeFn(curPage) {
      this.pagination.pageNo = curPage
      this.init()
    }

  }
}
</script>

<style lang="scss" scoped>
.my-order {
  width: 1200px;
  margin: 20px auto 50px;
  .tabs-wrap {
    display: flex;
    align-items: center;
    border-bottom: 2px solid #dddddd;
    margin-bottom: 16px;
    .tab-item {
      padding: 8px 15px 6px;
      font-size: 18px;
      line-height: 26px;
      /* font-weight: bold; */
      color: #333333;
      border-bottom: 2px solid transparent;
      margin-bottom: -2px;
      cursor: pointer;
      i {
        color: #ff0030;
        font-style: normal;
      }
      &.cur {
        color: #ff0030;
        border-bottom: 2px solid #ff0030;
      }
      &:hover {
        color: #ff0030;
      }
    }
    .c-line {
      width: 1px;
      height: 18px;
      background: #d8d8d8;
      font-size: 0;
      overflow: hidden;
    }
  }
  .order-table {
    width: 100%;
    border-collapse: collapse;
    font-size: 12px;
    color: #333333;
    border: none;
    .order-status {
      margin: 0 0 10px 0;
    }
    .btn-link {
      display: inline-block;
      cursor: pointer;
      color: #005aa0;
      & ~ .btn-link,
      & ~ .btn-block {
        margin-top: 10px;
      }
    }
    .btn-block {
      display: inline-block;
      border-radius: 4px 4px 4px 4px;
      color: #fff;
      padding: 5px 10px;
      font-size: 12px;
      cursor: pointer;
      & ~ .btn-link,
      & ~ .btn-block {
        margin-top: 10px;
      }
    }
    .btn-danger {
      background: #ff3c3e;
    }

    .btn-primary {
      background: #64a6ff;
    }
    .t-c-info {
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
          padding-left: 10px;
          padding-right: 10px;
        }

        &:nth-child(2),
        &:nth-child(3),
        &:nth-child(4) {
          border-left: none;
          border-right: none;
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
    .t-c-row {
      height: 20px;
      overflow: hidden;
      td {
        border: none;
      }
    }
    .t-c-header {
      td {
        border: 1px solid #dddddd;
        height: 40px;
        background: #f5f5f5;
        padding: 0 15px;
        color: #666666;
      }
      .t-btn-del {
        float: right;
        font-size: 16px;
        cursor: pointer;
      }
      .t-order-time {
        margin-right: 80px;
      }
    }
    thead {
      tr {
        border: 1px solid #dddddd;
      }
      th {
        height: 40px;
        background: #f5f5f5;
        border: none;
        text-align: center;
        color: #666666;
        &:first-child {
          text-align: left;
          padding-left: 15px;
        }
      }
    }
  }
}

.pagination-wrap{
  padding: 30px 25px;
  display: flex;
  justify-content: flex-end;
}
</style>
