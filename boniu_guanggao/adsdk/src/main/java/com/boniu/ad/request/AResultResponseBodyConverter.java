package com.boniu.ad.request;

import com.boniu.ad.bean.ResultBean;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import okhttp3.ResponseBody;
import retrofit2.Converter;

/**
 */
public class AResultResponseBodyConverter implements Converter<ResponseBody, ResultBean> {
    @Override
    public ResultBean convert(ResponseBody value) throws IOException {
        try {
            String response = value.string();
            JSONObject object = new JSONObject();
            try {
                object = new JSONObject(response);
            } catch (JSONException e) {
                e.printStackTrace();
            }
            ResultBean result = new ResultBean();
            result.errorMsg = object.optString("errorMsg");
            result.returnCode = object.optString("returnCode");
            result.success = object.optBoolean("success");
            result.result = object.optString("result");
            return result;
        } finally {
            value.close();
        }
    }
}