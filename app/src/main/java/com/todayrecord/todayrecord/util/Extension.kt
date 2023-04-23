package com.todayrecord.todayrecord.util

import android.content.Context
import android.view.View
import android.view.inputmethod.InputMethodManager
import androidx.databinding.ViewDataBinding
import androidx.fragment.app.Fragment
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.NavController
import androidx.navigation.NavDirections
import androidx.navigation.NavOptions
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import java.util.Objects.isNull
import kotlin.contracts.ExperimentalContracts
import kotlin.contracts.contract
import kotlin.math.roundToInt


// navigation
fun NavController.safeNavigate(
    navDestination: NavDirections,
    navOptions: NavOptions? = null
) {
    val action = currentDestination?.getAction(navDestination.actionId) ?: graph.getAction(navDestination.actionId)
    if (action != null && currentDestination?.id != action.destinationId) {
        navigate(navDestination, navOptions)
    }
}

// coroutine
fun Job?.cancelIfActive() {
    if (this?.isActive == true) {
        cancel()
    }
}

// dataBinding
fun <T : ViewDataBinding> T.executeAfter(block: T.() -> Unit) {
    block()
    executePendingBindings()
}

// keyboard
fun View.showKeyboard(requestFocus: Boolean = false) {
    if (requestFocus) this.requestFocus()
    (this.context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager).showSoftInput(this, InputMethodManager.SHOW_IMPLICIT)
}

fun View.hideKeyboard(clearFocus: Boolean = false) {
    if (clearFocus) this.clearFocus()
    (this.context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager).hideSoftInputFromWindow(this.windowToken, InputMethodManager.HIDE_NOT_ALWAYS)
}

// value Parse
fun Int.toPx(context: Context): Int = (this * context.resources.displayMetrics.density).roundToInt()

fun Int?.isNull(): Boolean {
    return this == null
}

@OptIn(ExperimentalContracts::class)
fun Int?.isNullOrZero(): Boolean {
    contract {
        returns(false) implies (this@isNullOrZero != null)
    }

    return isNull() || this == 0
}

// StateFlow
inline fun Fragment.launchAndRepeatWithViewLifecycle(
    minActiveState: Lifecycle.State = Lifecycle.State.STARTED,
    crossinline block: suspend CoroutineScope.() -> Unit
) {
    viewLifecycleOwner.lifecycleScope.launch {
        viewLifecycleOwner.lifecycle.repeatOnLifecycle(minActiveState) {
            block()
        }
    }
}