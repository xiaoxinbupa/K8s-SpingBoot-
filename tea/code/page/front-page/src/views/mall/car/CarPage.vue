<template>
  <div class="cart">
    <!-- 购物车优惠券 -->
    <!-- <cart-coupon /> -->

    <!-- 购物车详情列表 -->
    <div v-if="cartData && cartData.skus && cartData.skus.length!=0" class="card cart">
      <div class="title">
        购物车（全部{{ totalCount }}件）
      </div>
      <div class="list">
        <div class="header">
          <div class="box">
            <el-checkbox v-model="cartData.allChecked" @change="changeAllChecked" />
          </div>
          <div class="pic">
            全选
          </div>
          <div class="title">
            商品信息
          </div>
          <!-- <div class="spec">
            规格参数
          </div> -->
          <div class="price">
            单价
          </div>
          <div class="count">
            数量
          </div>
          <div class="amount">
            金额
          </div>
          <div class="operation">
            操作
          </div>

        </div>
        <div
          v-for="item in cartData.skus"
          :key="item.skuId"
          :class="{item:true, active:item.selected}"
        >
          <div class="box">
            <el-checkbox v-model="item.selected" @change="changeSelected(item)" />
          </div>
          <div class="pic">
            <router-link :to="{path:'malldetail', query:{skuId:item.skuId}}" target="_blank">
              <img
                class="img"
                :src="item.coverPicture"
              >
            </router-link>
          </div>
          <div class="title">
            <router-link :to="{path:'malldetail', query:{skuId:item.skuId}}" target="_blank">
              {{ item.title }}
            </router-link>
          </div>
          <!-- <div class="spec">
            <p
              v-for="pair in item.specificationPairs"
              :key="pair.propertyName"
            >
              {{ pair.propertyName }}：{{ pair.propertyValue }}
            </p>
          </div> -->
          <div class="price">
            <!-- <s>￥3299.00(优惠)</s> -->
            <p>￥{{ (item.price/100).toFixed(2) }}</p>
          </div>
          <div class="count">
            <el-input-number
              v-model="item.num"
              size="mini"
              style="width:90px;"
              :min="1"
              :max="99"
              :precision="0"
              :step="1"
              @change="changeNum(item)"
            />
          </div>
          <div class="amount">
            ￥{{ (item.totalAmounts/100).toFixed(2) }}
          </div>
          <div class="operation">
            <span class="del" @click="deleteItem(item.skuId)">删除</span>
          </div>
        </div>
      </div>
    </div>
    <div v-else style="text-align:center; padding: 30px 0 80px;">
      <el-empty :image="require(`@/assets/images/cart_empty.png`)" description="购物车还是空滴，快去选购吧！" />
      <router-link to="/mall">
        <el-button style="border-radius: 0px; border:1px solid #ff3c3e; color: #ff3c3e;">继续逛</el-button>
      </router-link>
    </div>

    <!-- 购物车合计 -->
    <div v-if="cartData && cartData.skus && cartData.skus.length!=0" class="card total">
      <el-checkbox
        v-model="cartData.allChecked"
        class="box"
        @change="changeAllChecked"
      >
        全选
      </el-checkbox>
      <p class="del">
        <span @click="deleteSelectedItem">批量删除选中商品</span>
      </p>
      <p class="count">已选商品 <span>{{ cartData.selectedCount }}</span> 件</p>
      <p class="total">合计：<span>￥{{ (cartData.totalAmounts/100).toFixed(2) }}</span></p>
      <div class="btn-price" @click="toPlaceOrder">结算</div>
    </div>
  </div>
</template>

