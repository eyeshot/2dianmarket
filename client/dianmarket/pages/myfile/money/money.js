
import dian from '../../../dian/index';

Page({
  data:{
    currrentMoney : ''
  },

  loadmoney: function () {
    console.log("begin to loadmoney...")
    var that = this
    dian.request({
      loading: true,
      url: '/xcx/front/my/getMoney',
      method: 'POST',
      success: function (res) {
        console.log("data:" + res.data.data)
        that.setData({
          currrentMoney: res.data.data.money
        })
      }
    });
  },

  toGetMoney: function(e)
  {
     console.info("money:" + e.currentTarget.dataset.money)
     wx.navigateTo({
       url: '../getmoney/getmoney?money=' + e.currentTarget.dataset.money
     })
  },

  moneylist: function (e)
  {
    wx.navigateTo({
      url: '../moneylist/moneylist',
    })
  },

  goBack: function (e) 
  {
    wx.navigateBack({});
  },

  onLoad:function(options){
    this.loadmoney()
  },
  onReady:function(){
    // 页面渲染完成
    
  },
  onShow:function(){
    // 页面显示
    
  },
  onHide:function(){
    // 页面隐藏
  },
  onUnload:function(){
    // 页面关闭
  }
})