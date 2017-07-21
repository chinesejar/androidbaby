package timeroute.androidbaby.widget;

import android.content.Context;
import android.graphics.PointF;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.GestureDetector;
import android.view.MotionEvent;

public class NoScrollViewPager extends ViewPager {
    /** 触摸时按下的点 **/
    PointF downP = new PointF();
    /** 触摸时当前的点 **/
    PointF curP = new PointF();

    private GestureDetector mGestureDetector;


    public NoScrollViewPager(Context context, AttributeSet attrs) {
        super(context, attrs);
        mGestureDetector = new GestureDetector(context, new XScrollDetector());
    }
    public NoScrollViewPager(Context context) {
        super(context);

        mGestureDetector = new GestureDetector(context, new XScrollDetector());
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        return super.onInterceptTouchEvent(ev);//default
    }

    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        //每次进行onTouch事件都记录当前的按下的坐标
        curP.x = ev.getX();
        curP.y = ev.getY();

        if(ev.getAction() == MotionEvent.ACTION_DOWN){
            //记录按下时候的坐标
            //切记不可用 downP = curP ，这样在改变curP的时候，downP也会改变
            downP.x = ev.getX();
            downP.y = ev.getY();
            //此句代码是为了通知他的父ViewPager现在进行的是本控件的操作，不要对我的操作进行干扰
            getParent().requestDisallowInterceptTouchEvent(true);
        }

        if(ev.getAction() == MotionEvent.ACTION_MOVE){
            //此句代码是为了通知他的父ViewPager现在进行的是本控件的操作，不要对我的操作进行干扰
            getParent().requestDisallowInterceptTouchEvent(true);
        }

        return super.onTouchEvent(ev);
    }

    private class XScrollDetector extends GestureDetector.SimpleOnGestureListener{
        @Override
        public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {
//            return super.onScroll(e1, e2, distanceX, distanceY);

            //接近水平滑动时子控件处理该事件，否则交给父控件处理
            return (Math.abs(distanceX) > Math.abs(distanceY));
        }
    }
}