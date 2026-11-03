# Unlocking and Mounting

**Unlocking checks the password against the masterkey**
**file; mounting is how the cleartext appears.** The lab
could do the first completely and the second only part
way — and the boundary is instructive.

## The three doors

`cryptomator-cli list-mounters` (transcript t01) lists the
doors this build offers on Linux:

```text
org.cryptomator.frontend.fuse.mount.LinuxFuseMountProvider
org.cryptomator.frontend.webdav.mount.LinuxGioMounter
org.cryptomator.frontend.webdav.mount.FallbackMounter
```

- **FUSE mount** — the vault appears at a mount point as a
  normal directory tree. This is what the desktop app does
  (with its own providers per platform), and it is the
  experience you will have on your own machine.
- **GIO mounter** — serves the vault over WebDAV and mounts
  that through GNOME's GIO layer, for desktop integration
  without FUSE.
- **Fallback mounter** — serves the vault over WebDAV on a
  loopback port and stops there. No mount at all — which is
  precisely why it is the door this lab could use.

The desktop app also offers a WebDAV presentation on some
platforms; the model is the same throughout — unlock once,
then present cleartext through one door while the vault
folder stays ciphertext.

## The FUSE attempt, and its exact failure

The lab tried the real mount first (transcript t04,
re-run in full in its appendix):

```text
fusermount3: fuse device not found, try 'modprobe fuse' first
org.cryptomator.integrations.mount.MountFailedException:
  org.cryptomator.jfuse.api.FuseMountFailedException:
  fuse_mount failed
```

`ls /dev/fuse` says the rest: the device does not exist in
this sandbox and cannot be loaded. Note what failed and
what didn't — the password was accepted and the vault
unlocked; only the mount had nowhere to attach. On any
normal Linux desktop, and on the desktop app everywhere,
this door simply works (documented-only here, and labelled
so wherever this guide relies on it).

## Unlocking with the CLI

The one command, with the password passed the safe way —
from an environment variable, never typed on the command
line where shell history and process lists can see it:

```text
$ export VAULT_PW=...            (from your password manager)
$ cryptomator-cli unlock \
    --password:env=VAULT_PW \
    --mounter=org.cryptomator.frontend.webdav.mount.FallbackMounter \
    --loopbackPort=18091 \
    /path/to/vault
```

Success looks undramatic in the log:

```text
[main] INFO  o.c.cli.Unlock - Unlocked and mounted vault
successfully to http://localhost:18091/<a fresh UUID>
```

That UUID path is per-unlock and random; read it from the
log each time. The process keeps running in the foreground
of its session — the vault stays unlocked exactly as long
as the process lives, which is the CLI's whole locking
story, told in Chapter 6.

Two failure shapes to recognise now (both transcript t07,
both one line each — this CLI does not do friendly
errors):

```text
org.cryptomator.cryptolib.api.InvalidPassphraseException
```
— the password is wrong. Nothing else is wrong; nothing
is damaged; try again with the right one.

```text
java.nio.file.NoSuchFileException: /path/plain/vault.cryptomator
```
— that folder is not a vault (the config token is the
first thing looked for).

**Reader check:** you should be able to name the three
mounters, say which one needs no `/dev/fuse` and why this
lab used it, and recognise the one-line errors for a wrong
password and a not-a-vault folder on sight.
