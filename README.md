# ComposeTextEffect
把Compose的Text组件玩出新高度

<img src="screen_shot/compose_text_effect.webp" width="400px">

----
该存储库对外开放的能力：**可折叠的组合项Text**
- 集成方式如下：

```groovy
  implementation "io.github.TheMelody:expandable_text_compose:1.0.0"
```

- 代码使用方式如下：
```kt
  ExpandableText(
	modifier = Modifier
	.verticalScroll(rememberScrollState())
	.padding(16.dp)
	.fillMaxWidth(),
	text = buildAnnotatedString { append("请输入你的超长文本，来吧展示，省略更多内容...") },
	textStyle = TextStyle(color = Color.Black, fontSize = 14.sp),
	expandStateText = "展开",
	collapseStateText = "收起"
  )
```

**其他的效果，请自行查阅Demo**
