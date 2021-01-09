import request from '@/utils/request'

export function login(data) {
  return request({
    url: '/login/doLogin',
    method: 'post',
    data
  })
}

export function getInfo(token) {
  return request({
    url: '/login/getInfo',
    method: 'get',
    params: { token }
  })
}

export function logout() {
  return request({
    url: '/login/logout',
    method: 'get'
  })
}

export function getMenus() {
  return request({
    url: '/login/getMenus',
    method: 'get'
  })
}
