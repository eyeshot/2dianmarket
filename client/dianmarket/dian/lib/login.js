var config = require('../config')
var constant = require('./constant')

const LOGIN_URL = `${config.host}/xcx/login/goWebchat`;//登录服务
const FULL_USER_INFO_URL = `${config.host}/xcx/login/decodeWxInfo`;//解密微信用户信息并保存在服务端
const CHECK_LOGIN_URL = `${config.host}/checkLogin`;//校验是否登录

/**
 * 校验登录
 */
var checkLogin = (success, fail) => {
    var dianToken = wx.getStorageSync(constant.DIAN_TOKEN);
    if (!dianToken) {
        typeof fail == "function" && fail();
    } else {
        wx.checkSession({
            success: function () {
              /** 
                wx.request({
                    url: CHECK_LOGIN_URL,
                    data: {
                        dianToken: dianToken
                    },
                    complete: function (res) {
                        if (res.statusCode != 200) {//失败
                            typeof fail == "function" && fail();
                        } else {//成功
                            typeof success == "function" && success();
                        }
                    }
                })
                */
              console.log("checkSession success!")
              typeof success == "function" && success();
            },
            fail: function () {
                typeof fail == "function" && fail();
            }
        })
    }
}

/**
 * 登录
 */
var login = (success, fail) => {
    console.info("login invoked...");
   // remoteLogin(success, fail)
    
    
    checkLogin(() => {
        console.log("已登录,不用重复登录了。。。");
        typeof success == "function" && success();
    }, () => {
        remoteLogin(success, fail)
    });
    
    
    
}

/**
 * 服务端请求登录
 */
var remoteLogin = (success, fail) => {
  console.info("remoteLogin invoked...")
    //调用登录接口
    wx.login({
        success: function (loginRes) {
            console.log("登录获取code", loginRes);
            wx.request({
                url: LOGIN_URL,
                data: {
                    code: loginRes.code
                },
                complete: function (res) {
                    console.log("login res:" + res)
                    if (res.statusCode != 200) {//失败
                        console.error("登陆失败", res);
                        var data = res.data || { msg: '无法请求服务器' };
                        if (typeof fail == "function") {
                            fail();
                        } else {
                            wx.showModal({
                                title: '提示',
                                content: data.msg,
                                showCancel: false
                            }); 
                        }
                    } else {//成功
                        console.log("登录成功", res);
                        console.log("res.data.data.code", res.data.code);
                        wx.setStorage({
                            key: constant.DIAN_TOKEN,
                            data: res.data.code
                        })
                        typeof success == "function" && success();
                    }
                }
            })
        },
        fail : function(res)
        {
            console.log("wx.login has error", res)
        }
    })
}

var getUserInfo = (success, fail) => {
    wx.getUserInfo({
        success: function (res) {
            console.log("获取用户信息", res);
            var userInfo = res.userInfo
            if (config.fullLogin) {//上传加密数据
                wx.request({
                    url: FULL_USER_INFO_URL,
                    data: {
                        dianToken: wx.getStorageSync(constant.DIAN_TOKEN),
                        encryptedData: res.encryptedData,
                        iv: res.iv
                    }, success: function (requestRes) {
                        typeof success == "function" && success(userInfo);
                    }
                });
            } else {
                typeof success == "function" && success(userInfo);
            }
        }, fail: function () {
            typeof fail == "function" && fail();
        }
    })
}

module.exports = {
    login: login,
    checkLogin: checkLogin,
    getUserInfo: getUserInfo
}