package com.mobiledevpro.apptemplate.ui.mainscreen.viewmodel

import android.app.Application
import android.util.Log
import androidx.lifecycle.*
import com.mobiledevpro.apptemplate.Event
import com.mobiledevpro.data.LOG_TAG_DEBUG
import com.mobiledevpro.database.model.User


/**
 * ViewModel for user data
 *
 *
 * Created by Dmitriy Chernysh on 11/8/19.
 *
 *
 * https://instagr.am/mobiledevpro
 * #MobileDevPro
 */
class UserDataViewModel(app: Application) : AndroidViewModel(app), LifecycleObserver {

    private val _cachedUserData = MutableLiveData<User>()

    //uses in two-way databinding
    val userName = MutableLiveData<String>()
    val userAge = MutableLiveData<String>()

    private val _navigateToUserView = MutableLiveData<Event<Boolean>>()
    val navigateToUserView: LiveData<Event<Boolean>> = _navigateToUserView

    private val _showToastEvent = MutableLiveData<Event<String>>()
    val showToastEvent: LiveData<Event<String>> = _showToastEvent

    private val _onSaveUserData = MutableLiveData<Event<User>>()
    val onSaveUserData: LiveData<Event<User>> = _onSaveUserData

    /**
     * LifeCycle-aware
     */
    init {
        Log.d(LOG_TAG_DEBUG, "UserDataViewModel created!")
    }

    /**
     * LifeCycle-aware
     */
    override fun onCleared() {
        super.onCleared()
        Log.d(LOG_TAG_DEBUG, "UserDataViewModel cleared!")
    }

    /**
     * It calls from xml layout
     */
    fun onClickUpdateUser() {
        val user = _cachedUserData.value
        user?.name = userName.value ?: ""
        user?.age = if (userAge.value.isNullOrEmpty()) 0 else userAge.value?.toInt() ?: 0

        if (user != null)
            _onSaveUserData.value = Event(user)
    }

    /**
     * It calls from xml layout
     */
    fun isUpdateButtonEnabled(): LiveData<Boolean> {
        val isEnabled = MediatorLiveData<Boolean>()

        isEnabled.addSource(userName) {
            isEnabled.value = isUserDataChanged()
        }

        isEnabled.addSource(userAge) {
            isEnabled.value = isUserDataChanged()
        }

        return isEnabled

    }

    fun setUserData(user: User) {
        _cachedUserData.value = user

        userName.value = user.name
        userAge.value = user.age.toString()
    }

    fun showToastMessage(message: String) {
        _showToastEvent.value = Event(message)
    }

    fun navigateToUserViewScreen() {
        _navigateToUserView.value = Event(true)
    }

    private fun isUserDataChanged(): Boolean {
        val cachedUser = _cachedUserData.value

        return (userName.value != cachedUser?.name) ||
                (userAge.value != cachedUser?.age.toString())
    }
}