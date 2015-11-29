package com.original.abroadeasy.util;

import android.content.Context;

import com.litesuits.orm.LiteOrm;
import com.litesuits.orm.db.assit.QueryBuilder;
import com.litesuits.orm.db.assit.WhereBuilder;
import com.litesuits.orm.db.model.ConflictAlgorithm;
import com.original.abroadeasy.BuildConfig;
import com.original.abroadeasy.model.HomeItem;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by zengjinlong on 15-11-28.
 */
public class LiteOrmDBUtil {

    public static final String DB_NAME = "abroadEasy.db";
    public static LiteOrm sLiteOrm;

    public static void init(Context context) {
        sLiteOrm = LiteOrm.newSingleInstance(context, DB_NAME);
        if (BuildConfig.DEBUG) {
            sLiteOrm.setDebugged(true);
        }
//        sLiteOrm = LiteOrm.newCascadeInstance(context, DB_NAME);
//        sLiteOrm.setDebugged(true);
    }

    public static LiteOrm getLiteOrm() {
        return sLiteOrm;
    }

    /**
     * 插入一条记录
     *
     * @param t
     */
    public static <T> void insert(T t) {
        sLiteOrm.save(t);
    }

    /**
     * 插入所有记录
     *
     * @param list
     */
    public static <T> void insertAll(List<T> list) {
        sLiteOrm.save(list);
    }

    /**
     * 查询所有
     *
     * @param cla
     * @return
     */
    public static <T> List<T> getQueryAll(Class<T> cla) {
        return sLiteOrm.query(cla);
    }

    /**
     * 查询  某字段 等于 Value的值
     *
     * @param cla
     * @param field
     * @param value
     * @return
     */
    public static <T> List<T> getQueryByWhere(Class<T> cla, String field, String[] value) {
        return sLiteOrm.<T>query(new QueryBuilder(cla).where(field + "=?", value));
    }

    /**
     * 查询  某字段 等于 Value的值  可以指定从1-20，就是分页
     *
     * @param cla
     * @param field
     * @param value
     * @param start
     * @param length
     * @return
     */
    public static <T> List<T> getQueryByWhereLength(Class<T> cla, String field, String[] value, int start, int length) {
        return sLiteOrm.<T>query(new QueryBuilder(cla).where(field + "=?", value).limit(start, length));
    }

    /**
     * 删除所有 某字段等于 Vlaue的值
     *
     * @param cla
     * @param field
     * @param value
     */
    public static <T> void deleteWhere(Class<T> cla, String field, String[] value) {
        sLiteOrm.delete(cla, WhereBuilder.create(cla).where(field + "=?", value));
    }

    /**
     * 删除所有
     *
     * @param cla
     */
    public static <T> void deleteAll(Class<T> cla) {
        sLiteOrm.deleteAll(cla);
    }

    /**
     * 仅在以存在时更新
     *
     * @param t
     */
    public static <T> void update(T t) {
        sLiteOrm.update(t, ConflictAlgorithm.Replace);
    }

    public static <T> void updateALL(List<T> list) {
        sLiteOrm.update(list);
    }

    private static int sTestNum = 0;
    public static void test() {
        LogUtil.d("DB test begin!!!!!!!!!!!!!!!!!!!!!");
        HomeItem item = HomeItem.getTestData(sTestNum++);
        List<HomeItem> list = new ArrayList<HomeItem>();
        for (int i = 0; i < 10; i++) {
            list.add(HomeItem.getTestData(sTestNum++));
        }
        //1、insert
        LiteOrmDBUtil.insert(item);
        //2、insertAll
        LiteOrmDBUtil.insertAll(list);
        //3、getQueryAll
        list = LiteOrmDBUtil.getQueryAll(HomeItem.class);
        LogUtil.d("list:" + list.size());
        for (HomeItem hi : list) {
            LogUtil.d("hi:" + hi.toString());
        }
        //4、getQueryByWhere
        list = LiteOrmDBUtil.getQueryByWhere(HomeItem.class, HomeItem.TITLE, new String[]{"test1"});
        if (list != null && list.size() > 0) {
            LogUtil.d("getQueryByWhere:" + list.size() + "," + list.get(0));
        }
        //5、getQueryByWhereLength
        list = LiteOrmDBUtil.getQueryByWhereLength(HomeItem.class, HomeItem.TITLE, new String[]{"test2"}, 0, 20);
        if (list != null && list.size() > 0) {
            LogUtil.d("getQueryByWhereLength:" + list.size() + "," + list.get(0));
        }
        //6、deleteWhere
        LiteOrmDBUtil.deleteWhere(HomeItem.class, HomeItem.TITLE, new String[]{"test3"});
        //7、deleteAll
//        LiteOrmDBUtil.deleteAll(HomeItem.class);

    }
}
