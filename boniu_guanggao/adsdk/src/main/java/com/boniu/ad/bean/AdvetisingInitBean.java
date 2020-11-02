package com.boniu.ad.bean;

import java.util.List;

public class AdvetisingInitBean {
    //0–开屏广告 1–激励视频 2–动态信息流 3–横幅 4–Banner 5–插屏 6–视频贴片
    private List<SdkAdverVOListBean> sdkAdverVOList;
    private List<SdkAppIdVOListBean> sdkAppIdVOList;

    public List<SdkAdverVOListBean> getSdkAdverVOList() {
        return sdkAdverVOList;
    }

    public void setSdkAdverVOList(List<SdkAdverVOListBean> sdkAdverVOList) {
        this.sdkAdverVOList = sdkAdverVOList;
    }

    public List<SdkAppIdVOListBean> getSdkAppIdVOList() {
        return sdkAppIdVOList;
    }

    public void setSdkAppIdVOList(List<SdkAppIdVOListBean> sdkAppIdVOList) {
        this.sdkAppIdVOList = sdkAppIdVOList;
    }

    public static class SdkAdverVOListBean {
        /**
         * advertisingSpaceName : 测试2-开屏
         * advertisingSpaceType : 0
         * list : [{"advertiseOrder":1,"advertiserName":"穿山甲","advertiserNo":"csj","advertisingSpaceId":"887305886","appId":"5054093","ifDirectCus":false},{"advertiseOrder":2,"advertiserName":"优量汇","advertiserNo":"ylh","advertisingSpaceId":"4021809880970678","appId":"1109946712","ifDirectCus":false},{"advertiseOrder":3,"advertiserName":"搏牛广告","advertiserNo":"bngg","advertisingSpaceId":"DCA874837419","appId":"DCM3284783","ifDirectCus":true}]
         * platform : ANDROID
         */
        private String adIdentity;
        private String advertisingSpaceName;
        private int advertisingSpaceType;
        private String platform;
        private List<ListBean> list;

        public String getAdIdentity() {
            return adIdentity;
        }

        public void setAdIdentity(String adIdentity) {
            this.adIdentity = adIdentity;
        }

        public String getAdvertisingSpaceName() {
            return advertisingSpaceName;
        }

        public void setAdvertisingSpaceName(String advertisingSpaceName) {
            this.advertisingSpaceName = advertisingSpaceName;
        }

        public int getAdvertisingSpaceType() {
            return advertisingSpaceType;
        }

        public void setAdvertisingSpaceType(int advertisingSpaceType) {
            this.advertisingSpaceType = advertisingSpaceType;
        }

        public String getPlatform() {
            return platform;
        }

        public void setPlatform(String platform) {
            this.platform = platform;
        }

        public List<ListBean> getList() {
            return list;
        }

        public void setList(List<ListBean> list) {
            this.list = list;
        }

        public static class ListBean {
            /**
             * advertiseOrder : 1
             * advertiserName : 穿山甲
             * advertiserNo : csj
             * advertisingSpaceId : 887305886
             * appId : 5054093
             * ifDirectCus : false
             */

            private int advertiseOrder;
            private String advertiserName;
            private String advertiserNo;
            private String advertisingSpaceId;
            private String appId;
            private boolean ifDirectCus;//是否直客

            public int getAdvertiseOrder() {
                return advertiseOrder;
            }

            public void setAdvertiseOrder(int advertiseOrder) {
                this.advertiseOrder = advertiseOrder;
            }

            public String getAdvertiserName() {
                return advertiserName;
            }

            public void setAdvertiserName(String advertiserName) {
                this.advertiserName = advertiserName;
            }

            public String getAdvertiserNo() {
                return advertiserNo;
            }

            public void setAdvertiserNo(String advertiserNo) {
                this.advertiserNo = advertiserNo;
            }

            public String getAdvertisingSpaceId() {
                return advertisingSpaceId;
            }

            public void setAdvertisingSpaceId(String advertisingSpaceId) {
                this.advertisingSpaceId = advertisingSpaceId;
            }

            public String getAppId() {
                return appId;
            }

            public void setAppId(String appId) {
                this.appId = appId;
            }

            public boolean isIfDirectCus() {
                return ifDirectCus;
            }

            public void setIfDirectCus(boolean ifDirectCus) {
                this.ifDirectCus = ifDirectCus;
            }
        }
    }

    public static class SdkAppIdVOListBean {
        /**
         * advertiserNo : bngg
         * appIdList : ["DCM3284783"]
         */

        private String advertiserNo;
        private List<String> appIdList;

        public String getAdvertiserNo() {
            return advertiserNo;
        }

        public void setAdvertiserNo(String advertiserNo) {
            this.advertiserNo = advertiserNo;
        }

        public List<String> getAppIdList() {
            return appIdList;
        }

        public void setAppIdList(List<String> appIdList) {
            this.appIdList = appIdList;
        }
    }
}
