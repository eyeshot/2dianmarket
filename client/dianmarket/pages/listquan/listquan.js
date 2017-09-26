
var currentPage = 1;//当前页,默认 第一页

import dian from '../../dian/index';
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
        beforeIndex: -1
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
    keywordSearch: function ()
    {
        console.log("keyword:" + this.data.keyword);
        page = 1;
        this.setData({
          list: [],
          searchLoadingComplete: false,
          toubaoYearmonth:''
        });
        this.fetchData();
    },

    toDetailsTap: function (e) {
      wx.navigateTo({
        url: "/pages/detail/detail?id=" + e.currentTarget.dataset.id
      })
    },

    detailSearch: function () {
      console.log("keyword:" + this.data.keyword);
      page = 1;
      this.setData({
        list: [],
        searchLoadingComplete: false,
        keyword:''
      });
      this.fetchData();
    },

    showOperate: function (e) {
      var id = e.currentTarget.dataset.id;
      console.info("beforeIndex: " + this.data.beforeIndex);
      console.info("id : " + id);
      console.log('doAni')

      if (this.data.beforeIndex != -1) {
        var backAni = wx.createAnimation({
          duration: 300,
          timingFunction: 'ease',
        })
        backAni.translateX(586).step()
        var beforeParam = {};
        var str = "source[" + this.data.beforeIndex + "]";
        beforeParam[str] = backAni;
        this.setData(beforeParam);

        var hiddenParam = {};
        var hiddenStr = "isHidden[" + this.data.beforeIndex  + "]";
        hiddenParam[hiddenStr] = 'none';
        this.setData(hiddenParam);

        if (this.data.beforeIndex == id) {
          this.setData(
            {
              beforeIndex: -1
            })
          console.info("return beforeIndex: " + this.data.beforeIndex);
          return;
        }

        this.setData(
          {
            beforeIndex: id
          })

        console.info("beforeIndex: " + this.data.beforeIndex);
      }

      var hiddenParam = {};
      var hiddenStr = "isHidden[" + id + "]";
      hiddenParam[hiddenStr] = 'inline';
      this.setData(hiddenParam);

      var animation = wx.createAnimation({
        duration: 300,
        timingFunction: 'ease',
      })
      this.animation = animation
      this.animation.translateX(0).step()
      this.setData(
        {
          beforeIndex: id
        })
      console.info("beforeIndex: " + this.data.beforeIndex);
      var param = {};
      var str = "source[" + id + "]";
      param[str] = this.animation;
      this.setData(param);

      this.setData({
        //name1: this.animation.export()
      })
    },

    backToCustomList: function()
    {
      this.setData({
        hideCustomList: false,
        hideSearchDetail: true
      })
    }, 
    bindToubaoYearMonthPickerChange: function (e) {
      this.setData({
        toubaoYearmonth: e.detail.value
      })
    },
    //输入框事件，每输入一个字符，就会触发一次  
    bindKeywordInput: function (e) {
      this.setData({
        keyword: e.detail.value
      })
    },
    toSearchTap:function (e) {
      wx.navigateTo({
        url: "/pages/search/search"
      })
    },
    fetchData: function () {//获取列表信息

        var that = this;
        this.setData({
          searchLoading: true,
        });
        dian.request({
          loading: true,
          url: '/xcx/wz/api/getPageList',
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
            for (var i = 0; i < that.data.list.length; i++)
            {
              var hiddenParam = {};
              var hiddenStr = "isHidden[" + i+ "]";
              hiddenParam[hiddenStr] = 'none';
              that.setData(hiddenParam);


              var animation = wx.createAnimation({
                duration: 300,
                timingFunction: 'ease',
              })
              this.animation = animation
              this.animation.translateX(586).step()
              var param = {};
              var str = "source[" + i + "]";
              param[str] = this.animation;
              that.setData(param);
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

