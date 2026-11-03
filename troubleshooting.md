# Troubleshooting

**Every error below actually happened in this lab**, and
is filed under the transcript that caught it. The CLI's
errors are terse Java exception names; this chapter is the
translation.

## `InvalidPassphraseException` — and nothing else (t07)

The unlock prints this one line and exits 1. It means the
password is wrong. Not "the vault is damaged", not "the
file is corrupt": wrong password. Check the password
manager entry, check for a changed password you forgot you
changed, then try again. There is no lockout for trying.

Relatedly: if a vault that *should* accept your password
rejects it after a sync, suspect a stale or mismatched
`masterkey.cryptomator` first — the password unwraps that
file, and a sync conflict copy of it is not the real one.

## `NoSuchFileException: .../vault.cryptomator` (t07)

You pointed the tool at a folder that is not a vault. The
config token is the first file unlocking looks for. Check
the path — a common slip is pointing at the folder *inside*
or *above* the vault folder rather than the vault folder
itself (the one containing `masterkey.cryptomator`,
`vault.cryptomator` and `d/`).

## `fuse device not found` / `fuse_mount failed` (t04)

```text
fusermount3: fuse device not found, try 'modprobe fuse' first
MountFailedException: ... FuseMountFailedException: fuse_mount failed
```

The vault unlocked fine; the *mount* has no FUSE to attach
to (`ls /dev/fuse` to confirm). On a normal desktop this is
a missing package or permission; in a container it is
usually final. Either fix FUSE on that machine, or use the
WebDAV fallback mounter (Chapter 6) and accept its
loopback discipline.

The desktop **AppImage** failing the same way is the same
cause one level up — "Cannot mount AppImage, please check
your FUSE setup" — and the workaround is
`--appimage-extract`, then run the extracted `AppRun`
(t09).

## The AppImage won't start, in two other flavours (t09)

- `./Cryptomator....AppImage: Permission denied` — the
  download has no execute bit. `chmod +x` it.
- The extracted app starts, logs, then exits 1 with
  `UnsupportedOperationException: Unable to open DISPLAY`
  — it is running somewhere with no display. It is a
  desktop app; give it a desktop (or use the CLI on that
  machine instead).

## `ContentRootMissingException` (t04a)

"The encrypted root directory of the vault is missing."
The folder has the key and token of a vault, but the
vault was never fully created — its `d/` tree has no root
bucket. In the lab this was a half-built vault; in the
wild it means an interrupted creation or a partially
copied vault. A *complete* copy (Chapter 9) is the fix;
there is nothing to repair by hand.

## Reading a file returns 500, or an integrity warning (t10)

```text
AuthenticationFailedException: Authentication of chunk N failed.
```

A chunk's authentication check failed: the ciphertext on
disk is not the ciphertext that was written. Causes are
the usual storage suspects — a sync tool that "fixed" a
conflict badly, a failing disk, a hand-edited vault
folder. Restore that file's `.c9r` from backup. Note the
blast radius is one chunk of one file; the rest of the
vault is unaffected, which the lab verified by reading
the neighbouring file immediately afterwards.

## Lab-helper errors you will only meet building labs (t03a, t03b)

Kept for completeness, because builders read these guides
too:

- `NullPointerException: this.cipherCombo is null` while
  creating a vault through the libraries — the config
  builder was not given a cipher combination. Set it
  explicitly (the lab's helper uses SIV_CTRMAC, matching
  the desktop's format 8).
- `UnsupportedClassVersionError ... class file version
  68.0` — the CLI's app jar is compiled for its bundled
  Java 24 and refuses to load on an older system JDK.
  Keep the CLI's app jar off a helper's classpath; the
  libraries load fine.

## When the answer is "that's the design"

Wrong-password silence, no create command, no lock
command, no password recovery: Chapters 4, 5, 8 and the
limits chapter cover these. If an error looks like the
tool refusing to help you open a vault without its
password, that is not a malfunction — it is the product.

**Reader check:** given any error in this chapter, you
should be able to say which layer failed — password,
folder, mount, file integrity, or expectations — before
reaching for a fix.
