# 小六壬时课 App — UI 美化与交互优化总览

> 记录两轮前端优化的完整内容。第二轮（2026-10-08）以「修复真实缺陷 + 无障碍 + 主题一致性」为主。

---

# 第二轮 · 2026-10-08

## 本轮目标

对 Material3 + Jetpack Compose 前端做一次系统性复查，重点不在「加装饰」，而在**修掉已经存在的真实缺陷**，并把设计系统的约束补全。

## 一、修掉的缺陷（按严重度）

### P0 · 深色主题下顶栏标题不可读

`AppTypography` 的每一个 `TextStyle` 都写死了 `color = InkBlack / InkDark / OnSurface ...`。

Compose 文本的取色优先级是 **`Text(color =)` > `style.color` > `LocalContentColor`**，
而 `Text(style = MaterialTheme.typography.xxx)` 拿到的正是 `style.color`。
结果：MaterialTheme 的 `titleContentColor` / `LocalContentColor` 被整体架空 ——
暗色模式下顶栏标题拿到 `InkDark (#2B2B2B)`，压在 `#0D0D0D` 背景上几乎不可见。

**修复**：Typography 里移除全部硬编码颜色，只保留字号/字重/字距/行高，颜色交回主题。
同时补齐 M3 缺失的 `displaySmall` / `headlineSmall` / `titleSmall`，避免回落到系统默认值造成层级不统一。

### P0 · Android 15+ 强制 edge-to-edge 下内容被系统栏遮挡

`MainActivity` 里 `enableEdgeToEdge()` 紧接着 `WindowCompat.setDecorFitsSystemWindows(window, true)` ——
后者把前者撤销了。但 `targetSdk = 36`（Android 15+）下 edge-to-edge 是**强制**的，
`setDecorFitsSystemWindows(true)` 不再生效。

与此同时，全项目 `grep` 不到任何 `Scaffold` / `WindowInsets` / `statusBarsPadding`，
7 个页面的根容器都是裸 `Column(fillMaxSize)`。

**结果**：首页顶部的深朱砂免责条被状态栏压住，各页底部内容被导航栏 / 手势条压住。

**修复**：
- 删掉矛盾的 `setDecorFitsSystemWindows(window, true)`；
- `Theme.kt` 同步切换状态栏与导航栏图标明暗；
- 新增 `Modifier.navigationBarsInset()` 与 `BottomInsetSpacer()`；
- `DisclaimerBar` 改为「背景全出血 + 文字避让状态栏」；
- 6 个带顶栏的页面内容区加 `navigationBarsInset()`（顶栏自身的 `TopAppBarDefaults.windowInsets` 已自动处理状态栏）。

### P1 · 深色主题下「时宫」高亮卡片是一块刺眼白斑

`ThreePalaceRow` / `KnowledgeDetailScreen` / `KnowledgeScreen` 用的是 `levelColorLight()`，
它在浅色主题下返回 `#E8F5E9` 这类浅色底 —— 直接放到深色主题上就是一块亮白。

讽刺的是项目里**已经写了** `levelColorDark()`，但从未被调用过（死代码）。

**修复**：新增 `levelSurfaceColor(level)` 按主题分派，`levelColorDark()` 终于用上了；
新增 `levelColorThemed(level)`，暗色主题下吉凶色使用提亮版本。

### P1 · `LevelTag` 文字对比度不达标

「半吉半凶」标签是白字压 `#C9A227` 黄底，对比度约 **2.36:1**，远低于 WCAG AA 的 4.5:1。

**修复**：新增 `onColorFor(background)`，按背景相对亮度自动选深字或浅字
（阈值取黑/白等对比度交叉点 L ≈ 0.179）。黄底改用墨字后约 **7.1:1**。

### P1 · 删除历史记录无法撤销、触摸目标过小

- 单条删除点下去就直接删，没有任何后悔药；
- 删除按钮 `IconButton(modifier = Modifier.size(28.dp))`，低于 48dp 最小触摸目标。

**修复**：
- 删除后弹 Snackbar 提供「撤销」，撤销即按原 id 写回（DAO 的 insert 是 REPLACE 策略）；
- 去掉显式 `size(28.dp)`，恢复 IconButton 默认 48dp 触摸目标，图标本身缩到 18dp。

### P2 · 首页每秒重算一次农历与二十四节气

`LaunchedEffect` 里 `while (true) { refreshCurrentTime(); delay(1000) }`，
但界面只显示到「分」。每秒一次完整的 `LunarCalendar` + `SolarTerms` 计算纯属浪费。

**修复**：睡到下一个整分钟边界再刷新。

### P2 · 首屏入场动画导致布局跳动

`AnimatedEntry` 用 `AnimatedVisibility` 切换可见性 —— 动画开始前元素**不占布局高度**，
页面先「短一截」再撑开，首屏可见明显跳动；而且 `ResultScreen` 有 8 个区块，
最后一个要等 360ms 才出现。

**修复**：改为只操作渲染层（`graphicsLayer` 的 alpha + translationY），
布局从头到尾稳定，动画期间也不触发重新布局。步长按 M3 建议从 45ms 收到 35ms。

### P2 · 组合期间直接调用 `popBackStack()`

`AppNavigation` 的 `Routes.RESULT` 分支在 `result == null` 时**直接在组合里**调
`navController.popBackStack()` —— 每次重组都会重复执行。

**修复**：移入 `LaunchedEffect`。

### P2 · 项目在本机无法构建

