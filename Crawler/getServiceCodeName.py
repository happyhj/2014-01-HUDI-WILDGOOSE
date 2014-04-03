#!/usr/bin/python
# -*- coding: utf-8 -*-

CTG_CODE = {
    "10": {
        "k": "정치",
        "e": "politics",
        "s01": {
            "k": "선거",
            "e": "election"
        },
        "s02": {
            "k": "국회·정당",
            "e": "assemgov"
        },
        "s03": {
            "k": "청와대",
            "e": "bluehouse"
        },
        "s04": {
            "k": "외교",
            "e": "diplomacy"
        },
        "s05": {
            "k": "국방",
            "e": "diplomacy"
        },
        "s06": {
            "k": "행정",
            "e": "government"
        }
    },
    "11": {
        "k": "경제",
        "e": "money",
        "s01": {
            "k": "국제경제",
            "e": "interecono"
        },
        "s02": {
            "k": "금융·보험",
            "e": "finance"
        },
        "s03": {
            "k": "증권",
            "e": "stock"
        },
        "s04": {
            "k": "부동산",
            "e": "estate"
        },
        "s05": {
            "k": "산업",
            "e": "industry"
        },
        "s06": {
            "k": "일터",
            "e": "job"
        },
        "s07": {
            "k": "직장인",
            "e": "office"
        }
    },
    "12": {
        "k": "사회",
        "e": "Life",
        "s01": {
            "k": "교육",
            "e": "education"
        },
        "s02": {
            "k": "복지·노동",
            "e": "welfare"
        },
        "s03": {
            "k": "사건·사고",
            "e": "accident"
        },
        "s04": {
            "k": "날씨",
            "e": "weather"
        },
        "s05": {
            "k": "건강",
            "e": "health"
        },
        "s06": {
            "k": "종교",
            "e": "religion"
        },
        "s07": {
            "k": "사람",
            "e": "people"
        },
        "s08": {
            "k": "여행",
            "e": "travel"
        },
        "s09": {
            "k": "맛집·멋집",
            "e": "restaurant"
        },
        "s10": {
            "k": "레포츠",
            "e": "leports"
        },
        "s11": {
            "k": "검찰법원",
            "e": "law"
        },
        "s12": {
            "k": "여행",
            "e": "travel"
        },
        "s13": {
            "k": "지역",
            "e": "local"
        },
        "s14": {
            "k": "푸드",
            "e": "food"
        }
    },
    "13": {
        "k": "지구촌",
        "e": "world",
        "s01": {
            "k": "미국·북미",
            "e": "northame"
        },
        "s02": {
            "k": "일본",
            "e": "japan"
        },
        "s03": {
            "k": "중국",
            "e": "china"
        },
        "s04": {
            "k": "러시아",
            "e": "russia"
        },
        "s05": {
            "k": "아·태",
            "e": "asiapacific"
        },
        "s06": {
            "k": "유럽",
            "e": "eu"
        },
        "s07": {
            "k": "중남미",
            "e": "southame"
        },
        "s08": {
            "k": "중동",
            "e": "middleeast"
        },
        "s09": {
            "k": "아프리카",
            "e": "africa"
        }
    },
    "14": {
        "k": "스포츠",
        "e": "sports",
        "s01": {
            "k": "야구",
            "e": "baseball"
        },
        "s03": {
            "k": "축구",
            "e": "soccer",
            "l02": {
                "k": "국내축구",
                "e": "soccer"
            },
            "l06": {
                "k": "일본축구",
                "e": "soccer"
            }
        },
        "s04": {
            "k": "농구",
            "e": "basketball",
            "l02": {
                "k": "국내농구",
                "e": "basketball"
            },
            "l03": {
                "k": "NBA",
                "e": "basketball"
            }
        },
        "s05": {
            "k": "배구",
            "e": "volleyball"
        },
        "s06": {
            "k": "골프",
            "e": "golf"
        }
    },
    "15": {
        "k": "연예",
        "e": "star",
        "s01": {
            "k": "영화",
            "e": "movie"
        },
        "s02": {
            "k": "방송",
            "e": "entertainment"
        },
        "s03": {
            "k": "연예가소식",
            "e": "entertainment"
        },
        "s04": {
            "k": "애니·만화",
            "e": "animation"
        }
    },
    "16": {
        "k": "IT소식",
        "e": "Infotech",
        "s01": {
            "k": "IT",
            "e": "it"
        },
        "s02": {
            "k": "바이오",
            "e": "bio"
        },
        "s03": {
            "k": "과학",
            "e": "science"
        },
        "s04": {
            "k": "반도체",
            "e": "semiconduct"
        },
        "s05": {
            "k": "컴퓨터",
            "e": "computer"
        },
        "s06": {
            "k": "인터넷",
            "e": "internet"
        },
        "s07": {
            "k": "게임",
            "e": "game"
        }
    },
    "17": {
        "k": "생활문화",
        "e": "culture",
        "s01": {
            "k": "종교·학술",
            "e": "religion"
        },
        "s02": {
            "k": "문화재",
            "e": "property"
        },
        "s03": {
            "k": "책·문학",
            "e": "book"
        },
        "s04": {
            "k": "클래식",
            "e": "classic"
        },
        "s05": {
            "k": "애니·만화",
            "e": "animation"
        },
        "s06": {
            "k": "전시·미술",
            "e": "performance"
        },
        "s07": {
            "k": "연극·무용",
            "e": "play"
        }
    },
    "20": {
        "k": "사설·칼럼",
        "e": "opinion",
        "s01": {
            "k": "사설",
            "e": "sasul"
        },
        "s02": {
            "k": "칼럼",
            "e": "column"
        },
        "s03": {
            "k": "운세",
            "e": "fortune"
        },
        "s04": {
            "k": "시",
            "e": "poem"
        },
        "s05": {
            "k": "영어",
            "e": "english"
        },
        "s06": {
            "k": "일어",
            "e": "japanese"
        },
        "s07": {
            "k": "중국어",
            "e": "chinese"
        },
        "s08": {
            "k": "우리말",
            "e": "korean"
        },
        "s09": {
            "k": "바로잡습니다",
            "e": "correct"
        }
    },
    "22": {
        "k": "화제",
        "e": "topic"
    },
    "23": {
        "k": "브랜드뉴스",
        "e": "",
        "s00": {
            "k": "재테크·부동산",
            "e": ""
        }
    }
}