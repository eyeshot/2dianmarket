Page({
  data: {
   // animationData: {}
    result: ['name1', 'name2', 'name3', 'name4'],
    isHidden: [false, false, false, false],
    source: [],
    beforeIndex : -1
  },
  onShow: function () {
  },
  doAni: function(e)
  {
   var id = e.currentTarget.dataset.id;
   console.info("beforeIndex: " + this.data.beforeIndex);
   console.info("id : " + id);
   console.log('doAni')
   
   if (this.data.beforeIndex != -1)
   {
     var backAni = wx.createAnimation({
       duration: 100,
       timingFunction: 'ease',
     })
     backAni.translateX(0).step()
     var beforeParam = {};
     var str = "source[" + this.data.beforeIndex + "]";
     beforeParam[str] = backAni;
     this.setData(beforeParam);

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
         beforeIndex : id
       })
     
     console.info("beforeIndex: " + this.data.beforeIndex);
   }
  
   var animation = wx.createAnimation({
     duration: 100,
     timingFunction: 'ease',
   })
   this.animation = animation
   this.animation.translateX(30).step()
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

  viewTap: function(e)
  {
     var target = e.currentTarget;
     this.animation.translate(30).step()
     this.animation.scale(2, 2).rotate(45).step()
     
     this.setData({
       target : this.animation.export()
     })
     
  },
  rotateAndScale: function () {
    // 旋转同时放大
    this.animation.rotate(45).scale(2, 2).step()
    this.setData({
      animationData: this.animation.export()
    })
  },
  rotateThenScale: function () {
    // 先旋转后放大
    this.animation.rotate(45).step()
    this.animation.scale(2, 2).step()
    this.setData({
      animationData: this.animation.export()
    })
  },
  rotateAndScaleThenTranslate: function () {
    // 先旋转同时放大，然后平移
    this.animation.rotate(45).scale(2, 2).step()
    this.animation.translate(100, 100).step({ duration: 1000 })
    this.setData({
      animationData: this.animation.export()
    })
  }
})