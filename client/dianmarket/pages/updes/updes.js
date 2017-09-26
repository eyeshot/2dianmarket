
import dian from '../../dian/index';

Page({
  data:{
    
  },

  doScan: function () {
    console.log("begin to camera...")
    wx.scanCode({
      success: (res) => {
        console.log(res)
        dian.request({
          loading: true,
          url: '/xcx/web/scan',
          method: 'POST',
          data: {
            jsvalue: res.result
          },
          success: function (res) {
            console.log("data:" + res.data.data)
            if (res.data.data == 'success') {
              console.log("scan success...");
            }
          }
        });
      }
    })
  },

  goBack: function (e) 
  {
    wx.navigateBack({});
  },

  onLoad:function(options){
   
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