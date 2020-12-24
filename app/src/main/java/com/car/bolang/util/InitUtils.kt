package com.car.bolang.util

import com.car.bolang.bean.*

object InitUtils {
    private val imageList= listOf("https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1606027959539&di=423ccfe6e29fc728b41c002221bb678a&imgtype=0&src=http%3A%2F%2F00.minipic.eastday.com%2F20190312%2F20190312105900_ee57909c2c5f04df5bf871253495cba5_9.jpeg",
        "https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1606027959537&di=93c8dffaa85b51dee659d57f21da8fd8&imgtype=0&src=http%3A%2F%2Fimg.cheshi-img.com%2Fsellernews%2F598600%2F598691%2Fec53b1aed30a312c.jpg",
        "https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1606027959537&di=9506ddab1ed6ecbba1707a22ac812ec0&imgtype=0&src=http%3A%2F%2Fpic1.win4000.com%2Fwallpaper%2F9%2F53ad417078714.jpg")



    private const val homeUrl="https://www.baidu.com"

    val deviceList= listOf(listOf("定量机20-0-1:25","定量机20-90-1:25","定量机80-0-1:20","定量机80-90-1:20","定量机80-0-1:40","定量机160-0-1:20","定量机160-0-1:40","定量机400-0-1:9")
                                                    ,listOf("自动胶枪AK403-短","自动胶枪AK403-长","自动胶枪AK703")
                                                    ,listOf("填充阀AK401","填充阀AK701")
                                                    , listOf("手动胶枪SCA","手动胶枪克虏伯")
                                                    , listOf("喉封SCA"))

    val classList=listOf("前部","下部一","下部二","主焊","侧围内板","侧围外办","四门","前后盖","维修")
    val groupList= listOf(listOf("前地板班组甲","前地板班组乙","前纵梁班组甲","前纵梁班组乙","后纵梁班组甲","后纵梁班组乙"),
                                        listOf("后地板班组甲","后地板班组乙","补焊线班组甲","补焊线班组乙"),
                                        listOf("点定班组甲","点定班组乙","分装班组甲","分装班组乙","前挡板班组甲","前挡板班组乙","补焊班组甲","补焊班组乙"),
                                        listOf("主焊线班组甲","主焊线班组乙","主焊线PSD班组甲 ","主焊线PSD班组乙","VBT班组甲","VBT班组乙"),
                                        listOf("左侧围分装班组甲","左侧围分装班组乙","左侧围自动线班组甲 ","左侧围自动线班组乙","右侧围分装班组甲","右侧围分装班组乙","右侧围自动线班组甲","右侧围自动线班组乙"),
                                        listOf("左侧围外板班组甲","左侧围外板班组乙","右侧围外板班组甲 ","右侧围外板班组乙","分装班组甲","分装班组乙"),
                                        listOf("左前门班组甲","左前门班组乙","右前门班组甲 ","右前门班组乙","压合技术组","后门班组甲","后门班组乙","新线体班组甲","新线体班组乙"),
                                        listOf("前盖班组甲","前盖班组乙","后盖班组甲 ","后盖班组乙","新线体班组甲","新线体班组乙"),
                                        listOf("总拼班组甲","总拼班组乙","分拼班组甲 ","分拼班组乙","机械化班组","机修班组"))

    val reasonList= listOf("周期性漏胶","突发漏胶","碰撞","线路故障","气路故障","新备件","试验件","其它")

    @JvmStatic
    fun  initNewsList():List<NewData>{
        var list :MutableList<NewData> =ArrayList()
        imageList.forEach {
            val data=NewData("2020-11-11",it,homeUrl,"大众大众，全球销量第一")
            list.add(data)
        }
        return  list

    }

    @JvmStatic
    fun  initModeList():List<SmartModelVo>{
        var list :MutableList<SmartModelVo> =ArrayList()
        imageList.forEach {
            val data=SmartModelVo("2020-11-11",1001,"大众集团模板")
            list.add(data)
        }
        return  list

    }


    @JvmStatic
    fun  initGoodList():List<GoodsVO>{
        var list :MutableList<GoodsVO> =ArrayList()
        var goodVO=GoodsVO("","定量机")
        list.add(goodVO)
        goodVO=GoodsVO("","自动胶枪")
        list.add(goodVO)
        goodVO=GoodsVO("","手动胶枪")
        list.add(goodVO)
        goodVO=GoodsVO("","填充阀")
        list.add(goodVO)
        goodVO=GoodsVO("","喉封")
        list.add(goodVO)
        goodVO=GoodsVO("","整机备件")
        list.add(goodVO)
        return  list

    }
}