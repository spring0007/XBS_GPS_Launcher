package com.sczn.wearlauncher.contact;

import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.provider.ContactsContract;

import com.google.gson.reflect.TypeToken;
import com.sczn.wearlauncher.app.LauncherApp;
import com.sczn.wearlauncher.app.MxyLog;
import com.sczn.wearlauncher.base.util.StringUtils;
import com.sczn.wearlauncher.socket.command.CommandHelper;
import com.sczn.wearlauncher.socket.command.bean.Linkman;
import com.sczn.wearlauncher.socket.command.obtain.BaseObtain;
import com.sczn.wearlauncher.util.GsonHelper;
import com.sczn.wearlauncher.util.SystemPermissionUtil;

import org.litepal.LitePal;

import java.util.List;

/**
 * Description:通讯录工具
 * Created by Bingo on 2019/1/11.
 */
public class ContactHelper {
    private static ContactHelper helper;

    /**
     * 单例
     *
     * @return
     */
    public static ContactHelper getInstance() {
        if (null == helper) {
            synchronized (ContactHelper.class) {
                if (null == helper) {
                    helper = new ContactHelper();
                }
            }
        }
        return helper;
    }

    /**
     * 一个添加联系人信息的例子
     *
     * @param mContext
     * @param name
     * @param phoneNumber
     */
    public void addContact(Context mContext, String name, String phoneNumber) {
        // 创建一个空的ContentValues
        ContentValues values = new ContentValues();

        // 向RawContacts.CONTENT_URI空值插入，
        // 先获取Android系统返回的rawContactId
        // 后面要基于此id插入值
        Uri rawContactUri = mContext.getContentResolver().insert(ContactsContract.RawContacts.CONTENT_URI, values);
        long rawContactId = ContentUris.parseId(rawContactUri);
        values.clear();

        values.put(ContactsContract.Contacts.Data.RAW_CONTACT_ID, rawContactId);
        // 内容类型
        values.put(ContactsContract.Contacts.Data.MIMETYPE, ContactsContract.CommonDataKinds.StructuredName.CONTENT_ITEM_TYPE);
        // 联系人名字
        values.put(ContactsContract.CommonDataKinds.StructuredName.GIVEN_NAME, name);
        // 向联系人URI添加联系人名字
        Uri Datauri = Uri.parse("content://com.android.contacts/data");
        mContext.getContentResolver().insert(Datauri, values);
        values.clear();

        values.put(ContactsContract.Contacts.Data.RAW_CONTACT_ID, rawContactId);
        values.put(ContactsContract.Contacts.Data.MIMETYPE, ContactsContract.CommonDataKinds.Phone.CONTENT_ITEM_TYPE);
        // 联系人的电话号码
        values.put(ContactsContract.CommonDataKinds.Phone.NUMBER, phoneNumber);
        // 电话类型
        values.put(ContactsContract.CommonDataKinds.Phone.TYPE, ContactsContract.CommonDataKinds.Phone.TYPE_MOBILE);
        // 向联系人电话号码URI添加电话号码
        mContext.getContentResolver().insert(Datauri, values);
        values.clear();

        values.put(ContactsContract.Contacts.Data.RAW_CONTACT_ID, rawContactId);
        values.put(ContactsContract.Contacts.Data.MIMETYPE, ContactsContract.CommonDataKinds.Email.CONTENT_ITEM_TYPE);
        // 联系人的Email地址
        values.put(ContactsContract.CommonDataKinds.Email.DATA, "xxxx@xxx.com");
        // 电子邮件的类型
        values.put(ContactsContract.CommonDataKinds.Email.TYPE, ContactsContract.CommonDataKinds.Email.TYPE_WORK);
        // 向联系人Email URI添加Email数据
        mContext.getContentResolver().insert(Datauri, values);
    }

