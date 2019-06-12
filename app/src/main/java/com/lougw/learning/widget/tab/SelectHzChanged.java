package com.lougw.learning.widget.tab;

/**
 * 微调控件(自定义View)中, 当选中的频率发生变化.
 * 是否添加一个延迟呢? 3s内未改变则执行 tune的调频.
 *
 * @author dundongfang on 2018/4/23.
 */
public interface SelectHzChanged {
    void onSelectHzChanged(int kHz);
}
