import request from '@/utils/request'

// 查询检查项目列表
export function queryAllRevenueData(params) {
  return request({
    url: '/statistics/revenue/queryAllRevenueData',
    method: 'get',
    params: params
  })
}
