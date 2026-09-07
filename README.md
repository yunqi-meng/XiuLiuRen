# 小六壬时课

> 中国传统民俗文化「小六壬（六壬时课 / 马前课）」Android 起课工具，纯离线、零依赖联网、零用户数据收集。

一款采用 **Kotlin + Jetpack Compose** 构建的 Material3 应用，以中式古典美学（墨色、朱砂、装饰金、印章、渐变）呈现传统小六壬三步起课法，支持一键起课、自定义时间、历史记录与六宫知识库查询。

---

## 功能特性

- **一键起课**：以当前时间（公历/农历/节气月/时辰）即时起课，得出月宫·日宫·时宫三宫结果
- **自定义时间起课**：任意指定日期与时辰推算
- **完整结果解读**：三宫象义、古诀原文、核心象义、分类解读（办事/求财/失物/行人/感情/健康）、综合断语、宜忌、对应数字与参考方位
- **六宫知识库**：大安、留连、速喜、赤口、小吉、空亡六宫的掌位、五行神煞、吉凶定性、口诀与起课方法
- **历史记录**：本地 Room 数据库持久化，支持查看与清空
- **明暗双主题**：自动跟随系统暗色模式，10+ 主题色函数自适应
- **纯离线**：不联网、不收集任何用户个人信息，所有数据存于本地设备

## 算法说明

采用民间流传最广的**基础版小六壬**算法，严格遵循正统三步法：

1. **大安起正月**：以大安宫起正月，顺时针点数到问事月份定**月宫**
2. **月上起日**：以月宫为初一顺数到问事日期定**日宫**
3. **日上起时**：以日宫为子时顺数到问事时辰定**时宫**

- 月份以**二十四节气**划分（非农历初一）
- 日子以 **23:00 子时**为界
- 三宫结合断吉凶（大吉 / 小吉 / 半吉半凶 / 凶 / 大凶）

> ⚠️ 仅适合测算近期日常小事，结果仅供民俗文化娱乐参考，不构成任何决策建议。

## 技术栈

| 分类 | 技术 |
|------|------|
| 语言 | Kotlin 1.9.24 |
| 平台 | Android minSdk 26 / targetSdk 36 / compileSdk 36 |
| 构建 | Gradle 8.13.2 (Kotlin DSL) |
| UI | Jetpack Compose + Material3 + Compose BOM 2024.06.00 |
| 导航 | Navigation Compose 2.7.7 |
| 数据 | Room 2.6.1 (KSP) + DataStore Preferences 1.1.1 |
| 历法 | 自实现 LunarCalendar / SolarTerms / Shichen |
| 主题 | 自研中式设计系统（墨/朱砂/装饰金 + 渐变 + 暗色主题） |

## 项目结构

```
app/
└── src/main/
    ├── AndroidManifest.xml
    ├── java/com/xiaoliuren/app/
    │   ├── MainActivity.kt              # 入口
    │   ├── core/                        # 核心算法
    │   │   ├── DivineEngine.kt          # 起课引擎
    │   │   ├── XiaoLiuRen.kt            # 小六壬算法
    │   │   ├── LunarCalendar.kt         # 农历转换
    │   │   ├── SolarTerms.kt            # 二十四节气
    │   │   └── Shichen.kt              # 时辰
    │   ├── data/                        # 数据层
    │   │   ├── db/                      # Room 数据库
    │   │   ├── HistoryRepository.kt
    │   │   ├── JudgmentGenerator.kt     # 断语生成
    │   │   └── LiuRenPalace.kt          # 六宫模型
    │   ├── ui/
    │   │   ├── components/              # 通用组件（渐变按钮/印章/装饰线等）
    │   │   ├── screens/                # 7 个页面
    │   │   ├── navigation/
    │   │   └── theme/                  # 色彩/主题/渐变
    │   └── viewmodel/
    └── res/                            # 资源（colors/themes/strings）
```

## 构建与运行

> 仓库未包含 `gradlew` 脚本与 `gradle-wrapper.jar`，请使用本地 Gradle 或 Android Studio 打开。

```bash
# 命令行（需本地 Gradle 8.x）
gradle :app:assembleDebug

# 或直接用 Android Studio 打开根目录，Sync 后 Run
```

输出 APK：`app/build/outputs/apk/debug/app-debug.apk`

## 免责声明

本工具仅供中国传统民俗文化研究与娱乐参考，不构成任何人生决策、医疗、法律、财务建议，请勿过度迷信。小六壬属于民俗占卜文化范畴，结果仅供参考，切勿作为现实决策依据。

## 许可

本项目仅供学习与文化交流使用。