<template>
  <div
    v-loading="loading"
    class="app-container"
    :element-loading-text="loadingText"
  >
    <!-- 挂号编号的查询条件开始 -->
    <el-form ref="queryForm" label-width="88px">
      <el-form-item label="挂号单ID" prop="regId">
        <el-input
          v-model="regId"
          clearable
          placeholder="请输入挂号单号"
          size="small"
          style="width:300px"
        />
        <el-button type="primary" icon="el-icon-search" size="mini" @click="handleQuery">查询</el-button>
        <el-button type="primary" icon="el-icon-search" size="mini" @click="handleQuery">读取IC卡</el-button>
      </el-form-item>
    </el-form>
    <!-- 挂号编号的查询条件结束 -->
    <!-- 挂号输入框下面的所有内容开始 -->
    <div v-if="careHistory.regId!==undefined">
      <!-- 病例信息开始 -->
      <el-card style="margin-bottom: 5px">
        <el-form label-position="right" label-width="120px" inline class="demo-table-expand">
          <el-form-item label="挂号单号:">
            <span v-text="careHistory.regId" />
          </el-form-item>
          <el-form-item label="医生姓名:">
            <span v-text="careHistory.userName" />
          </el-form-item>
          <el-form-item label="科室名称:">
            <span v-text="careHistory.deptName" />
          </el-form-item>
          <el-form-item label=" 患者姓名:">
            <span v-text="careHistory.patientName" />
          </el-form-item>
          <el-form-item label="就诊时间:">
            <span v-text="careHistory.careDate" />
          </el-form-item>
          <el-form-item label="主诉:">
            <span v-text="careHistory.caseTitle" />
          </el-form-item>
          <el-form-item label="诊断信息:">
            <span v-text="careHistory.caseResult" />
          </el-form-item>
          <el-form-item label="医生建议:">
            <span v-text="careHistory.doctorTips" />
          </el-form-item>
        </el-form>
      </el-card>
      <!-- 病例信息结束 -->
      <!-- 工具按钮开始 -->
      <el-card style="margin-bottom: 5px">
        <el-button type="success" icon="el-icon-finished" @click="handleSelectAll">全选</el-button>
        <el-button type="success" icon="el-icon-finished" @click="handleUnSelectAll">取消全选</el-button>
        <el-button type="danger" icon="el-icon-bank-card" @click="handleDoMedicine">发药</el-button>
      </el-card>
      <!-- 工具结束开始 -->
      <!-- 未支付的处方信息开始 -->
      <el-card style="margin-bottom: 5px">
        <el-collapse v-if="careOrders.length>0" v-model="activeNames">
          <el-collapse-item v-for="(item,index) in careOrders" :key="index" :name="index">
            <template slot="title">
              【药用处方】【{{ index+1 }}】【处方总额】:￥{{ item.allAmount }}
            </template>
            <el-table
              ref="refTable"
              v-loading="loading"
              border
              :data="item.careOrderItems"
              :row-class-name="tableRowClassName"
              @selection-change="handleCareOrderItemSelectionChange($event,item.coId)"
            >
              <el-table-column type="selection" width="55" align="center" />
              <el-table-column label="序号" align="center" type="index" width="50" />
              <el-table-column label="药品名称" align="center" prop="itemName" />
              <el-table-column label="数量" align="center" prop="num" />
              <el-table-column label="单价(元)" align="center" prop="price" />
              <el-table-column label="金额(元)" align="center" prop="amount" />
              <el-table-column label="备注" align="center" prop="remark" />
              <el-table-column label="状态" align="center" prop="status" :formatter="statusFormatter" />
            </el-table>
          </el-collapse-item>
        </el-collapse>
      </el-card>
      <!-- 未支付的处方信息结束 -->
      <!-- 挂号输入框下面的所有内容结束 -->
    </div>
    <div v-else style="text-align:center">请输入正确的挂号单ID查询</div>
  </div>
</template>
<script>
import { getChargedCareHistoryOnlyMedicinesByRegId, doMedicine } from '@/api/doctor/handleMedicine'
export default {
  data() {
    return {
      // 整体遮罩
      loading: false,
      // 设置遮罩层的文字
      loadingText: '',
      // 用户输入的挂号单号
      regId: undefined,
      // 病例对象
      careHistory: {},
      // 处方信息
      careOrders: [],
      // 处方详情的状态字典
      statusOptions: [],
      // 当前选中的所有项目集合
      itemObjs: [],
      // 展开的折叠面板的名字
      activeNames: []
    }
  },
  created() {
    // 加载状态的字典数据
    this.getDataByType('his_order_details_status').then(res => {
      this.statusOptions = res.data
    })
  },
  methods: {
    // 查询
    handleQuery() {
      if (this.regId === '') {
        this.msgError('请输入挂号单号')
        this.resetCurrentParams()
        return
      }
      this.careHistory = {}
      this.careOrders = []
      this.itemObjs = []
      this.loading = true
      this.loadingText = '数据查询中，请稍后...'
      getChargedCareHistoryOnlyMedicinesByRegId(this.regId).then(res => {
        this.careHistory = res.data.careHistory
        this.careOrders = res.data.careOrders
        this.loading = false
        this.loadingText = ''
        this.careOrders.filter((c, index) => {
          this.activeNames.push(index)
        })
      }).catch(() => {
        this.msgError('查询失败')
        this.loading = false
        this.loadingText = ''
      })
    },
    // 翻译处方详情状态
    statusFormatter(row) {
      return this.selectDictLabel(this.statusOptions, row.status)
    },
    // 监听多个表格的checkbox的选中事件
    handleCareOrderItemSelectionChange(selection, coId) {
      if (this.itemObjs.length === 0) {
        this.itemObjs = selection
      } else {
        for (let i = 0; i < this.itemObjs.length; i++) {
          const obj = this.itemObjs[i]
          if (obj.coId === coId) {
            this.itemObjs.splice(i, 1)
            i--
          }
        }
        // 最后再来添加
        selection.filter(newItem => {
          this.itemObjs.push(newItem)
        })
      }
    },
    tableRowClassName({ row, rowIndex }) {
      row.index = rowIndex
    },
    // 全选择
    handleSelectAll() { // 先取消再反选择
      const tables = this.$refs.refTable
      tables.filter(t => {
        t.clearSelection()
        t.toggleAllSelection() // 注意不要用错方法了
      })
    },
    // 取消全选
    handleUnSelectAll() {
      const tables = this.$refs.refTable
      tables.filter(t => {
        t.clearSelection()
      })
    },
    // 现金支付
    handleDoMedicine() {
      if (!this.careHistory.regId) {
        this.msgError('请输入挂号单ID查询')
        return
      } else if (this.itemObjs.length === 0) {
        this.msgError('请选择要发药的药品名')
        return
      } else {
        // 组装数据
        const itemIds = this.itemObjs.map(item => item.itemId)

        // 发送请求
        this.loading = true
        this.loadingText = '发药中。。。'
        this.$confirm('是否确定发药?', '提示', {
          confirmButtonText: '确定',
          cancelButtonText: '取消',
          type: 'warning'
        }).then(() => {
          doMedicine(itemIds).then(res => {
            this.msgSuccess('全部药品发放成功')
            this.resetCurrentParams()
            // this.handleQuery()
            this.loading = false
          }).catch(() => {
            this.msgError('发放失败原因：看右上角')
            this.loading = false
          })
        }).catch(() => {
          this.msgError('发药已取消')
          this.loading = false
        })
      }
    },
    // 清空careHistory和careOrders
    resetCurrentParams() {
      this.careHistory = {}
      this.careOrders = []
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
