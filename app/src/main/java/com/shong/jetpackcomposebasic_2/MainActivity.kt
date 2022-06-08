package com.shong.jetpackcomposebasic_2

import android.content.res.Configuration.UI_MODE_NIGHT_YES
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.spring
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ExpandLess
import androidx.compose.material.icons.filled.ExpandMore
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.shong.jetpackcomposebasic_2.ui.theme.JetpackComposeBasic_2Theme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            JetpackComposeBasic_2Theme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colors.background
                ) {
                    MyApp()
                }
            }
        }
    }
}

@Composable
fun MyApp() {
    // TODO: This state should be hoisted
    // by를 사용해주는 이유는 매번 .value를 입력할 필요가 없도록 해주는 속성임
    var shouldShowOnboarding by rememberSaveable { mutableStateOf(true) }

    if (shouldShowOnboarding) {
        OnboardingScreen { shouldShowOnboarding = false }
    } else {
//        Greetings()
        Greetings_Card()
    }
}

@Composable
private fun OnboardingScreen(onContinueClicked: () -> Unit) {
    Surface {
        Column(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text("Welcome to the Basics Codelab!")
            Button(
                modifier = Modifier.padding(vertical = 24.dp),
                onClick = onContinueClicked
            ) {
                Text(text = "Continue")
            }
        }
    }

}

@Preview(showBackground = true, widthDp = 320, heightDp = 320)
@Composable
fun OnboardingPreview() {
    JetpackComposeBasic_2Theme {
        OnboardingScreen {} // Do nothing on click.
    }
}

@Composable
private fun Greetings(names: List<String> = List(1000) { "$it" }) {
    // LazyColumn과 LazyRow는 Android 뷰의 RecyclerView와 동일함
    // LazyColumn은 RecyclerView와 같은 하위 요소를 재활용하지 않습니다.
    // 컴포저블을 방출하는 것은 Android Views를 인스턴스화하는 것보다 상대적으로 비용이 적게 들므로
    // LazyColumn은 스크롤 할 때 새 컴포저블을 방출하고 계속 성능을 유지합니다.
    LazyColumn(modifier = Modifier.padding(vertical = 4.dp)) {
        items(items = names) { name ->
            Greeting(name = name)
        }
    }
}

@Composable
private fun Greeting(name: String) {
    // remember : 스테이트를 기억하기위해 사용
    // rememberSaveable : 컴포서블을 다시 그릴때(스크롤이 이동한 후 다시돌아오는경우 등등)에도 저장되어있게 사용
    val expanded = rememberSaveable { mutableStateOf(false) }

    // animate*AsState를 사용하여 만든 애니메이션은 중단될 수 있습니다
    // 다양한 애니메이션은 참고 -> https://developer.android.com/jetpack/compose/animation
    val extraPadding by animateDpAsState(
        targetValue = if (expanded.value) 48.dp else 0.dp,
        animationSpec = spring(
            dampingRatio = Spring.DampingRatioMediumBouncy,
            stiffness = Spring.StiffnessLow
        )
    )

    Surface(
        color = MaterialTheme.colors.primary,
        modifier = Modifier.padding(vertical = 4.dp, horizontal = 8.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(24.dp)
        ) {
            Column(
                modifier = Modifier
                    .weight(1f)
                    .padding(bottom = extraPadding.coerceAtLeast(0.dp))
                // 패딩이 음수가 되지 않도록 해야 합니다. 패딩이 음수가 되면 앱이 다운될 수 있습니다.
                // 이로 인해 미세한 애니메이션 버그가 발생하며 이 버그는 추후 설정 완료에서 수정할 예정입니다.
            ) {
                Text(text = "Hello,")
                Text(
                    text = name, style = MaterialTheme.typography.h4.copy(
                        fontWeight = FontWeight.ExtraBold
                    )
                )
            }
            OutlinedButton(
                onClick = { expanded.value = !expanded.value }
            ) {
                Text(text = if (expanded.value) "Show less" else "Show more")
            }
        }
    }
}

@Preview(
    showBackground = true,
    widthDp = 320,
    uiMode = UI_MODE_NIGHT_YES,
    name = "DefaultPreviewDark"
)
@Preview(
    showBackground = true,
    widthDp = 320
)
@Composable
fun GreetingPreview() {
    JetpackComposeBasic_2Theme {
        Greetings()
    }
}

@Composable
private fun Greetings_Card(names: List<String> = List(1000) { "$it" }) {
    // LazyColumn과 LazyRow는 Android 뷰의 RecyclerView와 동일함
    // LazyColumn은 RecyclerView와 같은 하위 요소를 재활용하지 않습니다.
    // 컴포저블을 방출하는 것은 Android Views를 인스턴스화하는 것보다 상대적으로 비용이 적게 들므로
    // LazyColumn은 스크롤 할 때 새 컴포저블을 방출하고 계속 성능을 유지합니다.
    LazyColumn(modifier = Modifier.padding(vertical = 4.dp)) {
        items(items = names) { name ->
            Greeting_Card(name = name)
        }
    }
}

@Composable
private fun Greeting_Card(name: String) {
    Card(
        backgroundColor = MaterialTheme.colors.primary,
        modifier = Modifier.padding(vertical = 4.dp, horizontal = 8.dp)
    ) {
        CardContent(name)
    }
}

@Composable
private fun CardContent(name: String) {
    var expanded by remember { mutableStateOf(false) }

    Row(
        modifier = Modifier
            .padding(12.dp)
            .animateContentSize(
                animationSpec = spring(
                    dampingRatio = Spring.DampingRatioMediumBouncy,
                    stiffness = Spring.StiffnessLow
                )
            )
    ) {
        Column(
            modifier = Modifier
                .weight(1f)
                .padding(12.dp)
        ) {
            Text(text = "Hello, ")
            Text(
                text = name,
                style = MaterialTheme.typography.h4.copy(
                    fontWeight = FontWeight.ExtraBold
                )
            )
            if (expanded) {
                Text(
                    text = ("Composem ipsum color sit lazy, " +
                            "padding theme elit, sed do bouncy. ").repeat(4),
                )
            }
        }
        IconButton(onClick = { expanded = !expanded }) {
            Icon(
                imageVector = if (expanded) Icons.Filled.ExpandLess else Icons.Filled.ExpandMore,
                contentDescription = if (expanded) {
                    stringResource(R.string.show_less)
                } else {
                    stringResource(R.string.show_more)
                }

            )
        }
    }
}

@Preview(
    showBackground = true,
    widthDp = 320,
    uiMode = UI_MODE_NIGHT_YES,
    name = "DefaultPreviewDark"
)
@Preview(
    showBackground = true,
    widthDp = 320
)
@Composable
fun DefaultPreview() {
    JetpackComposeBasic_2Theme {
        Greetings_Card()
    }
}