package com.cdx.example.viewpager;

import com.cdx.example.viewpager.activity.ViewPagerKnowledgeActivity;
import com.cdx.example.viewpager.activity.ViewPagerTest1Activity;

import java.util.ArrayList;

import apis.amapv2.com.listviewlibrary.activity.BaseListActivty;
import apis.amapv2.com.listviewlibrary.bean.ItemObject;

public class MainActivity extends BaseListActivty {

    @Override
    protected void initData() {
        ArrayList<ItemObject> data = new ArrayList<>();
        data.add(new ItemObject("ViewPager的基础知识",ViewPagerKnowledgeActivity.class));
        data.add(new ItemObject("ViewPager实现图片,文字，小圆点切换",ViewPagerTest1Activity.class));

        mMyListView.setData(data);
    }
}
