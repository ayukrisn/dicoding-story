package com.ayukrisna.dicodingstory.view.ui.screen.auth.signup

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.requiredSize
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.ayukrisna.dicodingstory.R
import com.ayukrisna.dicodingstory.data.remote.response.RegisterResponse
import com.ayukrisna.dicodingstory.util.Result
import com.ayukrisna.dicodingstory.view.ui.component.AnimatedMovingImageVertical
import com.ayukrisna.dicodingstory.view.ui.component.CustomTextField
import com.ayukrisna.dicodingstory.view.ui.theme.DicodingStoryTheme
import org.koin.androidx.compose.koinViewModel


@Composable
fun SignupScreen(
    viewModel: SignupViewModel = koinViewModel(),
    onNavigateToLogin: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val signUpState by viewModel.signUpState.observeAsState(initial = Result.Loading)

    Surface {
        Column (
            verticalArrangement = Arrangement.Top,
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 16.dp, vertical = 42.dp)
                .verticalScroll(rememberScrollState()),
        ) {
            // Greetings
            AnimatedSignupImage()
            Text(
                stringResource(R.string.sign_up), style = MaterialTheme.typography.headlineMedium, fontWeight = FontWeight.Bold,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(0.dp, 8.dp, 0.dp, 8.dp))
            Text(
                stringResource(R.string.sign_up_description),
                style = MaterialTheme.typography.bodyMedium,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(0.dp, 0.dp, 0.dp, 24.dp))
            //Name Input Field
            NameTextField(viewModel)
            //Email Input Field
            EmailTextField(viewModel)
            //Password Input Field
            PasswordTextField(viewModel)
            //Sign up Button
            SignupButton(viewModel)
            //Login Button
            LoginButton({ onNavigateToLogin() })

            when (signUpState) {
                is Result.Idle -> {}
                is Result.Loading -> Text("Loading")
                is Result.Success<*> -> {
                    val registerResponse = (signUpState as Result.Success<RegisterResponse>).data
                    Text("Sign up Successful: ${registerResponse.message}")
                }
                is Result.Error -> {
                    val error = (signUpState as Result.Error).error
                    Text("Error: $error", color = Color.Red)
                }
            }
        }
    }
}

@Composable
fun SignupButton(
    viewModel: SignupViewModel,
    modifier: Modifier = Modifier
){
    Button(onClick = {
        viewModel.onEvent(SignupEvent.Submit)
    },
        modifier = Modifier
            .fillMaxWidth()
            .padding(0.dp, 16.dp, 0.dp, 8.dp)) {
        Text("Sign Up")
    }
}

@Composable
fun LoginButton(onNavigateToLogin: () -> Unit, modifier: Modifier = Modifier){
    OutlinedButton(onClick = {
        onNavigateToLogin()
    },
        modifier = Modifier
            .fillMaxWidth()
            .padding(0.dp, 0.dp, 0.dp, 8.dp)) {
        Text("Log In")
    }
}

@Composable
fun NameTextField(viewModel: SignupViewModel) {
    CustomTextField(
        title = stringResource(R.string.name),
        text = viewModel.formState.name,
        onValueChange = {
            viewModel.onEvent(SignupEvent.NameChanged(it))
        },
        keyboardType = KeyboardType.Text,
        imeAction = ImeAction.Next,
        isError = viewModel.formState.nameError != null,
        errorMessage = viewModel.formState.nameError,
        singleLine = true,
    )
}

@Composable
fun EmailTextField(viewModel: SignupViewModel) {
    CustomTextField(
        title = "Email",
        text = viewModel.formState.email,
        onValueChange = {
            viewModel.onEvent(SignupEvent.EmailChanged(it))
        },
        leadingIcon = painterResource(id = R.drawable.ic_email),
        keyboardType = KeyboardType.Email,
        imeAction = ImeAction.Next,
        isError = viewModel.formState.emailError != null,
        errorMessage = viewModel.formState.emailError,
        singleLine = true,
    )
}

@Composable
fun PasswordTextField(
    viewModel: SignupViewModel
) {
    DicodingStoryTheme {
        CustomTextField(
            title = "Password",
            text = viewModel.formState.password,
            onValueChange = {
                viewModel.onEvent(SignupEvent.PasswordChanged(it))
            },
            leadingIcon = painterResource(id = R.drawable.ic_lock),
            trailingIcon = {
                Box(
                    modifier = Modifier
                ) {
                    IconButton(
                        onClick =
                        {
                            viewModel.onEvent(SignupEvent.VisiblePassword(!(viewModel.formState.isVisiblePassword)))
                        }
                    ) {
                        Icon(
                            painter = if (viewModel.formState.isVisiblePassword) painterResource(
                                id = R.drawable.ic_visibility_off
                            ) else painterResource(
                                id = R.drawable.ic_visibility
                            ),
                            contentDescription = "Visible",
                            tint = MaterialTheme.colorScheme.primary,
                            modifier = Modifier
                                .requiredSize(48.dp)
                                .padding(16.dp)
                        )
                    }
                }
            },
            keyboardType = KeyboardType.Password,
            imeAction = ImeAction.Done,
            isError =  viewModel.formState.passwordError != null,
            isVisible = viewModel.formState.isVisiblePassword,
            errorMessage = viewModel.formState.passwordError,
            singleLine = true,
        )
    }
}

@Composable
fun AnimatedSignupImage() {
    AnimatedMovingImageVertical(
        painter = painterResource(id = R.drawable.image_signup),
        description = "Sign Up Image Animation"
    )
}
