package com.wenen.literead.model.video;

import java.util.List;

/**
 * Created by Wen_en on 16/8/19.
 */
public class VideoModel {

    /**
     * error : 0
     * data : {"room_id":"105441","room_thumb":"https://rpic.douyucdn.cn/z1608/19/13/105441_160819135116.jpg","cate_id":"1","cate_name":"英雄联盟","room_name":"陛老师：上路霸主 你们看我吊不","room_status":"1","owner_name":"胖丁大陛哥丶","avatar":"http://apic.douyucdn.cn/upload/avatar/face/201605/08/1a0926c6bd9ccf65b9d55c8e6b8ba51c_big.jpg?rltime","online":10210,"owner_weight":"3.86t","fans_num":"26091","start_time":"2016-08-19 12:17","gift":[{"id":"257","name":"狂欢巡游花车","type":"2","pc":500,"gx":5000,"desc":"赠送网站广播并派送出神秘宝箱","intro":"巡游奥运狂欢里约","mimg":"http://staticlive.douyucdn.cn/upload/dygift/1608/6429e98e522e19910e931018bc481b52.png","himg":"http://staticlive.douyucdn.cn/upload/dygift/1608/42e65637a526b1a653ff20ddf96c4255.gif"},{"id":"256","name":"扬帆起航","type":"2","pc":100,"gx":1000,"desc":"赠送房间广播","intro":"迎风斗浪、战胜自然、挑战自我","mimg":"http://staticlive.douyucdn.cn/upload/dygift/1608/4c6a9bcb5248f42e046410c2fe249af1.png","himg":"http://staticlive.douyucdn.cn/upload/dygift/1608/b918410cfa8ba3b0ea1b5237e4f68659.gif"},{"id":"255","name":"冠军奖杯","type":"2","pc":6,"gx":60,"desc":"","intro":"献给冠军的大礼","mimg":"http://staticlive.douyucdn.cn/upload/dygift/1608/c613b19a87cbc3fa3c99b1e43e1972c8.png","himg":"http://staticlive.douyucdn.cn/upload/dygift/1608/fd9bbbd4eb5b486e5ae3a4a2acd9c46b.gif"},{"id":"254","name":"奥运火炬","type":"2","pc":0.2,"gx":2,"desc":"","intro":"奥运火炬传递给你","mimg":"http://staticlive.douyucdn.cn/upload/dygift/1608/70bc42916b39abd53987440950e60ab7.png","himg":"http://staticlive.douyucdn.cn/upload/dygift/1608/f074a0fcda6fd048182a345469e7cee3.gif"},{"id":"253","name":"金牌","type":"2","pc":0.1,"gx":1,"desc":"","intro":"奥运金牌送给你","mimg":"http://staticlive.douyucdn.cn/upload/dygift/1608/834f7de300463c94f862eaac56068622.png","himg":"http://staticlive.douyucdn.cn/upload/dygift/1608/28aa3e5ba36401881bb0d2de1171e616.gif"},{"id":"252","name":"巴西烤肉","type":"1","pc":100,"gx":1,"desc":"","intro":"吃烤肉补体能参奥运","mimg":"http://staticlive.douyucdn.cn/upload/dygift/1608/6525d858a732c590f5bc6dc19d6ae9d6.png","himg":"http://staticlive.douyucdn.cn/upload/dygift/1608/feb8a85bd83ab346e51aa9fbb72c7648.png"}]}
     */

