package com.car.bolang.activity

import android.Manifest
import android.content.Context
import android.content.Intent
import android.util.Log
import com.car.bolang.common.BaseActivity
import com.car.bolang.network.HttpUtilsInterface
import com.car.bolang.network.NetHelpUtils
import com.car.bolang.network.UrlProtocol
import com.car.bolang.util.GsonUtil
import com.car.bolang.util.ToastUtils
import kotlinx.android.synthetic.main.activity_common_head.*
import kotlinx.android.synthetic.main.activity_feedback.*
import kotlinx.android.synthetic.main.activity_search.*
import android.widget.Toast
import android.content.pm.PackageManager
import android.Manifest.permission.WRITE_EXTERNAL_STORAGE
import android.app.Activity
import android.os.Build.VERSION_CODES.M
import androidx.annotation.RequiresApi
import androidx.core.content.ContextCompat
import com.car.bolang.fragment.SelectPictureFragment
import com.car.bolang.inter.TakePictureCallback
import android.provider.MediaStore
import androidx.core.content.FileProvider
import android.os.Build.VERSION_CODES
import android.os.Build.VERSION
import android.net.Uri
import android.graphics.BitmapFactory
import android.graphics.Bitmap
import android.view.View
import java.io.*
import android.provider.MediaStore.MediaColumns
import android.content.ContentResolver
import android.text.TextUtils
import com.car.bolang.R
import com.car.bolang.bean.*
import com.car.bolang.event.PaymentCallbackEvent
import com.car.bolang.util.Constants
import com.car.bolang.util.PreferencesUtil
import com.tencent.mm.opensdk.modelpay.PayReq
import com.tencent.mm.opensdk.openapi.IWXAPI
import com.tencent.mm.opensdk.openapi.WXAPIFactory
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode
import java.time.temporal.TemporalAmount


class FeedbackActivity : BaseActivity(), TakePictureCallback {
    private var api: IWXAPI? = null
    private var mUri: Uri? = null
    private var imageFile: File? = null
    private var imageUrl: String = ""

    var fragment: SelectPictureFragment? = null


