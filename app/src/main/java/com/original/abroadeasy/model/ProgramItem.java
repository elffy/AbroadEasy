package com.original.abroadeasy.model;

import com.litesuits.orm.db.annotation.Column;
import com.litesuits.orm.db.annotation.Table;

/**
 * Created by zengjinlong on 15-12-13.
 */
@Table("homeItems")
public class ProgramItem extends BaseModel {
    @Column("id") public int id;
    @Column("name") public String name;
    @Column("imageUri") public String image;

    public String toString() {
        return id + "," + name + "," + image;
    }
}
