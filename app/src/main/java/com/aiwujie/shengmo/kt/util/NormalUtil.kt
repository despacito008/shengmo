package com.aiwujie.shengmo.kt.util

import android.app.Activity
import android.arch.lifecycle.ViewModel
import android.arch.lifecycle.ViewModelProvider
import android.content.res.Resources
import android.graphics.BlurMaskFilter
import android.support.design.widget.TabLayout
import android.support.v4.view.ViewPager
import android.text.SpannableString
import android.text.Spanned
import android.util.TypedValue
import android.view.View
import android.view.WindowManager
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import com.aiwujie.shengmo.R
import com.aiwujie.shengmo.application.MyApp
import com.aiwujie.shengmo.kt.util.ViewClickDelay.SPACE_TIME
import com.aiwujie.shengmo.kt.util.ViewClickDelay.hash
import com.aiwujie.shengmo.kt.util.ViewClickDelay.lastClickTime
import com.aiwujie.shengmo.kt.util.dsl.OnPageChangeListenerDslImpl
import com.aiwujie.shengmo.kt.util.dsl.OnTabSelectedListenerImpl
import com.aiwujie.shengmo.kt.util.dsl.TextWatcherDslImpl
import com.aiwujie.shengmo.utils.AnimationUtil
import com.aiwujie.shengmo.utils.LogUtil
import com.aiwujie.shengmo.utils.SharedPreferencesUtils
import com.aiwujie.shengmo.zdyview.ATGroupSpan
import com.aiwujie.shengmo.zdyview.ATHighSpan
import com.aiwujie.shengmo.zdyview.ATSpan
import com.donkingliang.labels.LabelsView
import com.google.gson.Gson
import com.google.gson.JsonSyntaxException
import com.google.gson.reflect.TypeToken
import com.scwang.smartrefresh.layout.SmartRefreshLayout
import org.feezu.liuli.timeselector.Utils.TextUtil
import java.io.StringReader
import java.lang.reflect.Type
import java.util.*
import java.util.regex.Pattern


val Int.dp: Int
    get() {
        return TypedValue.applyDimension(
                TypedValue.COMPLEX_UNIT_DIP,
                this.toFloat(),
                Resources.getSystem().displayMetrics
        ).toInt()
    }


fun <T> Collection<T>.print(map: (T) -> String) = StringBuffer("\n[").also { sb ->
    this.forEach { e -> sb.append("\n\t${map(e)},") }
    sb.append("\n]")
}.toString()

fun <K, V> Map<K, V?>.print(map: (V?) -> String): String =
        StringBuilder("\n{").also { sb ->
            this.iterator().forEach { entry ->
                sb.append("\n\t[${entry.key}] = ${map(entry.value)}")
            }
            sb.append("\n}")
        }.toString()


object ViewClickDelay {
    var hash: Int = 0
    var lastClickTime: Long = 0
    var SPACE_TIME: Long = 2000
}

infix fun View.clickDelay(clickAction: () -> Unit) {
    this.setOnClickListener {
        if (this.hashCode() != hash) {
            hash = this.hashCode()
            lastClickTime = System.currentTimeMillis()
            clickAction()
        } else {
            val currentTime = System.currentTimeMillis()
            if (currentTime - lastClickTime > SPACE_TIME) {
                lastClickTime = System.currentTimeMillis()
                clickAction()
            }
        }
    }
}

fun Activity.alphaBackground(alpha: Float) {
    val lp: WindowManager.LayoutParams = window.attributes
    lp.alpha = alpha //0.0-1.0
    window.attributes = lp
}

fun String.showToast(duration: Int = Toast.LENGTH_SHORT) {
    Toast.makeText(MyApp.instance, this, duration).show()
}

fun EditText.showText(content: String) {
    setText(content)
    setSelection(this.text.length)
}

fun TextView.addTextChangedListenerDsl(init: TextWatcherDslImpl.() -> Unit) {
    val listener = TextWatcherDslImpl()
    listener.init()
    this.addTextChangedListener(listener)
}

fun TabLayout.addOnTabSelectedListenerDsl(init: OnTabSelectedListenerImpl.() -> Unit) {
    val listener = OnTabSelectedListenerImpl()
    listener.init()
    this.addOnTabSelectedListener(listener)
}

