# PlantDroid
1. ==植物图鉴==

   1. 植物识别

      1. 拍照，获取文件 【读取权限】

   2. 图鉴（list view)

      1. 按门纲目分类
      2. 搜索
      3. 详情页

   3. 通知 到达新地点（附加功能）

   4. 地图

      1. 定位权限

   5. 动画

      1. 美化
      2. 功能（例如植物在一年内的变化之类的）
   6. 没有提供的view

   

   

   1. 百度识图 / https://plant.id/

   2. 翻译api（可能）http://api.fanyi.baidu.com/api/trans/product/apidoc

   3. 存储在本地

      

# 生死竞速



![image-20211108194845179](Group%20work%E5%81%9A%E4%BB%80%E4%B9%88.assets/image-20211108194845179.png)



# 需要什么页面

- ==地图== (hyh)
  - 调API
  - 什么风格（真实/绘画）
  - 按照图片拍摄地址画出找到过的植物

- ==图鉴（list view)== (chj)
  - 先请求一部分数据，存
  - 搜索
  - 目录
    - Phylum 
    - Class 
    - Order

- 详情页 (yl)
  - 图
  - 简介
  - 分享（调微信）

- ==拍照==  (all)
  - 拍照（最基础功能）
  - https://plant.id/
  - 搜索结果页
    - 是否添加到图鉴内
- 后台（JD）

- 用户协议/设置
- 通知（收集到）

# 数据库

**所有<u>植物表</u>**

|                     |      |
| ------------------- | ---- |
| id                  |      |
| name                |      |
| common_names        |      |
| kingdom             |      |
| phylum              |      |
| *class*             |      |
| *order*             |      |
| *family*            |      |
| *genus*             |      |
| description         |      |
| img                 |      |
| edible_parts        |      |
| propagation_methods |      |
| is_owned            |      |
|                     |      |

|           |       |       |       |
| --------- | ----- | ----- | ----- |
| id        | 1     | 2     | 3     |
| parent_id | -1    | 1     | 1     |
| name      | 被子  | 百合  |       |
| type      | class | order | order |

**找到的<u>记录表</u>**

| id        |      |
| --------- | ---- |
| time      |      |
| altitude  |      |
| longitude |      |
| accuracy  |      |
| plant_id  | 外键 |

# 接口

获取单个详情的接口

获取多个













sqllite 插数据

页面

