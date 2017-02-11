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