两处构建阻塞：
1. Gradle 8.14.3 的 `kotlin-dsl/scripts/*/metadata.bin` 缓存损坏；
2. `app/build.gradle.kts` 里 `compileSdkMinor = 1` 让 AGP 去找 `platforms;android-36.1`，
   而本机 SDK 只有 `android-36`，且 SDK 源被代理拦截无法补装。

**修复**：清掉损坏缓存；移除 `compileSdkMinor`，统一用已安装的 `android-36`（与 `targetSdk = 36` 一致）。

### P3 · 其他

- `DecorativeDivider` 每帧重建渐变画笔 → 提到 composable 作用域并 `remember`；
- 删除三个从未被使用的顶层画笔（`CinnabarGradient` / `DecorativeLineBrush` / `GoldLineBrush`）——
  它们是死代码，且写死了浅色主题颜色，留着是个坑；
- `Icons.Default.MenuBook` / `ArrowBack` 迁移到 AutoMirrored 版本。

## 二、交互与无障碍增强

| 项 | 内容 |
|---|---|
| `ClickableCard`（新增） | 把散落在 3 个页面的「Surface + pressScale + shadow + clickable + haptic」收敛为一处，参数不再漂移；支持 `onClickLabel` |
| `AnimatedEntry` | 只动渲染层，消除布局跳动；步长 45ms → 35ms |
| 触觉反馈 | 保持原有轻触反馈，统一从 `LocalHapticFeedback` 取 |
| 无障碍语义 | `SectionTitle` 加 `heading()`，读屏可按标题跳转；`SealStamp` 用 `clearAndSetSemantics {}` 从朗读中摘除；`FeatureEntry` 图标改 `contentDescription = null`（文字标签已表达用途，避免念两遍） |
| 点击语义 | 知识库卡片「查看XX详解」、结果页「展开/收起XX」、时间选择卡「选择日期，当前 2026年10月8日」 |
| 对比度 | `LevelTag` 白字黄底 → 墨字黄底（2.36:1 → 7.1:1） |
| 触摸目标 | 历史记录删除按钮 28dp → 48dp |

## 三、改动文件

| 文件 | 改动 |
|------|------|
| `ui/theme/Theme.kt` | 重写：Typography 去硬编码色、补全字阶、`LocalIsDarkTheme`、主题色助手、`Motion` 动效令牌、删死代码 |
| `ui/theme/Color.kt` | `XiongRedDeep` 具名化、`JiGreenLight` 压暗以达标对比度 |
| `ui/components/CommonComponents.kt` | 重写：`ClickableCard` / `navigationBarsInset` / `BottomInsetSpacer` / `onColorFor` 新增，`AnimatedEntry` 重做，`DisclaimerBar` 全出血，语义补全 |
| `MainActivity.kt` | 移除矛盾的 `setDecorFitsSystemWindows` |
| `ui/navigation/AppNavigation.kt` | 动效令牌化、`popBackStack` 移入副作用 |
| `ui/screens/HomeScreen.kt` | insets、按分钟刷新、禁用原因提示、文字间距去冗余 |
| `ui/screens/ResultScreen.kt` | insets、暗色高亮卡片、`ClickableCard`、点击语义 |
| `ui/screens/CustomTimeScreen.kt` | 免责条全出血对齐、insets、`ClickableCard` |
| `ui/screens/HistoryScreen.kt` | insets、删除可撤销、触摸目标 |
| `ui/screens/KnowledgeScreen.kt` | insets、主题色分派、`ClickableCard` |
| `ui/screens/KnowledgeDetailScreen.kt` | insets、主题色分派 |
| `ui/screens/AboutScreen.kt` | insets |
| `data/HistoryRepository.kt` | 新增 `restore()` |
| `viewmodel/HistoryViewModel.kt` | 新增 `restore()` |
| `app/build.gradle.kts` | 移除 `compileSdkMinor`（改为编译 `android-36`） |

## 四、验证

```
gradle :app:compileDebugKotlin   → BUILD SUCCESSFUL
```

零新增依赖，全部使用 Compose 内置 API（`Brush` / `drawBehind` / `shadow` / `border` / `graphicsLayer` / `WindowInsets`）。

## 五、已知遗留

- 所有界面文案仍是硬编码中文字面量，`strings.xml` 基本没被使用 —— 单语言应用可用，
  但要做多语言需要先收敛到资源。
- 尚未接入「减少动态效果」（Remove animations）系统设置的无障碍开关。
- 缺少 Compose UI 测试与截图回归。

---

# 第一轮 · 2026-08-26

## 设计系统

- **色彩体系**：扩展墨色系 / 朱砂系 / 米白系，新增装饰金铜色系，新增 `SurfaceElevated` 与背景渐变色，补齐 14 个暗色主题色值
- **主题系统**：新增完整暗色主题、排版加字间距与行高、新增 4dp 基准形状阶梯、状态栏透明化
- **XML 资源**：`colors.xml` / `themes.xml` / `values-night/themes.xml`

## 通用组件

| 组件 | 内容 |
|------|------|
| DisclaimerBar | 渐变背景、字间距 |
| InfoCard | 14dp 圆角、精致阴影、描边 |
| SectionTitle | 渐变竖条 |
| LevelTag | 水平渐变填充、描边 |
| GradientButton（新增） | 渐变按钮、彩色阴影、按压动画、loading 态 |
| DecorativeDivider（新增） | 七色渐变分隔线 |
| SealStamp（新增） | 印章风格圆形标记 |
| OrnateCard（新增） | 四角装饰线卡片 |

## 页面

7 个页面统一为「渐变背景 + 中式古典美学」：印章式标题、古风卡片、装饰分隔线、吉凶色标识。

---

*第二轮生成时间：2026-10-08*
