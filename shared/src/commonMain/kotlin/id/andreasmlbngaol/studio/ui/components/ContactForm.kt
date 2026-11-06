package id.andreasmlbngaol.studio.ui.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Send
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import id.andreasmlbngaol.studio.state.LocalAppConfig
import id.andreasmlbngaol.studio.theme.bodySmallTextStyle
import id.andreasmlbngaol.studio.util.submitContactForm
import kotlinx.coroutines.launch

private enum class FormStatus {
    Idle,
    Sending,
    Success,
    Error,
}

private val emailPattern = Regex("^[^@\\s]+@[^@\\s]+\\.[^@\\s]+$")

@Composable
fun ContactForm(compact: Boolean, modifier: Modifier = Modifier) {
    val strings = LocalAppConfig.current.strings
    var name by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var message by remember { mutableStateOf("") }
    var nameError by remember { mutableStateOf(false) }
    var emailError by remember { mutableStateOf(false) }
    var messageError by remember { mutableStateOf(false) }
    var status by remember { mutableStateOf(FormStatus.Idle) }
    val scope = rememberCoroutineScope()

    fun submit() {
        nameError = name.isBlank()
        emailError = !emailPattern.matches(email.trim())
        messageError = message.trim().length < 10
        if (nameError || emailError || messageError) return
        status = FormStatus.Sending
        scope.launch {
            val sent = submitContactForm(name.trim(), email.trim(), message.trim())
            if (sent) {
                status = FormStatus.Success
                name = ""
                email = ""
                message = ""
            } else {
                status = FormStatus.Error
            }
        }
    }

    Column(
        modifier = modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(16.dp),
    ) {
        OutlinedTextField(
            value = name,
            onValueChange = {
                name = it
                nameError = false
            },
            modifier = Modifier.fillMaxWidth(),
            label = { Text(strings.formName) },
            placeholder = { Text(strings.formNamePlaceholder) },
            singleLine = true,
            isError = nameError,
            supportingText = if (nameError) {
                { Text(strings.formNameError) }
            } else {
                null
            },
            shape = MaterialTheme.shapes.extraSmall,
            keyboardOptions = KeyboardOptions(imeAction = ImeAction.Next),
        )
        OutlinedTextField(
            value = email,
            onValueChange = {
                email = it
                emailError = false
            },
            modifier = Modifier.fillMaxWidth(),
            label = { Text(strings.formEmail) },
            placeholder = { Text(strings.formEmailPlaceholder) },
            singleLine = true,
            isError = emailError,
            supportingText = if (emailError) {
                { Text(strings.formEmailError) }
            } else {
                null
            },
            shape = MaterialTheme.shapes.extraSmall,
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email, imeAction = ImeAction.Next),
        )
        OutlinedTextField(
            value = message,
            onValueChange = {
                message = it
                messageError = false
            },
            modifier = Modifier.fillMaxWidth(),
            label = { Text(strings.formMessage) },
            placeholder = { Text(strings.formMessagePlaceholder) },
            minLines = 4,
            isError = messageError,
            supportingText = if (messageError) {
                { Text(strings.formMessageError) }
            } else {
                null
            },
            shape = MaterialTheme.shapes.extraSmall,
            keyboardOptions = KeyboardOptions(imeAction = ImeAction.Default),
        )
        Button(
            onClick = { submit() },
            shapes = ButtonDefaults.shapes(),
            modifier = Modifier.height(56.dp),
            enabled = status != FormStatus.Sending,
            contentPadding = PaddingValues(horizontal = 24.dp),
        ) {
            Icon(Icons.AutoMirrored.Filled.Send, contentDescription = null, modifier = Modifier.size(24.dp))
            Spacer(Modifier.width(8.dp))
            Text(
                text = if (status == FormStatus.Sending) strings.formSending else strings.formSend,
                style = MaterialTheme.typography.titleMedium,
            )
        }
        when (status) {
            FormStatus.Success -> Text(
                text = strings.formSuccess,
                style = bodySmallTextStyle(compact),
                color = MaterialTheme.colorScheme.primary,
            )
            FormStatus.Error -> Text(
                text = strings.formError,
                style = bodySmallTextStyle(compact),
                color = MaterialTheme.colorScheme.error,
            )
            else -> Unit
        }
    }
}
