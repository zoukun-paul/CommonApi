
> 学习项目，该项目只要时记录、实践自己所学，实现了比较采用的API。如有比较好的建议我也非常欢迎

API

| AP I| 状态 | 描述 |
|---|---|---|
|邮箱|<span style='color:green'><span style='color:green'>已完成</span></span>|stmp|
|短信|<del>不考虑</del>|要收费，现<del>不考虑</del>|
|ip|<span style='color:green'>已完成</span>|ip定位、经纬度距离计算（待测试）|
|中英文翻译|<span style='color:red'>待开发</span>||
|在线聊天|<del>不考虑</del>||
|视频会议|<del>不考虑</del>|RTC|
|oss|<span style='color:green'>已完成</span>||
|图片压缩|<span style='color:red'>待开发</span>||
|编码、解码、转码|<span style='color:red'>待开发</span>|base64、unicode...|
|加密|<span style='color:red'>待开发</span>||
|字符图片|<span style='color:red'>待开发</span>||
|代理池|<span style='color:red'>待开发</span>|https://cuiqingcai.com/7048.html|
|二维码生成器|已完成||
|短链接|已完成|已完成|

## 文件
    1. 按照功能上划分，总体上可以分为两种类型。
        第一种为共享使用没有用户标识，可以共享访问，没有与用户绑定，没有任何限制权限；第二类
        第二种为用户文件，与用户关联；用户可以管理自己上传的文件。
    2. 对与‘共享文件’，采用md5来唯一标识文件，减少重复文件占用的空间。
    3. 对应‘用户文件’，采用主键来唯一标识文件，并记录上传用户的ID与用户绑定。

## 邮件
    1. 按使用方式分为，两种类型
        第一种 需要先进行验证授权，然后进行邮件操作，最后撤销授权（可选-有效时间后会自动撤销权限）
        第二种 直接进行邮件操作，但需要携带身份信息。