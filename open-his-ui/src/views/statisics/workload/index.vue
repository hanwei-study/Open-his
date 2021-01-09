<template>
  <div class="app-container">
    <div style="color:red;text-align:center">*注意默认只查询当天的统计数据，如果要查询其它的，请选择范围查询</div>
    <br>
    <el-tabs v-model="activeName" :stretch="true" @tab-click="handleClick">
      <el-tab-pane label="医生工作量统计列表" name="workload">
        <!-- 查询条件开始 -->
        <el-form ref="queryWorkloadFrom" :model="queryWorkloadParams" :inline="true" label-width="68px">
          <el-form-item label="医生名称" prop="doctorName">
            <el-input
              v-model="queryWorkloadParams.doctorName"
              placeholder="请输入医生名称"
              clearable
              size="small"
              style="width:180px"
            />
          </el-form-item>
          <el-form-item label="接诊时间">
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
            <el-button type="primary" icon="el-icon-search" size="mini" @click="handleWorkloadQuery">搜索</el-button>
            <el-button type="primary" icon="el-icon-refresh" size="mini" @click="resetWorkloadQuery">重置</el-button>
          </el-form-item>
        </el-form>
        <!-- 查询条件结束 -->
        <!-- 数据表格开始 -->
        <el-table v-loading="loading" border :data="workloadList" show-summary>
          <el-table-column label="挂号ID" prop="regId" align="center" />
          <el-table-column label="医生编号" prop="userId" align="center" />
          <el-table-column label="医生姓名" prop="doctorName" align="center" />
          <el-table-column label="挂号费用" prop="regAmount" align="center" />
          <el-table-column label="患者姓名" prop="patientName" align="center" />
          <el-table-column label="就诊时间" prop="visitDate" align="center" />
        </el-table>
        <!-- 数据结束结束 -->

      </el-tab-pane>
      <el-tab-pane label="总体工作量统计列表" name="workloadStat">
        <!-- 查询条件开始 -->
        <el-form ref="queryWorkloadStatFrom" :model="queryWorkloadStatParams" :inline="true" label-width="68px">
          <el-form-item label="医生姓名" prop="doctorName">
            <el-input
              v-model="queryWorkloadStatParams.doctorName"
              placeholder="请输入医生姓名"
              clearable
              size="small"
              style="width:180px"
            />
          </el-form-item>
          <el-form-item label="接诊时间">
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
            <el-button type="primary" icon="el-icon-search" size="mini" @click="handleWorkloadStatQuery">搜索</el-button>
            <el-button type="primary" icon="el-icon-refresh" size="mini" @click="resetWorkloadStatQuery">重置</el-button>
          </el-form-item>
        </el-form>
        <!-- 查询条件结束 -->
        <!-- 数据表格开始 -->
        <el-table v-loading="loading" border :data="workloadStatList" show-summary>
          <el-table-column label="医生编号" prop="userId" align="center" />
          <el-table-column label="医生姓名" prop="doctorName" align="center" />
          <el-table-column label="挂号总金额" prop="totalAmount" align="center" />
          <el-table-column label="接诊数量" prop="count" align="center" />
        </el-table>
        <!-- 数据结束结束 -->
      </el-tab-pane>
    </el-tabs>
  </div>
</template>

<script>
import { queryWorkload, queryWorkloadStat } from '@/api/statistics/workload'
export default {
  data() {
    return {
      // 遮罩
      loading: false,
      // 被激活的tab名称
      activeName: 'workload',
      // 工作量总工作量数据
      workloadList: [],
      // 工作量的工作量数量数据
      workloadStatList: [],
      // 日期范围
      dateRange: [],
      // 列表查询参数
      queryWorkloadParams: {
        doctorName: undefined
      },
      // 数量统计查询参数
      queryWorkloadStatParams: {
        doctorName: undefined
      }
    }
  },
  created() {
    // 默认查询工作量列表
    this.listWorkload()
  },
  methods: {
    // 点击tab事件
    handleClick() {
      console.log(this.activeName)
      if (this.activeName === 'workload') {
        this.listWorkload()
      } else {
        this.listWorkloadStat()
      }
    },
    // 查询工作量列表
    listWorkload() {
      this.loading = true
      queryWorkload(this.addDateRange(this.queryWorkloadParams, this.dateRange)).then(res => {
        this.workloadList = res.data
        this.loading = false
      }).catch(() => {
        this.msgError('查询失败')
        this.loading = false
      })
    },
    // 工作量列表查询
    handleWorkloadQuery() {
      this.listWorkload()
    },
    // 重置工作量列表查询
    resetWorkloadQuery() {
      this.resetForm('queryWorkloadFrom')
      this.dateRange = []
      this.listWorkload()
    },
    // 查询工作量列表
    listWorkloadStat() {
      this.loading = true
      queryWorkloadStat(this.addDateRange(this.queryWorkloadStatParams, this.dateRange)).then(res => {
        this.workloadStatList = res.data
        this.loading = false
      }).catch(() => {
        this.msgError('查询失败')
        this.loading = false
      })
    },
    // 工作量列表查询
    handleWorkloadStatQuery() {
      this.listWorkloadStat()
    },
    // 重置工作量列表查询
    resetWorkloadStatQuery() {
      this.resetForm('queryWorkloadStatFrom')
      this.dateRange = []
      this.listWorkloadStat()
    }
  }
}
</script>
