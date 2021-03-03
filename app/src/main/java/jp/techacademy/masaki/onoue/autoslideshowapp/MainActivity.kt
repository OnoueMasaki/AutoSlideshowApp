package jp.techacademy.masaki.onoue.autoslideshowapp

import android.Manifest
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.content.pm.PackageManager
import android.os.Build
import android.util.Log
import android.provider.MediaStore
import android.content.ContentUris
import android.database.Cursor
import android.os.Handler
import android.view.WindowManager
import kotlinx.android.synthetic.main.activity_main.*
import java.util.*

class MainActivity : AppCompatActivity() {

    private val PERMISSIONS_REQUEST_CODE = 100

    lateinit var cursor: Cursor

    private var mTimer: Timer? = null

    // タイマー用の時間のための変数
    private var mTimerSec = 0.0

    private var mHandler = Handler()






    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // Android6.0以降の場合
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            // パーミッションの許可状態を確認する
            if (checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
                // 許可されている
                getContentsInfo()
            } else {
                // 許可されていないので許可ダイアログを表示する
                requestPermissions(arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE), PERMISSIONS_REQUEST_CODE)

                window.addFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE)
            }
        // Android5.0系以下の場合
        } else {
            getContentsInfo()
        }


        next_button.setOnClickListener{
            // 進むボタンを押したときの処理
            // 次の画像があるか？
            if(cursor.moveToNext()) {
                // ある場合
                // 画像を取ってきて表示する
                val fieldIndex = cursor.getColumnIndex(MediaStore.Images.Media._ID)
                val id = cursor.getLong(fieldIndex)
                val imageUri = ContentUris.withAppendedId(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, id)

                imageView.setImageURI(imageUri)
            } else if(cursor.moveToFirst()) {
                // ない場合
                // 一番最初の画像を取ってきて表示する
                val fieldIndex = cursor.getColumnIndex(MediaStore.Images.Media._ID)
                val id = cursor.getLong(fieldIndex)
                val imageUri = ContentUris.withAppendedId(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, id)

                imageView.setImageURI(imageUri)
            }



        }
        back_button.setOnClickListener{
            // 戻るボタンを押したときの処理
            //前の画像があるか？
            if(cursor.moveToPrevious()) {
                // ある場合
                // 画像を取ってきて表示する
                val fieldIndex = cursor.getColumnIndex(MediaStore.Images.Media._ID)
                val id = cursor.getLong(fieldIndex)
                val imageUri = ContentUris.withAppendedId(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, id)

                imageView.setImageURI(imageUri)
            } else if(cursor.moveToLast()) {
                // ない場合
                // 一番最後の画像を取ってきて表示する
                val fieldIndex = cursor.getColumnIndex(MediaStore.Images.Media._ID)
                val id = cursor.getLong(fieldIndex)
                val imageUri = ContentUris.withAppendedId(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, id)

                imageView.setImageURI(imageUri)
            }
        }

        playstop_button.setOnClickListener {
            if(playstop_button.text == "再生") {
                playstop_button.text = "停止"
            }else {
                playstop_button.text = "再生"
            }

            if (mTimer == null) {
                // 進むボタンと戻るボタンのクリック無効
                next_button.isClickable = false
                back_button.isClickable = false

                // タイマーの作成
                mTimer = Timer()

                // タイマーの始動
                mTimer!!.schedule(object : TimerTask() {
                    override fun run() {
                        mTimerSec += 0.1
                        mHandler.post {
                            if(cursor.moveToNext()) {
                                // ある場合
                                // 画像を取ってきて表示する
                                val fieldIndex = cursor.getColumnIndex(MediaStore.Images.Media._ID)
                                val id = cursor.getLong(fieldIndex)
                                val imageUri = ContentUris.withAppendedId(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, id)

                                imageView.setImageURI(imageUri)
                            } else if(cursor.moveToFirst()) {
                                // ない場合
                                // 一番最初の画像を取ってきて表示する
                                val fieldIndex = cursor.getColumnIndex(MediaStore.Images.Media._ID)
                                val id = cursor.getLong(fieldIndex)
                                val imageUri = ContentUris.withAppendedId(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, id)

                                imageView.setImageURI(imageUri)
                            }
                        }
                    }
                }, 2000, 2000) // 最初に始動させるまで2000ミリ秒、ループの間隔を2000ミリ秒 に設定
            } else {
                // 戻るボタンと進むボタンのクリック有効
                next_button.isClickable = true
                back_button.isClickable = true

                mTimer!!.cancel()
                mTimer = null
            }
            }


    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String>, grantResults: IntArray) {
        when (requestCode) {
            PERMISSIONS_REQUEST_CODE ->
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    getContentsInfo()
                }
        }
    }

    private fun getContentsInfo() {
        // 画像の情報を取得する
        val resolver = contentResolver
        this.cursor = resolver.query(
            MediaStore.Images.Media.EXTERNAL_CONTENT_URI, //　データの取得
            null, // 項目(null = 全項目)
            null, // フィルタ条件 (null = フィルタなし)
            null, // フィルタ用パラメータ
            null //ソート(nullソートなし)
        )!!

        if(cursor.moveToFirst()) {

                // indexからIDを取得し、そのIDから画像のURIを取得する
                val fieldIndex = cursor.getColumnIndex(MediaStore.Images.Media._ID)
                val id = cursor.getLong(fieldIndex)
                val imageUri = ContentUris.withAppendedId(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, id)

                imageView.setImageURI(imageUri)

        }


    }










}