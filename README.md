# FreefishAssistant
A real time monitoring of idle fish commodity dynamics app

#接口地址及参数
###地址：
    https://s.2.taobao.com/list/list.htm

###参数：
- q：搜索关键字
- ist：列表方式（0）
- spm：排序方式（2007.1000337.6.2.nEgtu7）
- start：价格开始
- end：价格结束
- divisionId：行政代码
- page：第几页


###自定义参数
- is_Show：是否开启监控（显示|不显示）

###保证每次点击刷新获取到的都是最新的数据
- 每次搜索开始的时候读取该关键字对应的上次搜索的第一个id，搜索的时候与该id对比，如果和该id一样，说明是上次搜索的第一个，则不添加到本次搜索结果中。

### 监控管理操作
- 长按删除
- 点击修改

### 备注
- 由于客户要求，不需要每次都删除上一次的加载记录，所以我将JsoupUtils中的判断是否是上次搜索的第一个注释掉了，并且加了30分钟前发布监控
`
            //每一类只取前9个
            for (int i = 0; i < count; i++) {
                Element element = elements.get(i);
                int minute = Integer.parseInt(element.getElementsByClass("item-pub-time").get(0).text().replace("分钟前", ""));
                if(minute > 30){
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
`

### 借鉴*闲鱼监控助手*
- 地址：http://www.coolapk.com/apk/com.zmy.xianyu
- 注意！最新版本的闲鱼调用不了。