<script>
import { getCarList, selectCartItem, unSelectCartItem, selectAllCartItem, unSelectAllCartItem, removeCartItem, removeAllCartItem, modifyNum } from '@/apis/mall'
export default {
  // components: { cartCoupon },
  data() {
    return {
      cartData: null // 保存购物车完整的数据
    }
  },

  computed: {
    totalCount() {
      let total = 0
      if (this.cartData && this.cartData.skus) {
        for (let i = 0; i < this.cartData.skus.length; i++) {
          total += this.cartData.skus[i].num
        }
      }
      return total
    }
  },

  mounted() {
    this.init()
  },
  methods: {

    /** 根据当前购物项的状态，更新cartData对象并渲染页面。计算并新增两个新属性：
     *  allChecked：当前是否全部选中
     *  selectedCount：当前选中的商品数量
     *  计算完成后，更新this.cartData */
    renderCartData() {
      let allChecked = true
      let selectedCount = 0
      let totalAmounts = 0
      if (this.cartData && this.cartData.skus) {
        for (let i = 0; i < this.cartData.skus.length; i++) {
          const item = this.cartData.skus[i]
          if (item.selected) {
            selectedCount += item.num
            totalAmounts += item.num * item.price
          } else {
            allChecked = false
          }
          // 更新购物项小计
          item.totalAmounts = item.num * item.price
        }
      }
      this.cartData.allChecked = allChecked
      this.cartData.selectedCount = selectedCount
      this.cartData.totalAmounts = totalAmounts
    },

    /** 点击全选或全不选 */
    changeAllChecked(newVal) {
      console.log('需要设置为：' + (newVal ? '全选中' : '全不选中'))
      if (this.cartData && this.cartData.skus) {
        for (let i = 0; i < this.cartData.skus.length; i++) {
          const item = this.cartData.skus[i]
          item.selected = newVal
        }
      }
      this.renderCartData()
      // 发送请求，修改当前后端购物车数据的全选状态
      if (newVal) { // 若用户需要全选
        selectAllCartItem().catch(err => {
          this.$message(err)
        })
      } else { // 若用户需要全不选
        unSelectAllCartItem().catch(err => {
          this.$message(err)
        })
      }
    },

    /** 当修改购物项的选中状态时 */
    changeSelected(item) {
      console.log('修改购物项选中状态:', item)
      this.renderCartData()
      // 发送请求，修改当前购物项的选中状态
      if (item.selected) {
        // 异步发送请求更新后端数据
        selectCartItem(item.skuId).catch(err => {
          this.$message(err)
        })
      } else {
        unSelectCartItem(item.skuId).catch(err => {
          this.$message(err)
        })
      }
    },

    /** 改变数量 */
    changeNum(item) {
      this.renderCartData()
      // 发送请求，修改当前购物项的数量
      const _data = { skuId: item.skuId, num: item.num }
      modifyNum(_data).catch(err => {
        this.$message(err)
      })
    },

    /** 删除购物项 */
    deleteItem(skuId) {
      this.$confirm('此操作将删除该购物项, 是否继续?', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(() => { // 用户点了确定
        removeCartItem(skuId).then(res => {
          this.init() // 重新加载列表
        })
      }).catch(() => { // 用户点了取消
      })
    },

    /** 删除购物项 */
    deleteSelectedItem() {
      this.$confirm('此操作将删除所有购物项, 是否继续?', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(() => { // 用户点了确定
        removeAllCartItem().then(res => {
          this.init() // 重新加载列表
        })
      }).catch(() => { // 用户点了取消
      })
    },

    /** 初始化加载当前用户的购物车列表 */
    init() {
      getCarList().then(res => {
        console.log('加载购物车列表', res)
        this.cartData = res
        this.renderCartData()
      }).catch(err => {
        console.error('加载购物车列表失败', err)
      })
    },
    // 提交订单
    toPlaceOrder() {
      this.$router.push({ path: 'orderpage' })
    }
  }
}
</script>

<style lang="scss" scoped>
.cart {
  width: 1090px;
  margin: 30px auto 10px;
  background: #fff;
}

.card {
  border: 1px solid #dddddd;
}

/** 购物车列表相关样式 */
.cart {
  & > .title {
    height: 45px;
    line-height: 45px;
    font-size: 16px;
    color: #161616;
    font-weight: bold;
    padding-left: 16px;
    border-bottom: 1px solid #DDD;
  }

  /** 购物车列表项样式 */
  & > .list {
    /** 表头样式 */
    .header {
      display: flex;
      flex-direction: row;
      align-items: flex-start;
      justify-content: space-around;
      padding: 20px;
      border-bottom: 1px solid #DDD;
      font-size: 16px;
      font-weight: bolder;
      color: #333;
      .pic {
        width: 80px;
      }
      .title {
        width: 240px;
      }
      .spec {
        width: 170px;
      }
      .price {
        width: 100px;
      }
      .count {
        width: 100px;
      }
      .amount {
        width: 100px;
      }
      .operation {
        width: 80px;
      }
    }

    /** 列表项样式 */
    .item {
      display: flex;
      flex-direction: row;
      align-items: flex-start;
      justify-content: space-around;
      padding: 20px;
      border-bottom: 1px solid #DDD;
      font-weight: 500;

      .pic {
        width: 80px;
        .img {
          width: 80px;
          height: 80px;
          border: 1px solid #f6f6f6;
          cursor: pointer;
        }
      }
      .title {
        width: 240px;
        font-size: 12px;
        color: #333;
        line-height: 18px;
        cursor: pointer;
      }
      .spec {
        width: 170px;
        p {
          margin: 0;
          font-size: 12px;
          font-family: Source Han Sans CN-Medium, Source Han Sans CN;
          color: #9C9C9C;
          line-height: 18px;
        }
      }
      .price {
        width: 100px;
        font-size: 12px;
        line-height: 18px;
        s {
          color: #666;
        }
        p {
          margin: 0;
        }
      }
      .count {
        width: 100px;
      }
      .amount {
        width: 100px;
        color: #ff3e3c;
        font-size: 14px;
      }
      .operation {
        width: 80px;
        font-size: 14px;
        .del {
          cursor: pointer;
        }
        .del:hover {
          color: #ff3e3c;
        }
      }
    }

    .item.active {
      background-color: #FDF5F3;
    }
  }
}

.total {
  height: 70px;
  display: flex;
  align-items: center;
  color: #333;

  .box {
    margin: 0px 30px;
  }
  .del {
    font-size: 12px;
    flex: 1;
    span {
      cursor: pointer;
    }
    span:hover {
      color: #ff3e3c;
    }
  }
  .count {
    font-size: 14px;
    margin-right: 20px;

    & > span {
      color: #ff3e3c;
      font-weight: bolder;
      font-size: 16px;
    }
  }
  .total {
    font-size: 14px;
    margin-right: 20px;

    & > span {
      font-size: 16px;
      font-weight: 500;
      color: #ff3e3c;
    }
  }
  .btn-price {
    width: 140px;
    background-color: #ff3e3c;
    font-size: 18px;
    color: #fff;
    text-align: center;
    line-height: 70px;
    cursor: pointer;
  }
}
</style>
