package com.xiaoliuren.app.data

/**
 * 小六壬六宫枚举（含全部静态解读数据）
 *
 * 六宫固定顺序（顺时针循环，index 0-5）：
 *   大安 → 留连 → 速喜 → 赤口 → 小吉 → 空亡
 *
 * 吉凶等级：
 *   0 = 大吉      1 = 小吉      2 = 半吉半凶
 *   3 = 凶        4 = 大凶
 *
 * 所有数据内置，无需外部加载
 */
enum class LiuRenPalace(
    val index: Int,
    val palaceName: String,
    val wuxing: String,
    val shensha: String,
    val jiXiongLevel: Int,
    val handPosition: String,
    val coreTone: String,
    val ancientFormula: String,
    val coreMeaning: String,
    val workMeaning: String,
    val moneyMeaning: String,
    val lostMeaning: String,
    val travelerMeaning: String,
    val loveMeaning: String,
    val healthMeaning: String,
    val officialMeaning: String,
    val suitable: String,
    val avoid: String,
    val numbers: List<Int>,
    val direction: String,
    val supplement: String
) {
    DA_AN(
        index = 0,
        palaceName = "大安",
        wuxing = "阳木",
        shensha = "青龙",
        jiXiongLevel = 0,
        handPosition = "食指根部（第一指节下端，靠近手掌处）",
        coreTone = "安稳静止、守成平顺",
        ancientFormula = "大安事事昌，求财在坤方，失物去不远，宅舍保安康。\n行人身未动，病者主无妨，将军回田野，仔细更推详。",
        coreMeaning = "静吉动平，主安稳、停滞、守成，一切以不动、不变、等待为吉。",
        workMeaning = "事情平稳无大波折，但进展缓慢，宜静不宜动，原地等待即可，主动冒进反而损耗。",
        moneyMeaning = "正财稳定，偏财无望；适合守业、固定收入，不宜投资、投机、借钱。",
        lostMeaning = "物品未丢失，就在家中或熟悉的地方，没走远，多在角落、低处、木质家具附近，仔细寻找可找回。",
        travelerMeaning = "人尚未动身，或原地停留，暂时不会回来，也暂无消息。",
        loveMeaning = "关系稳定但平淡，无大进展，也无大矛盾；问复合则暂时没有变动。",
        healthMeaning = "病情不重，无大碍，慢慢休养即可痊愈，无需过度担心。",
        officialMeaning = "官非口舌较轻，不会有大的纠纷，宜静观其变，不宜主动挑起事端。",
        suitable = "守成、等待、休养",
        avoid = "主动出击、大额投资、搬迁变动",
        numbers = listOf(1, 5, 7),
        direction = "东南、西南",
        supplement = "大安主静，问动则平，问静则吉。出行偏慢易延误，但安全无虞。"
    ),

    LIU_LIAN(
        index = 1,
        palaceName = "留连",
        wuxing = "阴水",
        shensha = "玄武",
        jiXiongLevel = 2,
        handPosition = "食指指尖（第一指节上端）",
        coreTone = "拖延纠缠、反复暗昧",
        ancientFormula = "留连事难成，求谋日未明，官事只宜缓，去者未回程。\n失物南方见，急讨方遂心，更需防口舌，人口且平平。",
        coreMeaning = "拖延、纠缠、反复、暗昧，事情悬而未决，越急越慢，无明确结果。",
        workMeaning = "一拖再拖，反复变动，没有明确期限，短期内难有结果，不可急于求成。",
        moneyMeaning = "钱财反复，到手又出波折，回款慢、账目纠缠，不宜催债、签约。",
        lostMeaning = "物品还在，但被遮挡或被人挪动，难找，多在南方、水边、阴暗处，急找难成，放一放反而有线索。",
        travelerMeaning = "被琐事绊住，归期延后，音讯稀少，短期内回不来。",
        loveMeaning = "关系拉扯纠缠，藕断丝连，分不彻底也合不痛快，多有猜忌、暧昧不明。",
        healthMeaning = "病情缠绵，好转慢，容易反复，多是湿气、肠胃、睡眠类问题。",
        officialMeaning = "官司纠纷拖延不决，不宜硬刚，适合缓处理、私下和解。",
        suitable = "缓行、拖延、冷处理",
        avoid = "催促、决策、签约、争执",
        numbers = listOf(2, 7, 8),
        direction = "北方",
        supplement = "并非全凶。问“麻烦会不会结束”主纠缠不散；问“感情能不能复合”主尚有牵连。"
    ),

    SU_XI(
        index = 2,
        palaceName = "速喜",
        wuxing = "阳火",
        shensha = "朱雀",
        jiXiongLevel = 0,
        handPosition = "中指指尖",
        coreTone = "快速喜庆、消息立至",
        ancientFormula = "速喜喜来临，求财向南行，失物申未午，逢人路上寻。\n官事有福德，病者无祸侵，田宅六畜吉，行人有信音。",
        coreMeaning = "快速、喜庆、消息立至，好事来得快，时效性极强，需趁热打铁。",
        workMeaning = "进展极快，短时间内就有结果，多有喜讯，适合主动出击。",
        moneyMeaning = "得财快，适合短期求财、南方求财，交易顺利，回款迅速。",
        lostMeaning = "物品在室外、路上，很快能找到线索，多在南边、热闹处，问问路人可得消息。",
        travelerMeaning = "人很快就到，或马上有消息传来，当日或近期必有回音。",
        loveMeaning = "有喜讯，进展快，暧昧期易确定关系，问见面则很快能见到。",
        healthMeaning = "病情好转快，很快痊愈，多是上火、炎症类急性问题，好得也快。",
        officialMeaning = "官司有贵人相助，逢凶化吉，能得到好结果。",
        suitable = "行动、签约、求财、见面",
        avoid = "拖延、犹豫",
        numbers = listOf(3, 6, 9),
        direction = "南方",
        supplement = "吉气来得快去得也快，不主长久，只主短期快速应验，错过时机则运势消退。"
    ),

    CHI_KOU(
        index = 3,
        palaceName = "赤口",
        wuxing = "阴金",
        shensha = "白虎",
        jiXiongLevel = 3,
        handPosition = "无名指指尖",
        coreTone = "口舌是非、破损损耗",
        ancientFormula = "赤口主口舌，官非切要防，失物急去寻，行人有惊慌。\n鸡犬多作怪，病者出西方，更须防咒诅，恐怕染瘟殃。",
        coreMeaning = "口舌是非、破损损耗、小人冲撞，主争执、伤害、破财。",
        workMeaning = "容易起争执、谈崩，多有小人背后诋毁，事情阻碍重重，难顺利。",
        moneyMeaning = "易破财、有经济纠纷，不宜投资、合伙、借钱，容易亏空、赖账。",
        lostMeaning = "物品易损坏、丢失难寻，要尽快找，晚了大概率找不回，多在西边、金属器物旁。",
        travelerMeaning = "在外有惊吓、是非，路途不顺，有波折。",
        loveMeaning = "多争吵、矛盾激化，容易分手、闹僵，口舌冲突不断。",
        healthMeaning = "易磕碰受伤、上火发炎、呼吸道问题，西方就医更利，防意外伤。",
        officialMeaning = "官司不利，易吃官司、受处罚，尽量避免冲突，别打官司。",
        suitable = "低调、忍让、避冲突",
        avoid = "争执、投资、出行、与人理论",
        numbers = listOf(4, 7, 10),
        direction = "西方",
        supplement = "凶性有轻重：问小事主拌嘴、小损耗；问大事主官司、重伤、大破财。"
    ),

    XIAO_JI(
        index = 4,
        palaceName = "小吉",
        wuxing = "阳水",
        shensha = "六合",
        jiXiongLevel = 1,
        handPosition = "无名指根部",
        coreTone = "小有顺遂、得人相助",
        ancientFormula = "小吉最吉昌，路上好商量，阴人来报喜，失物在坤方。\n行人立便至，交易甚是强，凡事皆和合，病者祷上苍。",
        coreMeaning = "小吉小利、人际和合、得人相助，主小事可成，大事力不足。",
        workMeaning = "小事能成，大事力度不够，多靠他人帮忙促成，外出办事更顺。",
        moneyMeaning = "小有收益，财不大但稳，适合小本生意、短途求财，交易顺利。",
        lostMeaning = "能找回，多在西南方位、低处、角落，或被女性、晚辈捡到。",
        travelerMeaning = "很快就到，行程顺利，有消息传来。",
        loveMeaning = "关系和谐，有小惊喜，多有女性牵线，适合沟通、约会、协商。",
        healthMeaning = "病情向好，无大碍，慢慢恢复，多靠调养、旁人照料。",
        officialMeaning = "纠纷能和解，有人从中说和，不至于闹大。",
        suitable = "沟通、合作、出行、求助",
        avoid = "贪大求全、冒进扩张",
        numbers = listOf(3, 5, 8),
        direction = "西南",
        supplement = "常主“阴人助力”，即女性、暗中的贵人、不显眼的人帮忙。"
    ),

    KONG_WANG(
        index = 5,
        palaceName = "空亡",
        wuxing = "阴土",
        shensha = "勾陈",
        jiXiongLevel = 4,
        handPosition = "中指根部（靠近手掌正中处）",
        coreTone = "虚空落空、徒劳无功",
        ancientFormula = "空亡事不祥，阴人多乖张，求财无利益，行人有灾殃。\n失物寻不见，官事有刑伤，病人逢暗鬼，禳解保安康。",
        coreMeaning = "虚空、落空、虚假、徒劳无功，凡事有名无实，竹篮打水一场空。",
        workMeaning = "大概率不成，看似有希望实则一场空，承诺兑现不了，白费功夫。",
        moneyMeaning = "求财无望，投入没回报，容易被骗、踩坑，严禁投资、借钱。",
        lostMeaning = "大概率找不回来，丢失无踪，或被人拿走难以寻回。",
        travelerMeaning = "音讯断绝，联系不上，或路途有灾殃、不顺。",
        loveMeaning = "关系虚假、口头承诺不算数，有缘无分，难有结果，易遇烂桃花。",
        healthMeaning = "病情缠绵反复，查不出病因，或精神萎靡、运势低迷，需好好休养。",
        officialMeaning = "官司不利，易受刑罚、损失，有理说不清，败诉概率大。",
        suitable = "止损、放弃、低调蛰伏",
        avoid = "任何决策、投资、承诺、远行",
        numbers = listOf(3, 6, 9),
        direction = "北方",
        supplement = "并非绝对全凶。若问烦心事、病痛、小人，空亡主消散无踪，反而是吉兆；问正事、求财、成事则为大凶。"
    );

    companion object {
        /** 按索引获取宫位 */
        fun fromIndex(index: Int): LiuRenPalace = entries[index]

        /** 吉凶等级文字 */
        fun levelText(level: Int): String = when (level) {
            0 -> "大吉"
            1 -> "小吉"
            2 -> "半吉半凶"
            3 -> "凶"
            4 -> "大凶"
            else -> "未知"
        }

        /** 吉凶等级简短文字（用于标签） */
        fun levelShortText(level: Int): String = when (level) {
            0 -> "大吉"
            1 -> "小吉"
            2 -> "半吉"
            3 -> "凶"
            4 -> "大凶"
            else -> "未知"
        }
    }

    /** 吉凶等级文字 */
    val levelText: String get() = levelText(jiXiongLevel)

    /** 五行神煞合称（如"阳木·青龙"） */
    val wuxingShensha: String get() = "$wuxing·$shensha"

    /** 宫位全称（如"大安·青龙木"） */
    val fullName: String get() = "$palaceName·$shensha${wuxing.takeLast(1)}"
}