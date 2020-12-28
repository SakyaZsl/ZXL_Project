package com.car.bolang.network



class UrlProtocol {
        companion object{

                //         const val  URL_HOST="http://101.132.168.76:8090"
                const val  URL_HOST="http://39.98.65.101:8080"
                const val  URL_IMG="http://101.132.168.76:8090/imgs/"
                const val  LOGIN= "$URL_HOST/app/login"
                const val  SEND_MSG= "$URL_HOST/app/sendMsg/"
                const val  GOOD_LIST="$URL_HOST/app/getGoodsList"
                const val  BRAND_LIST="$URL_HOST/app/getBrandList"
                const val  NEWS_LIST="$URL_HOST/app/getNewsList"
                //首页金价
                const val  HOME_PAGE="$URL_HOST/app/homePage"
                //填写邀请人邀请码
                const val  ADD_INVITE="$URL_HOST/app/fillInvitees"
                //充值年卡和季卡
                const val  RECHARGE_VIP="$URL_HOST/app/userRecharge"
                const val  START_PAYMENT="$URL_HOST/wxPay/appPay"
                //
                const val  MEMBER_CARD_LIST="$URL_HOST/app/getMemberCardList"
                //问题列表
                const val  PROBLEM_LIST="$URL_HOST/app/getProblemLit"
                const val  UPLOAD_AVATAR="$URL_HOST/rlsb/borrow/record/file_upload"
                const val  ASK_QUESTION="$URL_HOST/app/askQuestions"
                const val  QUESTION_PRICE="$URL_HOST/app/questionsPrice"
                const val  HOME_BANNER="$URL_HOST/app/getRotationChartList"
                const val  USER_INFO="$URL_HOST/app/getUserById"
                const val  TEST_SUBMIT="$URL_HOST/rlsb/borrow/record/add"
                const val GET_TOKEN = 1
                const val SUCCESS_CODE = "00"
                val CHECK_TOKEN = 2



        }
}