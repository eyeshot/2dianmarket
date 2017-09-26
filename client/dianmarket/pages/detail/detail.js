// pages/lab/lab.js
var WxParse = require('../../wxParse/wxParse.js');
import dian from '../../dian/index';
Page({
  data:{
    mainID: '',
    nodes: ''
  },
  onLoad:function(options){
    // 页面初始化 options为页面跳转所带来的参数
    console.info("detail ID:" + options.id)
    this.setData({
      mainID: options.id
    })
    var that = this;

    this.fetchData();
    
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
  },

  fetchData: function () {//获取文章详情信息

    var that = this;
    this.setData({
      searchLoading: true,
    });
    dian.request({
      loading: true,
      url: '/xcx/wz/api/getDetail',
      method: 'POST',
      data: {
        id: that.data.mainID
      },
      success: function (res) {
        console.log(res.data.data)
        that.setData({
          //list: that.data.list.concat(res.data.data)
          nodes: res.data.data.detail
        });

        var aHrefHrefData = res.data.data.detail;

        WxParse.wxParse('aHrefHrefData', 'html', aHrefHrefData, that, 30);
        
      }
    });
  },
  insertNodeTap: function(e){
    var that = this;
    var insertData = '<div style="color:red;text-align:center;padding:20px;">我是一个被插入的元素</div>';
    WxParse.wxParse('insertData', 'html', insertData, that);
  },
  wxParseTagATap: function(e){
    var href = e.currentTarget.dataset.src;
    console.log(href);
    //我们可以在这里进行一些路由处理
    if(href.indexOf(index) > 0){
      // wx.redirectTo({
      //   url: '../index/index'
      // })

    }

  }
})