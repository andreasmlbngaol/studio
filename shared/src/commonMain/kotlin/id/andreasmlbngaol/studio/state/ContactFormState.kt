package id.andreasmlbngaol.studio.state

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue

/**
 * Session-scoped success flag for the contact form. Living outside the
 * composable keeps the confirmation visible after navigating away and back,
 * while a page refresh clears it and makes the form available again.
 */
object ContactFormState {
    var sent by mutableStateOf(false)
        private set

    fun markSent() {
        sent = true
    }
}
