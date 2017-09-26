// pages/detail/detail.js

import dian from '../../dian/index';
var config = require('../../dian/config');
var app = getApp();

Page({
  data:{
    id:"",
    currentStatus: '1',
    birthday:'',
    baodan:'',
    title:'',
    itemtype: ['项目代码', '功能代码'],
    tempFilePaths: [],
    itemtypeVaule: '',
    itemTypeIndex: '',
    hidSelectCode: false,
    hidFileName: true,
    fileName: '',
    fileID: ''
  },


  bindTypePickerChange: function (e) {
    var typeValue = '';
    if (e.detail.value == 0)
    {
      typeValue = '项目代码';
      this.setData({
        itemTypeIndex: 0
      })
    }
    else if (e.detail.value == 1)
    {
      typeValue = '功能代码';
      this.setData({
        itemTypeIndex: 1
      })
    }
    this.setData({
      itemtypeVaule: typeValue
    })
  },

  deletepic: function (e)
  {
     var id = e.currentTarget.dataset.id;
     console.info("id: " + id)
     var param = {};
     //var str = "tempFilePaths[" + id + "]";
     //param[str] = this.animation;
     this.data.tempFilePaths.splice(id, 1)
     this.setData({
       tempFilePaths: this.data.tempFilePaths
     });
  },

  selectpic: function (e)
  {
    var that = this;
    wx.chooseImage({
      count: 9, // 默认9
      sizeType: ['original', 'compressed'], // 可以指定是原图还是压缩图，默认二者都有
      sourceType: ['album', 'camera'], // 可以指定来源是相册还是相机，默认二者都有
      success: function (res) {
        // 返回选定照片的本地文件路径列表，tempFilePath可以作为img标签的src属性显示图片
        //var tempFilePaths = res.tempFilePaths
        that.setData({
          tempFilePaths : that.data.tempFilePaths.concat(res.tempFilePaths)
        })
      }
    })
  },

  selectcode: function(e)
  {
    wx.navigateTo({
      url: "/pages/filelist/filelist"
    })
  },

  saveData: function (e)
  {
    if (e.detail.value.title == '') {
      wx.showToast({
        title: '标题不能为空',
        image: "../../images/gantan.png",
        duration: 1000
      })
      return
    }

    if (this.data.itemtypeVaule == '') {
      wx.showToast({
        title: '类型不能为空',
        image: "../../images/gantan.png",
        duration: 1000
      })
      return
    }

    if (e.detail.value.workday == '') {
      wx.showToast({
        title: '金额不能为空',
        image: "../../images/gantan.png",
        duration: 1000
      })
      return
    }

    if (this.data.tempFilePaths.length <= 0)
    {
      wx.showToast({
        title: '示例图不能为空',
        image: "../../images/gantan.png",
        duration: 1000
      })
      return
    }

    if (e.detail.value.content == '')
    {
      wx.showToast({
        title: '描述不能为空',
        image: "../../images/gantan.png",
        duration: 1000
      })
      return
    } 

    if (this.data.fileName == '')
    {
      wx.showToast({
        title: '代码不能为空',
        image: "../../images/gantan.png",
        duration: 1000
      })
      return
    }
    if (e.detail.value.content == '') {
      wx.showToast({
        title: '详情不能为空',
        image: "../../images/gantan.png",
        duration: 1000
      })
      return
    }

    if (this.data.tempFilePaths.length == 0) {
      wx.showToast({
        title: '图片不能为空',
        image: "../../images/gantan.png",
        duration: 1000
      })
      return
    }
    
    wx.showLoading();
    var that = this;
    dian.request({
      loading: true,
      url: '/xcx/front/code/saveCode',
      method: 'POST',
      data: {
        title: e.detail.value.title,
        itemTypeIndex: that.data.itemTypeIndex,
        money: e.detail.value.money,
        content: e.detail.value.content,
        fileName: that.data.fileName,
        fileID: that.data.fileID
      },
      success: function (res) {
        console.log("data:" + res.data.data)
        var mainID = res.data.data;
        var pathLength = that.data.tempFilePaths.length;
        console.log("pathLength:" + pathLength);
        var uploadIndex = 1;
        //开始上传图片
        for (var i = 0; i < pathLength; i++) {
          wx.uploadFile({
            url: config.host + '/xcx/front/upload/img',
            filePath: that.data.tempFilePaths[i],
            name: 'file',
            formData: {
              'parentID': mainID,
              'temppaths': that.data.tempFilePaths[i],
              'imgType': 2
            },
            success: function (res) {
              var data = res.data
              //do something
              if (uploadIndex == pathLength)
              {
                wx.showToast({
                  title: '创建成功',
                  icon: 'success',
                  duration: 2000
                })
                //列表页面刷新
                var pages = getCurrentPages();
                var currPage = pages[pages.length - 1];  //当前页面
                var prevPage = pages[pages.length - 2]; //上一个页面
                prevPage.setData({
                  isRefresh: true
                });
                setTimeout(function () {
                  wx.navigateBack({});
                },2000);
              }
              uploadIndex ++;
            }
          })
        }
      }
    });

  },

  goBack: function (e) 
  {
    wx.navigateBack({});
  },

  onLoad:function(options){
    // 页面初始化 options为页面跳转所带来的参数

    var id = options.id;
    var title = options.title;
    console.log("msg detail ID: " + id)
    console.log("msg title: " + title)
    this.setData({
      id: id,
      title: title
    })
  },
  onReady:function(){
    // 页面渲染完成
    
  },
  onShow:function(){
    // 页面显示
    console.log("fileID: " + this.data.fileID)
    console.log("fileName: " + this.data.fileName)
    if(this.data.fileName != '')
    {
      this.setData({
        hidSelectCode : true,
        hidFileName: false,
      })
    }
  },
  onHide:function(){
    // 页面隐藏
  },
  onUnload:function(){
    // 页面关闭
  }
})