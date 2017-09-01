package com.gurezkiygmail.foxhunting;

import android.app.Application;
import android.graphics.Bitmap;
import android.util.Base64;

import com.nostra13.universalimageloader.core.DisplayImageOptions;

import java.io.ByteArrayOutputStream;

/**
 * Created by gurez on 12.08.2017.
 */
public class Constants {
    public static DisplayImageOptions options;
    public static final int SELECT_PHOTO = 1;
    public static final int CAMERA = 2;
    public static boolean appendNotificationMessages = true;
    public static final int NOTIFICATION_ID = 100;
    public static final int NOTIFICATION_ID_BIG_IMAGE = 101;

    public static String getStringImage(Bitmap bmp){
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bmp.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        byte[] imageBytes = baos.toByteArray();
        String encodedImage = Base64.encodeToString(imageBytes, Base64.DEFAULT);
        return encodedImage;
    }
    public static String getErrorMessage(String id){
        String response = "Нет соединения с сервером";
        if(id!=null)
        switch (id){
            case  "-1":{
                response = "Ошибка сервера";
                break;
            }
            case "3":{
                response = "Пароль должен соержать хотябы 1 цифру";
                break;
            }
            case "4":{
                response = "Пароль должен содержать хотябы 1 символ";
                break;
            }
            case "5":{
                response = "Неверный контроллер";
                break;
            }
            case "6":{
                response = "Неверное действие";
                break;
            }
            case "7":{
                response = "Пользователь с этим номером телефона уже зарегистрирован";
                break;
            }
            case "8":{
                response = "Длина имени менее 5 символов";
                break;
            }
            case "9":{
                response = "Некорректная дата";
                break;
            }
            case "10":{
                response = "Неверный идентификатор устройства";
                break;
            }
            case "11":{
                response = "Некорректный номер телефона";
                break;
            }
            case "12":{
                response = "Неверный логин и/или пароль";
                break;
            }
            case "13":{
                response = "Код недействителен";
                break;
            }
            default:break;
        }
        return response;
    }
    public static class URL{
        //public static final String HOST = "http://192.168.1.3/api/";
        public static final String HOST = "http://grandchili.by/api/";
        public static  final String REGISTRATION = HOST + "users/add";
        public static  final String AUTHORIZE = HOST + "users/auth";
        public static  final String SEND_CODE = HOST + "users/activation";
        public static  final String UPDATE = HOST + "users/update";
        public static  final String LOGOUT = HOST + "users/logout";
        public static  final String UPLOAD = HOST + "files/upload";
    }
}
