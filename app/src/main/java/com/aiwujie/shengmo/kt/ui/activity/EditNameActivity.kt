package com.aiwujie.shengmo.kt.ui.activity

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.text.InputFilter
import android.view.View
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import com.aiwujie.shengmo.R
import com.aiwujie.shengmo.activity.VipWebActivity
import com.aiwujie.shengmo.base.BaseActivity
import com.aiwujie.shengmo.http.HttpUrl
import com.aiwujie.shengmo.kt.util.IntentKey
import com.aiwujie.shengmo.kt.util.NormalConstant
import com.aiwujie.shengmo.kt.util.showText
import com.aiwujie.shengmo.utils.StatusBarUtil


/**
 * @ProjectName: workspace2
 * @Package: com.aiwujie.shengmo.kt.ui.activity
 * @ClassName: EditNameActivity
 * @Author: xmf
 * @CreateDate: 2022/4/7 16:34
 * @Description:修改昵称
 */
class EditNameActivity:BaseActivity() {
    private lateinit var etEditName:EditText
    private lateinit var tvEditNameRule:TextView
    // 标题栏
    private lateinit var tvTitle: TextView
    private lateinit var tvTitleRight: TextView
    private lateinit var ivTitleRight: ImageView
    private lateinit var ivTitleBack: ImageView
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.app_activity_edit_name)
        StatusBarUtil.showLightStatusBar(this)
        tvTitle = findViewById(R.id.tv_normal_title_title)
        ivTitleBack = findViewById(R.id.iv_normal_title_back)
        ivTitleRight = findViewById(R.id.iv_normal_title_more)
        tvTitleRight = findViewById(R.id.tv_normal_title_more)
        tvTitle.text = "修改昵称"
        ivTitleRight.visibility = View.GONE
        tvTitleRight.visibility = View.VISIBLE
        tvTitleRight.text = "提交"
        ivTitleBack.setOnClickListener { finish() }
        tvEditNameRule = findViewById(R.id.tv_edit_name_rule)
        etEditName = findViewById(R.id.et_edit_name)
        tvEditNameRule.setOnClickListener {
            VipWebActivity.start(this, "图文规范", HttpUrl.NetPic() + HttpUrl.PicTextHtml)
        }
        tvTitleRight.setOnClickListener {
            val intent = Intent()
            intent.putExtra(IntentKey.NAME, etEditName.text.toString())
            setResult(NormalConstant.RESULT_CODE_OK, intent)
            finish()
        }
        intent.getStringExtra(IntentKey.NAME)?.run {
            etEditName.showText(this)
        }
        val filter = InputFilter { source, _, _, _, _, _ ->
            //返回null表示接收输入的字符,返回空字符串表示不接受输入的字符
            if (source == " ") "" else null
        }
        etEditName.filters = arrayOf(filter)
    }

    fun start(context: Context, name: String) {
        val intent = Intent()
        intent.putExtra(IntentKey.NAME, name)
        context.startActivity(intent)
    }
}
