import dian from '../../dian/index';
var app = getApp();

Page({
    data: {
        dataList: []
    },

    /**
     * 进入页面
     */
    onLoad: function () {
    },

    /**
     * 
     */
    onShow: function () {
        var that = this;
        if (!app.data.rankLoaded) {//未加载数据则加载
            dian.request({
                loading: true,
                url: '/spriderproject/rank/list',
                success: function (res) {
                    that.setData({
                      dataList: JSON.parse(res.data.data)
                    });
                    app.data.rankLoaded = true;//加载完成
                }
            });
        }
    },

    /**
     *下拉刷新
     */
    onPullDownRefresh: function () {
        wx.stopPullDownRefresh();
        var that = this;
        dian.request({
            loading: true,
            url: '/spriderproject/rank/list',
            success: function (res) {
                that.setData({
                  dataList: JSON.parse(res.data.data)
                });
                app.data.rankLoaded = true;//加载完成
            }
        });
    },

    onShareAppMessage: function () {
        return {
            title: '赞赏排行榜',
            desc: '微信支付开源(小程序+服务器源码)',
            path: '/pages/rank/rank'
        }
    }
})
