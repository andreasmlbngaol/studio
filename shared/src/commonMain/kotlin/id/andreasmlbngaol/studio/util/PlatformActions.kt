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
