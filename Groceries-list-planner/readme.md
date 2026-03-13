Groceries List Planner – Setup & Run Guide
==========================================

This project is a small Clojure program. Below are **basic installation instructions for Clojure** on Windows, Linux, and macOS, followed by the **commands to run this program**.

For full, up‑to‑date details see the official Clojure install guide:  
`https://clojure.org/guides/install_clojure`

---

## 1. Prerequisites (all platforms)

- **Java**  
  - Install a Java LTS version (8, 11, 17, 21, or 25).  
  - The Clojure site recommends Temurin from Adoptium. After installation, make sure `java` is on your `PATH` (you can check with `java -version`).

---

## 2. Install Clojure

### Windows

You have two common options:

- **Option A – Windows Subsystem for Linux (WSL) – recommended**
  1. Install WSL (for example Ubuntu) from the Microsoft Store.
  2. Open an Ubuntu (WSL) terminal.
  3. Follow the **Linux** instructions below inside that WSL terminal.

### Linux

1. Make sure you have these dependencies installed: `bash`, `curl`, `rlwrap`, and Java.
2. Download and run the installer script (from the official guide), e.g.:
   - `curl -L -O https://github.com/clojure/brew-install/releases/latest/download/linux-install.sh`
   - `chmod +x linux-install.sh`
   - `sudo ./linux-install.sh`
3. This will install the `clj` and `clojure` commands system‑wide.

### macOS

1. Install **Homebrew** (see `https://brew.sh/` if you don’t have it).
2. Install Clojure CLI via Homebrew:
   - `brew install clojure/tools/clojure`
3. After this, `clj` and `clojure` should be available in your terminal.

For all platforms, refer to `https://clojure.org/guides/install_clojure` for the most accurate, current installation details.

---

## 3. Run the Project

From a terminal (PowerShell, WSL, or other shell), run:

```bash
cd groceries-list-planner/Groceries-list-planner
```

Make sure you are in the `Groceries-list-planner` directory that contains `deps.edn`.

### Windows (using WSL – recommended)

1. In Windows, start Ubuntu:
   ```bash
   wsl -d Ubuntu
   ```
2. Inside the Ubuntu (WSL) shell, go to the project:
   ```bash
   cd groceries-list-planner/Groceries-list-planner
   ```
3. Run the program:

   ```bash
   clj -M -m groceries-list-planner.core
   ```

The program should start and you can use the terminal to configure your week plan and create your grocerylist. type 'start' to begin and follow the terminal instruction.
