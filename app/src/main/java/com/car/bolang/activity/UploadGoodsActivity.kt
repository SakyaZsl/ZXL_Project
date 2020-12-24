package com.car.bolang.activity

import android.Manifest
import android.annotation.SuppressLint
import android.app.Activity
import android.content.ContentResolver
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.hardware.camera2.CameraManager
import android.net.Uri
import android.os.Build
import android.os.Environment
import android.provider.MediaStore
import android.text.TextUtils
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider
import com.car.bolang.R
import com.car.bolang.bean.*
import com.car.bolang.common.BaseActivity
import com.car.bolang.event.PaymentCallbackEvent
import com.car.bolang.fragment.SelectPictureFragment
import com.car.bolang.inter.TakePictureCallback
import com.car.bolang.network.HttpUtilsInterface
import com.car.bolang.network.NetHelpUtils
import com.car.bolang.network.UrlProtocol
import com.car.bolang.util.Constants
import com.car.bolang.util.GsonUtil
import com.car.bolang.util.PreferencesUtil
import com.car.bolang.util.ToastUtils
import com.tencent.mm.opensdk.modelpay.PayReq
import com.tencent.mm.opensdk.openapi.IWXAPI
import com.tencent.mm.opensdk.openapi.WXAPIFactory
import kotlinx.android.synthetic.main.activity_common_head.*
import kotlinx.android.synthetic.main.activity_feedback.*
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode
import java.io.*

class UploadGoodsActivity : BaseActivity(), TakePictureCallback {
    private var api: IWXAPI? = null
    private var mUri: Uri? = null
    private var imageFile: File? = null
    private var imageUrl: String = ""

    var fragment: SelectPictureFragment? = null


    companion object {
        const val REQUEST_PERMISSION_CODE = 1000
        const val REQUEST_TAKE_PHOTO_CODE = 1001
        fun startAction(context: Context) {
            val intent = Intent(context, UploadGoodsActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
            context.startActivity(intent)
        }
    }

    override fun setLayoutResID(): Int {
        return R.layout.activity_feedback
    }

    @RequiresApi(Build.VERSION_CODES.M)
    override fun init() {
        tvTitle.text = "拍照登记"
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
        val params = AskQuestionsReq(
            etFeedback.text.toString(),
            imageUrl,
            PreferencesUtil.getInstance().userId
        )
        Log.e("zzzz", GsonUtil.GsonString(params))
        NetHelpUtils.okGoBodyPost(
            this,
            UrlProtocol.ASK_QUESTION,
            GsonUtil.GsonString(params),
            object :
                HttpUtilsInterface {
                override fun onSuccess(result: String?) {
                    mLoadingDialog?.dismiss()
                    result?.let {
                        val bean = GsonUtil.GsonToBean(it, BaseBean::class.java)
                        if (bean.code == 0) {
                            ToastUtils.toastShort(this@UploadGoodsActivity, "谢谢您的反馈")
                            finish()
                        }
                    }
                }

                override fun onError(code: Int, errorMsg: String?) {
                    ToastUtils.toastShort(this@UploadGoodsActivity, "请求失败，请重试")
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
                    val bean = GsonUtil.GsonToBean(result, UploadImgVO::class.java)

                }

                override fun onError(code: Int, errorMsg: String?) {
                    ToastUtils.toastShort(this@UploadGoodsActivity, "请求失败，请重试")
                    mLoadingDialog?.dismiss()
                }
            })
        }

    }


    private fun getOrder() {
        if (imageFile == null) {
            ToastUtils.toastShort(this, "请上传照片")
            return
        }
        upLoadImg(imageFile)
//        MainActivity.startAction(this)
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
            Manifest.permission.WRITE_EXTERNAL_STORAGE
        ) == PackageManager.PERMISSION_GRANTED

