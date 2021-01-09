<template>
  <div class="app-container">
    <!-- 查询条件开始 -->
    <el-card style="margin-bottom:3px">
      <el-form ref="queryForm" :model="queryParams" :inline="true" label-width="68px">
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
        </el-form-item>
        <el-form-item label="患者姓名" prop="patientName">
          <el-input
            v-model="queryParams.patientName"
            placeholder="请输入患者姓名"
            clearable
            size="small"
            style="width:380px"
          />
        </el-form-item>
        <el-form-item>
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
        :data="checkResultList"
      >
        <el-table-column type="expand">
          <template slot-scope="props">
            <el-form label-position="left" inline class="demo-table-expand">
              <el-form-item label="价格">
                <span>{{ props.row.price }}</span>
              </el-form-item>
              <el-form-item label="创建时间">
                <span>{{ props.row.createTime }}</span>
              </el-form-item>
              <el-form-item label="检查结果描述">
                <span>{{ props.row.resultMsg }}</span>
              </el-form-item>
            </el-form>
          </template>
        </el-table-column>
        <el-table-column label="检查单位" align="center" prop="itemId" width="250" />
        <el-table-column label="挂号单号" align="center" width="300" prop="regId" />
        <el-table-column label="项目名称" align="center" prop="checkItemName" />
        <el-table-column label="患者姓名" align="center" prop="patientName" />
        <el-table-column label="检查状态" align="center" prop="resultStatus" :formatter="resultStatusFormatter" />
        <el-table-column label="操作" align="center">
          <template slot-scope="scope">
            <el-button type="text" :disabled="scope.row.resultStatus==='0'" icon="el-icon-view" size="mini" @click="handleViewResult(scope.row)">查看结果</el-button>
          </template>
        </el-table-column>
      </el-table>
      <!-- 分页控件开始 -->
      <el-pagination
        v-show="total>0"
        :current-page="queryParams.pageNum"
        :page-sizes="[5, 10, 20, 30]"
        :page-size="queryParams.pageSize"
        layout="total, sizes, prev, pager, next, jumper"
        :total="total"
        @size-change="handleSizeChange"
        @current-change="handleCurrentChange"
      />
    <!-- 分页控件结束 -->
    </el-card>
    <!-- 数据表格结束 -->
    <!-- 录入检查结果弹层开始 -->
    <el-dialog
      :title="title"
      :visible.sync="open"
      width="1000px"
      center
      append-to-body
    >
      <el-form ref="form" label-width="80px">
        <el-form-item label="检查结果" prop="resultMsg">
          {{ currentResult.resultMsg }}
        </el-form-item>
        <div v-for="(item,index) in currentResult.resultImg" :key="index">
          <img :src="item.url">
        </div>
      </el-form>
      <span slot="footer" class="dialog-footer">
        <el-button @click="cancel">取 消</el-button>
      </span>
    </el-dialog>
    <!-- 录入检查结果弹层开始 -->

  </div>
</template>
<script>
import { selectAllCheckItem } from '@/api/system/checkItem'
import { queryAllCheckResultForPage } from '@/api/doctor/checkResult'

export default {
  data() {
    return {
      // 遮罩
      loading: false,
      // 总条数
      total: 0,
      // 检查项目数据
      checkItemOptions: [],
      // 查询参数
      queryParams: {
        regId: undefined,
        patientName: undefined,
        checkItemIds: [],
        pageNum: 1,
        pageSize: 10
      },
      // 是否全选
      checkAll: true,
      // 是否为半选状态
      isIndeterminate: false,
      // 检查结果的状态字典
      resultStatusOptions: [],
      // 检查结果的数据
      checkResultList: [],
      // 是否打开录入结果的弹出层
      open: false,
      // 弹出层的标题
      title: '',
      // 当前查看的数据
      currentResult: {
        resultMsg: undefined,
        resultImg: []
      }
    }
  },
  created() {
    // 加载所有可用的检查项目
    selectAllCheckItem().then(res => {
      this.checkItemOptions = res.data
      this.queryParams.checkItemIds = this.checkItemOptions.map(item => item.checkItemId)
      this.queryAllCheckResultForPage()
    })
    // 加载状态的字典数据
    this.getDataByType('his_check_result_status').then(res => {
      this.resultStatusOptions = res.data
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
      this.queryAllCheckResultForPage()
    },

    // 翻译处方详情状态
    resultStatusFormatter(row) {
      return this.selectDictLabel(this.resultStatusOptions, row.resultStatus)
    },
    // 根据条件查询检查中的检查项目
    queryAllCheckResultForPage() {
      this.loading = true
      queryAllCheckResultForPage(this.queryParams).then(res => {
        this.checkResultList = res.data
        this.loading = false
        this.total = res.total
        console.log(this.total)
      }).catch(() => {
        this.msgError('查询失败')
        this.loading = false
      })
    },
    // 分页pageSize变化时触发
    handleSizeChange(val) {
      this.queryParams.pageSize = val
      // 重新查询
      this.queryAllCheckResultForPage()
    },
    // 点击上一页  下一页，跳转到哪一页面时触发
    handleCurrentChange(val) {
      this.queryParams.pageNum = val
      // 重新查询
      this.queryAllCheckResultForPage()
    },
    // 查询
    handleQuery() {
      this.queryAllCheckResultForPage()
    },
    // 重置
    resetQuery() {
      this.resetForm('queryForm')
      this.queryParams.checkItemIds = this.checkItemOptions.map(item => item.checkItemId)
      this.queryAllCheckResultForPage()
    },
    // 查看结果
    handleViewResult(row) {
      // 打开录入结果的弹出层
      this.open = true
      this.title = '查看【' + row.patientName + '】的检查结果'
      // 记录当前选中的详情ID
      this.currentResult.resultMsg = row.resultMsg
      if (row.resultImg !== '') {
        this.currentResult.resultImg = JSON.parse(row.resultImg)
      }
    },
    // 关闭
    cancel() {
      this.open = false
      this.currentResult = {
        resultMsg: undefined,
        resultImg: []
      }
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
