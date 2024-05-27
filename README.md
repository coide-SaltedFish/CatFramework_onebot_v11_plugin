# 猫爬架的OneBot11协议实现

**需要引入[通用QQ接口抽象层](https://github.com/coide-SaltedFish/CatFramework_QQBotUniversalAbstractionLayer)和[事件处理扩展](https://github.com/coide-SaltedFish/CatFramework_eventhandler_extend)**

还在完善中...

## 连接到Bot

引入插件并启动框架初始化信息后，在框架根目录下的`data/config/org.sereinfish.catcat.framework.onebot.v11/`文件夹下创建`config.yml`文件，写入如下配置

```yaml
bots:
  -
    type: "ReverseWebSocket"
    host: "127.0.0.1"
    port: 5800
```

其中连接类型目前仅支持`ReverseWebSocket`，`Host`和`Port`根据实际使用的机器人平台配置填写

重启框架，即可自动连接