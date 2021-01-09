<template>
  <div v-loading="loading" class="app-container">
    <div style="color:red;text-align:center">*注意默认只查询当天的统计数据，如果要查询其它的，请选择范围查询</div>
    <br>
    <!-- 查询条件开始 -->
    <el-card class="cardmargin">
      <el-form ref="queryForm" :model="queryParams" :inline="true" label-width="68px">
        <el-form-item label="选择日期">
          <el-date-picker
            v-model="dateRange"
            size="small"
            style="width:240px"
            value-format="yyyy-MM-dd"
            type="daterange"
            range-separator="-"
            start-placeholde="开始日期"
            end-placeholde="结束日期"
          />
        </el-form-item>
        <el-form-item>
          <el-button type="primary" icon="el-icon-search" size="mini" @click="handleQuery">搜索</el-button>
          <el-button type="primary" icon="el-icon-refresh" size="mini" @click="resetQuery">重置</el-button>
        </el-form-item>
      </el-form>
    </el-card>
    <!-- 查询条件结果 -->
    <el-card class="cardmargin">
      合计收入:￥<span>{{ revenueObj.totalRevenue }}</span>
    </el-card>
    <el-card class="cardmargin">
      收支概况:
      <span class="spancls">总收入￥{{ revenueObj.overview.toll }}</span>
      <span class="spancls spanred">总退费￥{{ revenueObj.overview.refund }}</span>
    </el-card>
    <el-card class="cardmargin">
      收入渠道:
      <span class="spancls">现金支付￥{{ revenueObj.channel.cashIncome }}</span>
      <span class="spancls spanred">现金退费￥{{ revenueObj.channel.cashRefund }}</span>
    </el-card>
    <!-- 图表开始 -->
    <el-card>
      <el-row :gutter="32">
        <el-col :xs="24" :sm="24" :lg="8">
          <div>
            <pie-chart ref="p1" :prop-pie-data="revenueOverview" />
          </div>
        </el-col>
        <el-col :xs="24" :sm="24" :lg="8">
          <div>
            <pie-chart ref="p2" :prop-pie-data="incomeChanel" />
          </div>
        </el-col>
        <el-col :xs="24" :sm="24" :lg="8">
          <div>
            <pie-chart ref="p3" :prop-pie-data="refund" />
          </div>
        </el-col>
      </el-row>
    </el-card>
    <!-- 图表结束 -->

  </div>
</template>

<script>
import PieChart from './components/PieChart'
import { queryAllRevenueData } from '@/api/statistics/revenue'

export default {
  components: {
    PieChart
  },
  data() {
    return {
      // 遮罩
      loading: false,
      // 查询参数
      queryParams: {},
      // 日期范围
      dateRange: undefined,
      // 数组结构
      revenueObj: {
        totalRevenue: 0.00, // 合计收入
        overview: { // 收支概况: 总收入￥0 总退费￥0
          toll: 0.00,
          refund: 0.00
        },
        channel: { // 收入渠道: 现金支付￥0 支付宝支付￥0 现金退费￥0 支付宝退费￥0
          cashIncome: 0.00,
          cashRefund: 0.00
        }
      },
      // 声明图表的数据
      revenueOverview: { // 收支概况
        title: '收支概况',
        data: [
          { value: 320, name: '收费金额' },
          { value: 240, name: '退费金额' }
        ]
      },
      incomeChanel: { // 收入渠道
        title: '收入渠道',
        data: [
          { value: 4, name: '现金收入' },
          { value: 1, name: '支付宝收入' }
        ]
      },
      refund: { // 退款金额和渠道
        title: '退款',
        data: [
          { value: 200, name: '现金退费' },
          { value: 100, name: '支付宝退费' }
        ]
      }
    }
  },
  created() {
    this.loadData()
  },
  methods: {
    // 查询数据
    loadData() {
      this.loading = true
      queryAllRevenueData(this.addDateRange(this.queryParams, this.dateRange)).then(res => {
        this.revenueObj = res.data.revenueObj
        this.revenueOverview = res.data.revenueOverview
        this.incomeChanel = res.data.incomeChanel
        this.refund = res.data.refund
        this.loading = false
      }).catch(() => {
        this.msgError('查询失败')
        this.loading = false
      })
    },
    // 条件查询
    handleQuery() {
      this.loadData()
    },
    // 重置查询条件
    resetQuery() {
      this.resetForm('queryForm')
      this.dateRange = []
      this.loadData()
    }
  }
}
</script>
<style  scoped>
  .cardmargin{
    margin-bottom: 3px;
  }
  .spancls{
    display: inline-block;
    margin-left: 80px;
  }
  .spanred{
    color: red;
  }
</style>
