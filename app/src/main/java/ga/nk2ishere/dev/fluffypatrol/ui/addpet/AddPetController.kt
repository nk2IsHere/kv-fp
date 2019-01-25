package ga.nk2ishere.dev.fluffypatrol.ui.addpet

import android.content.Intent
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.os.Environment
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import com.arellomobile.mvp.presenter.InjectPresenter
import com.arellomobile.mvp.presenter.ProvidePresenter
import com.google.android.material.snackbar.Snackbar
import ga.nk2ishere.dev.base.BaseController
import ga.nk2ishere.dev.fluffypatrol.R
import ga.nk2ishere.dev.fluffypatrol.common.data.Dog
import ga.nk2ishere.dev.fluffypatrol.ui.misc.CameraActivity
import ga.nk2ishere.dev.utils.ReducedTextWatcher
import kotlinx.android.synthetic.main.add_pet.view.*
import java.io.File
import java.util.*

class AddPetController(args: Bundle): BaseController(args), AddPetView, AddPetViewPMRenderer {

    private val renderDispatcher = AddPetViewPMDiffDispatcher.Builder()
            .target(this)
            .build()
    private var previousAddPetViewPM: AddPetViewPM? = null


    companion object {
        val ADD_PET_EDIT_PHOTO_REQUEST = 1

        fun create(): AddPetController =
                AddPetController(Bundle())
    }

    private fun updatePMAction(action: AddPetViewUpdatePMAction) {
        renderDispatcher.dispatch(action.pm, previousAddPetViewPM)
        previousAddPetViewPM = action.pm
    }

    override fun renderNameError(nameError: String?) {
        view?.name?.error = nameError
    }

    override fun renderAgeError(ageError: String?) {
        view?.age?.error = ageError
    }

    override fun renderPhotoError(photoError: String?) {
        view?.let { view -> Snackbar.make(view, photoError ?: return, Snackbar.LENGTH_LONG).show() }
    }

    override fun renderPhoto(photo: UUID?) {
        applicationContext?.let { view?.photo?.setImageDrawable(Drawable.createFromPath(File(it.filesDir, "${photo ?: return}.jpg").absolutePath)) }
    }

    private fun saveAction(action: AddPetViewSavePetAction) {
        router.handleBack()
    }

    private fun notifyPetSavedAction(action: AddPetViewNotifyPetSavedAction) {
        handleGlobalEvent(AddPetViewPetSavedGlobalEvent())
    }

    private fun openCameraAction(action: AddPetViewOpenCameraAction) {
        action.pm.photoPath?.let {
            val intent = Intent(activity, CameraActivity::class.java)
            intent.putExtra(CameraActivity.CAMERA_FILE_PATH, it)
            startActivityForResult(intent, ADD_PET_EDIT_PHOTO_REQUEST)
        }
    }

    @InjectPresenter lateinit var presenter: AddPetPresenter
    @ProvidePresenter fun providePresenter(): AddPetPresenter = AddPetPresenter(applicationContext)

    override fun getLayoutId(): Int = R.layout.add_pet

    override fun initView(view: View) {
        view.name.addTextChangedListener(ReducedTextWatcher { presenter.handleViewEvent(AddPetViewNameEditedEvent(it)) })
        view.age.addTextChangedListener(ReducedTextWatcher { presenter.handleViewEvent(AddPetViewAgeEditedEvent(it.toIntOrNull() ?: -1)) })
        view.gender.setOnCheckedChangeListener { _, it -> presenter.handleViewEvent(AddPetViewGenderEditedEvent(it)) }
        view.type.apply {
            adapter = ArrayAdapter<String>(applicationContext, android.R.layout.simple_spinner_dropdown_item, Dog.Type.values().map { it.prettyName })
            this.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
                override fun onNothingSelected(p0: AdapterView<*>?) = Unit
                override fun onItemSelected(p0: AdapterView<*>, p1: View?, p2: Int, p3: Long) { presenter.handleViewEvent(AddPetViewTypeEditedEvent(p0.adapter.getItem(p2) as String)) }
            }
        }
        view.takePhoto.setOnClickListener { presenter.handleViewEvent(AddPetViewPhotoButtonClickedEvent()) }
        view.add.setOnClickListener { presenter.handleViewEvent(AddPetViewSavePetButtonClickedEvent()) }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if(requestCode == ADD_PET_EDIT_PHOTO_REQUEST) presenter.handleViewEvent(AddPetViewPhotoEditedEvent())
    }

    override fun applyAction(action: AddPetViewAction) { when(action) {
        is AddPetViewSavePetAction -> saveAction(action)
        is AddPetViewUpdatePMAction -> updatePMAction(action)
        is AddPetViewOpenCameraAction -> openCameraAction(action)
        is AddPetViewNotifyPetSavedAction -> notifyPetSavedAction(action)
    } }

    override fun applyActionWithSkip(action: AddPetViewAction) { when(action) {
        is AddPetViewSavePetAction -> saveAction(action)
        is AddPetViewOpenCameraAction -> openCameraAction(action)
        is AddPetViewNotifyPetSavedAction -> notifyPetSavedAction(action)
    } }
}