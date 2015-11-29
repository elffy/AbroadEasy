package com.original.abroadeasy.model;

import com.litesuits.orm.db.annotation.Column;
import com.litesuits.orm.db.annotation.PrimaryKey;
import com.litesuits.orm.db.enums.AssignType;

/**
 * Created by zengjinlong on 15-11-28.
 */
public class BlogItem extends BaseModel {
    @PrimaryKey(AssignType.AUTO_INCREMENT) @Column("_id") protected long id;
}
