<template>
  <div class="app-container">
    <div style="color:red;text-align:center">*注意默认只查询当天的统计数据，如果要查询其它的，请选择范围查询</div>
    <br>
    <el-tabs v-model="activeName" :stretch="true" @tab-click="handleClick">
      <el-tab-pane label="药品销售列表" name="drug">
        <!-- 查询条件开始 -->
        <el-form ref="queryDrugFrom" :model="queryDrugParams" :inline="true" label-width="68px">
          <el-form-item label="药品名称" prop="drugName">
            <el-input
              v-model="queryDrugParams.drugName"
              placeholder="请输入药品名称"
              clearable
              size="small"
              style="width:180px"
            />
          </el-form-item>
          <el-form-item label="交易时间">
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
            <el-button type="primary" icon="el-icon-search" size="mini" @click="handleDrugQuery">搜索</el-button>
            <el-button type="primary" icon="el-icon-refresh" size="mini" @click="resetDrugQuery">重置</el-button>
          </el-form-item>
        </el-form>
        <!-- 查询条件结束 -->
        <!-- 数据表格开始 -->
        <el-table v-loading="loading" border :data="drugList" show-summary>
          <el-table-column label="药品ID" prop="medicinesId" align="center" />
          <el-table-column label="药品名称" prop="medicinesName" align="center" />
          <el-table-column label="销售价格" prop="price" align="center" />
          <el-table-column label="交易数量" prop="num" align="center" />
          <el-table-column label="交易总价" prop="amount" align="center" />
          <el-table-column label="创建时间" prop="createTime" align="center" />
        </el-table>
        <!-- 数据结束结束 -->

      </el-tab-pane>
      <el-tab-pane label="药品销售统计列表" name="drugStat">
        <!-- 查询条件开始 -->
        <el-form ref="queryDrugStatFrom" :model="queryDrugStatParams" :inline="true" label-width="68px">
          <el-form-item label="药品名称" prop="drugName">
            <el-input
              v-model="queryDrugStatParams.drugName"
              placeholder="请输入药品名称"
              clearable
              size="small"
              style="width:180px"
            />
          </el-form-item>
          <el-form-item label="交易时间">
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
            <el-button type="primary" icon="el-icon-search" size="mini" @click="handleDrugStatQuery">搜索</el-button>
            <el-button type="primary" icon="el-icon-refresh" size="mini" @click="resetDrugStatQuery">重置</el-button>
          </el-form-item>
        </el-form>
        <!-- 查询条件结束 -->
        <!-- 数据表格开始 -->
        <el-table v-loading="loading" border :data="drugStatList" show-summary>
          <el-table-column label="药品ID" prop="medicinesId" align="center" />
          <el-table-column label="药品名称" prop="medicinesName" align="center" />
          <el-table-column label="总金额" prop="totalAmount" align="center" />
          <el-table-column label="销售数量" prop="count" align="center" />
        </el-table>
        <!-- 数据结束结束 -->
      </el-tab-pane>
    </el-tabs>
  </div>
</template>

<script>
import { queryDrug, queryDrugStat } from '@/api/statistics/drug'
export default {
  data() {
    return {
      // 遮罩
      loading: false,
      // 被激活的tab名称
      activeName: 'drug',
      // 药品总销售数据
      drugList: [],
      // 药品的销售数量数据
      drugStatList: [],
      // 日期范围
      dateRange: [],
      // 列表查询参数
      queryDrugParams: {
        drugName: undefined
      },
      // 数量统计查询参数
      queryDrugStatParams: {
        drugName: undefined
      }
    }
  },
  created() {
    // 默认查询销售列表
    this.listDrug()
  },
  methods: {
    // 点击tab事件
    handleClick() {
      console.log(this.activeName)
      if (this.activeName === 'drug') {
        this.listDrug()
      } else {
        this.listDrugStat()
      }
    },
    // 查询销售列表
    listDrug() {
      this.loading = true
      queryDrug(this.addDateRange(this.queryDrugParams, this.dateRange)).then(res => {
        this.drugList = res.data
        this.loading = false
      }).catch(() => {
        this.msgError('查询失败')
        this.loading = false
      })
    },
    // 销售列表查询
    handleDrugQuery() {
      this.listDrug()
    },
    // 重置销售列表查询
    resetDrugQuery() {
      this.resetForm('queryDrugFrom')
      this.dateRange = []
      this.listDrug()
    },
    // 查询销售列表
    listDrugStat() {
      this.loading = true
      queryDrugStat(this.addDateRange(this.queryDrugStatParams, this.dateRange)).then(res => {
        this.drugStatList = res.data
        this.loading = false
      }).catch(() => {
        this.msgError('查询失败')
        this.loading = false
      })
    },
    // 销售列表查询
    handleDrugStatQuery() {
      this.listDrugStat()
    },
    // 重置销售列表查询
    resetDrugStatQuery() {
      this.resetForm('queryDrugStatFrom')
      this.dateRange = []
      this.listDrugStat()
    }
  }
}
</script>
