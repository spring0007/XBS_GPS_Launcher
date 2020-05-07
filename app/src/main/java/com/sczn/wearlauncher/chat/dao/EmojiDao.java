package com.sczn.wearlauncher.chat.dao;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.sczn.wearlauncher.chat.model.EmojiModel;

import java.util.ArrayList;
import java.util.List;

import cn.com.waterworld.alarmclocklib.db.BaseDao;
import cn.com.waterworld.alarmclocklib.db.DBHelper;
import cn.com.waterworld.alarmclocklib.db.TableConfig;

/**
 * Created by k.liang on 2018/4/19 19:27
 */
public class EmojiDao extends BaseDao {

    private SQLiteDatabase db;

    public EmojiDao() {
        super();
        db = DBHelper.getInstance().getWritableDatabase();
    }

    @Override
    public void insert(Object o) {
        if (o instanceof EmojiModel) {
            EmojiModel emojiBean = (EmojiModel) o;
            if (db != null) {
                ContentValues contentValues = new ContentValues();
                contentValues.put(TableConfig.EMOJI.EMOJI_ID, emojiBean.getEmojiId());
                contentValues.put(TableConfig.EMOJI.EMOJI_NAME, emojiBean.getEmojiName());
                contentValues.put(TableConfig.EMOJI.EMOJI_PACKAGE_ID, emojiBean.getPackageId());
                contentValues.put(TableConfig.EMOJI.EMOJI_PACKAGE_NAME, emojiBean.getPackageName());
                contentValues.put(TableConfig.EMOJI.EMOJI_PIC_URL, emojiBean.getEmojiPicUrl());
                contentValues.put(TableConfig.EMOJI.EMOJI_THUMB_URL, emojiBean.getEmojiThumbUrl());
                contentValues.put(TableConfig.SAVE_TIME, System.currentTimeMillis() / 1000);
                db.replace(TableConfig.EMOJI.EMOJI_TABLE, null, contentValues);
            }
        }
    }

    @Override
    public List queryAll() {
        List<EmojiModel> list = new ArrayList<>();
        if (db != null) {
            Cursor cursor = null;
            try {
                cursor = db.query(TableConfig.EMOJI.EMOJI_TABLE, null, null, null, null, null, null);
                if (cursor != null && cursor.getCount() > 0) {
                    while (cursor.moveToNext()) {
                        int packageIDindex = cursor.getColumnIndex(TableConfig.EMOJI.EMOJI_PACKAGE_ID);
                        String packageid = cursor.getString(packageIDindex);

                        int nameIndex = cursor.getColumnIndex(TableConfig.EMOJI.EMOJI_NAME);
                        String name = cursor.getString(nameIndex);

                        int packageNameIndex = cursor.getColumnIndex(TableConfig.EMOJI.EMOJI_PACKAGE_NAME);
                        String packageName = cursor.getString(packageNameIndex);

                        int idIndex = cursor.getColumnIndex(TableConfig.EMOJI.EMOJI_ID);
                        String id = cursor.getString(idIndex);

                        int picIndex = cursor.getColumnIndex(TableConfig.EMOJI.EMOJI_PIC_URL);
                        String pic = cursor.getString(picIndex);

                        int thumbIndex = cursor.getColumnIndex(TableConfig.EMOJI.EMOJI_THUMB_URL);
                        String thumb = cursor.getString(thumbIndex);

                        EmojiModel emojiModel = new EmojiModel();
                        emojiModel.setEmojiId(id);
                        emojiModel.setEmojiName(name);
                        emojiModel.setPackageId(packageid);
                        emojiModel.setPackageName(packageName);
                        emojiModel.setEmojiPicUrl(pic);
                        emojiModel.setEmojiThumbUrl(thumb);
                        list.add(emojiModel);
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                if (cursor != null) {
                    cursor.close();
                }
            }
        }
        return list;
    }

    @Override
    public Object query(Object o) {
        return null;
    }

    @Override
    public List queryList(Object o) {
        return null;
    }

    @Override
    public boolean updata(Object o) {
        return false;
    }

    @Override
    public boolean delete(Object o) {
        return false;
    }

    @Override
    public boolean deleteAll() {
        return false;
    }
}
