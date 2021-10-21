# PlayerPrefix 玩家前缀管理

![CodeSize](https://img.shields.io/github/languages/code-size/CarmJos/PlayerPrefix)
[![License](https://img.shields.io/github/license/CarmJos/PlayerPrefix)](https://opensource.org/licenses/mit-license.php)
[![Build](https://github.com/CarmJos/PlayerPrefix/actions/workflows/maven.yml/badge.svg?branch=master)](https://github.com/CarmJos/PlayerPrefix/actions/workflows/maven.yml)
![Support](https://img.shields.io/badge/Minecraft-Java%201.12--Latest-yellow)
![](https://visitor-badge.glitch.me/badge?page_id=PlayerPrefix.readme)

## 简介

本插件是一款轻量玩家前缀管理插件，支持以指令操作给不同的玩家发放称号、复制称号，并存储到本地yml文件中。

称号将只显示在玩家头顶上，玩家可选打开或关闭称号显示。

本插件基于Spigot实现，功能简单，运行稳定，**理论上支持全版本**。

本插件由 [桦木原Harmoland](https://www.mcbbs.net/thread-1028923-1-1.html) 请求本人开发，经过授权后开源。

## 依赖

- **[必须]** 插件本体基于 [Spigot-API](https://hub.spigotmc.org/stash/projects/SPIGOT) 、[BukkitAPI](http://bukkit.org/) 实现。

详细依赖列表可见 [Dependencies](https://github.com/CarmJos/PlayerPrefix/network/dependencies) 。

## 指令

### 玩家指令

```text
# /prefix list 
- 列出所拥有的所有前缀
# /prefix toggle 
- 开关前缀显示
# /prefix set <ID> 
- 选择一个前缀并使用
```

### 管理员指令

```text
# /prefix list <玩家名> 
- 列出所有前缀
# /prefix add <玩家名> <前缀> 
- 添加一个前缀
# /prefix remove <玩家名> <ID> 
- 移除一个前缀
# /prefix set <玩家名> <ID> 
- 设置其使用的前缀
# /prefix addAll <玩家名> <源玩家名> 
- 添加所有源玩家的前缀到目标玩家
# /prefix copy <玩家名> <源玩家名> 
- 设置目标玩家的前缀列表为源玩家的前缀列表
```

## 变量

```text
# %PlayerPrefix%
- 获取玩家当前的前缀内容。
```

## [玩家数据文件](https://github.com/CarmJos/PlayerPrefix/blob/master/example/userdata.yml) 示例

```yaml
prefixes:
  enable: true # 该玩家是否启用前缀
  using: 0 # 正在使用的前缀ID
  list: #前缀内容列表
    1: "&d&l可爱的&d"
    2: "&b&l帅气的&b"
```

## 支持与捐赠

若您觉得本插件做的不错，您可以捐赠支持我！

感谢您成为开源项目的支持者！

<img height=25% width=25% src="https://raw.githubusercontent.com/CarmJos/CarmJos/main/img/donate-code.jpg" />

## 开源协议

本项目源码采用 [The MIT License](https://opensource.org/licenses/mit-license.php) 开源协议。

> ### 关于 MIT 协议
> MIT 协议可能是几大开源协议中最宽松的一个，核心条款是：
>
> 该软件及其相关文档对所有人免费，可以任意处置，包括使用，复制，修改，合并，发表，分发，再授权，或者销售。唯一的限制是，软件中必须包含上述版 权和许可提示。
>
> 这意味着：
> - 你可以自由使用，复制，修改，可以用于自己的项目。
> - 可以免费分发或用来盈利。
> - 唯一的限制是必须包含许可声明。
> - MIT 协议是所有开源许可中最宽松的一个，除了必须包含许可声明外，再无任何限制。
>
> *以上文字来自 [五种开源协议GPL,LGPL,BSD,MIT,Apache](https://www.oschina.net/question/54100_9455) 。*
