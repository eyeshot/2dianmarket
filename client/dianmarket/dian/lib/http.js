var config = require('../config')
var constant = require('./constant')

var request = options => {
    var data = options.data || {};
    data.dian_token = wx.getStorageSync(constant.DIAN_TOKEN);//每次请求组装上token
    console.log("wx.getStorageSync(constant.DIAN_TOKEN): " + wx.getStorageSync(constant.DIAN_TOKEN))
    console.log("token:" + wx.getStorageSync(constant.DIAN_TOKEN));
    var url = options.url;
    if (url.indexOf("http") != 0) {
        url = `${config.host}${url}`;
    }

    var method = options.method

    console.info("method:" + method);

    if(method == '')
    {
      method = 'GET'
    }

    console.info("url:" + url)

    if (options.loading) {
        /*
        wx.showToast({
            title: '加载中',
            icon: 'loading',
            duration: 10000
        })*/
        wx.showNavigationBarLoading();
    };

    wx.request({
        url: url,
        data: data,
        method: method,
        complete: function (res) {
            if (options.loading) {
                /*
                 wx.hideToast();*/
                wx.hideNavigationBarLoading();
            }

            if (res.statusCode != 200) {//失败
                console.error("失败", options.url, res);
                typeof options.fail == "function" && options.fail(res);
            } else {//成功
                console.log("成功", options.url, res);
                typeof options.success == "function" && options.success(res);
            }
        }
    });
}

module.exports = {
    request: request
}