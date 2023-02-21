package com.example.kp8easytodo

import android.content.Context
import android.util.Log
import androidx.compose.runtime.*
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import androidx.lifecycle.ViewModel
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import java.lang.reflect.Type
import androidx.datastore.preferences.core.edit
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.withContext


val DATASTOREKEYNAME = "KP8Todo"    //dataStore的Key

class TodoViewModel(private val context: Context):ViewModel() {

    var gson = Gson()

    //使用的代辦事項列表
    private var dataStoreTodoList = mutableStateListOf<String>()


    // 建立單一實例
    companion object {
        private val Context.dataStoree: DataStore<Preferences> by preferencesDataStore(DATASTOREKEYNAME)
        val USER_TODO_DATASTORE_KEY = stringPreferencesKey(DATASTOREKEYNAME)
    }


    //新增事項
    fun addTodo (todo:String) {
        dataStoreTodoList.add(todo)
        saveTodoList()
    }


    //將dataStoreTodoList轉成字串JSON形式後存入本機資料
    fun saveTodoList () {
        val dataStoreJson: String = gson.toJson(dataStoreTodoList)
        runBlocking {
            withContext(Dispatchers.IO) {
                saveTodoByDataStore(dataStoreJson)
            }
        }
    }


    // 將傳入的字串轉型成SnapshotStateList後指派給dataStoreTodoList
    fun fetchTodoListByDataStore(str:String):SnapshotStateList<String> {
        Log.d("STR fetchTodoListByDataStore","fetchTodoListByDataStore $str")
        if (str.isNotEmpty() && str != "null") {
            val dataStoreType: Type = object : TypeToken<SnapshotStateList<String>>() {}.type
            dataStoreTodoList =  (gson.fromJson<Any>(str, dataStoreType) as SnapshotStateList<String>)
           return dataStoreTodoList
        }else{
            return dataStoreTodoList
        }
    }


    //從DataStore內取出存於本機的字串
    val getTodoListByDataStore: Flow<String?> = context.dataStoree.data
        .map { preferences ->
            preferences[USER_TODO_DATASTORE_KEY] ?: "null"
        }


    //將字串存入本機資料內
    suspend fun saveTodoByDataStore(todos: String) {
        context.dataStoree.edit { preferences ->
            preferences[USER_TODO_DATASTORE_KEY] = todos
        }
    }
}