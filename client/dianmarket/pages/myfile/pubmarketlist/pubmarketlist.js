
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
        typeID: '',
        keyword:'',
        hideMessage:true,
        messagecount:0,
        hideCustomList: false,
        hideSearchDetail: true,
        monthArray:['1','2','3','4','5','6','7','8','9','10','11','12'],
        toubaoYearmonth:'',
        source: [],
        isHidden: [],
        beforeIndex: -1,
        collectImgs:[],
        isHiddenPart: true,
        currentMainID: '', //记录主键
        currentContent: ''
    },

    // 页面初始化 options为页面跳转所带来的参数
    onLoad: function (options) {
        console.info("typeID: " + options.typeID)
        var that = this;
        this.setData({
          typeID: options.typeID
        })
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
          searchLoadingComplete: false,
          toubaoYearmonth: ''
        });
        this.fetchData();
    },

    preImages: function (e) {
      var that = this
      var pid = e.currentTarget.dataset.pid
      var id = e.currentTarget.dataset.id
      var nowpic = this.data.list[pid].imglist[id].img
      var picList = this.data.list[pid].imglist;
      var urls = [];
      for (var i = 0; i < picList.length; i++) {
        urls.push(picList[i].img);
      }
      wx.previewImage({
        current: nowpic, // 当前显示图片的http链接
        urls: urls, // 需要预览的图片http链接列表
      })
    },
  

    doDelete: function (e)
    {
      var that = this;
      wx.showModal({
        title: '您确定要删除吗?',
        success: function (res) {
          if (res.confirm) {
            console.log('用户点击确定')
            that.doDeleteMarket(e)
          } else if (res.cancel) {
            console.log('用户点击取消')
          }
        }
      })
    },

    doDeleteMarket: function (e) 
    {
      var mainid = e.currentTarget.dataset.id;
      var collectIndex = e.currentTarget.dataset.index;
      console.info("docollect mainID: " + mainid);
      var that = this;
      dian.request({
        loading: true,
        url: '/xcx/front/upload/doDeleteMarket',
        method: 'POST',
        data: {
          mainID: mainid
        },
        success: function (res) {
          console.log(res.data.data)
          if(res.data.data == 'success')
          {
            wx.showToast({
              title: '删除成功',
              icon: 'success',
              duration: 1000
            })
            page = 1;
            that.setData({
              list: [],
              searchLoadingComplete: false
            });
            that.fetchData();
          } else if (res.data.data == 'fail')
          {
            wx.showToast({
              title: '删除失败',
              icon: 'success',
              duration: 1000
            })
          }
        }
      });

    },
    fetchData: function () {//获取列表信息

        var that = this;
        this.setData({
          searchLoading: true,
        });
        dian.request({
          loading: true,
          url: '/xcx/front/my/myTaskList',
          method: 'POST',
          data: {
            pageIndex: page,
            pageSize: 10
          },
          success: function (res) {
            console.log(res.data.data)
            that.setData({
              searchLoading: false,
              list: that.data.list.concat(res.data.data)
            });
            //console.info("list :" + that.data.list)
            for (var i = 0; i < that.data.list.length; i++) {
              var params = {};
              var collectStr = "collectImgs[" + i + "]";
              if (that.data.list[i].isCollect == '1')
              {
                params[collectStr] = '../../images/like-red.png';
              } else 
              {
                params[collectStr] = '../../images/like-gray.png';
              }
                
              that.setData(params);
            }
            
            if (res.data.data.length == 0) {
              that.setData({
                searchLoadingComplete: true
              });
            } else {
              page++;
            }
          }
        });
    },
    toMessageTab: function () {
      this.setData({
        hideMessage: true
      })
      wx.switchTab({
        url: '../message/message'
      })
    }
})

