# 一些通用的功能，做个纪录
1. swagger文档
~~~
#开启swagger，默认关闭
common.swagger.enabled=true
#扫描的包的路径
common.swagger.base-package=com.hyy.test
common.swagger.version=1.0
common.swagger.title=文档名称
~~~
2. 全局异常处理
~~~
处理了常见的一些异常,主要是@NotNull之类的参数检验异常
cn.hyy.common.exception.ExceptionHandler
返回的类
cn.hyy.common.result.Result
~~~
3. 访问日志
~~~
#开启日志功能，默认关闭
common.log.enabled=true
#需要纪录日志接口所在的包路径
common.log.base-package=com.hyy.test.controller
#超时时间，单位ms，访问时间超过这个的做出警告
common.log.overtime=10000
~~~
4. 一些工具类
~~~
excel的导入导出：cn.hyy.common.utils.ExcelUtil
根据富文本生成pdf：cn.hyy.common.utils.PdfUtil
~~~