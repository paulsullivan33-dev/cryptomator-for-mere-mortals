# Installing Cryptomator

**Two official ways in: the desktop app for normal use,**
**and a command-line tool for scripts and servers.** Both
come from the same project and open the same vaults.

## The desktop app

For your own machine, this is the install that matters:
download the app for Windows, macOS or Linux from
cryptomator.org, or install it from your platform's store
or your Linux distribution's packages. It creates vaults,
mounts them as drives, and handles password changes and
recovery keys through ordinary windows. That click-through
experience is described in this guide but, per the home
page, not lab-run — the sandbox used for the lab has no
display for it to open on.

The Linux download is an AppImage. The lab fetched the
current one and recorded exactly what arrived
(transcript t00):

```text
Cryptomator-1.19.6-x86_64.AppImage
sha256: a6167f6ab3c5f161b31726b94bcf0abcb7f78c
        857b16e485adeb8e5096b74acd
size:   59,075,064 bytes
```

Two habits from the lab worth keeping:

- **Checksum your download.** Compute the SHA-256 of what
  you fetched and compare it with the value published with
  the release. The lab's value above matched the official
  release page's file.
- **An AppImage needs the execute bit**: `chmod +x` before
  the first run, or the shell answers `Permission denied`
  (transcript t09 learned this so you don't have to).

On a machine *with* FUSE, double-clicking runs the app. On
the lab's sandbox — no FUSE device at all — even the
AppImage's self-mount fails:

```text
fuse: device not found, try 'modprobe fuse' first
Cannot mount AppImage, please check your FUSE setup.
```

The project's escape hatch works anyway:
`--appimage-extract` unpacks it without FUSE, and the
extracted `AppRun` genuinely starts the desktop app — it
logs its environment, then stops in a headless room with
`java.lang.UnsupportedOperationException: Unable to open
DISPLAY`. Inside the package, for the curious, sit the same
libraries the CLI carries, in newer versions (cryptofs
2.11.1, cryptolib 2.4.0). Full evidence: transcript t09.

## The command-line tool

`cryptomator-cli` is the official CLI: a zip from the
project's GitHub releases containing a launcher, the
project's libraries, and its own bundled Java runtime — so
it does not need a JRE installed to run. The lab's copy:

```text
cryptomator-cli-0.6.2-linux-x64.zip
sha256: 6c2ac174f94a2ff30fdfa00ac43669703f1bca1f
        a633a762dc336bf9d794b1cb
$ cryptomator-cli --version
0.6.2
```

Ask it what it can do before believing any guide (this one
included) — transcript t01:

```text
$ cryptomator-cli --help
Usage: cryptomator-cli [-hV] [COMMAND]
Commands:
  unlock         unlock a vault
  list-mounters  List all available mount providers
  help           Displays help information about a command
```

Hold that output in mind: **unlock** is the whole verb
list. There is no `create`, no `add`, no `lock` command —
the next chapter deals with what that means.

## The Java note, for the lab only

The lab also needed a system JDK (OpenJDK 21) to compile
two small helper programs against the project's libraries —
that's lab scaffolding for creating a vault without the
desktop app, covered in Chapter 4. Normal readers need none
of it: desktop app to create, CLI to unlock.

**Reader check:** you should be able to name the two
official downloads, say which one a headless server wants,
verify either one's SHA-256 before running it — and recite
the CLI's three commands, noticing which obvious one is
missing.
