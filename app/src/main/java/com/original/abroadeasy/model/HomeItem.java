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
    //@Column("desc") public String desc;
    //@Column("author") public String author;
    @Column("imageUrl") public String imageUrl;
    //@Column("articleUrl") public String articleUrl;
    //@Column("timeStamp") public long timeStamp;
    @Column("average") public String average;
    @Column("description") public String description;

    public static HomeItem getTestData(int testNum) {
        HomeItem item = new HomeItem();
        item.title = "test" + testNum;
        item.average = "this is item " + testNum;
        item.description = "elffy";
        item.imageUrl = "www.pic/num/" + testNum;
        return item;
    }
    public String toString() {
        return title + "," +  average + "," + imageUrl + "," + description;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setAverage(String average) {
        this.average = average;
    }

    public String getAverage() {
        return average;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void fillDatas(String title, String imageUrl, String average,
                          String description) {
        setAverage(average);
        setTitle(title);
        setDescription(description);
        setImageUrl(imageUrl);
    }
}