fun ViewPager.addOnPageChangeListenerDsl(init: OnPageChangeListenerDslImpl.() -> Unit) {
    val listener = OnPageChangeListenerDslImpl()
    listener.init()
    this.addOnPageChangeListener(listener)
}

fun List<Any>.toLog(pre: String = "") {
    for (any in this) {
        LogUtil.d(pre + any)
    }
}

fun List<Any>.doReplace(index: Int, obj: Any) {
    if (this.size > index) {
        Collections.replaceAll(this, this[index], obj)
    }
}

fun List<Any>.toStr(): String {
    val sb = StringBuilder()
    for (i in this.indices) {
        if (i == this.size - 1) {
            sb.append(this[i])
        } else {
            sb.append((this[i].toString() + ","))
        }
    }
    return sb.toString()
}

fun List<String>.toPicStr(): String {
    val sb = StringBuilder()
    for (i in this.indices) {
        if (!this[i].contains("fail")) {
            if (i == this.size - 1) {
                sb.append(this[i])
            } else {
                sb.append((this[i] + ","))
            }
        }
    }
    return sb.toString()
}

fun List<String>.toPicStr2(): String {
    val sb = StringBuilder()
    for (i in this.indices) {
        if (!this[i].contains("fail")) {
            if (i == this.size - 1) {
                sb.append(this[i])
            } else {
                sb.append((this[i] + "|"))
            }
        }
    }
    return sb.toString()
}

inline fun <reified T> String.toJsonBean(): T? {
    val type = object : TypeToken<T>() {}.type
    return Gson().fromJson2<T>(this, type)
}

//拓展函数 Kotlin
@Throws(JsonSyntaxException::class)
inline fun <reified T> Gson.fromJson2(json: String?, typeOfT: Type): T? {
    if (json == null) {
        return null
    }
    val reader = StringReader(json)
    return fromJson(reader, typeOfT)
}


fun LabelsView.showListSelect(list: ArrayList<String>) {
    if (list != null && list.size > 0) {
        setSelects(list.map {
            it.toInt() - 1
        })
    }
}


fun LabelsView.showSelect(position: String) {
    if (!TextUtil.isEmpty(position) && position.toInt() > 0) {
        setSelects(position.run {
            toInt() - 1
        })
    }

}

fun LabelsView.showSelectNew(position: String) {
    if (!TextUtil.isEmpty(position)) {
        setSelects(position.run {
            toInt()
        })
    }

}

fun List<Int>.toStrAddOne(): String {
    val sb = StringBuilder()
    for (i in this.indices) {
        if (i == this.size - 1) {
            sb.append(this[i] + 1)
        } else {
            sb.append(((this[i] + 1).toString() + ","))
        }
    }
    return sb.toString()
}

fun List<Int>.toRole(str: String): String? {
    var role: String? = null
    if (this.isNotEmpty()) {
        when (str) {
            "role" -> {
                when (this[0]) {
                    0 -> role = "S"
                    1 -> role = "M"
                    2 -> role = "SM"
                    3 -> role = "~"
                    4 -> role = "-"
                }
            }
            "les" -> {
                when (this[0]) {
                    0 -> role = "T"
                    1 -> role = "P"
                    2 -> role = "H"
                    3 -> role = "~"
                }
            }
            "gay" -> {
                when (this[0]) {
                    0 -> role = "1"
                    1 -> role = "0"
                    2 -> role = "0.5"
                    3 -> role = "~"
                }
            }
        }
    }

    return role
}


//从sp里面取key的值
fun String.getSpValue(defaultValue: String = ""): String {
    return SharedPreferencesUtils.geParam(MyApp.instance, this, defaultValue) as String
}

//从sp里面取key的值
fun String.getSpValue(defaultValue: Any): Any {
    return SharedPreferencesUtils.getParam(MyApp.instance, this, defaultValue)
    //return SharedPreferencesUtils.geParam(MyApp.instance, this, defaultValue)
}

//sp存值
fun String.saveSpValue(value: Any) {
    SharedPreferencesUtils.setParam(MyApp.instance, this, value)
}

