package com.wenen.literead.model.image;

import java.io.Serializable;
import java.util.List;

/**
 * Created by Wen_en on 16/8/15.
 */
public class ImageListModel implements Serializable {


    /**
     * status : true
     * total : 885
     * tngou : [{"count":48,"fcount":0,"galleryclass":4,"id":899,"img":"/ext/160814/109dfeb558fbc9e920091daca2bea134.jpg","rcount":0,"size":9,"time":1471183310000,"title":"大胸美女酥胸美腿性感街拍"},{"count":26,"fcount":0,"galleryclass":5,"id":898,"img":"/ext/160814/3b984da341cb3090b8abf288a22fe0d6.jpg","rcount":0,"size":8,"time":1471183265000,"title":"性感长腿美女职业装美腿"},{"count":34,"fcount":0,"galleryclass":2,"id":897,"img":"/ext/160814/1370a4e14031bc50835cbafd5be5be5c.jpg","rcount":0,"size":10,"time":1471183225000,"title":"性感长腿美女居家背心写真美胸诱人"},{"count":29,"fcount":0,"galleryclass":3,"id":896,"img":"/ext/160814/99373edd89598c8737fb6094ffe50157.jpg","rcount":0,"size":11,"time":1471183186000,"title":"紧身裙美女肉色丝袜漂亮美腿"},{"count":30,"fcount":0,"galleryclass":1,"id":895,"img":"/ext/160814/847654339e7a2a6bca19d1a9d412ed31.jpg","rcount":0,"size":6,"time":1471183138000,"title":"性感制服美女眼神迷离香肩诱人"},{"count":27,"fcount":0,"galleryclass":6,"id":894,"img":"/ext/160814/90c03bde2e210d1bd82cb69621f3682b.jpg","rcount":0,"size":9,"time":1471183071000,"title":"长腿美女印花短裙美腿性感"},{"count":312,"fcount":0,"galleryclass":1,"id":893,"img":"/ext/160812/b7e2dcca4a1637d901fc8b493cfb223f.jpg","rcount":0,"size":16,"time":1471002043000,"title":"翘臀美女王可欣性感私房情趣美照"},{"count":91,"fcount":0,"galleryclass":5,"id":892,"img":"/ext/160812/9734d32a64db95d78e3564125aecef6b.jpg","rcount":0,"size":9,"time":1471001991000,"title":"美女秘书蕾丝内衣极品气质性感"},{"count":69,"fcount":0,"galleryclass":4,"id":891,"img":"/ext/160812/f9ad9778a3c196272b61de773bb8d9a4.jpg","rcount":0,"size":13,"time":1471001914000,"title":"甜美秘书Beautyleg粉红连衣裙美腿"},{"count":137,"fcount":0,"galleryclass":2,"id":890,"img":"/ext/160812/1b5cae335ebff81c5f7781993d3778e6.jpg","rcount":0,"size":10,"time":1471001863000,"title":"韩系性感美女街头警察制服诱人"},{"count":137,"fcount":0,"galleryclass":3,"id":889,"img":"/ext/160812/d623a8430478eeca56a1006308faa750.jpg","rcount":0,"size":10,"time":1471001817000,"title":"长腿美女黑丝袜性感私拍美女"},{"count":252,"fcount":0,"galleryclass":3,"id":888,"img":"/ext/160810/9c5d36caec539a7be7e356af44c019be.jpg","rcount":0,"size":17,"time":1470835672000,"title":"夏晴Miso黑色包屁裙黑色丝袜美腿美女"},{"count":383,"fcount":0,"galleryclass":1,"id":887,"img":"/ext/160810/56e5aa11cdb36f957637159ab0c22d2b.jpg","rcount":0,"size":9,"time":1470835598000,"title":"极品女神City酥胸半露性感美腿翘臀撩人"},{"count":140,"fcount":0,"galleryclass":5,"id":886,"img":"/ext/160810/6f719cc43f193ee00b19892192cf0c97.jpg","rcount":0,"size":11,"time":1470835452000,"title":"性感熟女极品美腿写真"},{"count":122,"fcount":0,"galleryclass":4,"id":885,"img":"/ext/160810/6f89b062e9a4b58bc76159e0cdd3d02f.jpg","rcount":0,"size":11,"time":1470835344000,"title":"性感少妇极品黑丝性感写真"},{"count":98,"fcount":0,"galleryclass":4,"id":884,"img":"/ext/160810/2ce480815e6c71aa344ddda444d9e59f.jpg","rcount":0,"size":11,"time":1470835269000,"title":"格子短裙性感长腿美女小雪制服"},{"count":66,"fcount":0,"galleryclass":5,"id":883,"img":"/ext/160810/a6db45e7334cea053f368a6bca694793.jpg","rcount":0,"size":9,"time":1470835149000,"title":"妖艳美女性感长腿私房撩人"},{"count":187,"fcount":0,"galleryclass":1,"id":882,"img":"/ext/160810/5cd1505e190c8e58b05393dc8e02c4aa.jpg","rcount":0,"size":8,"time":1470835085000,"title":"火辣美女私房性感撩人秀美胸长腿"},{"count":57,"fcount":0,"galleryclass":4,"id":881,"img":"/ext/160810/543d8cfe6e8ce1c3f5bad84b8ed80fd3.jpg","rcount":0,"size":9,"time":1470834971000,"title":"性感女神黑丝美腿极品性感图片"},{"count":110,"fcount":0,"galleryclass":3,"id":880,"img":"/ext/160810/3894766e68bf67508592406d23a9fd27.jpg","rcount":0,"size":15,"time":1470834735000,"title":"台湾美女腿模小雪Winnie丝袜美腿"}]
     */

    public boolean status;
    public int total;
    /**
     * count : 48
     * fcount : 0
     * galleryclass : 4
     * id : 899
     * img : /ext/160814/109dfeb558fbc9e920091daca2bea134.jpg
     * rcount : 0
     * size : 9
     * time : 1471183310000
     * title : 大胸美女酥胸美腿性感街拍
     */

    public List<TngouEntity> tngou;

    public static class TngouEntity implements Serializable{
        public int count;
        public int fcount;
        public int galleryclass;
        public int id;
        public String img;
        public int rcount;
        public int size;
        public long time;
        public String title;
    }
}
