<template>
  <div class="order-page wrapper-box">
    <div class="order-table">
      <div class="order-table-header">
        <span>收货信息</span>
        <span class="add-address" @click="openAddress()">新增地址</span>
      </div>
      <div class="adress-container">
        <el-radio-group v-model="radio">
          <el-radio
            v-for="(item,index) in adressList"
            :key="index"
            :label="item.id"
          >
            {{ `${item.consigneeName} ${item.consigneeMobile} ${item.consigneeDetailAddress}` }}
            <el-button type="text" @click="addressUpdate(item)">修改</el-button>
            <el-button type="text" @click="deleteAddress(item)">删除</el-button>
          </el-radio>
        </el-radio-group>
      </div>
    </div>
    <div class="order-table">
      <div class="order-table-header">确认订单</div>
      <common-table
        key="OrderPage"
        :table-data="orderData"
        :table-columns="orderColumns"
        @row-click="rowClick"
      />
      <div class="order-table-bottom">
        <div class="order-total">
          <div class="span-item">
            <span>商品合计 ：</span><span>¥{{ getBit(orderData.selectedTotalAmounts - orderData.logisticsAmounts) }}</span>
          </div>
          <div class="span-item">
            <span>邮费 ：</span><span>¥{{ orderData.logisticsAmounts }}</span>
          </div>
          <p class="line" />
          <div class="span-item">
            <span>应付总额 ：</span><span class="amount">¥{{ orderData.selectedTotalAmounts }}</span>
          </div>
          <el-button type="danger" @click="toOrderCreate">去付款</el-button>
          <!-- <el-button type="danger" @click="getHtml">获取html</el-button> -->
        </div>
      </div>
    </div>
    <AddressDialog v-if="addressDialog" :address-dialog="addressDialog" :dialog-title="dialogTitle" :address-id="addressId" :user-id="userId" @closeDialog="closeDialog" @loadAddress="loadAddress" />
  </div>
</template>
<script>
import CommonTable from '../components/CommonTable'
import AddressDialog from './AddressDialog.vue'
import { orderCreate, orderCashDesk, cartIndex, getAddressByUserId, deleteAddress } from '@/apis/mall'
import { toCheckPayFn } from '@/utils/index.js'
import { mapActions } from 'vuex'
let _WIN_ = null

export default {
  name: 'OrderPage',
  components: {
    CommonTable,
    AddressDialog
  },
  data() {
    return {
      orderData: { totalCount: 1, list: [{ goodsCode: 1 }, { goodsCode: 2 }], logisticsAmounts: 0, selectedTotalAmounts: 0 },
      orderColumns: [
        {
          isPicture: true,
          label: '商品信息',
          minWidth: 120,
          'show-overflow-tooltip': true
        },
        {
          prop: 'title',
          label: '规格参数',
          minWidth: 120,
          'show-overflow-tooltip': true,
          formatter(row, column, cellValue, index) {
            let result = ''; const tArray = row.specificationPairs || []
            tArray.forEach((item, index) => {
              result += item.propertyName + ':' + item.propertyValue + '; '
            })
            return result
          }
        },
        {
          prop: 'price',
          label: '单价',
          minWidth: 120,
          'show-overflow-tooltip': true,
          formatter(row, column, cellValue, index) {
            return cellValue / 100 + '元'
          }
        },
        {
          prop: 'num',
          label: '数量',
          minWidth: 120,
          'show-overflow-tooltip': true
        },
        {
          prop: 'totalAmounts',
          label: '小计',
          minWidth: 120,
          'show-overflow-tooltip': true,
          formatter(row, column, cellValue, index) {
            return cellValue / 100 + '元'
          }
        }
      ],
      activeNames: ['1'],
      radio: 0,
      adressList: [],
      addressDialog: false,
      dialogTitle: '',
      addressId: '',
      userId: ''
    }
  },
  mounted() {
    this.getCartIndex()
    this.getAddressByUserId()
    this.getCityTree()
  },
  methods: {
    ...mapActions('address', ['getCityTree']),
    rowClick(data) {
      const newLink = this.$router.resolve({ path: 'malldetail', query: { skuId: data.skuId }})
      window.open(newLink.href, '_blank')
    },
    getCartIndex() {
      cartIndex()
        .then(res => {
          this.orderData.list = (res.skus || []).filter(item => item.selected)
          this.orderData.logisticsAmounts = res.logisticsAmounts / 100 || 0
          this.orderData.selectedTotalAmounts = res.selectedTotalAmounts / 100 || 0
        })
    },
    toOrderCreate() {
      const _data = {
        orderChannel: 1,
        orderType: 1,
        skuList: [
          { num: 1, skuId: 8 }
        ],
        userAddressId: this.radio
      }
      _data.skuList = this.orderData.list.map(item => {
        return {
          num: item.num,
          skuId: item.skuId
        }
      })
      _WIN_ = window.open()
      orderCreate(_data)
        .then(res => {
          this.getHtml(res)
        }).catch(_ => {
          if (_WIN_) {
            _WIN_.close()
            _WIN_ = null
          }
        })
    },
    getHtml(orderNo) {
      orderCashDesk({ orderNo })
        .then(res => {
          // 失败情况TODO
          if (_WIN_) {
            _WIN_.close()
            _WIN_ = null
          }
        }).catch((err) => {
          // 成功情况
          if (_WIN_) {
            _WIN_.document.write(err.data)
          }
          toCheckPayFn(orderNo, _WIN_, () => {
            this.$router.push({ name: 'orderInfo', query: { orderNo }})
          })
        })
    },
    // 判断是否为json字符串
    isJSON(str) {
      if (typeof str === 'string') {
        try {
          var obj = JSON.parse(str)
          if (typeof obj === 'object' && obj) {
            return true
          } else {
            return false
          }
        } catch (e) {
          return false
        }
      } else {
        return false
      }
    },
    getBit(value, bit = 2) {
      let str = Number(value)
      str = str.toFixed(bit)
      return str
    },
    openAddress() {
      this.addressDialog = true
      this.dialogTitle = '新增地址'
      this.addressId = ''
    },
    closeDialog() {
      this.addressDialog = false
    },
    // 获取地址列表
    async getAddressByUserId() {
      try {
        const res = await getAddressByUserId({ userId: 1 })
        this.adressList = res
        // 默认选中地址中的第0项没有则赋默认值为0
        if (this.adressList && this.adressList.length > 0) {
          this.radio = this.adressList[0].id
        }
      } catch (error) {
        console.log(error)
      }
    },
    // 地址更新弹窗打开
    addressUpdate(item) {
      this.addressDialog = true
      this.dialogTitle = '编辑地址'
      this.addressId = item.id
      this.userId = item.userId
    },
    // 删除地址
    async deleteAddress(item) {
      try {
        const res = await deleteAddress({ id: item.id })
        this.$message.success('删除成功！')
        this.getAddressByUserId()
      } catch (error) {
        this.$message.error('删除失败！')
      }
    },
    // 新建、编辑地址后更新地址列表
    loadAddress() {
      this.getAddressByUserId()
    }
  }
}
</script>

