
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
        isHidden: [],
        fileKeyID: ''
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
 

    toDetailsTap: function (e) {
      wx.navigateTo({
        url: "/pages/detail/detail?id=" + e.currentTarget.dataset.id
      })
    },

    selectFile: function (e) 
    {
      for (var i = 0; i < this.data.list.length; i++) 
      {
        var hiddenParam = {};
        var hiddenStr = "isHidden[" + i + "]";
        hiddenParam[hiddenStr] = 'none';
        this.setData(hiddenParam);
      }

      var id = e.currentTarget.dataset.id;
      var hiddenParam = {};
      var hiddenStr = "isHidden[" + id + "]";
      hiddenParam[hiddenStr] = 'inline';
      this.setData(hiddenParam);

      var key = e.currentTarget.dataset.key;
      var name = e.currentTarget.dataset.name;
      console.log("key : " + key)
      console.log("name : " + name)

      var pages = getCurrentPages();
      var currPage = pages[pages.length - 1];  //当前页面
      var prevPage = pages[pages.length - 2]; //上一个页面
      //直接调用上一个页面的setData()方法，把数据存到上一个页面即编辑款项页面中去  
      prevPage.setData({
        fileID: key,
        fileName: name 
      });
      
      this.setData({
        fileKeyID: key
      })

    },

    confirm: function() 
    {
      if (this.data.fileKeyID == '')
      {
        wx.showToast({
          title: '请选择文件',
          image: "../../images/gantan.png",
          duration: 1000
        })
        return
      }
      wx.navigateBack({});
    },

    gotoFileDes: function()
    {
      wx.navigateTo({
        url: "/pages/updes/updes"
      })
    },
    
    fetchData: function () {//获取列表信息

        var that = this;
        this.setData({
          searchLoading: true,
        });
        dian.request({
          loading: true,
          url: '/xcx/front/code/getFileList',
          method: 'POST',
          data: {
            pageIndex: page,
            pageSize: 20
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
              var hiddenStr = "isHidden[" + i + "]";
              hiddenParam[hiddenStr] = 'none';
              that.setData(hiddenParam);
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
    }
})

