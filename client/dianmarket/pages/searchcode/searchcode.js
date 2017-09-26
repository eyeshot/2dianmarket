
var currentPage = 1;//当前页,默认 第一页

import dian from '../../dian/index';
var page = 1;
var page_size = 10;

var app = getApp();

Page({
  data: {
    list: [],
    searchLoading: false, //"上拉加载"的变量，默认false，隐藏  
    searchLoadingComplete: false,  //“没有数据”的变量，默认false，隐藏 
    typeID: '',
    keyword: '',
    hideMessage: true,
    messagecount: 0,
    hideCustomList: false,
    hideSearchDetail: true,
    monthArray: ['1', '2', '3', '4', '5', '6', '7', '8', '9', '10', '11', '12'],
    toubaoYearmonth: '',
    source: [],
    isHidden: [],
    beforeIndex: -1,
    collectImgs: [],
    isHiddenPart: true,
    currentMainID: '', //记录主键
    currentContent: ''
  },

  // 页面初始化 options为页面跳转所带来的参数
  onLoad: function (options) {
    var that = this;
    wx.getSystemInfo({
      success: function (res) {
        console.log(res.windowWidth)
        console.log(res.windowHeight)
        that.setData({
          width: res.windowWidth,
          height: res.windowHeight
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
      searchLoadingComplete: false
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
  searchCancel: function () {
    wx.navigateBack({

    })
  }, 
  keywordSearch: function () {
    console.log("keyword:" + this.data.keyword);
    page = 1;
    this.setData({
      list: [],
      searchLoadingComplete: false,
      toubaoYearmonth: ''
    });
    this.fetchData();
  },

  toDetailsTap: function (e) {
    wx.navigateTo({
      url: "/pages/codewrite/codewrite?id=" + e.currentTarget.dataset.id
    })
  },

  detailSearch: function () {
    console.log("keyword:" + this.data.keyword);
    page = 1;
    this.setData({
      list: [],
      searchLoadingComplete: false,
      keyword: ''
    });
    this.fetchData();
  },

  cancelPart: function () {
    this.setData({
      isHiddenPart: true
    })
  },

  showPart: function (e) {
    var mainid = e.currentTarget.dataset.id;
    this.setData({
      // isHiddenPart: false,
      currentMainID: mainid
    })
    this.requestPartTake()
  },

  savePartData: function (e) {
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
    this.requestPartTake()
  },

  requestPartTake: function () {

    var that = this;
    dian.request({
      loading: true,
      url: '/xcx/front/upload/doPartake',
      method: 'POST',
      data: {
        mainID: that.data.currentMainID,
        content: that.data.currentContent
      },
      success: function (res) {
        console.log("data:" + res.data.data)
        if (res.data.data == 'success') {
          wx.showToast({
            title: '报名成功',
            icon: 'success',
            duration: 1000
          })
          this.setData({
            isHiddenPart: true,
            currentContent: ''
          })
        } else if (res.data.data == 'applyed') {
          wx.showToast({
            title: '已竞标',
            icon: 'success',
            duration: 1000
          })
        } else if (res.data.data == 'blank') {
          that.setData({
            isHiddenPart: false
          })
        }
      }
    });

  },

  payCheck: function (event) {
    var id = event.currentTarget.dataset.id;
    var resid = event.currentTarget.dataset.resid;
    var that = this;

    dian.request({
      loading: true,
      url: '/xcx/pay/doCheckOrder',
      data: {
        //total: total * 100,
        total: 1,
        recordid: id,
        resid: resid
      },
      success: function (res) {
        var resultData = JSON.parse(res.data.data);
        if (resultData == '1') //已经购买
        {
          wx.showToast({
            title: '已经购买过了',
            image: "../../images/gantan.png",
            duration: 1000
          })
        } else if (resultData == '0') //还没有购买
        {
          wx.showModal({
            title: '您确认购买该代码吗?',
            success: function (res) {
              if (res.confirm) {
                console.log('用户点击确定')
                that.selectItem(event)
              } else if (res.cancel) {
                console.log('用户点击取消')
              }
            }
          })
        } else {
          wx.showToast({
            title: '系统错误!',
            image: "../../images/gantan.png",
            duration: 1000
          })
        }
      },
      fail: function (res) {

      }
    });

  },

  /**
 * 开始付款购买
 */
  selectItem: function (event) {
    var total = event.currentTarget.dataset.money;
    var id = event.currentTarget.dataset.id;
    var resid = event.currentTarget.dataset.resid;
    var that = this;
    that.setData({ selected: total });

    dian.request({
      loading: true,
      url: '/xcx/pay/doPreOrder',
      data: {
        //total: total * 100,
        total: 1,
        recordid: id,
        resid: resid
      },
      success: function (res) {
        console.log("获取支付密匙", res);
        var resultData = JSON.parse(res.data.data);
        console.log("timestamp:" + resultData.timestamp);

        wx.requestPayment({
          timeStamp: '' + resultData.timestamp,
          nonceStr: resultData.nonce,
          package: 'prepay_id=' + resultData.package,
          signType: 'MD5',
          paySign: resultData.paySign,
          success: function (res) {
            wx.showToast({
              title: '支付成功,感谢',
              icon: 'success'
            });
          },
          fail: function (res) {
            wx.showToast({
              title: '已取消支付',
              icon: 'success'
            });
            setTimeout(function () {
              wx.navigateTo({
                url: "/pages/downdes/downdes"
              })
            }, 2000);
          },
          complete: function () {
            that.setData({ selected: 0 });//取消选中
          }
        });

      },
      fail: function (res) {
        //弹出对话框提示支付失败信息
        wx.showModal({
          showCancel: false,
          title: '提示',
          content: res.data.msg,
          success: function (res) {
            if (res.confirm) {
              console.log('用户点击确定');
              dian.getUserInfo((userInfo) => {
                console.log("已获取数据", userInfo);
                app.data.userInfo = userInfo;
              }, () => {
                console.log("用户拒绝提供信息");
              });
            }
          }
        });
        that.setData({ selected: 0 });//取消选中
      }
    });
  },

  backToCustomList: function () {
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
  toSearchTap: function (e) {
    wx.navigateTo({
      url: "/pages/searchcode/searchcode"
    })
  },
  doCollect: function (e) {
    var mainid = e.currentTarget.dataset.id;
    var collectIndex = e.currentTarget.dataset.index;
    console.info("docollect mainID: " + mainid);
    console.info("docollect index: " + collectIndex)
    var that = this;
    dian.request({
      loading: true,
      url: '/xcx/front/upload/doCollect',
      method: 'POST',
      data: {
        mainID: mainid
      },
      success: function (res) {
        console.log(res.data.data)
        if (res.data.data == 'success') {
          var params = {};
          var collectStr = "collectImgs[" + collectIndex + "]";
          params[collectStr] = '../../images/like-red.png';
          that.setData(params);

          wx.showToast({
            title: '关注成功',
            icon: 'success',
            duration: 1000
          })
        } else if (res.data.data == 'remove') {
          console.log("cancel do collect...")
          var params = {};
          var collectStr = "collectImgs[" + collectIndex + "]";
          params[collectStr] = '../../images/like-gray.png';
          that.setData(params);

          wx.showToast({
            title: '取消成功',
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
      url: '/xcx/front/code/getCodeList',
      method: 'POST',
      data: {
        pageIndex: page,
        pageSize: 10,
        keyword: that.data.keyword
      },
      success: function (res) {
        console.log(res.data.data)
        that.setData({
          searchLoading: false,
          list: that.data.list.concat(res.data.data)
        });
        console.info("list :" + that.data.list)
        for (var i = 0; i < that.data.list.length; i++) {
          var params = {};
          var collectStr = "collectImgs[" + i + "]";
          if (that.data.list[i].isCollect == '1') {
            params[collectStr] = '../../images/like-red.png';
          } else {
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
  }
  
})