    companion object {
        const val REQUEST_PERMISSION_CODE = 1000
        const val REQUEST_TAKE_PHOTO_CODE = 1001
        fun startAction(context: Context) {
            val intent = Intent(context, FeedbackActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
            context.startActivity(intent)
        }
    }

    override fun setLayoutResID(): Int {
        return R.layout.activity_feedback
    }

    @RequiresApi(M)
    override fun init() {
        tvTitle.text = "问题"
        EventBus.getDefault().register(this)
        api = WXAPIFactory.createWXAPI(this, Constants.WX_APP_ID)
        btnCommitProblem.setOnClickListener {
            getOrder()
        }
        llTakePicture.setOnClickListener {
            if (!checkPermission()) {
                requestPermissions()
            } else {
                fragment?.let {
                    it.show(supportFragmentManager, "hx")
                } ?: {
                    fragment = SelectPictureFragment()
                    fragment?.setCallback(this)
                    fragment?.show(supportFragmentManager, "hx")
                }.invoke()

            }
        }
        ivBack.setOnClickListener {
            onBackPressed()
        }
    }

    override fun initData() {

    }

    private fun feedback() {
        mLoadingDialog?.show()
        val params = AskQuestionsReq(etFeedback.text.toString(), imageUrl, PreferencesUtil.getInstance().userId)
        Log.e("zzzz",GsonUtil.GsonString(params))
        NetHelpUtils.okGoBodyPost(this, UrlProtocol.ASK_QUESTION, GsonUtil.GsonString(params), object :
            HttpUtilsInterface {
            override fun onSuccess(result: String?) {
                mLoadingDialog?.dismiss()
                result?.let {
                    val bean=GsonUtil.GsonToBean(it,BaseBean::class.java)
                    if(bean.code==0){
                        ToastUtils.toastShort(this@FeedbackActivity,"谢谢您的反馈")
                        finish()
                    }
                }
            }

            override fun onError(code: Int, errorMsg: String?) {
                ToastUtils.toastShort(this@FeedbackActivity, "请求失败，请重试")
                mLoadingDialog?.dismiss()
            }
        })
    }

    private fun upLoadImg(file: File?) {
        file?.let {
            mLoadingDialog?.show()
            val param = HashMap<String, String>()
            NetHelpUtils.uploadFile(this, UrlProtocol.UPLOAD_AVATAR, param, "file", file, object :
                HttpUtilsInterface {
                override fun onSuccess(result: String?) {
                    mLoadingDialog?.dismiss()
                    Log.e("zzzz", "上传图片 result${result}")
                    val bean=GsonUtil.GsonToBean(result,UploadImgVO::class.java)
                    if(bean.code==NetHelpUtils.SUCCESS){
                        imageUrl=getImageUrl(bean.message)
                    }
                }

                override fun onError(code: Int, errorMsg: String?) {
                    ToastUtils.toastShort(this@FeedbackActivity, "请求失败，请重试")
                    mLoadingDialog?.dismiss()
                }
            })
        }

    }


    private fun  getOrder(){
        if(TextUtils.isEmpty(etFeedback.text.toString())){
            ToastUtils.toastShort(this,"请输入反馈内容")
            return

        }
        mLoadingDialog?.show()
        val param=HashMap<String,String>()
        NetHelpUtils.okgoGet(this, UrlProtocol.QUESTION_PRICE, param, object :
            HttpUtilsInterface {
            override fun onSuccess(result: String?) {
                Log.e("zzzz","getOrder result${result}")
                mLoadingDialog?.dismiss()
                result?.let {
                  val bean=GsonUtil.GsonToBean(it,QuestionOrderVO::class.java)
                    bean?.data?.let {data->
                        startPayment(data.amount)
                    }?:{
                        ToastUtils.toastShort(this@FeedbackActivity, "订单请求失败，请重试")
                    }.invoke()

                }
            }

            override fun onError(code: Int, errorMsg: String?) {
                ToastUtils.toastShort(this@FeedbackActivity, "请求失败，请重试")
                mLoadingDialog?.dismiss()
            }
        })
    }


    private fun startPayment(amount: Float) {
        mLoadingDialog?.show()
        val map = java.util.HashMap<String, String>()
        map["totalFee"] =amount.toString()
        map["attach"] = "提问反馈"
        NetHelpUtils.okgoPost(this, UrlProtocol.START_PAYMENT, map, object :
            HttpUtilsInterface {
            override fun onSuccess(result: String?) {
                Log.e("zzzz", "startPayment${result}")
                result?.let {
                    val bean = GsonUtil.GsonToBean(it, PaymentResultVO::class.java)
                    bean.data?.let { data ->
                        startWxPay(data)
                    }

                } ?: {
                    ToastUtils.toastShort(
                        this@FeedbackActivity,
                        getString(R.string.payment_error)
                    )
                }.invoke()
                mLoadingDialog?.dismiss()
            }

            override fun onError(code: Int, errorMsg: String?) {
                Log.e("zzzz", "onError${errorMsg}")
                mLoadingDialog?.dismiss()
                ToastUtils.toastShort(
                    this@FeedbackActivity,
                    getString(R.string.network_error)
                )
            }
        })
    }

    fun startWxPay(bean: PaymentData) {
        val req = PayReq()
        req.appId = bean.appId
        req.partnerId = bean.partnerid
        req.prepayId = bean.prepayid
        req.nonceStr = bean.nonceStr
        req.timeStamp = bean.timeStamp
        req.packageValue = "Sign=WXPay"
        req.sign = bean.signType
        req.extData = "app data" // optional
        // 在支付之前，如果应用没有注册到微信，应该先调用IWXMsg.registerApp将应用注册到微信
        api?.sendReq(req)

    }


    //检查权限
    private fun checkPermission(): Boolean {
        //是否有权限
        val haveCameraPermission = ContextCompat.checkSelfPermission(
            this,
            Manifest.permission.CAMERA
        ) == PackageManager.PERMISSION_GRANTED

        val haveWritePermission = ContextCompat.checkSelfPermission(
            this,
            WRITE_EXTERNAL_STORAGE
        ) == PackageManager.PERMISSION_GRANTED

        return haveCameraPermission && haveWritePermission

    }

    // 请求所需权限
    @RequiresApi(api = M)
    private fun requestPermissions() {
        requestPermissions(
            arrayOf(
                Manifest.permission.CAMERA,
                Manifest.permission.WRITE_EXTERNAL_STORAGE
            ), REQUEST_PERMISSION_CODE
        )
    }

    // 请求权限后会在这里回调
    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        when (requestCode) {
            REQUEST_PERMISSION_CODE -> {

                var allowAllPermission = false

                for (i in grantResults.indices) {
                    if (grantResults[0] != PackageManager.PERMISSION_GRANTED) {//被拒绝授权
                        allowAllPermission = false
                        break
                    }
                    allowAllPermission = true
                }

                if (allowAllPermission) {

                } else {
                    Toast.makeText(this, "该功能需要授权方可使用", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }


    override fun takeCamera() {
        fragment?.dismiss()
        takePhoto()
    }

    override fun takeAlbum() {

    }

    private fun takePhoto() {
        // 步骤一：创建存储照片的文件
        val path = filesDir.toString() + File.separator + "images" + File.separator
        val file = File(path, "feed${System.currentTimeMillis()}.jpg")
        imageFile = File(path, "feed${System.currentTimeMillis()}.jpg")
        if (!file.parentFile.exists())
            file.parentFile.mkdirs()

        if (VERSION.SDK_INT >= VERSION_CODES.N) {
            //步骤二：Android 7.0及以上获取文件 Uri
            mUri = FileProvider.getUriForFile(
                this@FeedbackActivity,
                "com.car.bolang.akm",
                file
            )
        } else {
            //步骤三：获取文件Uri
            mUri = Uri.fromFile(file)
        }
        //步骤四：调取系统拍照
        val intent = Intent("android.media.action.IMAGE_CAPTURE")
        intent.putExtra(MediaStore.EXTRA_OUTPUT, mUri)
        startActivityForResult(intent, REQUEST_TAKE_PHOTO_CODE)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        when (requestCode) {
            REQUEST_TAKE_PHOTO_CODE -> {
                if (resultCode == Activity.RESULT_OK) {
                    var bm: Bitmap? = null
                    try {
                        bm = getBitmapFormUri(mUri!!)
                    } catch (e: FileNotFoundException) {
                        e.printStackTrace()
                    } catch (e: IOException) {
                        e.printStackTrace()
                    }
                    bm?.let {
                        changeViewToImg()
                        ivFeedback.setImageBitmap(it)
                        saveBitmapFile(bm)
                        upLoadImg(imageFile)
                    }

                }
            }
            else -> {
            }
        }
    }

    private fun changeViewToImg() {
        ivFeedback.visibility = View.VISIBLE
        llTakePicture.visibility = View.GONE
    }

    fun changeViewNoData() {
        ivFeedback.visibility = View.GONE
        llTakePicture.visibility = View.VISIBLE
    }

    @Throws(FileNotFoundException::class, IOException::class)
    fun getBitmapFormUri(uri: Uri): Bitmap? {
        var input = contentResolver.openInputStream(uri)

        //这一段代码是不加载文件到内存中也得到bitmap的真是宽高，主要是设置inJustDecodeBounds为true
        val onlyBoundsOptions = BitmapFactory.Options()
        onlyBoundsOptions.inJustDecodeBounds = true//不加载到内存
        onlyBoundsOptions.inDither = true//optional
        onlyBoundsOptions.inPreferredConfig = Bitmap.Config.RGB_565//optional
        BitmapFactory.decodeStream(input, null, onlyBoundsOptions)
        input!!.close()
        val originalWidth = onlyBoundsOptions.outWidth
        val originalHeight = onlyBoundsOptions.outHeight
        if (originalWidth == -1 || originalHeight == -1)
            return null

        //图片分辨率以480x800为标准
        val hh = 800f//这里设置高度为800f
        val ww = 480f//这里设置宽度为480f
        //缩放比，由于是固定比例缩放，只用高或者宽其中一个数据进行计算即可
        var be = 1//be=1表示不缩放
        if (originalWidth > originalHeight && originalWidth > ww) {//如果宽度大的话根据宽度固定大小缩放
            be = (originalWidth / ww).toInt()
        } else if (originalWidth < originalHeight && originalHeight > hh) {//如果高度高的话根据宽度固定大小缩放
            be = (originalHeight / hh).toInt()
        }
        if (be <= 0)
            be = 1
        //比例压缩
        val bitmapOptions = BitmapFactory.Options()
        bitmapOptions.inSampleSize = be//设置缩放比例
        bitmapOptions.inDither = true
        bitmapOptions.inPreferredConfig = Bitmap.Config.RGB_565
        input = contentResolver.openInputStream(uri)
        val bitmap = BitmapFactory.decodeStream(input, null, bitmapOptions)
        input!!.close()

        return compressImage(bitmap!!)//再进行质量压缩
    }

    private fun compressImage(image: Bitmap): Bitmap? {
        val baos = ByteArrayOutputStream()
        image.compress(Bitmap.CompressFormat.JPEG, 100, baos)//质量压缩方法，这里100表示不压缩，把压缩后的数据存放到baos中
        var options = 100
        while (baos.toByteArray().size / 1024 > 100) {  //循环判断如果压缩后图片是否大于100kb,大于继续压缩
            baos.reset()//重置baos即清空baos
            //第一个参数 ：图片格式 ，第二个参数： 图片质量，100为最高，0为最差  ，第三个参数：保存压缩后的数据的流
            image.compress(Bitmap.CompressFormat.JPEG, options, baos)//这里压缩options，把压缩后的数据存放到baos中
            options -= 10//每次都减少10
            if (options <= 0)
                break
        }
        val isBm = ByteArrayInputStream(baos.toByteArray())//把压缩后的数据baos存放到ByteArrayInputStream中
        return BitmapFactory.decodeStream(isBm, null, null)
    }


    /**
     * Gets the corresponding path to a file from the given content:// URI
     * @param selectedVideoUri The content:// URI to find the file path from
     * @param contentResolver The content resolver to use to perform the query.
     * @return the file path as a string
     */
    fun getFilePathFromContentUri(
        selectedVideoUri: Uri,
        contentResolver: ContentResolver
    ): String {
        val filePath: String
        val filePathColumn = arrayOf(MediaColumns.DATA)

        val cursor = contentResolver.query(selectedVideoUri, filePathColumn, null, null, null)
        //      也可用下面的方法拿到cursor
        //      Cursor cursor = this.context.managedQuery(selectedVideoUri, filePathColumn, null, null, null);

        cursor!!.moveToFirst()

        val columnIndex = cursor.getColumnIndex(filePathColumn[0])
        filePath = cursor.getString(columnIndex)
        cursor.close()
        return filePath
    }


    fun saveBitmapFile(bitmap: Bitmap) {
        //将要保存图片的路径
        try {
            val bos = BufferedOutputStream(FileOutputStream(imageFile))
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, bos);
            bos.flush()
            bos.close()
        } catch (e: IOException) {
            e.printStackTrace()
        }
    }


    fun  getImageUrl(name:String):String{
        return "http://bolang.laingman.com:8090/imgs/${name}"
    }

    @Subscribe(threadMode = ThreadMode.MAIN, sticky = true)
    fun onGetStickyEvent(message: PaymentCallbackEvent) {
        Log.e("zzzz", "message${message.payStatus}")
        if(message.payStatus==0){
            feedback()
        }else{
            ToastUtils.toastShort(this,getString(R.string.payment_failed))
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        EventBus.getDefault().unregister(this)
    }

}