    public int error;
    /**
     * room_id : 105441
     * room_thumb : https://rpic.douyucdn.cn/z1608/19/13/105441_160819135116.jpg
     * cate_id : 1
     * cate_name : 英雄联盟
     * room_name : 陛老师：上路霸主 你们看我吊不
     * room_status : 1
     * owner_name : 胖丁大陛哥丶
     * avatar : http://apic.douyucdn.cn/upload/avatar/face/201605/08/1a0926c6bd9ccf65b9d55c8e6b8ba51c_big.jpg?rltime
     * online : 10210
     * owner_weight : 3.86t
     * fans_num : 26091
     * start_time : 2016-08-19 12:17
     * gift : [{"id":"257","name":"狂欢巡游花车","type":"2","pc":500,"gx":5000,"desc":"赠送网站广播并派送出神秘宝箱","intro":"巡游奥运狂欢里约","mimg":"http://staticlive.douyucdn.cn/upload/dygift/1608/6429e98e522e19910e931018bc481b52.png","himg":"http://staticlive.douyucdn.cn/upload/dygift/1608/42e65637a526b1a653ff20ddf96c4255.gif"},{"id":"256","name":"扬帆起航","type":"2","pc":100,"gx":1000,"desc":"赠送房间广播","intro":"迎风斗浪、战胜自然、挑战自我","mimg":"http://staticlive.douyucdn.cn/upload/dygift/1608/4c6a9bcb5248f42e046410c2fe249af1.png","himg":"http://staticlive.douyucdn.cn/upload/dygift/1608/b918410cfa8ba3b0ea1b5237e4f68659.gif"},{"id":"255","name":"冠军奖杯","type":"2","pc":6,"gx":60,"desc":"","intro":"献给冠军的大礼","mimg":"http://staticlive.douyucdn.cn/upload/dygift/1608/c613b19a87cbc3fa3c99b1e43e1972c8.png","himg":"http://staticlive.douyucdn.cn/upload/dygift/1608/fd9bbbd4eb5b486e5ae3a4a2acd9c46b.gif"},{"id":"254","name":"奥运火炬","type":"2","pc":0.2,"gx":2,"desc":"","intro":"奥运火炬传递给你","mimg":"http://staticlive.douyucdn.cn/upload/dygift/1608/70bc42916b39abd53987440950e60ab7.png","himg":"http://staticlive.douyucdn.cn/upload/dygift/1608/f074a0fcda6fd048182a345469e7cee3.gif"},{"id":"253","name":"金牌","type":"2","pc":0.1,"gx":1,"desc":"","intro":"奥运金牌送给你","mimg":"http://staticlive.douyucdn.cn/upload/dygift/1608/834f7de300463c94f862eaac56068622.png","himg":"http://staticlive.douyucdn.cn/upload/dygift/1608/28aa3e5ba36401881bb0d2de1171e616.gif"},{"id":"252","name":"巴西烤肉","type":"1","pc":100,"gx":1,"desc":"","intro":"吃烤肉补体能参奥运","mimg":"http://staticlive.douyucdn.cn/upload/dygift/1608/6525d858a732c590f5bc6dc19d6ae9d6.png","himg":"http://staticlive.douyucdn.cn/upload/dygift/1608/feb8a85bd83ab346e51aa9fbb72c7648.png"}]
     */

    public DataEntity data;

    public static class DataEntity {
        public String room_id;
        public String room_thumb;
        public String cate_id;
        public String cate_name;
        public String room_name;
        public String room_status;
        public String owner_name;
        public String avatar;
        public int online;
        public String owner_weight;
        public String fans_num;
        public String start_time;
        /**
         * id : 257
         * name : 狂欢巡游花车
         * type : 2
         * pc : 500
         * gx : 5000
         * desc : 赠送网站广播并派送出神秘宝箱
         * intro : 巡游奥运狂欢里约
         * mimg : http://staticlive.douyucdn.cn/upload/dygift/1608/6429e98e522e19910e931018bc481b52.png
         * himg : http://staticlive.douyucdn.cn/upload/dygift/1608/42e65637a526b1a653ff20ddf96c4255.gif
         */

        public List<GiftEntity> gift;

        public static class GiftEntity {
            public String id;
            public String name;
            public String type;
            public int pc;
            public int gx;
            public String desc;
            public String intro;
            public String mimg;
            public String himg;
        }
    }
}
