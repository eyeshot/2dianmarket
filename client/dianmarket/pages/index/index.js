//index.js  
//获取应用实例  
import dian from '../../dian/index';
var app = getApp()
Page({
  data: {
    currentStatus: '0',
    width: '',
    height: '',
    itemWidth: '',
    groupArr: [],
    detailList: [],
    groupNum : '0',
    routers: [
    ]
  },
  statusTap: function (e) {
    var curType = e.currentTarget.dataset.index;
    console.log("curType:" + curType);
    this.setData({
      currentStatus: curType,
      routers: []
    });

    for (var i = 0; i < this.data.groupNum; i++)
    {
      console.log("i:" + i)
      if(curType == i)
      {
        
        this.setData({
          routers: this.data.groupArr[i].type
        });
      }
    }

  }, 

  onShow: function () {
    var that = this;  
  },
  onLoad: function () {
    console.log('onLoad')
    var that = this

    wx.getSystemInfo({
      success: function (res) {
        console.log(res.model)
        console.log(res.pixelRatio)
        console.log(res.windowWidth)
        console.log(res.windowHeight)
        console.log(res.language)
        console.log(res.version)
        console.log(res.platform)
        console.log(res.windowWidth / 3)
        that.setData({
          width: res.windowWidth,
          height:  res.windowHeight,
          itemWidth: res.windowWidth / 3
        })
      }
    })

    dian.request({
      loading: true,
      url: '/xcx/type/api/getGroupType',
      success: function (res) {
        console.log(res.data.data.detail)
        console.log(res.data.data.total)

        console.log(res.data.data.detail[0])
        var totalNum = res.data.data.total
        that.setData({
          groupNum: res.data.data.total,
          groupArr: (res.data.data.detail),
          routers: res.data.data.detail[0].type
        });

        console.log("groupNum from server: " + that.data.groupNum);

      }
    });

  }
}) 