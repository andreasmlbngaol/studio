package id.andreasmlbngaol.studio.util

/** Opens an external link in a new browser tab. */
expect fun openUrl(url: String)

/** Updates the browser tab title. */
expect fun setDocumentTitle(title: String)

/**
 * Marks the first Compose frame as ready so the web shell can fade the canvas in.
 * No-op on platforms without a DOM.
 */
expect fun markAppReady()

/**
 * Sends the contact form to the Cloudflare Pages Function at `/api/contact`.
 * Returns true when the endpoint accepted the message.
 */
expect suspend fun submitContactForm(name: String, email: String, message: String): Boolean

/** Whether this browser/device can enter fullscreen at all (iOS Safari on iPhone cannot). */
expect fun isFullscreenSupported(): Boolean

/** Whether the document is in fullscreen right now. */
expect fun isFullscreenActive(): Boolean

/** Requests fullscreen for the whole page. Must be called from a user gesture. */
expect fun enterFullscreen()

/** Leaves fullscreen. Safe to call when not in fullscreen. */
expect fun exitFullscreen()

/** Listens for fullscreen changes. Returns an unsubscribe function. */
expect fun observeFullscreen(onChange: (Boolean) -> Unit): () -> Unit
