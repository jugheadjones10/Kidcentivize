package com.yj.kidcentivize.ui.onboard

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.yj.kidcentivize.api.ApiStatus
import com.yj.kidcentivize.repository.UserRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class OnboardViewModel @Inject constructor(private val userRepository: UserRepository) : ViewModel(){

    val users = userRepository.users

    private val _status = MutableLiveData<ApiStatus>()
    val status: LiveData<ApiStatus>
        get() = _status

    private val _identity = MutableLiveData<Identity>()
    val identity: LiveData<Identity>
        get() = _identity

    private val _name = MutableLiveData<String>()
    val name: LiveData<String>
        get() = _name

    fun setStatus(status: ApiStatus){
        _status.value = status
    }

    fun setIdentity(identity: Identity){
        _identity.value = identity
    }

    fun setName(name: String){
        _name.value = name
    }

    //Kid interface
    val kidCode = userRepository.kidCode
    val parentCreated : LiveData<Boolean> = userRepository.parentCreated
    fun getKidCode(){
        viewModelScope.launch {
            _status.value = ApiStatus.LOADING
            try {
                userRepository.getKidCode(name.value!!)
                _status.value = ApiStatus.DONE
            } catch (e: Exception) {
                e.printStackTrace()
                _status.value = ApiStatus.ERROR
            }
        }
    }

    fun listenForParent(kidCode: String){
        userRepository.listenForParent(kidCode)
    }

    //Parent interface
    fun submitKidCode(code: String){
        viewModelScope.launch {
            try {
                userRepository.submitKidCode(code, name.value!!)
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    enum class Identity{
        PARENT, CHILD
    }
}