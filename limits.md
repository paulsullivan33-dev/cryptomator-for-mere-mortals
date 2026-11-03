# Limits

**What Cryptomator will not do, stated as plainly as what**
**it will.**

## The password is a single point of total failure

There is no admin, no escrow and no reset flow for a
standalone vault. Lose the password and the recovery key
and the data is gone, permanently, by design (Chapter 8).
The same design means one password opens *everything*:
there are no per-user accounts, no read-only members and
no audit log of who opened a shared vault.

## Metadata survives

Encryption covers contents and filenames. It does not
cover the number of files and folders, their approximate
sizes (ciphertext overhead is small and predictable), or
their change times. An observer learns that a vault exists
and roughly how busy it is (Chapter 7). If that is your
threat, this is the wrong shape of tool.

## An unlocked vault is just files

While unlocked — mounted or served — the cleartext is
available to anything running as you, including malware.
Cryptomator protects data at rest on other people's
storage; it is not a sandbox, and it does not protect a
compromised machine. Lock vaults you are not using.

## The vault folder is not user-serviceable

The `.c9r` tree is a database. Renaming, moving, "cleaning
up" or partially syncing it by hand corrupts the structure
it encodes; a missing `masterkey.cryptomator` makes the
rest expensive gravel. Tools that copy whole folders are
safe; fingers are not.

## The CLI is a fraction of the product

Measured in this lab, not folklore: the official CLI
unlocks and nothing else. It cannot create vaults
(transcript t02), change passwords, produce recovery keys
or manage vaults; there is not even a lock command —
ending the process is the lock. Automation beyond
"unlock, transfer files, stop" needs the desktop app, the
libraries, or another tool. The WebDAV door it offers is
also the slow, simple presentation — fine for scripts,
not a substitute for a mount at desktop scale.

## Mounting needs the platform's help

The normal mounted experience rides on FUSE (or platform
equivalents). Where FUSE is absent — a restricted
container, a locked-down server, this lab's sandbox — the
mount does not happen, full stop, and even the desktop
AppImage cannot self-mount to start. The fallback is the
WebDAV door, with its loopback-only discipline.

## Format and version realities

Vaults are format 8: older Cryptomator releases that
predate the format cannot open them, and the config token
refuses to pair a masterkey with the wrong vault. Keep
apps reasonably current on every machine that shares a
vault, and expect a one-way door if a future format ever
supersedes 8.

## Not covered here, by scope

Cryptomator Hub (hosted team key management), the mobile
apps' specifics, and integration keyrings are outside this
guide's lab; the limits above are the standalone vault's
own.

**Reader check:** you should be able to recite the three
hardest limits without notes — a lost password is final,
metadata still shows, and the CLI unlocks only — and name
the situation each one rules out.
