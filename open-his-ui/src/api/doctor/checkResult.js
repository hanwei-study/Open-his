import request from '@/utils/request'

// 根据挂号单号和项目IDS查询要检查的项目
export function queryNeedCheckItem(data) {
  return request({
    url: '/doctor/check/queryNeedCheckItem',
    method: 'post',
    data: data
  })
}

// 根据检查单号查询要检查的项目详情
export function queryCheckItemByItemId(itemId) {
  return request({
    url: '/doctor/check/queryCheckItemByItemId/' + itemId,
    method: 'get'
  })
}

// 开始检查
export function startCheck(itemId) {
  return request({
    url: '/doctor/check/startCheck/' + itemId,
    method: 'post'
  })
}

// 查询所有检查中的项目
export function queryAllCheckingResultForPage(data) {
  return request({
    url: '/doctor/check/queryAllCheckingResultForPage',
    method: 'post',
    data: data
  })
}

// 完成检查
export function completeCheckResult(data) {
  return request({
    url: '/doctor/check/completeCheckResult',
    method: 'post',
    data: data
  })
}

// 查询所有检查中的和检查完成了的项目
export function queryAllCheckResultForPage(data) {
  return request({
    url: '/doctor/check/queryAllCheckResultForPage',
    method: 'post',
    data: data
  })
}
