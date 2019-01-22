package com.cdx.example.viewpager.activity;

import android.content.Context;
import android.os.Handler;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.cdx.example.viewpager.R;

public class ViewPagerTest1Activity extends AppCompatActivity {

    private ViewPager mVp;//ViewPager
    private TextView mTvTitle;//显示的信息
    private int[] imageResIds = {R.drawable.fj1,R.drawable.fj2,R.drawable.fj3,
            R.drawable.fj4,R.drawable.fj5,R.drawable.fj6};
    private String[] descs = {"风景1","风景2","风景3","风景4","风景5","风景6"};
    private ImageView[] mIvsDost = new ImageView[6];//存放ImageView的集合
    private Context mContext;
    private LinearLayout mLlDots;
    private int currentItem;

    private Handler mHandler = new Handler(){
        public void handleMessage(android.os.Message msg) {
            //处理:空消息
            if(msg.what == 1){
                currentItem = mVp.getCurrentItem();
                //当时最后一个选项时，让当前条目成为第一个
                if(currentItem==mVp.getAdapter().getCount()-1){
                    currentItem = 0;
                }else{
                    currentItem ++;
                }
                //通过这个方法用来设置ViewPager下一个显示的View
                mVp.setCurrentItem(currentItem);
                //然后继续发送一个空的Handler,实现无限轮播
                mHandler.sendEmptyMessageDelayed(1, 1000);
            }
        }
    };


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_pager_test1);

        initParamsAndValues();

        initView();

        initData();
    }

    private void initParamsAndValues() {
        mContext = this;
    }

    private void initView() {
        mVp = findViewById(R.id.vp);
        mTvTitle = findViewById(R.id.tv_title);
        mLlDots = findViewById(R.id.ll_dots);
    }

    @Override
    protected void onStart() {
        super.onStart();
        //当从别的页面回来的时候，还是能够继续播放
        mHandler.sendEmptyMessageDelayed(1, 3000);
    }

    private void initData() {
        mVp.setAdapter(adpter);
        mVp.setOnPageChangeListener(mOnPageChangeListener);

        initDots();
        mIvsDost[0].setSelected(true);
        mTvTitle.setText(descs[0]);
    }

    private void initDots() {
        //初始化小圆点
        for(int i=0;i<mIvsDost.length;i++){
            ImageView iv = new ImageView(mContext);
            //Shape是一个特殊的图片
            //保存了两种状态，选择和没有选择，两种状态下小圆点的显示
            iv.setBackgroundResource(R.drawable.dot);
            //由于创建的圆点的父容器是LinearLayout，所以采用父容器的LinearLayout
            //为什么我的小圆点不显示呢？
            //因为生成小圆点的时候，必须要指定这个小圆点的高度和宽度。
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(5, 5);
            //设置右边距
            params.rightMargin = 5;
            //给ImageView设置参数
            iv.setLayoutParams(params);
            mIvsDost[i] = iv;
            mLlDots.addView(iv);
        }
    }

    ViewPager.OnPageChangeListener mOnPageChangeListener = new ViewPager.OnPageChangeListener() {
        @Override
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

        }

        @Override
        public void onPageSelected(int position) {
            mTvTitle.setText(descs[position % 6]);
            for(int i=0;i<mIvsDost.length;i++){
                mIvsDost[i].setSelected(false);
            }
            mIvsDost[position % 6].setSelected(true);
        }

        @Override
        public void onPageScrollStateChanged(int state) {
            //ViewPager.SCROLL_STATE_IDLE;//空闲状态
            //ViewPager.SCROLL_STATE_DRAGGING;//拖动的状态
            //ViewPager.SCROLL_STATE_SETTLING;//拖动到最后一个的状态
            //如果是滑动状态取消自动滑动
            if (state == ViewPager.SCROLL_STATE_IDLE) {
                mHandler.sendEmptyMessageDelayed(1, 3000);
            }else{
                mHandler.removeMessages(1);
            }
        }
    };

    /**
     * ViewPager的适配器
     */
    PagerAdapter adpter = new PagerAdapter() {

        public boolean isViewFromObject(View arg0, Object arg1) {
            return arg0 == arg1;
        }

        @Override
        public int getCount() {
            //设置图片的个数为无限大，这样就能实现无限轮播的效果了
            return imageResIds.length*10000;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            //从container中移除View
            container.removeView((View) object);
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            ImageView imageView  = new ImageView(mContext);
            //由于父容器是ViewPager,所以ViewPager的参数是ViewPager.LayoutParams
//            ViewPager.LayoutParams params = new ViewPager.LayoutParams();
            ViewGroup.LayoutParams params = new ViewGroup.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
            imageView.setLayoutParams(params);
            imageView.setImageResource(imageResIds[position % 6]);
            imageView.setScaleType(ImageView.ScaleType.FIT_XY);
            container.addView(imageView);
            return imageView;
        }
    };

    @Override
    protected void onStop() {
        super.onStop();
        mHandler.removeMessages(1);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mHandler.removeMessages(1);
    }
}
