export function resetForm(refName) {
  if (this.$refs[refName]) {
    this.$refs[refName].resetFields()
  }
}
// 把日期范围构造成beginTime和endTime
export function addDateRange(params, dateRange) {
  var search = params
  search.beginTime = ''
  search.endTime = ''
  if (dateRange != null && dateRange !== '' && dateRange !== undefined) {
    search.beginTime = this.dateRange[0]
    search.endTime = this.dateRange[1]
  }
  return search
}
// 状态翻译 datas是当前状态数据列表 value要翻译的值
export function selectDictLabel(datas, value) {
  var actions = []
  Object.keys(datas).map((key) => {
    if (datas[key].dictValue === value) {
      actions.push(datas[key].dictLabel)
    }
  })
  return actions.join('')
}

/** 构造树
 * @Param {*} data 数据源
 * @Param {*} id 字段ID 默认id
 * @Param {*} parentId 父节字段  默认 parentId
 * @Param {*} 子节点字段  默认 children
 * @Param {*} rootId 根节点ID 默认0
*/
export function handleTree(data, id, parentId, children, rootId) {
  id = id || 'id'
  parentId = parentId || 'parentId'
  children = children || 'children'
  rootId = rootId || 0
  // 对源数据深度克隆
  const cloneData = JSON.parse(JSON.stringify(data))
  // 循环所有项目
  const treeData = cloneData.filter(father => {
    const branchArr = cloneData.filter(child => {
      return father[id] === child[parentId]
    })
    branchArr.length > 0 ? father.children = branchArr : ''
    // 返回上一层
    return father[parentId] === rootId
  })
  return treeData !== '' ? treeData : data
}
/**
 * 根据出生年月计算年龄的方法
 *  @param birthday 格式必须为2020-08-08
*/
export function getAge(birthday) {
  var birArr = birthday.split('-')
  var birYear = parseInt(birArr[0])
  var birMonth = parseInt(birArr[1])
  var birDay = parseInt(birArr[2])

  d = new Date()
  var nowYear = d.getFullYear()
  var nowMonth = d.getMonth() + 1 // 记得加1
  var nowDay = d.getDate()
  var returnAge

  if (birArr == null) {
    return false
  }
  var d = new Date(birYear, birMonth - 1, birDay)
  if (d.getFullYear() === birYear && (d.getMonth() + 1) === birMonth && d.getDate() === birDay) {
    if (nowYear === birYear) {
      returnAge = 0 // 今年出生的
    } else {
      var ageDiff = nowYear - birYear
      if (ageDiff > 0) {
        if (nowMonth === birMonth) {
          var dayDiff = nowDay - birDay
          if (dayDiff < 0) {
            returnAge = ageDiff - 1
          } else {
            returnAge = ageDiff
          }
        } else {
          var monthDiff = nowMonth - birMonth
          if (monthDiff < 0) {
            returnAge = ageDiff - 1
          } else {
            returnAge = ageDiff
          }
        }
      } else {
        return '出生日期晚于今天，数据有误' // 返回-1 表示出生日期输入错误 晚于今天
      }
    }
    return returnAge
  } else {
    return ('输入的日期格式错误！')
  }
}

// 判断当前时间是上午1  下午2  晚上3
export function getCurrentTimeType() {
  const now = new Date()
  const hour = now.getHours()
  if (hour >= 6 && hour < 12) {
    return '1'
  } else if (hour >= 12 && hour < 18) {
    return '2'
  } else if (hour >= 0 && hour < 6 || hour >= 18 && hour < 24) {
    return '3'
  }
}
