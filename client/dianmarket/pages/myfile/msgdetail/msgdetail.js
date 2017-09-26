
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
        isHiddenPart: true,
        title: '',
        content: '',
        nickname: '',
        headimg: '',
        fromopenid: '',
        taskid: '', 
        needReport: '',
        reportOpenid: '',
        isMyMsg: false
    },

    // 页面初始化 options为页面跳转所带来的参数
    onLoad: function (options) {
      console.info("msgid: " + options.msgid)
        var that = this;
        this.setData({
          msgid: options.msgid
        })
        page = 1;
        this.requestMsgDetail()
        this.fetchData();

        wx.getSystemInfo({
          success: function (res) {
            console.log(res.windowWidth)
            console.log(res.windowHeight)
            that.setData({
              width: res.windowWidth,
              height: res.windowHeight - 50
            })
          }
        })
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

    toDetailsTap: function (e) {
      wx.navigateTo({
        url: "/pages/detail/detail?id=" + e.currentTarget.dataset.id
      })
    },

    cancelReport: function () {
      this.setData({
        isHiddenPart: true
      })
    },

    showReport: function () {
      this.setData({
        isHiddenPart: false
      })
    },

    saveReportData: function (e) {
      console.info("saveReportData invoked...")
      if (e.detail.value.content == '') {
        wx.showToast({
          title: '内容不能为空',
          image: "../../images/gantan.png",
          duration: 1000
        })
        return
      }
      wx.showLoading();
      this.setData({
        currentContent: e.detail.value.content
      })
      this.doSaveReportData()
    },

    doSaveReportData: function () {
      console.log("reportOpenid:" + this.data.reportOpenid )
      var that = this;
      dian.request({
        loading: true,
        url: '/xcx/front/msg/msgReport',
        method: 'POST',
        data: {
          fromopenid: that.data.reportOpenid,
          content: that.data.currentContent,
          msgid: that.data.msgid
        },
        success: function (res) {
          console.log("data:" + res.data.data)
          if (res.data.data == 'success') {
            wx.showToast({
              title: '回复成功',
              icon: 'success',
              duration: 1000
            })
            that.setData({
              isHiddenPart: true,
              currentContent: ''
            })
            that.setData({
              list: []
            })
            page = 1;
            that.requestMsgDetail()
            that.fetchData();
          } else if (res.data.data == 'fail') {
            wx.showToast({
              title: '系统错误',
              duration: 1000
            })
          } 
        }
      });

    },

    requestMsgDetail: function () {

      var that = this;
      dian.request({
        loading: true,
        url: '/xcx/front/msg/msgDetail',
        method: 'POST',
        data: {
          msgid: that.data.msgid
        },
        success: function (res) {
          console.log("data:" + res.data.data)
          that.setData({
            title: res.data.data.title,
            content: res.data.data.content,
            nickname: res.data.data.nickname,
            headimg: res.data.data.headimg,
            fromopenid: res.data.data.fromopenid,
            taskid: res.data.data.taskid,
            needReport: res.data.data.needReport,
            reportOpenid: res.data.data.reportOpenid,
            isMyMsg: res.data.data.mymsg
          })
          
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
          url: '/xcx/front/msg/msgDetailList',
          method: 'POST',
          data: {
            pageIndex: page,
            pageSize: 10,
            parentid: that.data.msgid
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
    },

    preChoose: function (e) {
      var that = this;
      wx.showModal({
        title: '您确定要选择TA接单吗?',
        content: '选择后，将不能修改，请谨慎操作',
        success: function (res) {
          if (res.confirm) {
            console.log('用户点击确定')
            that.doChoose(e)
          } else if (res.cancel) {
            console.log('用户点击取消')
          }
        }
      })

    },

    doChoose: function (e) {

      var that = this;
      console.log(that.data.taskid)
      dian.request({
        loading: true,
        url: '/xcx/front/msg/doChooseOuter',
        method: 'POST',
        data: {
          taskid: that.data.taskid,
          fromopenid: that.data.reportOpenid,
          msgid: that.data.msgid
        },
        success: function (res) {
          console.log("data:" + res.data.data)
          if (res.data.data == 'success') {
            wx.showToast({
              title: '操作成功，已经通知对方接单',
              icon: 'success',
              duration: 2000
            })
            that.setData({
              isHiddenPart: true,
              currentContent: ''
            })
          } else if (res.data.data == 'choosed') {
            wx.showToast({
              title: '已经有人接单了',
              icon: 'success',
              duration: 2000
            })
          } else if (res.data.data == 'blank') {
            that.setData({
              isHiddenPart: false
            })
          }
        }
      });

    }
})

