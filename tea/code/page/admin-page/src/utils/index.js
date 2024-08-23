/**
 * Created by PanJiaChen on 16/11/18.
 */

/**
 * Parse the time to string
 * @param {(Object|string|number)} time
 * @param {string} cFormat
 * @returns {string | null}
 */
export function parseTime(time, cFormat) {
  if (arguments.length === 0 || !time) {
    return null
  }
  const format = cFormat || '{y}-{m}-{d} {h}:{i}:{s}'
  let date
  if (typeof time === 'object') {
    date = time
  } else {
    if ((typeof time === 'string')) {
      if ((/^[0-9]+$/.test(time))) {
        // support "1548221490638"
        time = parseInt(time)
      } else {
        // support safari
        // https://stackoverflow.com/questions/4310953/invalid-date-in-safari
        time = time.replace(new RegExp(/-/gm), '/')
      }
    }

    if ((typeof time === 'number') && (time.toString().length === 10)) {
      time = time * 1000
    }
    date = new Date(time)
  }
  const formatObj = {
    y: date.getFullYear(),
    m: date.getMonth() + 1,
    d: date.getDate(),
    h: date.getHours(),
    i: date.getMinutes(),
    s: date.getSeconds(),
    a: date.getDay()
  }
  const time_str = format.replace(/{([ymdhisa])+}/g, (result, key) => {
    const value = formatObj[key]
    // Note: getDay() returns 0 on Sunday
    if (key === 'a') { return ['日', '一', '二', '三', '四', '五', '六'][value ] }
    return value.toString().padStart(2, '0')
  })
  return time_str
}

/**
 * @param {number} time
 * @param {string} option
 * @returns {string}
 */
export function formatTime(time, option) {
  if (('' + time).length === 10) {
    time = parseInt(time) * 1000
  } else {
    time = +time
  }
  const d = new Date(time)
  const now = Date.now()

  const diff = (now - d) / 1000

  if (diff < 30) {
    return '刚刚'
  } else if (diff < 3600) {
    // less 1 hour
    return Math.ceil(diff / 60) + '分钟前'
  } else if (diff < 3600 * 24) {
    return Math.ceil(diff / 3600) + '小时前'
  } else if (diff < 3600 * 24 * 2) {
    return '1天前'
  }
  if (option) {
    return parseTime(time, option)
  } else {
    return (
      d.getMonth() +
      1 +
      '月' +
      d.getDate() +
      '日' +
      d.getHours() +
      '时' +
      d.getMinutes() +
      '分'
    )
  }
}

/**
 * @param {string} url
 * @returns {Object}
 */
export function param2Obj(url) {
  const search = decodeURIComponent(url.split('?')[1]).replace(/\+/g, ' ')
  if (!search) {
    return {}
  }
  const obj = {}
  const searchArr = search.split('&')
  searchArr.forEach(v => {
    const index = v.indexOf('=')
    if (index !== -1) {
      const name = v.substring(0, index)
      const val = v.substring(index + 1, v.length)
      obj[name] = val
    }
  })
  return obj
}
/*
 * 时间戳   ==>   YYYY-MM-DD hh:mm:ss   YYYY年MM月DD日hh小时mm分ss秒  YYYY年MM月DD日
 * 用法: formatTime(time, 'YYYY-MM-DD hh:mm')
 */
export function formatTimes(time, format) {
  if (!format) {
    format = 'YYYY-MM-DD'
  }
  time = (time && String(time).length < 13) ? new Date(time * 1000) : new Date(time)

  var o = {
    'M+': time.getMonth() + 1, // month
    'D+': time.getDate(), // day
    'h+': time.getHours(), // hour
    'm+': time.getMinutes(), // minute
    's+': time.getSeconds(), // second
    'q+': Math.floor((time.getMonth() + 3) / 3), // quarter
    'S': time.getMilliseconds() // millisecond
  }

  if (/(Y+)/.test(format)) {
    format = format.replace(RegExp.$1, (time.getFullYear() + '').substr(4 - RegExp.$1.length))
  }

  for (var k in o) {
    if (new RegExp('(' + k + ')').test(format)) {
      format = format.replace(RegExp.$1, RegExp.$1.length == 1 ? o[k] : ('00' + o[k]).substr(('' + o[k]).length))
    }
  }
  return format
}

/* 深拷贝 */
export function deepCopy(target, source) {
  if (!target) {
    throw Error(`Cannot convert undefined or null to object`)
  }
  for (const k in source) {
    const val = source[k]
    if (typeof val === 'object') {
      if (!target[k]) {
        target[k] = Array.isArray(source[k]) ? [] : {}
      }
      deepCopy(target[k], val)
    } else {
      target[k] = val
    }
  }
  return target
}

// 判断图片是否存在 有效返回true, 无效返回false
export function isImgUrlValid(imgurl) {
  return new Promise((resolve, reject) => {
    const ImgObj = new Image() // 判断图片是否存在
    ImgObj.src = imgurl
    ImgObj.onload = (res) => {
      resolve(res)
    }
    ImgObj.onerror = (err) => {
      reject(err)
    }
  }).catch((e) => { }) // 加上这句不会报错（Uncaught (in promise)）
}

