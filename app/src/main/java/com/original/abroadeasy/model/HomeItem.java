package com.original.abroadeasy.model;

import com.litesuits.orm.db.annotation.Column;
import com.litesuits.orm.db.annotation.Table;

/**
 * Created by zengjinlong on 15-11-28.
 */
@Table("homeItems")
public class HomeItem extends BaseModel {
    public static final String TITLE = "title";
    @Column(TITLE) public String title;
    @Column("desc") public String desc;
    @Column("author") public String author;
    @Column("imageUrl") public String imageUrl;
    @Column("articleUrl") public String articleUrl;
    @Column("timeStamp") public long timeStamp;
    @Column("imageWidth") public int imageWidth;
    @Column("imageHeight") public int imageHeight;

    public static HomeItem getTestData(int testNum) {
        HomeItem item = new HomeItem();
        item.title = "test" + testNum;
        item.desc = "this is item " + testNum;
        item.author = "elffy";
        item.imageUrl = "www.pic/num/" + testNum;
        return item;
    }
    public String toString() {
        return title + "," + desc + "," + author + "," + imageUrl + "," + timeStamp;
    }
}
