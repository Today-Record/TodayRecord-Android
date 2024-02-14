package com.todayrecord.presentation.screen.base

import android.app.Dialog
import android.content.res.ColorStateList
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.ColorRes
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.platform.ViewCompositionStrategy
import androidx.core.content.ContextCompat
import androidx.core.view.ViewCompat
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.google.android.material.shape.MaterialShapeDrawable
import com.google.android.material.shape.ShapeAppearanceModel
import com.todayrecord.designsystem.theme.TodayRecordTheme
import com.todayrecord.presentation.R

abstract class ComposableBottomSheetDialogFragment : BottomSheetDialogFragment() {

    @Composable
    abstract fun RootContent()

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val dialog: Dialog = super.onCreateDialog(savedInstanceState)

        if (dialog is BottomSheetDialog) {
            dialog.setOnShowListener {
                val bottomSheetView = dialog.findViewById<View>(com.google.android.material.R.id.design_bottom_sheet)

                bottomSheetView?.post {
                    dialog.behavior.state = BottomSheetBehavior.STATE_EXPANDED

                    val newMaterialShapeDrawable: MaterialShapeDrawable = createMaterialShapeDrawable(bottomSheetView)
                    ViewCompat.setBackground(bottomSheetView, newMaterialShapeDrawable)
                }
            }
        }

        return dialog
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return ComposeView(requireContext()).apply {
            // Dispose of the Composition when the view's LifecycleOwner
            // is destroyed
            setViewCompositionStrategy(ViewCompositionStrategy.DisposeOnViewTreeLifecycleDestroyed)
            setContent {
                TodayRecordTheme {
                    RootContent()
                }
            }
        }
    }

    @ColorRes
    protected open val backgroundColor: Int? = null

    private fun createMaterialShapeDrawable(bottomSheet: View): MaterialShapeDrawable {
        val shapeAppearanceModel = ShapeAppearanceModel.builder(context, 0, R.style.ShapeAppearance_TodayRecord_SmallComponent).build()

        //Create a new MaterialShapeDrawable (you can't use the original MaterialShapeDrawable in the BottomSheet)
        val currentMaterialShapeDrawable = bottomSheet.background as? MaterialShapeDrawable ?: MaterialShapeDrawable()
        val newMaterialShapeDrawable = MaterialShapeDrawable(shapeAppearanceModel)
        //Copy the attributes in the new MaterialShapeDrawable
        newMaterialShapeDrawable.initializeElevationOverlay(context)
        newMaterialShapeDrawable.fillColor = backgroundColor?.let { ColorStateList.valueOf(ContextCompat.getColor(requireContext(), it)) } ?: currentMaterialShapeDrawable.fillColor
        newMaterialShapeDrawable.tintList = currentMaterialShapeDrawable.tintList
        newMaterialShapeDrawable.elevation = currentMaterialShapeDrawable.elevation
        newMaterialShapeDrawable.strokeWidth = currentMaterialShapeDrawable.strokeWidth
        newMaterialShapeDrawable.strokeColor = currentMaterialShapeDrawable.strokeColor
        return newMaterialShapeDrawable
    }
}