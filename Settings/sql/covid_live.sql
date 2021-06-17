DROP TABLE IF EXISTS `covid_live`;
CREATE TABLE `covid_live` (
  `live_date` date NOT NULL,
  `seoul` int(11) NOT NULL,
  `incheon` int(11) NOT NULL,
  `gwangju` int(11) NOT NULL,
  `daejeon` int(11) NOT NULL,
  `daegu` int(11) NOT NULL,
  `busan` int(11) NOT NULL,
  `ulsan` int(11) NOT NULL,
  `sejong` int(11) NOT NULL,
  `gyeonggi` int(11) NOT NULL,
  `gangwon` int(11) NOT NULL,
  `chungbuk` int(11) NOT NULL,
  `chungnam` int(11) NOT NULL,
  `jeonbuk` int(11) NOT NULL,
  `jeonnam` int(11) NOT NULL,
  `gyeongbuk` int(11) NOT NULL,
  `gyeongnam` int(11) NOT NULL,
  `jeju` int(11) NOT NULL,
  PRIMARY KEY (`live_date`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

LOCK TABLES `covid_live` WRITE;
INSERT INTO `covid_live` VALUES ('2021-06-16',31,11,0,0,0,0,0,0,102,0,6,2,0,10,0,25,5);
UNLOCK TABLES;