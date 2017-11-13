package com.example.qingchen.vrmr.bean;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;
import android.support.annotation.NonNull;

import java.util.List;

/**
 * @author qingchen
 * @date 17-11-10
 */
@Entity
public class InfoBean {

    /**
     * code : 200
     * msg : success
     * newslist : [{"ctime":"2017-06-06 16:00","title":"模式思考：为什么星巴克成了印钞机？","description":"创业新闻","picUrl":"http://pic.chinaz.com/thumb/2017/0606/2017060614515724.jpg","url":"http://www.chinaz.com/start/2017/0606/720004.shtml"},{"ctime":"2017-06-06 16:00","title":"佛学文化社群平台，\u201c般若\u201d要连接寺庙、僧人、善信","description":"创业新闻","picUrl":"http://pic.chinaz.com/thumb/2017/0606/6363235766401474079822974.jpeg","url":"http://www.chinaz.com/start/2017/0606/720017.shtml"},{"ctime":"2017-06-06 16:00","title":"短视频不再是东北人的天下 \u201c川军\u201d已崛起","description":"创业新闻","picUrl":"http://pic.chinaz.com/thumb/2017/0606/6363235842103727035736068.png","url":"http://www.chinaz.com/start/2017/0606/720028.shtml"},{"ctime":"2017-06-06 11:00","title":"中国文青创业者的最大金主：远离风口，专注慢公司","description":"创业新闻","picUrl":"http://pic.chinaz.com/thumb/2017/0606/2017060610250221.jpg","url":"http://www.chinaz.com/start/2017/0606/719534.shtml"},{"ctime":"2017-06-06 10:00","title":"给程序员当经纪人，程序员客栈完成300万元天使轮融资","description":"创业新闻","picUrl":"http://pic.chinaz.com/thumb/2017/0606/201706060900398654.jpg","url":"http://www.chinaz.com/start/2017/0606/719406.shtml"},{"ctime":"2017-06-06 10:00","title":"经历多次押宝失败的TOM网，可能要因政策原因而彻底断了挣扎","description":"创业新闻","picUrl":"http://pic.chinaz.com/thumb/2017/0606/6363233672258475907689785.jpeg","url":"http://www.chinaz.com/start/2017/0606/719411.shtml"},{"ctime":"2017-06-05 17:00","title":"网易蜗牛读书:突破付费模式 敲碎纸电隔阂","description":"创业新闻","picUrl":"http://pic.chinaz.com/thumb/2017/0605/201706051550205463.jpg","url":"http://www.chinaz.com/start/2017/0605/718537.shtml"},{"ctime":"2017-06-05 17:00","title":"五年之后，网易新闻为什么彻底放弃了\u201c有态度\u201d？","description":"创业新闻","picUrl":"http://pic.chinaz.com/thumb/2017/0605/201706051600289006.jpg","url":"http://www.chinaz.com/start/2017/0605/718557.shtml"},{"ctime":"2017-06-05 17:00","title":"一年赚了知性女青年7000万元，它却是行业不擅长挣钱的公司","description":"创业新闻","picUrl":"http://pic.chinaz.com/thumb/2017/0605/201706051619455958.jpg","url":"http://www.chinaz.com/start/2017/0605/718595.shtml"},{"ctime":"2017-06-05 15:00","title":"从兴起到洗牌仅用一年！谁让这场全民狂欢戛然而止？","description":"创业新闻","picUrl":"http://pic.chinaz.com/thumb/2017/0605/6363226806028247287344888.jpg","url":"http://www.chinaz.com/start/2017/0605/718364.shtml"}]
     */

    private int code;
    private String msg;
    private List<NewslistBean> newslist;

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public List<NewslistBean> getNewslist() {
        return newslist;
    }

    public void setNewslist(List<NewslistBean> newslist) {
        this.newslist = newslist;
    }

    @Entity
    public static class NewslistBean {
        /**
         * ctime : 2017-06-06 16:00
         * title : 模式思考：为什么星巴克成了印钞机？
         * description : 创业新闻
         * picUrl : http://pic.chinaz.com/thumb/2017/0606/2017060614515724.jpg
         * url : http://www.chinaz.com/start/2017/0606/720004.shtml
         */
        @PrimaryKey
        private int id;
        private String ctime;
        private String title;
        private String description;
        private String picUrl;
        private String url;

        public String getCtime() {
            return ctime;
        }

        public void setCtime(String ctime) {
            this.ctime = ctime;
        }

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public String getDescription() {
            return description;
        }

        public void setDescription(String description) {
            this.description = description;
        }

        public String getPicUrl() {
            return picUrl;
        }

        public void setPicUrl(String picUrl) {
            this.picUrl = picUrl;
        }

        public String getUrl() {
            return url;
        }

        public void setUrl(String url) {
            this.url = url;
        }

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }
    }
}
