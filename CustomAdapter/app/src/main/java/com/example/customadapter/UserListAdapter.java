package com.example.customadapter;

import android.content.Context;
import android.graphics.Color;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashSet;


    public class UserListAdapter extends BaseAdapter {
        Context ctx;
        ArrayList<User> users;

        public UserListAdapter(Context ctx, ArrayList<User> users) {
            this.ctx = ctx;
            this.users = users;
        }

        @Override
        public int getCount() {
            return users.size();
        }

        @Override
        public Object getItem(int position) {
            return users.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            // получаем данные из коллекции
            Date begin = new Date();
            User u = users.get(position);

            // создаём разметку (контейнер)
            convertView = LayoutInflater.from(ctx).
                    inflate(R.layout.useritem, parent, false);
            // получаем ссылки на элементы интерфейса
            ImageView ivUserpic = convertView.findViewById(R.id.userpic);
            convertView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    v.setBackgroundColor(Color.GREEN);
                }
            });
            TextView tvName = convertView.findViewById(R.id.name);
            TextView tvPhone = convertView.findViewById(R.id.phone);

            // задаём содержание
            tvName.setText(u.name);
            tvPhone.setText(u.phoneNumber);
            switch (u.sex) {
                case MAN: ivUserpic.setImageResource(R.drawable.user_man); break;
                case WOMAN: ivUserpic.setImageResource(R.drawable.user_woman); break;
                case UNKNOWN: ivUserpic.setImageResource(R.drawable.user_unknown); break;
            }
            Date finish = new Date();
            Log.d("mytag", "getView time: "+(finish.getTime()-begin.getTime()));
            return convertView;
        }

    public void sortByName() {
        Collections.sort(users, new Comparator<User>() {
            @Override
            public int compare(User user1, User user2) {
                return user1.name.compareToIgnoreCase(user2.name);
            }
        });
        notifyDataSetChanged();
    }

    public void sortBySex() {
        Collections.sort(users, new Comparator<User>() {
            @Override
            public int compare(User user1, User user2) {
                return user1.sex.compareTo(user2.sex);
            }
        });
        notifyDataSetChanged();
    }

    public void sortByPhoneNumber() {
        Collections.sort(users, new Comparator<User>() {
            @Override
            public int compare(User user1, User user2) {
                return user1.phoneNumber.compareTo(user2.phoneNumber);
            }
        });
        notifyDataSetChanged();
    }
    public void addUser(User newUser) {
        users.add(newUser);
        notifyDataSetChanged();
    }
}
