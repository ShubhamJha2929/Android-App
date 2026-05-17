# Fix: "Can't be downloaded" in Android Studio

Follow these steps **in order**. The bottom panel usually shows **what** failed (Gradle, SDK, or emulator image).

---

## Step 1: See the exact error

1. In Android Studio, open the **Build** tab at the bottom (or **Sync**).
2. Scroll up and find the **first red line**.
3. Common phrases:
   - `Failed to download Gradle` → Step 2
   - `Failed to install SDK` / `platforms;android-34` → Step 3
   - `System image` / `emulator` → Step 4
   - `Could not resolve` / `google()` / `maven` → Step 5

---

## Step 2: Fix Gradle download

1. **File → Settings** (or **Android Studio → Settings** on Mac).
2. **Build, Execution, Deployment → Build Tools → Gradle**.
3. Set **Gradle JDK** to **17** (Embedded JDK or jbr-17).
4. Under **Gradle**, choose:
   - **Gradle version:** use default, or pick one already installed (e.g. 8.7).
5. Click **OK**.
6. **File → Sync Project with Gradle Files**.

If it still fails (slow/blocked internet):

- Try **mobile hotspot** or another network.
- Temporarily **turn off VPN**.
- **File → Invalidate Caches → Invalidate and Restart**.

---

## Step 3: Install Android SDK manually (most common fix)

1. **Tools → SDK Manager**.
2. Tab **SDK Platforms**:
   - Check **Android 14.0 ("UpsideDownCake")** — API **34**
   - Click **Apply** → **OK** and wait until download finishes.
3. Tab **SDK Tools**:
   - Check **Android SDK Build-Tools 34**
   - Check **Android SDK Platform-Tools**
   - Check **Android Emulator** (if using virtual phone)
   - Click **Apply** → **OK**.

4. Create file `local.properties` in project root  
   `C:\Users\RAJ\AksharaDeepaTutor\local.properties`  
   with **one line** (change `RAJ` if your Windows username is different):

```properties
sdk.dir=C\:\\Users\\RAJ\\AppData\\Local\\Android\\Sdk
```

5. **File → Sync Project with Gradle Files** again.

---

## Step 4: Fix emulator "can't download" (system image)

If the error appears when creating a **virtual device**:

1. **Tools → SDK Manager → SDK Platforms**.
2. Check **Show Package Details** (bottom right).
3. Under **Android 14.0 (API 34)**, check:
   - **Google APIs Intel x86_64 Atom System Image** (PC/emulator), OR  
   - **Google APIs ARM 64 v8a** (some newer setups).
4. Click **Apply** and wait for full download.
5. Then **Tools → Device Manager → Create Device** again.

**Easier option:** use a **real phone** with USB debugging (no emulator download needed).

---

## Step 5: Fix library / internet (Maven) errors

1. **File → Settings → Appearance & Behavior → System Settings → HTTP Proxy**
   - If you don't use a proxy: **No proxy**.
2. Ensure date/time on PC is correct.
3. Sync again: **File → Sync Project with Gradle Files**.

---

## Step 6: Missing Gradle wrapper (gradlew)

This project may be missing `gradlew.bat`. Android Studio can still sync if Gradle is configured.

**Option A — Let Android Studio fix it**

1. Close project.
2. **File → New → New Project → Empty Activity** (any name) → Finish.
3. In the **new** project folder, copy these to `AksharaDeepaTutor`:
   - `gradlew`
   - `gradlew.bat`
   - `gradle\wrapper\gradle-wrapper.jar`
4. Open **AksharaDeepaTutor** again and sync.

**Option B — Use installed Gradle**

1. **File → Settings → Build Tools → Gradle**
2. Select **Specified location** and point to Gradle bundled with Android Studio,  
   often:  
   `C:\Program Files\Android\Android Studio\gradle`

---

## Step 7: Run the app

1. Start emulator **or** connect phone (USB debugging on).
2. Green **Run ▶** with module **app** selected.

---

## Still stuck?

Copy the **full red error text** from the **Build** tab (last 15–20 lines) and share it for a exact fix.
