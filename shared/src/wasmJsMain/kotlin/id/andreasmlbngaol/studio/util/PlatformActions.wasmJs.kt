package id.andreasmlbngaol.studio.util

import kotlinx.browser.document
import kotlinx.browser.window
import kotlinx.coroutines.suspendCancellableCoroutine
import org.w3c.xhr.XMLHttpRequest
import kotlin.coroutines.resume

actual fun openUrl(url: String) {
    window.open(url, "_blank", "noopener,noreferrer")
}

actual fun setDocumentTitle(title: String) {
    document.title = title
}

actual fun markAppReady() {
    document.body?.classList?.add("app-ready")
}

/**
 * Same-origin `/api/contact` by default (Cloudflare Pages).
 * For GitHub Pages, point the meta tag to a deployed Worker:
 * `<meta name="contact-endpoint" content="https://...workers.dev/api/contact">`.
 */
private val contactEndpoint: String
    get() {
        val value = document.querySelector("meta[name='contact-endpoint']")
            ?.getAttribute("content")
            ?.trim()
        return if (value.isNullOrEmpty()) "/api/contact" else value
    }

actual suspend fun submitContactForm(name: String, email: String, message: String): Boolean =
    suspendCancellableCoroutine { continuation ->
        val request = XMLHttpRequest()
        request.open("POST", contactEndpoint)
        request.setRequestHeader("Content-Type", "application/json")
        request.onload = {
            if (continuation.isActive) {
                continuation.resume(request.status.toInt() in 200..299)
            }
        }
        request.onerror = {
            if (continuation.isActive) continuation.resume(false)
        }
        request.send(
            """{"name":"${escape(name)}","email":"${escape(email)}","message":"${escape(message)}"}""",
        )
    }

private fun escape(value: String): String = buildString {
    for (char in value) {
        when (char) {
            '\\' -> append("\\\\")
            '"' -> append("\\\"")
            '\n' -> append("\\n")
            '\r' -> append("\\r")
            '\t' -> append("\\t")
            else -> if (char.code < 0x20) append("\\u").append(char.code.toString(16).padStart(4, '0')) else append(char)
        }
    }
}