    /**
     * 清空系统通信录数据
     *
     * @param mContext
     */
    public void clearContact(Context mContext) {
        ContentResolver cr = mContext.getContentResolver();
        // 查询contacts表的所有记录
        Cursor cursor = cr.query(ContactsContract.Contacts.CONTENT_URI, null,
                null, null, null);
        if (cursor == null) {
            return;
        }
        // 如果记录不为空
        if (cursor.getCount() > 0) {
            // 游标初始指向查询结果的第一条记录的上方，执行moveToNext函数会判断
            // 下一条记录是否存在，如果存在，指向下一条记录。否则，返回false。
            while (cursor.moveToNext()) {
                //  String rawContactId = "";
                // 从Contacts表当中取得ContactId
//                  String id = cursor.getString(cursor
//                          .getColumnIndex(ContactsContract.Contacts._ID));

                String name = cursor.getString(cursor
                        .getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME));
                //根据姓名求id
                Uri uri = Uri.parse("content://com.android.contacts/raw_contacts");

                Cursor cursor1 = cr.query(uri, new String[]{ContactsContract.Contacts.Data._ID}, "display_name=?", new String[]{name}, null);
                if (cursor1 == null) {
                    cursor.close();
                    return;
                }
                if (cursor1.moveToFirst()) {
                    int id = cursor1.getInt(0);
                    //根据id删除data中的相应数据
                    cr.delete(uri, "display_name=?", new String[]{name});
                    uri = Uri.parse("content://com.android.contacts/data");
                    cr.delete(uri, "raw_contact_id=?", new String[]{id + ""});
                }
                cursor1.close();
            }
        }
        cursor.close();
    }

    /**
     * 获取通信录处理
     * 并且更新本地的数据
     *
     * @param json
     */
    public void logic(String json) {
        List<Linkman> mDataList;
        MxyLog.d("logic", "获取通信录:" + json);
        final BaseObtain<List<Linkman>> dataBeans = GsonHelper.getInstance().getGson().fromJson(json, new TypeToken<BaseObtain<List<Linkman>>>() {
        }.getType());
        if (dataBeans != null && dataBeans.getData() != null && dataBeans.getData().size() > 0) {
            mDataList = dataBeans.getData();
            LitePal.deleteAll(Linkman.class);
            LitePal.saveAll(mDataList);
            StringBuilder whileList = new StringBuilder();
            StringBuilder sosList = new StringBuilder();
            StringBuilder listenerList = new StringBuilder();
            clearContact(LauncherApp.getAppContext());//清空
            for (Linkman linkman : mDataList) {
                String role = linkman.getRole();
                whileList.append(linkman.getPhone()).append(",");
                //--->>>1白名单: 1501347107,1501347107,1501347107,1501347107
                //--->>>2紧急通话权限: 1501347107,1501347107,1501347107,1501347107
                //--->>>3单向聆听权限: 1501347107,1501347107,1501347107,1501347107
                if (role != null && role.contains("2")) {
                    sosList.append(linkman.getPhone()).append(",");
                }
                if (role != null && role.contains("3")) {
                    listenerList.append(linkman.getPhone()).append(",");
                }
                //再添加联系人
                addContact(LauncherApp.getAppContext(), linkman.getName(), linkman.getPhone());
            }
            if (whileList.length() > 0) {
                whileList.deleteCharAt(whileList.length() - 1);
            }
            if (sosList.length() > 0) {
                sosList.deleteCharAt(sosList.length() - 1);
            }
            if (listenerList.length() > 0) {
                listenerList.deleteCharAt(listenerList.length() - 1);
            }
            SystemPermissionUtil.saveWhitelistData(whileList.toString());
            SystemPermissionUtil.saveSosData(sosList.toString());
            SystemPermissionUtil.saveOneWayListenerData(listenerList.toString());
        }
    }

    /**
     * 解析返回电话本
     *
     * @param result
     * @param id     区分是前5个还是后5个
     */
    public void logicGprs(String result, int id) {
        if (result.contains(",")) {
            String[] data = result.split(",");
            clearContact(LauncherApp.getAppContext());
            LitePal.deleteAll("Linkman", "watchId = ?", String.valueOf(id));
            for (int i = 1; i < data.length; i++) {//去掉第一个字符
                MxyLog.w("logicGprs", i + "," + data[i]);
                if (i % 2 != 0 && !StringUtils.isEmpty(data[i])) {
                    Linkman l = new Linkman();
                    l.setPhone(data[i]);
                    l.setName(CommandHelper.hexUnicode2String(data[i + 1]));
                    l.setWatchId(id);
                    l.saveOrUpdate("phone = ?", l.getPhone());
                    addContact(LauncherApp.getAppContext(), l.getName(), l.getPhone());
                }
            }
        }
    }
}
