import request from '@/utils/request'

// 医生工作量统计列表
export function queryWorkload(params) {
  return request({
    url: '/statistics/workload/queryWorkload',
    method: 'get',
    params: params
  })
}
// 总体工作量统计列表
export function queryWorkloadStat(params) {
  return request({
    url: '/statistics/workload/queryWorkloadStat',
    method: 'get',
    params: params
  })
}
