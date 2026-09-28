This is a Kotlin Multiplatform module targeting Web (Wasm).

It renders the in-app documentation (Markdown files under
[composeResources/files/docs](./src/commonMain/composeResources/files/docs)) as a standalone
web app, sharing UI code with the mobile app via the root [:shared](../shared/src) module.

### Running

- Wasm target (faster, modern browsers): `./gradlew :docsApp:wasmJsBrowserDevelopmentRun`
- JS target (slower, supports older browsers): `./gradlew :docsApp:jsBrowserDevelopmentRun`

---

Learn more about [Kotlin Multiplatform](https://www.jetbrains.com/help/kotlin-multiplatform-dev/get-started.html),
[Compose Multiplatform](https://kotlinlang.org/compose-multiplatform/),
[Kotlin/Wasm](https://kotl.in/wasm/)…
