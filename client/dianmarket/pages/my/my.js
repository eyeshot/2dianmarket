//index.js
import dian from '../../dian/index';
//获取应用实例
var app = getApp();
Page( {
  data: {
    userInfo: {},
    haveMsg : false
  },
  //设置处理函数
  setting: function() {
    wx.navigateTo( {
      url: '../settingdetail/settingdetail'
    })
  },
  toMoney: function () {
    wx.navigateTo({
      url: '../myfile/money/money'
    })
  },
  toBuyList: function () {
    wx.navigateTo({
      url: '../myfile/buycodelist/buycodelist'
    })
  },
  toPubMarket: function () {
    wx.navigateTo({
      url: '../myfile/pubmarketlist/pubmarketlist'
    })
  },
  toPubCode: function () {
    wx.navigateTo({
      url: '../myfile/pubcodelist/pubcodelist'
    })
  },
  toMsgList: function () 
  {
    wx.navigateTo({
      url: '../myfile/msglist/msglist'
    })
  },
  toAbout: function () {
    wx.navigateTo({
      url: '../myfile/about/about'
    })
  },
  checkNewMsg: function () 
  {
    var that = this;
    dian.request({
      loading: true,
      url: '/xcx/front/msg/checkNewMsg',
      method: 'POST',
      success: function (res) {
        console.log("data:" + res.data.data)
        if (res.data.data == '1') {  //存在未读消息
          that.setData({
            haveMsg: true  
          })
        } else if (res.data.data == '0') { //不存在未读消息
          that.setData({
            haveMsg: false
          })
        } 
      }
    });
  },

  onShareAppMessage: function (res) {
    if (res.from === 'button') {
      // 来自页面内转发按钮
      console.log(res.target)
    }
    return {
      title: '最专业的小程序外包市场!',
      path: '/pages/main/main',
      success: function (res) {
        // 转发成功
        wx.showToast({
          title: '分享成功',
          icon: 'success',
          duration: 2000
        })
      },
      fail: function (res) {
        // 转发失败
        wx.showToast({
          title: '取消分享',
          icon: 'success',
          duration: 2000
        })
      }
    }
  },

  goToShare: function()
  {
    Page.onShareAppMessage()
  },

  settingBack: function () {
    wx.navigateTo({
      url: '../logs/logs'
    })
  },

  onLoad: function() {
    var that = this
    this.setData({
      userInfo: app.globalData.userInfo
    })
  },

  onShow: function () {
    // 页面显示
    console.log("onShow...");
    this.checkNewMsg()
  },

  onReady: function () {
    wx.setNavigationBarTitle({
      title: '我的'
    })
  }
})
