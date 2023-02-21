package com.example.kp8easytodo

import android.content.Context
import androidx.activity.ComponentActivity
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.Button
import androidx.compose.material.Divider
import androidx.compose.material.Text
import androidx.compose.material.TextField
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.kp8easytodo.ui.theme.KP8EasyTodoTheme


@Composable
fun TodoListView(context: Context) {

    // 透過remember建立ViewModel
    val todoViewModel by remember { mutableStateOf(TodoViewModel(context)) }

    // 取得DataStore內建立的JSON字串
    val storeString = todoViewModel.getTodoListByDataStore.collectAsState(initial = "").value

    // 儲存TextField的文字
    val todoString = rememberSaveable { mutableStateOf("") }

    Column() {
        Row(modifier = Modifier.padding(5.dp)) {

            // 輸入待辦的textField
            TextField(
                value = todoString.value,
                onValueChange = { todoString.value = it },
                modifier = Modifier.padding(end = 10.dp)
            )

            //送出待辦的Button
            Button(
                onClick = {
                    todoViewModel.addTodo(todoString.value)
                    todoString.value = ""
                }, modifier = Modifier
                    .weight(0.3f)
                    .padding(4.dp)
            ) {
                Text(text = "送出")
            }
        }

        // 顯示待辦的列表
        LazyColumn() {
            items(todoViewModel.fetchTodoListByDataStore(storeString ?: ""))
            { todo ->
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(60.dp)
                        .fillMaxSize(),
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {

                    Text(
                        text = todo,
                        fontSize = 20.sp,
                        textAlign = TextAlign.Center,
                    )
                }
                Divider(color = Color.Red, thickness = 2.dp)
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun DefaultPreview1() {
    KP8EasyTodoTheme {
        TodoListView(ComponentActivity())
    }
}