# Fix: Gradle Read timed out

Error: `Could not install Gradle distribution` + `SocketTimeoutException: Read timed out`

---

## Fix A — Sync again (timeout increased)

The project timeout was raised to **10 minutes**. In Android Studio:

1. **File → Sync Project with Gradle Files**
2. Wait patiently (5–15 minutes on slow Wi‑Fi). Do not cancel.

Use **mobile hotspot** if college Wi‑Fi is slow or blocked.

---

## Fix B — Download Gradle in browser (best for slow internet)

1. Open in Chrome/Edge (can pause/resume downloads):

   https://services.gradle.org/distributions/gradle-8.7-bin.zip

   File size is about **130 MB**.

2. Press **Win + R**, paste and Enter:

   ```
   %USERPROFILE%\.gradle\wrapper\dists
   ```

3. Open folder **`gradle-8.7-bin`** → open the **only subfolder** inside (long random name like `a1b2c3...`).

4. Move **`gradle-8.7-bin.zip`** into that subfolder (the zip you downloaded).

5. Delete any `.part` or incomplete files in that folder.

6. Android Studio → **File → Sync Project with Gradle Files**.

Gradle will extract the zip automatically. Do **not** unzip it yourself.

---

## Fix C — Use Android Studio’s Gradle (no download)

1. **File → Settings**
2. **Build, Execution, Deployment → Build Tools → Gradle**
3. **Distribution:** select **Local installation** or browse to Gradle inside Android Studio, for example:

   ```
   C:\Program Files\Android\Android Studio\gradle
   ```

   (Pick the `gradle-8.x` folder if you see several.)

4. **Apply → OK**
5. **File → Sync Project with Gradle Files**

---

## Fix D — Turn off VPN / fix proxy

1. **File → Settings → HTTP Proxy** → **No proxy**
2. Turn off VPN
3. Sync again
