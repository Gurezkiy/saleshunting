package com.gurezkiygmail.foxhunting;

/**
 * Created by gurez on 12.08.2017.
 */
public class User {
    /*id: "10",
    name: "Nick Gurezkiy",
    phone: "375292696923",
    birthday: "814334400",
    avatar: "192.168.1.2/uploads/images/deactivated.jpg",
    access_level: "0"*/

    public int id;
    public String name;
    public String phone;
    public long birthday;
    public String avatar;
    public int access_level;
    public  int pet;
    public float max_sale;
    public  int count_sales;
    public User(String _id, String _name, String _phone, String _birthday, String _avatar, String _access_level, String _pet, String _max_sale,String _count_sales){
        try{
            this.id = Integer.parseInt(_id);
            this.name = _name;
            this.phone = _phone;
            this.birthday = Long.parseLong(_birthday);
            this.avatar = _avatar;
            this.access_level = Integer.parseInt(_access_level);
            this.pet = Integer.parseInt(_pet);
            this.max_sale = Float.parseFloat(_max_sale);
            this.count_sales = Integer.parseInt(_count_sales);
        }catch (Exception e){}
    }

    public void setPet(int pet) {
        this.pet = pet;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setAvatar(String _avatar){
        this.avatar = _avatar;
    }
}
