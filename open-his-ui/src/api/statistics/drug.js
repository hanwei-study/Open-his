import request from '@/utils/request'

// 查询销售药品列表
export function queryDrug(params) {
  return request({
    url: '/statistics/drug/queryDrug',
    method: 'get',
    params: params
  })
}
// 查询销售药品统计列表
export function queryDrugStat(params) {
  return request({
    url: '/statistics/drug/queryDrugStat',
    method: 'get',
    params: params
  })
}
