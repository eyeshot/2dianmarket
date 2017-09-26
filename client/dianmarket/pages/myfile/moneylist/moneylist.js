
var currentPage = 1;//当前页,默认 第一页

import dian from '../../../dian/index';
var page = 1;
var page_size = 10;


var app = getApp();

Page({
    data: {
        list: [],
        searchLoading: true, //"上拉加载"的变量，默认false，隐藏  
        searchLoadingComplete: false,  //“没有数据”的变量，默认false，隐藏 
        
    },

    // 页面初始化 options为页面跳转所带来的参数
    onLoad: function (options) {
        
        page = 1;
        this.fetchData();
    },

    onShow: function () {
      // 页面显示
      console.log("onShow...");
      
    },

    onPullDownRefresh: function () { //下拉刷新
        page = 1;
        this.setData({
            list: [],
            searchLoadingComplete : false
        });
        this.fetchData();
        
    },
    onReachBottom: function () { // 上拉加载更多
        // Do something when page reach bottom.
        console.log("page = " + page)
        this.setData({
          searchLoadingComplete: false
        });
        this.fetchData();
    },
   
    fetchData: function () {//获取列表信息

        var that = this;
        this.setData({
          searchLoading: true,
        });
        dian.request({
          loading: true,
          url: '/xcx/front/my/moneyList',
          method: 'POST',
          data: {
            pageIndex: page,
            pageSize: 10,
            typeID: that.data.typeID
          },
          success: function (res) {
            console.log(res.data.data)
            that.setData({
              searchLoading: false,
              list: that.data.list.concat(res.data.data)
            });
            if (res.data.data.length == 0) {
              that.setData({
                searchLoadingComplete: true
              });
            } else {
              page++;
            }
          }
        });
    }
})

