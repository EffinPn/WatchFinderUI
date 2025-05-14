package com.example.watchfinder.screens

import android.app.DatePickerDialog
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Done
import androidx.compose.material.icons.filled.Info
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.watchfinder.viewmodels.RegisterVM
import java.util.Calendar
import java.util.Locale

@Composable
fun Register(
    viewModel: RegisterVM = hiltViewModel(),
    onRegisterSuccess: () -> Unit,
    onNavigateToLogin: () -> Unit
) {
    val uiState by viewModel.uiState.collectAsState()

    LaunchedEffect(uiState.registrationSuccess) {
        if (uiState.registrationSuccess) {
            onRegisterSuccess()
            viewModel.onRegistrationNavigated()
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 20.dp, vertical = 10.dp)
            .imePadding(),
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            "Registro",
            style = MaterialTheme.typography.headlineMedium,
            modifier = Modifier.align(Alignment.CenterHorizontally)
        )
        Spacer(modifier = Modifier.height(20.dp))

        OutlinedTextField(
            value = uiState.nameInput,
            onValueChange = viewModel::onNameChange,
            label = { Text("Nombre completo") },
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 4.dp),
            singleLine = true,
            isError = uiState.registrationError?.contains("nombre", ignoreCase = true) == true
        )

        OutlinedTextField(
            value = uiState.nickInput,
            onValueChange = viewModel::onNickChange,
            label = { Text("Nickname (para login)") },
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 4.dp),
            singleLine = true,
            isError = uiState.registrationError?.contains("nick", ignoreCase = true) == true
                    || uiState.registrationError?.contains(
                "usuario",
                ignoreCase = true
            ) == true
        )

        OutlinedTextField(
            value = uiState.emailInput,
            onValueChange = viewModel::onEmailChange,
            label = { Text("Email") },
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 4.dp),
            singleLine = true,
            isError = uiState.registrationError?.contains("email", ignoreCase = true) == true
        )

        OutlinedTextField(
            value = uiState.passwordInput,
            onValueChange = viewModel::onPasswordChange,
            label = { Text("Contraseña") },
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 4.dp),
            singleLine = true,
            visualTransformation = if (uiState.isPasswordVisible) VisualTransformation.None else PasswordVisualTransformation(),
            trailingIcon = {
                val image = if (uiState.isPasswordVisible) Icons.Filled.Info else Icons.Filled.Done
                val description =
                    if (uiState.isPasswordVisible) "Ocultar contraseña" else "Mostrar contraseña"
                IconButton(onClick = viewModel::togglePasswordVisibility) {
                    Icon(imageVector = image, contentDescription = description)
                }
            },
            isError = uiState.registrationError?.contains("contraseña", ignoreCase = true) == true
        )

        OutlinedTextField(
            value = uiState.repeatPasswordInput,
            onValueChange = viewModel::onRepeatPasswordChange,
            label = { Text("Repetir contraseña") },
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 4.dp),
            singleLine = true,
            visualTransformation = if (uiState.isRepeatPasswordVisible) VisualTransformation.None else PasswordVisualTransformation(),
            trailingIcon = {
                val image =
                    if (uiState.isRepeatPasswordVisible) Icons.Filled.Info else Icons.Filled.Done
                val description =
                    if (uiState.isRepeatPasswordVisible) "Ocultar contraseña" else "Mostrar contraseña"
                IconButton(onClick = viewModel::toggleRepeatPasswordVisibility) {
                    Icon(imageVector = image, contentDescription = description)
                }
            },
            isError = uiState.registrationError?.contains(
                "contraseña",
                ignoreCase = true
            ) == true
        )

        Spacer(modifier = Modifier.height(10.dp))

        AgeChoose(
            selectedDate = uiState.birthDateInput,
            onDateSelected = viewModel::onBirthDateChange
        )

        if (uiState.registrationError != null) {
            Text(
                text = uiState.registrationError!!,
                color = MaterialTheme.colorScheme.error,
                style = MaterialTheme.typography.bodySmall,
                modifier = Modifier
                    .padding(top = 8.dp)
                    .align(Alignment.CenterHorizontally)
            )
        } else {
            Spacer(modifier = Modifier.height(24.dp))
        }

        Button(
            onClick = viewModel::attemptRegistration,
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 10.dp)
                .height(50.dp),
            enabled = !uiState.isLoading
        ) {
            if (uiState.isLoading) {
                Progress()
            } else {
                Text("Registrar usuario")
            }
        }

        Text(
            text = "¿Ya tienes cuenta? Inicia sesión",
            modifier = Modifier
                .padding(top = 10.dp)
                .align(Alignment.CenterHorizontally)
                .clickable { onNavigateToLogin() },
            color = MaterialTheme.colorScheme.primary
        )
    }
}

@Composable
fun AgeChoose(
    selectedDate: String,
    onDateSelected: (String) -> Unit
) {
    val context = LocalContext.current
    val calendar = Calendar.getInstance()
    var initialYear = calendar.get(Calendar.YEAR)
    var initialMonth = calendar.get(Calendar.MONTH)
    var initialDay = calendar.get(Calendar.DAY_OF_MONTH)

    if (selectedDate.isNotEmpty()) {
        try {
            val parts = selectedDate.split("/")
            if (parts.size == 3) {
                initialDay = parts[0].toInt()
                initialMonth = parts[1].toInt() - 1
                initialYear = parts[2].toInt()
            } else {
                val partsIso = selectedDate.split("-")
                if (partsIso.size == 3) {
                    initialYear = partsIso[0].toInt()
                    initialMonth = partsIso[1].toInt() - 1
                    initialDay = partsIso[2].toInt()
                }
            }
        } catch (e: Exception) {
            println("Error parsing date $selectedDate: ${e.message}")
        }
    }


    val datePickerDialog = remember {
        DatePickerDialog(
            context,
            { _, year, month, dayOfMonth ->
                val formattedDate =
                    String.format(Locale.getDefault(), "%02d/%02d/%d", dayOfMonth, month + 1, year)
                onDateSelected(formattedDate)
            },
            initialYear,
            initialMonth,
            initialDay
        ).apply {
            datePicker.maxDate = calendar.timeInMillis
        }
    }

    OutlinedButton(
        onClick = { datePickerDialog.show() },
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp)
    ) {
        Text(if (selectedDate.isBlank()) "Seleccionar fecha de nacimiento" else "Nacido el: $selectedDate")
    }
}

