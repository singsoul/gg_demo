package com.boniu.ad.interfaces;

import com.boniu.ad.bean.MeterialBean;

import java.util.List;

public interface MeterialInterfaces {
    void meterialList(List<MeterialBean> list);
    void onError(String msg);
}
