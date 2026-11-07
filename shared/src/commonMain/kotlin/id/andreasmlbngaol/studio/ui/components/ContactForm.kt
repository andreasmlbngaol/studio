package id.andreasmlbngaol.studio.ui.components

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Send
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.semantics.LiveRegionMode
import androidx.compose.ui.semantics.liveRegion
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import id.andreasmlbngaol.studio.state.ContactFormState
import id.andreasmlbngaol.studio.state.LocalAppConfig
import id.andreasmlbngaol.studio.theme.bodySmallTextStyle
import id.andreasmlbngaol.studio.theme.bodyTextStyle
import id.andreasmlbngaol.studio.util.submitContactForm
import kotlinx.coroutines.launch

private enum class FormStatus {
    Idle,
    Sending,
    Error,
}

private val emailPattern = Regex("^[^@\\s]+@[^@\\s]+\\.[^@\\s]+$")

@Composable
fun ContactForm(compact: Boolean, modifier: Modifier = Modifier) {
    AnimatedContent(
        targetState = ContactFormState.sent,
        modifier = modifier.fillMaxWidth(),
        transitionSpec = { fadeIn(tween(240)) togetherWith fadeOut(tween(160)) },
        label = "contact-form",
    ) { sent ->
        if (sent) {
            ContactSuccess(compact)
        } else {
            ContactFields(compact)
        }
    }
}

@Composable
private fun ContactSuccess(compact: Boolean) {
    val strings = LocalAppConfig.current.strings
    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .semantics { liveRegion = LiveRegionMode.Polite },
        shape = MaterialTheme.shapes.extraLarge,
        color = MaterialTheme.colorScheme.primary,
        contentColor = MaterialTheme.colorScheme.onPrimary,
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 24.dp, vertical = if (compact) 32.dp else 44.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(12.dp),
        ) {
            Icon(
                imageVector = Icons.Filled.CheckCircle,
                contentDescription = null,
                modifier = Modifier.size(if (compact) 44.dp else 52.dp),
            )
            Text(
                text = strings.formSuccessTitle,
                style = MaterialTheme.typography.titleLarge,
                textAlign = TextAlign.Center,
            )
            Text(
                text = strings.formSuccess,
                style = bodyTextStyle(compact),
                textAlign = TextAlign.Center,
            )
        }
    }
}

@Composable
private fun ContactFields(compact: Boolean) {
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
                ContactFormState.markSent()
            } else {
                status = FormStatus.Error
            }
        }
    }

    Column(
        modifier = Modifier.fillMaxWidth(),
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
        if (status == FormStatus.Error) {
            Text(
                text = strings.formError,
                style = bodySmallTextStyle(compact),
                color = MaterialTheme.colorScheme.error,
            )
        }
    }
}
