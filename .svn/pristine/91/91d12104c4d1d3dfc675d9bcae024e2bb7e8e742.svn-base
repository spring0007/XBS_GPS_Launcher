package cn.com.waterworld.alarmclocklib.db;

import java.util.List;

/**
 * Created by wangfeng on 2018/6/11.
 */
public abstract class BaseDao<T> {

    //插入
    public abstract void insert(T t);

    //查询所有
    public abstract List<T> queryAll();

    //根据某个条件查询一列
    public abstract T query(T t);

    //根据某个条件查询多列
    public abstract List<T> queryList(T t);

    //修改
    public abstract boolean updata(T t);

    //根据某一条件删除
    public abstract boolean delete(T t);

    //删除全部
    public abstract boolean deleteAll();
}
