import request from '@/utils/request'

// 根据挂号ID查询已支付的药品处方信息及详情
export function getChargedCareHistoryOnlyMedicinesByRegId(regId) {
  return request({
    url: '/doctor/handleMedicine/getChargedCareHistoryOnlyMedicinesByRegId/' + regId,
    method: 'get'
  })
}

// 处理发药
export function doMedicine(data) {
  return request({
    url: '/doctor/handleMedicine/doMedicine',
    method: 'post',
    data: data
  })
}