<style lang="scss" >
@import "~@/assets/styles/common.scss";
.order-page{
  .order-table{
    margin: 20px auto;
    width: 1090px;
    .order-table-header{
      height: 42px;
      line-height: 40px;
      background: #F5F5F5;
      border: 1px solid #DDDDDD;
      padding: 0 22px;
      font-size: 14px;
      font-weight: 400;
      color: #666666;
      .add-address{
        color: #409eff;
        margin-left: 15px;
        cursor: pointer;
        float: right;
      }
    }
    .adress-container{
      background: #ffff;
      border-left: 1px solid #DDDDDD;
      border-right: 1px solid #DDDDDD;
      border-bottom: 1px solid #DDDDDD;
      padding: 22px;
      .el-radio{
        height: 34px;
        line-height: 34px;
        background: #FFFFFF;
        border-radius: 0px 0px 0px 0px;
        display: block;
        // border: 1px solid #FF3C3E;
        &.is-checked{
          .el-radio__label{
            font-size: 14px;
            font-weight: 500;
            color: #333333;
          }
          .el-radio__inner{
            border-color: #FF3C3E;
            background: #FF3C3E;
            font-size: 16px;
          }
        }
        .el-radio__label{
          width: 420px;
          font-size: 12px;
          font-weight: 400;
          color: #333333;
        }
        .el-radio__inner{
          color: #C2C2C2;
          width: 16px;
          height: 16px;
        }
      }
    }
    .common-table{
      border-left: 1px solid #DDDDDD;
      border-right: 1px solid #DDDDDD;
      .el-table__header{
        .cell{
          height: 40px;
          line-height: 40px;
          font-size: 14px;
          color: #666666;
        }
      }
    }
    .order-table-bottom{
      height: 278px;
      background: #fff;
      border-radius: 0px 0px 0px 0px;
      opacity: 1;
      border: 1px solid #E9E8E8;
      padding: 20px 35px;
      margin-bottom: 164px;
      .order-total{
        width: 280px;
        float: right;
        height: 180px;
        .span-item{
          span{
            height: 30px;
            font-size: 14px;
            font-weight: 400;
            color: #3D3D3D;
            line-height: 30px;
            text-align: right;
            display: inline-block;
          }
          span:nth-child(even){
             width: 90px;
          }
          span:nth-child(odd){
            width: 180px;
          }
        }
        .line{
          width: 200px;
          height: 1px;
          background: #D8D8D8;
          float: right;
        }
        .amount{
          font-size: 18px;
          font-family: Source Han Sans CN-Bold, Source Han Sans CN;
          font-weight: bold;
          color: #FF3C3E;
        }
      }
    }
  }
  .el-button--danger{
    width: 170px;
    height: 50px;
    background: #FF3C3E;
    font-size: 18px;
    font-weight: 400;
    color: #FFFFFF;
    float: right;
    margin-top: 18px;

  }
}
.el-dialog {
  text-align: left;
  .el-dialog__header {
    background-color: #f2f3f5;
    padding: 11px 24px;
    font-weight: 600;
    line-height: 22px;
    .el-dialog__title {
      font-size: 14px;
    }
    .el-dialog__headerbtn {
      top: 14px;
    }
  }
  .el-dialog__body {
    padding: 24px;
  }
}
</style>
