package com.example.navigation.data

import android.app.Application
import androidx.lifecycle.*
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import com.example.navigation.data.SettingsDataStore
import javax.inject.Inject

@HiltViewModel
class SettingsViewModel @Inject constructor(
    application: Application
) : AndroidViewModel(application) {

    private val dataStore = SettingsDataStore(application)

    val userName = dataStore.userName.asLiveData()
    val userEmail = dataStore.userEmail.asLiveData()
    val appColor = dataStore.appColor.asLiveData()
    val autoArm = dataStore.autoArm.asLiveData()
    val notifications = dataStore.notifications.asLiveData()

    fun saveUserName(name: String) = viewModelScope.launch {
        dataStore.saveUserName(name)
    }

    fun saveUserEmail(email: String) = viewModelScope.launch {
        dataStore.saveUserEmail(email)
    }

    fun saveAppColor(color: Int) = viewModelScope.launch {
        dataStore.saveAppColor(color)
    }

    fun saveAutoArm(enabled: Boolean) = viewModelScope.launch {
        dataStore.saveAutoArm(enabled)
    }

    fun saveNotifications(enabled: Boolean) = viewModelScope.launch {
        dataStore.saveNotifications(enabled)
    }
}