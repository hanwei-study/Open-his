import request from '@/utils/request'

// 查询检查项目统计列表
export function queryCheck(params) {
  return request({
    url: '/statistics/check/queryCheck',
    method: 'get',
    params: params
  })
}
// 查询检查项目数量统计列表
export function queryCheckStat(params) {
  return request({
    url: '/statistics/check/queryCheckStat',
    method: 'get',
    params: params
  })
}
