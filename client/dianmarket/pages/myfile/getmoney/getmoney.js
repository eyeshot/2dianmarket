
import dian from '../../../dian/index';

Page({
  data:{
    inputMoney: 0,
    haveMoney: 0,
    cashBtnDisable: false
  },

  doGetCash: function () {
    var that = this
    dian.request({
      loading: true,
      url: '/xcx/front/my/getCash',
      method: 'POST',
      data: {
        cash: that.data.inputMoney
      },
      success: function (res) {
        console.log("data:" + res.data.data)
        if (res.data.data == 'success') {
          console.log("cash success...");
          //修改上个页面的金额
          var pages = getCurrentPages();
          var currPage = pages[pages.length - 1];  //当前页面
          var prevPage = pages[pages.length - 2]; //上一个页面
          //直接调用上一个页面的setData()方法，把数据存到上一个页面即编辑款项页面中去  
          var myMoney = that.data.haveMoney;
          var input = that.data.inputMoney;
          var leftmoney = myMoney - input;
          console.log("leftMoney:" + leftmoney)
          prevPage.setData({
            currrentMoney: leftmoney
          });
          wx.showToast({
            title: '提现成功!',
            duration: 2000
          })
          setTimeout(function () {
            wx.navigateBack({});
          }, 2000);
        } else if (res.data.data == 'fail')
        {
          this.setData({
            cashBtnDisable: false
          })
        }
      }
    });
  },

  beginCache: function (e)
  {
    console.log("haveMoney:" + this.data.haveMoney)
    console.log("inputMoney:" + this.data.inputMoney)
    var myMoney = this.data.haveMoney;
    var input = this.data.inputMoney;
    if ((input) > (myMoney))
     {
      wx.showToast({
        title: '金额超限',
        image: "../../../images/gantan.png",
        duration: 1000
      })
      return 
     }
     this.setData({
       cashBtnDisable: true
     })
    this.doGetCash()
  },

  inputMoney: function (e)
  {
    console.log(e.detail.value)
     this.setData({
       inputMoney: e.detail.value
     })
  },

  goBack: function (e) 
  {
    wx.navigateBack({});
  },

  onLoad:function(options){

    var havemoney = options.money
    console.log("get money: " + havemoney)
    this.setData({
      haveMoney: havemoney
    })
   
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