        return haveCameraPermission && haveWritePermission

    }

    // 请求所需权限
    @RequiresApi(api = Build.VERSION_CODES.M)
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
        imageFile = File(path, "feed${System.currentTimeMillis()}.jpg")
        Log.e("zzzz", imageFile!!.absolutePath)
        if (!imageFile!!.parentFile.exists())
            imageFile!!.parentFile.mkdirs()

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            //步骤二：Android 7.0及以上获取文件 Uri
            mUri = FileProvider.getUriForFile(
                this@UploadGoodsActivity,
                "com.car.bolang.akm",
                imageFile!!
            )
        } else {
            //步骤三：获取文件Uri
            val path = File(getExternalFilesDir(null), "image/")
            if (!path.exists()) {
                path.mkdirs()
            }
            imageFile = File(path, "feed${System.currentTimeMillis()}.jpg")
            mUri = Uri.fromFile(imageFile)
        }
        //步骤四：调取系统拍照
        val intent = Intent("android.media.action.IMAGE_CAPTURE")
        intent.putExtra(MediaStore.EXTRA_OUTPUT, mUri)
        startActivityForResult(intent, REQUEST_TAKE_PHOTO_CODE)
    }

    @SuppressLint("MissingSuperCall")
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        when (requestCode) {
            REQUEST_TAKE_PHOTO_CODE -> {
                if (resultCode == Activity.RESULT_OK) {
                    ToastUtils.toastShort(this, "拍照成功")
                    if (Build.VERSION.SDK_INT < Build.VERSION_CODES.N) {
                        ToastUtils.toastLong(this, "版本对了")
                        loadBitmap()?.let {
                            ToastUtils.toastShort(this, "有图片了")
                            ivFeedback.setImageBitmap(it)
                            return
                        }
                        ToastUtils.toastLong(this, "没有图片了")
                    }
                    var bm: Bitmap? = null
                    try {
                        bm = getBitmapFormUri2(mUri!!)
                    } catch (e: FileNotFoundException) {
                        e.printStackTrace()
                    } catch (e: IOException) {
                        e.printStackTrace()
                    }
                    bm?.let {
                        ivFeedback.setImageBitmap(it)
                        saveBitmapFile(bm)
                    }

                }
            }
            else -> {
                ToastUtils.toastShort(this, "拍照失败")
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
        val ww = 600f//这里设置宽度为480f
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

    @Throws(FileNotFoundException::class, IOException::class)
    fun getBitmapFormUri2(uri: Uri): Bitmap? {
        tvTest.text = imageFile?.absolutePath

        var input = contentResolver.openInputStream(uri)
        //比例压缩
        val bitmapOptions = BitmapFactory.Options()
        bitmapOptions.inSampleSize = 1//设置缩放比例
        bitmapOptions.inDither = true
        bitmapOptions.inPreferredConfig = Bitmap.Config.RGB_565
        val bitmap = BitmapFactory.decodeStream(input, null, bitmapOptions)
        input!!.close()

        return bitmap//再进行质量压缩
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
        val filePathColumn = arrayOf(MediaStore.MediaColumns.DATA)

        val cursor = contentResolver.query(selectedVideoUri, filePathColumn, null, null, null)
        //      也可用下面的方法拿到cursor
        //      Cursor cursor = this.context.managedQuery(selectedVideoUri, filePathColumn, null, null, null);

        cursor!!.moveToFirst()

        val columnIndex = cursor.getColumnIndex(filePathColumn[0])
        filePath = cursor.getString(columnIndex)
        cursor.close()
        return filePath
    }


    private fun saveBitmapFile(bitmap: Bitmap) {
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


    fun getImageUrl(name: String): String {
        return "http://bolang.laingman.com:8090/imgs/${name}"
    }

    @Subscribe(threadMode = ThreadMode.MAIN, sticky = true)
    fun onGetStickyEvent(message: PaymentCallbackEvent) {
        Log.e("zzzz", "message${message.payStatus}")
        if (message.payStatus == 0) {
            feedback()
        } else {
            ToastUtils.toastShort(this, getString(R.string.payment_failed))
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        EventBus.getDefault().unregister(this)
    }


    fun loadBitmap(): Bitmap? {
        val opt = BitmapFactory.Options()
        opt.inJustDecodeBounds = true
        var bitmap = BitmapFactory.decodeFile(imageFile!!.absolutePath, opt)
        // 获取到这个图片的原始宽度和高度
        // 获取画布中间方框的宽度和高度
        // 获取到这个图片的原始宽度和高度
	    val picWidth = opt.outWidth
	    val picHeight = opt.outHeight
	// 获取画布中间方框的宽度和高度
	    val  screenWidth = 800
	    val  screenHeight = 600
        // isSampleSize是表示对图片的缩放程度，比如值为2图片的宽度和高度都变为以前的1/2
        opt.inSampleSize = 1
        // isSampleSize是表示对图片的缩放程度，比如值为2图片的宽度和高度都变为以前的1/2
        // 根据屏的大小和图片大小计算出缩放比例
        if (picWidth > picHeight) {
            if (picWidth > screenWidth)
                opt.inSampleSize = picWidth / screenWidth
        } else {
            if (picHeight > screenHeight)
                opt.inSampleSize = picHeight / screenHeight
        }
        // 生成有像素经过缩放了的bitmap
        opt.inJustDecodeBounds = false;
        // 根据屏的大小和图片大小计算出缩放比例
        opt.inJustDecodeBounds = false
        bitmap = BitmapFactory.decodeFile(imageFile!!.absolutePath, opt)
        // 生成有像素经过缩放了的bitmap
        if(bitmap==null){
            ToastUtils.toastLong(this,"位图解析失败 呵呵呵 ")
        }
        return bitmap
    }


}