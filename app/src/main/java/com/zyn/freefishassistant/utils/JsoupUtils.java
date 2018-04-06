package com.zyn.freefishassistant.utils;

import android.content.Context;
import android.util.Log;

import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;
import com.zyn.freefishassistant.beans.GoodsDetailBean;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * 文件名 JsoupUtils
 * 使用jsoup对html数据进行解析
 * 版本信息，版本号 v1.0
 * 创建日期 2017/2/7
 * 版权声明 Created by ZengYinan
 */

public class JsoupUtils {

    public static String baseUrl = "https://s.2.taobao.com/list/list.htm";
    public static int queryCount = 0;

    /**
     * 获取商品列表，最多9条
     *
     * @param data 参数数据
     * @return
     */
    public static List<GoodsDetailBean> getEntityData(Context mContext, Map<String, String> data, String lastId) {

        List<GoodsDetailBean> goodsDetailBeanList = new ArrayList<GoodsDetailBean>();

        if (data.get("is_Show").equals("不显示")) {
            return goodsDetailBeanList;
        }

        String url = baseUrl + "?t=" + System.currentTimeMillis();

        for (Map.Entry<String, String> entry : data.entrySet()) {
            if (!entry.getKey().equals("is_Show")) {
                String key = entry.getKey();
                String value = "";
                value = entry.getValue();
                url += "&" + key + "=" + value;
            }
        }

        try {
            url = URLEncoderURI.encode(url, "gbk");

            Log.e("TAG", "url:" + url);

            OkHttpClient client = new OkHttpClient();

            Request request = null;

            if (queryCount >= 1000) {
                request = new Request
                        .Builder()
                        .url(url)
                        .addHeader("Accept", "text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,*/*;q=0.8")
                        .addHeader("Accept-Language", "zh-CN,zh;q=0.8,en;q=0.6")
                        .addHeader("Cache-Control", "max-age=0")
                        .addHeader("Connection", "keep-alive")
                        .addHeader("Cookie", "_uab_collina=152282227792711970906309; cna=bnHpELL7nzQCAavdiPI9SjXO; hng=CN%7Czh-CN%7CCNY%7C156; thw=cn; miid=7060675441659287368; UM_distinctid=1623bd6f08a5a2-0463b9f45665af-5d4e211f-1fa400-1623bd6f08b7d4; l=AnNzInOP/O9tWlkJBr9lctElg32btQdq; uc3=nk2=ty5ADnl3KA%3D%3D&id2=W80p86Fpa88J&vt3=F8dBz4KLWlllcrlRKKE%3D&lg2=U%2BGCWk%2F75gdr5Q%3D%3D; lgc=%5Cu66FE%5Cu6960zyn; tracknick=%5Cu66FE%5Cu6960zyn; t=60c2578b2fdf1e39d49f0c187e56b603; _cc_=WqG3DMC9EA%3D%3D; tg=0; enc=4BSuBlBVVCj%2FytowVooinSE08TYPVVahskmDT5qe5evHChiM1pE5uwZ4crdpN07s5VT9N0xQ7WDZKjX8XSMu9A%3D%3D; cookie2=32807b55048ef300fae6c93dd9f7de8e; v=0; _tb_token_=e3eb67f3361e5; _umdata=09FE5CB1CF9363E0C87DA08CFBDF43C0211C3E081E70B2E8826FE2B670125B34EB5F4C55456F73CDCD43AD3E795C914C56FCED1532265FC29B34489E5E0846CC; x5sec=7b2269646c653b32223a22623935653833316465653161306132343437376138636430336265363338393043496e5a6b645946454976652f623731735054753941453d227d; mt=ci=-1_0; CNZZDATA1252911424=1091689292-1522821212-https%253A%252F%252Fs.2.taobao.com%252F%7C1522821212; CNZZDATA30058275=cnzz_eid%3D80617283-1522818273-https%253A%252F%252Fs.2.taobao.com%252F%26ntime%3D1522821996; uc1=cookie14=UoTePM%2BPcaLI%2FQ%3D%3D; isg=BM_PExoUyQLff80KuWmUjKkUXmMZXCHzXw809eHcaz5FsO-y6cSzZs2ituAOyPuO")
                        .addHeader("Host", "s.2.taobao.com")
                        .addHeader("Referer", "https://s.2.taobao.com/list/list.htm/_____tmd_____/verify/?nc_token=33cd5a1e2a5ea6c754e861d4c6ef47b7&nc_session_id=019D8EDDunjkinSA2v--8JrugRbBBruxDfL42M35QNsn_WSAEJew4LLCQEfa9aOQmxrfAcyJ4FsSmOByecrf-5hpfcPb8UC9wuU6iLMQxGrZrwsawgDHNGwsKj3FII7CHPeQ7mrXRjBgFWE6WVKjEsChvnDQ_A5RFY0qmmMDOUFzkHtqsnVuhzut6MJ3bRtadcSk2avd43XOVsa3RK1WtpwZClEq_b3zoeHw1j8uSqrE4hlRyYao3XNjxJaJrYrpLvl-ysVbMR5l9gkF8WCMQ0ckk3rdUlaMHEFVbWbxmrBeN92NBQAjUE2XCgGGXLfvKjDZKyQsopqiDi7dmpl1WFGMK4Q9cCG5DdHziwZJAo7aYNhvJWcdFLbPx34Szu7VMUzjoWetDzlEG_fPq6qBJUXljwQQRccMM1cwnT7Rlg0uCrqhIAmPnF-NjFSopYMyAk&nc_sig=058KBJDusi6jw6c11rGwk73FvSv62uYfij5-g9IO4hfhtVj_g0ciOD6BYdrv4JhZhlWFG61ih1asAX8cZCxi8SmOBKiaAtCN87RvoBr56WwvRcmtLi3T6kOfcCt-ntqQ_nV234jAhuhnhsJIU1reZ2QS5u7ZAS4vw65JbqyiLxcBTzUARgPYVSCejp-yIiOSCzBpyVKOIrn8LMBCZJZW0cTUpKC_8B-XuvDkPZrcRtzRWrThWALUqY26DlV2UCdExZgxB-aHP8YbbRF4HwEoT_A3FUQnBr5NQx24afeY1jz5UMYngGRFn1p1Q0fTpaYZdzvQ1r_R7IiMDasDUkztf5Iop9vBRgfhK3T0b2LCS3UWV31Feo472CETkYmB4yNzBrRdtGphEKIe-dTBh_DO2xV_7sRVk1cwqrykMAsM0FTG5as9k6tu-v_iMI8DzT2AWGDMaHnBQs5kqAnLJ2r6gBDcdCfPddAP2UfuouqoYvfOgtqBcM2ZNv2_2URGywqv2XQ2baHrXuB-PGvFWLZ61tfwetKTCnCEUsXkB5XOIzITNpTq8R3suzuhk441Q8VsOrwcoeUDuPozd_89SqjY_LhIFo1wCq6Ub86rta0CqJyTnSkj4W7HgmLj8cDyGBPlsxHtCN7iwDurJ3JunfmkzH645xSj9Ic2Q_rfPcnRgXm-_g0F7gRDH01qVXAc8pl6Hc9utzYVLtPhkiczA8M3cpoxgQbTHuQ-2AdqATjYunFEI&x5secdata=5e0c8e1365474455070961b803bd560607b52cabf5960afff39b64ce58073f7856d51f6f740b3754d3d4b33d30db7656e24ce2cf16bc87a759e5d78b59bbda085d48a0456707bf8ea6481a41d6ee965b8de558990b51a94454a308719ae637b5c63e5edaf6977d68a3b4ec79e38fd242f764d7a09aeb25ab297c4e9919b3cf21926cb1f8302336218d1d512e1c900597173e2c733c580fd79cb335431ebd7e2602e758e5e81da7616fe358c85b0ef23cc6fcd6518a0a1a6ecfe3c183edc521f5154f144e4c5cc427ace363d5807b5d0e72512b67fe689764c70bf1ac8526c9128ebf1d46a22b23ddadf0720b8bddd9da655529fdb14f8bd6d62a26613ab2fc6a6408ab4c57a7add18d72983b2f90cafd46fabac8e27bc6ec8d92390e192c8fd9330a4d6e52d16b36bd3e1c87e3cfc9ea0991097333dcabef7b5c79d1cd4dd11c200d90e62e907738bafa7af51ae95dee3654659224ac423ffd54ca3965a27e0774013f36db6c8ed2a799dceae3fb7f83670239065448a3ea9c3c36405be852f5491683df18a9cc7677011e65d9bfe86fc52bccc49a6ead4e3b710feecdc2054d02fd9650369d7e997da4e7a16182f01b772cd482ee48ae44b995820b22c8bccdab1e1b70729bcf99c5a183a24bbe7d0aa35b6937d1e38085d8e653ce47929f65e5e862a34fcd03c8fc7f897f08b6a1e9&x5step=100")
                        .addHeader("Upgrade-Insecure-Requests", "1")
                        .addHeader("User-Agent", "Mozilla/5.0 (Windows NT 10.0; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/55.0.2883.87 Safari/537.36")
                        .build();
            } else {
                request = new Request
                        .Builder()
                        .url(url)
                        .addHeader("Accept", "text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,*/*;q=0.8")
                        .addHeader("Accept-Language", "zh-CN,zh;q=0.8")
                        .addHeader("Cache-Control", "max-age=0")
                        .addHeader("Connection", "keep-alive")
                        .addHeader("Cookie", "cna=/HdhD06WEDoCAdNCd6zSNaNW; miid=8911443805606368317; l=AlBQChc7DW1KWCQwhvAWfToYoJSimTRj; UM_distinctid=1611ee874bd181-052548450d4bfe-36465d60-100200-1611ee874be1b3; enc=VPo8OqAVAJjO%2BSIamu%2BdVUz5XXLU5xAdRhd7uMeeV0cSvIrw5ywndUXiOnbbbacnrxcx3Umd2w9PY9irlZ5GIg%3D%3D; thw=cn; hng=CN%7Czh-CN%7CCNY%7C156; uc3=nk2=AHXTCIgScL6Ahg%3D%3D&id2=UUBYgO02cgr9gA%3D%3D&vt3=F8dBz4KLXma1DyQruB8%3D&lg2=Vq8l%2BKCLz3%2F65A%3D%3D; lgc=coverman%5Cu7684; tracknick=coverman%5Cu7684; t=96771cc4170bec63cb1a5beecb78c8ab; _cc_=WqG3DMC9EA%3D%3D; tg=0; cookie2=1d681358e44b84579543bff4c545de3c; v=0; _tb_token_=e53383be63d33; mt=ci=-1_0; uc1=cookie14=UoTePM6RhrA1Lg%3D%3D; CNZZDATA1252911424=822935394-1522987015-%7C1522987015; CNZZDATA30058275=cnzz_eid%3D1609964548-1522989410-%26ntime%3D1522989410; isg=BKWlkS1NE2LvU3eeA2HfxqrctGEfSlkdP3FBKaeKYVzrvsUwbzJpRDNcTCDIhXEs")
                        .addHeader("Upgrade-Insecure-Requests", "1")
                        .addHeader("User-Agent", "Mozilla/5.0 (Windows NT 6.1) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/55.0.2883.87 Safari/537.36")
                        .build();
            }

            Response response = client.newCall(request).execute();

            String htmlSource = "";
            if (response.isSuccessful()) {
                htmlSource = response.body().string();
            }

            Document doc = Jsoup.parse(htmlSource);

            String html = doc.outerHtml();
            String[] htmls = html.split("div");

            Elements elements = doc.getElementsByClass("item-info-wrapper item-idle clearfix");

            int count = 0;
            if (elements.size() >= 9) {
                count = 9;
            } else {
                count = elements.size();
            }

            //每一类只取前9个
            for (int i = 0; i < count; i++) {
                Element element = elements.get(i);
                int minute = Integer.parseInt(element.getElementsByClass("item-pub-time").get(0).text().replace("分钟前", ""));
                if (minute > 30) {
                    break;
                }
                GoodsDetailBean goodsDetailBean = new GoodsDetailBean();
                //商品列表图片
                String list_imgUrl = element.getElementsByTag("img").get(0).attr("src");
                list_imgUrl = list_imgUrl.substring(2, list_imgUrl.length());
                //商品链接
                String goods_url = element.getElementsByTag("a").get(0).attr("href");
                //商品ID
                String id = goods_url.split("id=")[1];

//                if(id.equals(lastId)){
//                    //这是上次搜索的第一个
//                    break;//不显示了
//                }

                //商品标题
                String title = element.getElementsByTag("a").get(1).text();
                //商品价格
                String price = element.getElementsByTag("em").get(0).text();
                //商品描述
                String desc = element.getElementsByClass("item-description").get(0).text();

                goodsDetailBean.setGoodsId(id);
                goodsDetailBean.setList_desc(desc);
                goodsDetailBean.setList_imgUrl(list_imgUrl);
                goodsDetailBean.setPrice(price);
                goodsDetailBean.setTitle(title);
                goodsDetailBean.setUrl(goods_url);

                goodsDetailBeanList.add(goodsDetailBean);
            }

        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
        queryCount++;
        if (queryCount >= 2000) {
            queryCount = 0;
        }
        return goodsDetailBeanList;
    }
}