//加载完成 并且还能加载更多
fun SmartRefreshLayout.finishLoadMoreWithMoreData() {
    finishLoadMore()
    resetNoMoreData()
}


fun String.toDynamicSpannable(nameStr: String, idStr: String): SpannableString {
    val spannableString = SpannableString(this)
    if (nameStr.isNullOrEmpty() || idStr.isNullOrEmpty()) {
        return spannableString
    }
    if (nameStr.isNotEmpty() && idStr.isNotEmpty()) {
        if (!nameStr.isNullOrEmpty() && !idStr.isNullOrEmpty()) {
            val split: Array<String?> = idStr.split(",".toRegex()).toTypedArray()
            val split1 = nameStr.split(",".toRegex()).toTypedArray()
            for (i in split1.indices) {
                if (split1[i].trim { it <= ' ' }.contains("*")) {
                    split1[i] = split1[i].trim { it <= ' ' }.replace("*", "\\*")
                }
                val patten = Pattern.quote(split1[i].trim { it <= ' ' })
                val compile = Pattern.compile(patten)
                val matcher = compile.matcher(this)
                while (matcher.find()) {
                    val start = matcher.start()
                    if (split1[i].trim { it <= ' ' }.contains("*")) {
                        split1[i] = split1[i].trim { it <= ' ' }.replace("\\", "")
                    }
                    if (!TextUtil.isEmpty(split[i]) && start >= 0) {
                        if (split1[i].startsWith("@[群]")) {
                            spannableString.setSpan(ATGroupSpan(split[i]), start, start + split1[i].trim { it <= ' ' }.length, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)
                        } else if (split1[i].startsWith("@[高端]")) {
                            spannableString.setSpan(ATHighSpan(split[i]), start, start + split1[i].trim { it <= ' ' }.length, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)
                        } else {
                            spannableString.setSpan(ATSpan(split[i]), start, start + split1[i].trim { it <= ' ' }.length, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)
                        }
                    }
                }
            }
        }
    }
    return spannableString
}

fun TextView.setTextSizeWithDp(dp: Float) {
    this.setTextSize(TypedValue.COMPLEX_UNIT_DIP, dp.toFloat())
}

class ParamViewModelFactory<VM : ViewModel>(
        private val factory: () -> VM
) : ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return factory() as T
    }
}

fun TextView.BlurFliter() {
    var filter = BlurMaskFilter(20F, BlurMaskFilter.Blur.NORMAL)
    this.setLayerType(View.LAYER_TYPE_HARDWARE, null)
    this.paint.maskFilter = filter
}

fun ImageView.showUserLevelRole(role: Int) {
    when (role) {
        0 -> {
            this.visibility = View.INVISIBLE
        }
        2 -> {
            this.visibility = View.VISIBLE
            this.setImageResource(R.drawable.user_manager)
        }
        6 -> {
            this.visibility = View.VISIBLE
            this.setImageResource(R.drawable.user_svip_year)
        }
        7 -> {
            this.visibility = View.VISIBLE
            this.setImageResource(R.drawable.user_svip)
        }
        8 -> {
            this.visibility = View.VISIBLE
            this.setImageResource(R.drawable.user_vip_year)
        }
        9 -> {
            this.visibility = View.VISIBLE
            this.setImageResource(R.drawable.user_vip)
        }
        10 -> {
            this.visibility = View.VISIBLE
            this.setImageResource(R.mipmap.guanfangbiaozhilan)
        }
    }
}

//控件 屏幕底部显示隐藏动画
fun View.showOrHideBottomAnim(isShow: Boolean) {
    if (isShow) {
        if (visibility == View.GONE) {
            visibility = View.VISIBLE
            animation = AnimationUtil.moveToViewLocation()
        }
    } else {
        if (visibility == View.VISIBLE) {
            postDelayed({ visibility = View.GONE }, 500)
            animation = AnimationUtil.moveToViewBottom()
        }
    }
}


//inline fun <reified VM:ViewModel> AppCompatActivity.viewModel(noinline factory: () -> VM):Lazy<VM> = ViewModels{ParamViewModelFactory(factory)}


