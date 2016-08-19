package com.wenen.literead.model.image;

import java.io.Serializable;
import java.util.List;

/**
 * Created by Wen_en on 16/8/15.
 */
public class ImageModel implements Serializable{

    /**
     * count : 4932
     * fcount : 0
     * galleryclass : 1
     * id : 10
     * img : /ext/150714/832903f1079ad2a74867e5cbd9dcf1a2.jpg
     * list : [{"gallery":10,"id":187,"src":"/ext/150714/832903f1079ad2a74867e5cbd9dcf1a2.jpg"},{"gallery":10,"id":188,"src":"/ext/150714/379ef82a7395be83084aaf2df792f1aa.jpg"},{"gallery":10,"id":189,"src":"/ext/150714/456dee4283fe7ae07e623802012de30d.jpg"},{"gallery":10,"id":190,"src":"/ext/150714/acd51dddc3ee6ab973050b8a969d34fa.jpg"},{"gallery":10,"id":191,"src":"/ext/150714/a74b06ae07807d4cf861d3662a50dcf8.jpg"},{"gallery":10,"id":192,"src":"/ext/150714/fc0ddec9f438f2b383a9c01483cbf885.jpg"},{"gallery":10,"id":193,"src":"/ext/150714/2b89ffba4d98b137e924504205f4d61b.jpg"},{"gallery":10,"id":194,"src":"/ext/150714/d82e9aaae6e2eb2b62a9cca5d3d993e4.jpg"},{"gallery":10,"id":195,"src":"/ext/150714/068a7c6994c8e669878221ffd5dec07c.jpg"},{"gallery":10,"id":196,"src":"/ext/150714/e3616cf7cf18e63e5facec1a20535ba6.jpg"},{"gallery":10,"id":197,"src":"/ext/150714/df3ea80d7ecbd8f025d5ec31635ccdbf.jpg"},{"gallery":10,"id":198,"src":"/ext/150714/a2c23f46b5a1b140cd028c18b5745599.jpg"}]
     * rcount : 0
     * size : 12
     * status : true
     * time : 1436876134000
     * title : 美腿丝袜美女
     * url : http://www.tngou.net/tnfs/show/10
     */

    public int count;
    public int fcount;
    public int galleryclass;
    public int id;
    public String img;
    public int rcount;
    public int size;
    public boolean status;
    public long time;
    public String title;
    public String url;
    /**
     * gallery : 10
     * id : 187
     * src : /ext/150714/832903f1079ad2a74867e5cbd9dcf1a2.jpg
     */

    public List<ListEntity> list;

    public static class ListEntity implements Serializable{
        public int gallery;
        public int id;
        public String src;
    }
}
