<template>
  <div class="app-container">
    <!-- 查询条件开始 -->
    <el-card style="margin-bottom:3px">
      <el-form ref="queryForm" :model="queryParams" label-width="68px">
        <el-form-item label="检查项目" prop="checkItemIds">
          <el-checkbox v-model="checkAll" :indeterminate="isIndeterminate" @change="handleCheckAllChange">全选</el-checkbox>
          <div style="margin: 15px 0;" />
          <el-checkbox-group v-model="queryParams.checkItemIds" @change="handleCheckedItemChange">
            <el-checkbox
              v-for="d in checkItemOptions"
              :key="d.checkItemId"
              :label="d.checkItemId"
              :value="d.checkItemId"
            >{{ d.checkItemName }}</el-checkbox>
          </el-checkbox-group>
        </el-form-item>
        <el-form-item label="挂号单号" prop="regId">
          <el-input
            v-model="queryParams.regId"
            placeholder="请输入挂号单号"
            clearable
            size="small"
            style="width:380px"
          />
          <el-button type="primary" icon="el-icon-search" size="mini" @click="handleQuery">搜索</el-button>
          <el-button type="primary" icon="el-icon-refresh" size="mini" @click="resetQuery">重置</el-button>
        </el-form-item>
      </el-form>
    </el-card>
    <!-- 查询条件结束 -->
    <!-- 数据表格开始 -->
    <el-card style="margin-bottom:3px">
      <el-table
        ref="refTable"
        v-loading="loading"
        border
        highlight-current-row
        :data="careOrderItems"
        @current-change="handleCurrentChange"
      >
        <el-table-column label="序号" align="center" type="index" width="50" />
        <el-table-column label="详情ID" align="center" width="300" prop="itemId" />
        <el-table-column label="项目名称" align="center" prop="itemName" />
        <el-table-column label="数量" align="center" prop="num" />
        <el-table-column label="单价(元)" align="center" prop="price" />
        <el-table-column label="金额(元)" align="center" prop="amount" />
        <el-table-column label="备注" align="center" prop="remark" />
        <el-table-column label="状态" align="center" prop="status" :formatter="statusFormatter" />
      </el-table>
    </el-card>
    <!-- 数据表格结束 -->
    <!-- 开始检查开始 -->
    <el-card style="margin-bottom:3px">
      <div v-if="showBottom" v-loading="bottomLoading">
        <el-form label-position="left" label-width="120px" inline class="demo-table-expand">
          <el-form-item label="挂号单号:">
            <span>{{ careHistoryObj.regId }}</span>
          </el-form-item>
          <el-form-item label="患者姓名:">
            <span>{{ careOrderObj.patientName }}</span>
          </el-form-item>
          <el-form-item label="检查项目:">
            <span>{{ careOrderItemObj.itemName }}</span>
          </el-form-item>
          <el-form-item label="检查备注:">
            <span>{{ careOrderItemObj.remark }}</span>
          </el-form-item>
        </el-form>
        <div>
          <el-button type="primary" style="width:100%" icon="el-icon-plus" @click="handleStartCheck">开始检查</el-button>
        </div>
      </div>
      <div v-else style="text-align:center">
        暂无数据
      </div>
    </el-card>
    <!-- 开始检查结束 -->

  </div>
</template>
<script>
import { selectAllCheckItem } from '@/api/system/checkItem'
import { queryNeedCheckItem, queryCheckItemByItemId, startCheck } from '@/api/doctor/checkResult'
export default {
  data() {
    return {
      // 遮罩
      loading: false,
      // 检查项目数据
      checkItemOptions: [],
      // 查询参数
      queryParams: {
        regId: undefined,
        checkItemIds: []
      },
      // 是否全选
      checkAll: true,
      // 是否为半选状态
      isIndeterminate: false,
      // 处方详情的表格数据
      careOrderItems: [],
      // 当前选中的行
      currentRow: undefined,
      // 处方详情的状态字典
      statusOptions: [],
      // 是否显示开始检查的card
      showBottom: false,
      // 下面的开始card的遮罩
      bottomLoading: false,
      // 详情对象
      careOrderItemObj: {},
      // 处方对象
      careOrderObj: {},
      // 病历对象
      careHistoryObj: {}

    }
  },
  created() {
    // 加载所有可用的检查项目
    selectAllCheckItem().then(res => {
      this.checkItemOptions = res.data
      this.queryParams.checkItemIds = this.checkItemOptions.map(item => item.checkItemId)
      this.queryNeedCheckItem()
    })
    // 加载状态的字典数据
    this.getDataByType('his_order_details_status').then(res => {
      this.statusOptions = res.data
    })
  },
  methods: {
    // 全选
    handleCheckAllChange(val) {
      this.queryParams.checkItemIds = val ? this.checkItemOptions.map(item => item.checkItemId) : []
      this.isIndeterminate = false
    },
    // 选择某一个项目
    handleCheckedItemChange(value) {
      const checkedCount = value.length
      this.checkAll = checkedCount === this.checkItemOptions.length
      this.isIndeterminate = checkedCount > 0 && checkedCount < this.checkItemOptions.length
    },

    // 翻译处方详情状态
    statusFormatter(row) {
      return this.selectDictLabel(this.statusOptions, row.status)
    },
    // 根据条件查询已支付的检查项目
    queryNeedCheckItem() {
      this.loading = true
      queryNeedCheckItem(this.queryParams).then(res => {
        this.careOrderItems = res.data
        this.loading = false
      }).catch(() => {
        this.msgError('查询失败')
        this.loading = false
      })
    },
    // 选中某一行之后
    handleCurrentChange(val) {
      this.currentRow = val
      if (val !== null) {
        this.bottomLoading = true
        queryCheckItemByItemId(this.currentRow.itemId).then(res => {
          this.careOrderItemObj = res.data.item
          this.careOrderObj = res.data.careOrder
          this.careHistoryObj = res.data.careHistory
          this.showBottom = true
          this.bottomLoading = false
        }).catch(() => {
          this.msgError('加载失败')
          this.bottomLoading = false
        })
      }
    },
    // 查询
    handleQuery() {
      this.queryNeedCheckItem()
      this.showBottom = false
    },
    // 重置
    resetQuery() {
      this.resetForm('queryForm')
      this.showBottom = false
      this.queryParams.checkItemIds = this.checkItemOptions.map(item => item.checkItemId)
      this.queryNeedCheckItem()
    },
    // 开始检查
    handleStartCheck() {
      const tx = this
      tx.$confirm('是否确定开始检查?', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(() => {
        startCheck(this.careOrderItemObj.itemId).then(res => {
          tx.msgSuccess('开始检查成功')
          tx.queryNeedCheckItem()// 重新查询
          tx.showBottom = false
        }).catch(() => {
          tx.msgError('开始检查失败')
          tx.loading = false
        })
      }).catch(() => {
        tx.msgError('开始已取消')
        tx.loading = false
      })
    }
  }
}
</script>
<style scoped>
  .demo-table-expand {
    font-size: 0;
  }
  .demo-table-expand label {
    width: 90px;
    color: #99a9bf;
  }
  .demo-table-expand .el-form-item {
    margin-right: 0;
    margin-bottom: 0;
    width: 50%;
  }
</style>
