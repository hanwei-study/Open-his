import request from '@/utils/request'

// 根据挂号ID查询已支付的信息及详情
export function getChargedCareHistoryByRegId(regId) {
  return request({
    url: '/doctor/backfee/getChargedCareHistoryByRegId/' + regId,
    method: 'get'
  })
}

// 退费里面的现金退费
export function createOrderBackfeeWithCash(data) {
  return request({
    url: '/doctor/backfee/createOrderBackfeeWithCash',
    method: 'post',
    data: data
  })
}

// 分页查询所有退费订单
export function queryAllOrderBackfeeForPage(params) {
  return request({
    url: '/doctor/backfee/queryAllOrderBackfeeForPage',
    method: 'get',
    params: params
  })
}

// 根据订单ID查询订单详情
export function queryOrderBackfeeItemByBackId(backId) {
  return request({
    url: '/doctor/backfee/queryOrderBackfeeItemByBackId/' + backId,
    method: 'get'
  })
}
