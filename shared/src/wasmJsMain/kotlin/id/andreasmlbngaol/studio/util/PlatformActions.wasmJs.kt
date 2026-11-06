@file:OptIn(ExperimentalWasmJsInterop::class)

package id.andreasmlbngaol.studio.util

import kotlinx.browser.document
import kotlinx.browser.window
import kotlinx.coroutines.suspendCancellableCoroutine
import org.w3c.dom.events.Event
import org.w3c.xhr.XMLHttpRequest
import kotlin.coroutines.resume
import kotlin.js.ExperimentalWasmJsInterop
import kotlin.js.js

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

// Safari exposes the API only with the webkit prefix, so every call checks both.
actual fun isFullscreenSupported(): Boolean =
    js("!!(document.fullscreenEnabled || document.webkitFullscreenEnabled)")

actual fun isFullscreenActive(): Boolean =
    js("!!(document.fullscreenElement || document.webkitFullscreenElement)")

actual fun enterFullscreen() {
    js(
        """(() => {
            const el = document.documentElement;
            const fn = el.requestFullscreen || el.webkitRequestFullscreen;
            if (!fn) return;
            const result = fn.call(el);
            if (result && typeof result.catch === 'function') result.catch(() => {});
        })()""",
    )
}

actual fun exitFullscreen() {
    js(
        """(() => {
            const fn = document.exitFullscreen || document.webkitExitFullscreen;
            if (!fn) return;
            const result = fn.call(document);
            if (result && typeof result.catch === 'function') result.catch(() => {});
        })()""",
    )
}

actual fun observeFullscreen(onChange: (Boolean) -> Unit): () -> Unit {
    val listener: (Event) -> Unit = { onChange(isFullscreenActive()) }
    document.addEventListener("fullscreenchange", listener)
    document.addEventListener("webkitfullscreenchange", listener)
    return {
        document.removeEventListener("fullscreenchange", listener)
        document.removeEventListener("webkitfullscreenchange", listener)
    }
}
