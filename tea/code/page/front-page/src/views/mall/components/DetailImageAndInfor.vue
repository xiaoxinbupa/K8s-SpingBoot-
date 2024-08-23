<template>
  <div class="introduction">
    <div class="intro-img">
      <el-image
        v-for="(item, i) in detailData.attachList"
        v-show="currentImgIndex==i"
        :key="i"
        style="border:1px solid #EEE"
        :src="item"
        alt=""
        fit="contain"
      />
      <div class="thumbnail">
        <el-image
          v-for="(item, i) in detailData.attachList"
          :key="i"
          :src="item"
          fit="contain"
          alt=""
          :class="{img:true, active:i==currentImgIndex}"
          @mouseover="currentImgIndex=i"
        />
      </div>
    </div>
    <div class="intro-text">
      <!-- 标题部分 -->
      <div class="title">
        <p>{{ detailData.title }}</p>
        <span>月销5000+</span>
      </div>
      <!-- 价格信息部分 -->
      <div class="price">
        <div class="row">
          <p class="label">价格</p>
          <p class="content red">
            ￥<span class="val">{{ (detailData.listPrice/100).toFixed(2) }}</span>
          </p>
        </div>
        <div class="row">
          <p class="label">优惠券</p>
          <p class="content">
            <span class="coupon-tag" :style="`background-image: url(${require('@/assets/images/tags_coupon.png')});`">满130享9折</span>
            <span class="coupon-tag" :style="`background-image: url(${require('@/assets/images/tags_coupon.png')});`">满300享8折</span>
            <span class="coupon-tag" :style="`background-image: url(${require('@/assets/images/tags_coupon.png')});`">满600享7折</span>
          </p>
        </div>
        <div class="row">
          <p class="label">促销</p>
          <p class="content">
            <span class="tag">多买优惠</span>
            <span class="text">满2件减15.00元；满3件减30元</span>
          </p>
        </div>

      </div>
      <!-- 配置信息 -->
      <div class="sku">
        <div class="item">
          <span class="label">数量：</span>
          <div class="tags">
            <el-input-number
              v-model="count"
              :min="1"
              :max="999"
              :precision="0"
            />
          </div>
        </div>

        <div class="item">
          <div class="tags" @click="addCarts()">
            <el-button
              class="primary"
              icon="el-icon-shopping-cart-1"
            >加入购物车</el-button>
          </div>
        </div>
      </div>
    </div>
  </div>

</template>

<script>
import { addCart } from '@/apis/mall'
export default {
  props: {
    detailData: {
      type: Object,
      default: () => { }
    }
  },
  data() {
    return {
      currentImgIndex: 0, // 当前图片下标
      count: 1
    }
  },
  methods: {
    // 商品加入购物车
    async addCarts() {
      try {
        const res = await addCart({ num: this.count, skuId: this.detailData.id })
        this.$router.push({
          path: '/carpage'
        })
      } catch (error) {
        console.log(error)
      }
    }
  }
}
</script>

<style>
.introduction{
  display: flex;
}
.intro-img {
  width: 428px;
  height: 520px;
}
.intro-img .el-image {
  width: 428px;
  height: 428px;
}
.thumbnail {
  display: flex;
  flex-direction: row;
  margin-top: 16px;
}
.thumbnail .img {
  width: 76px;
  height: 76px;
  margin-right: 12px;
  flex-shrink: 0;
  border: 2px solid trasparent;
}
.intro-img .thumbnail .img.active {
  border: 2px solid #444;
}
.introduction .intro-text {
  margin-left: 41px;
  width: 100%;
}

.introduction .intro-text .title p{
  font-size: 20px;
  font-weight: bold;
  margin: 10px 0px 5px 0px;
}

.introduction .intro-text .title span{
  font-size: 14px;
  color: #999;
}

.introduction .intro-text .price {
  width: 100%;
  background-color: #f3f4f3;
  height: 124px;
  margin-top: 21px;
}

.introduction .intro-text .price .row {
  display: flex;
  flex-direction: row;
  padding: 8px 10px;
  align-items: center;
}

.introduction .intro-text .price .row .label {
  width: 41px;
  height: 20px;
  font-size: 13px;
  color: #666;
  text-align: left;
  display: flex;
  align-items: center;
  margin: 0px 5px;
}

.introduction .intro-text .price .row p {
  margin: 0px;
  padding: 0px;
}

.introduction .intro-text .price .row .content .val {
  font-size: 28px;
}

.introduction .intro-text .price .row .content.red {
  color: #ff3c3e;
}

.introduction .intro-text .price .row .content .coupon-tag {
  color: #ff3c3e;
  font-size: 12px;
  background-size: 100% 100%;
  background-repeat: no-repeat;
  padding: 2px 5px;
  margin-right: 5px;
}

.introduction .intro-text .price .row .content .tag {
  color: #ff3c3e;
  border: 1px solid #ff3c3e;
  background-color: #F5F2EE;
  font-size: 12px;
  margin-right: 5px;
  padding: 2px 3px;
  cursor: pointer;
  user-select: none;
}

.introduction .intro-text .price .row .content .text {
  color: #666666;
  font-size: 12px;
}

.introduction .intro-text .sku .item {
  margin-top: 24px;
  display: flex;
  align-items: flex-start;
  font-size: 13px;
}

.introduction .intro-text .sku .item .label {
  min-width: 80px;
  height: 30px;
  text-align: left;
  line-height: 30px;
}

.introduction .intro-text .sku .item .tags .tag {
  display: inline-block;
  min-width: 64px;
  height: 30px;
  border: 1px solid #DDD;
  text-align: center;
  margin:0px 8px 5px 0px;
  line-height: 30px;
  cursor: pointer;
  user-select: none;
}

.introduction .intro-text .sku .item .tags .tag.active {
  border: 1px solid #ff3c3e;
  color: #ff3c3e;
}

.introduction .intro-text .sku .item .tags button.default{
  border: 1px solid #ff3c3e;
  border-radius: 0px;
  color: #ff3c3e;
  width: 150px;
  padding: 15px;
}

.introduction .intro-text .sku .item .tags button.default:hover{
  background-color: #fff;
}

.introduction .intro-text .sku .item .tags button.primary{
  border: 1px solid #ff3c3e;
  border-radius: 0px;
  color: #fff;
  width: 150px;
  background-color: #ff3c3e;
  padding: 15px;

}

.introduction .intro-text .sku .item .tags button.primary:hover{
  background-color: #ff3c3e;
}
</style>
