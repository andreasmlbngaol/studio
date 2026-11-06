# Studio

Portfolio of Andreas Manatar Lumban Gaol, built with Compose Multiplatform for Web (Kotlin/Wasm).

**Live:** https://andreasmlbngaol.github.io/studio/

## Stack

- Kotlin 2.4.21 and Compose Multiplatform 1.13
- Material 3 Expressive
- Kotlin/Wasm
- Cloudflare Worker + Resend for the contact form

## Development

```bash
./gradlew :webApp:wasmJsBrowserDevelopmentRun
./gradlew :webApp:wasmJsBrowserDistribution
./gradlew :shared:wasmJsTest
```

The contact form posts to a Cloudflare Worker. The Resend API key is stored as a Worker secret and never shipped to the client.

## License

Licensed under the Apache License, Version 2.0. See [LICENSE](LICENSE).
