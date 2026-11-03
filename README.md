# Cryptomator for Mere Mortals

**One folder that encrypts every file before it syncs.**

Cryptomator turns an ordinary folder into a vault. Files you
drop in are encrypted one by one — contents and filenames —
before anything writes them to disk, so the folder can sit
in Dropbox, Google Drive, a synced share or a backup
without the storage ever seeing a readable file. Unlock the
vault and it appears as a normal drive or folder of normal
files. Lock it, and what remains on disk is ciphertext with
gibberish names.

This guide builds a real vault, adds real files and reads
them back, then inspects the vault from the outside with
`grep` to prove that neither the filenames nor the contents
survive in cleartext anywhere in the vault tree. It also
proves the folder is portable, that one flipped byte is
detected, and what a wrong password costs you.

## The one rule, stated once here and repeated inside

Your vault password is the only key. There is no backdoor,
no reset link and nobody at Cryptomator who can open your
vault for you. Forget the password and the files are gone —
that is not a bug; it is the entire point of the design.
Keep the password in a password manager, and treat the
small `masterkey.cryptomator` file inside every vault as
the crown jewels, because it is.

## What the lab ran

Everything in the main path of this guide was run for real,
on the official releases, in a small Linux sandbox.

- **cryptomator-cli 0.6.2** — the project's official
  command-line tool (a Java program with its own bundled
  runtime). Full command transcripts of everything it did.
- **Cryptomator desktop 1.19.6** — the official AppImage,
  downloaded and checksum-verified, extracted and started.

Highlights, all with transcripts in the lab folder:

- The CLI turns out to have **no create command** — verified
  live. The lab vault was therefore created with the
  project's own released libraries (the same cryptolib and
  cryptofs code the apps ship), then unlocked, filled and
  read by the official CLI itself. The detour is documented
  in full, failures included; on your own machine you will
  create vaults in the desktop app like a normal person.
- Files were added and read back through the CLI's WebDAV
  mode, and came back **byte-for-byte identical** (sha256
  checked).
- `grep` over the whole vault tree found **no trace** of
  the cleartext filenames, the folder name, a marker
  sentence or ordinary words from the files.
- A copied vault unlocked independently, one flipped byte
  was rejected with an authentication failure while its
  neighbour read back perfectly, and a password change was
  proven both ways — old password refused, new one accepted.

## What the lab could not do, said plainly

- **Mounting.** A vault normally appears as a mounted drive
  through FUSE. This sandbox has no `/dev/fuse` device at
  all, so the mount itself is documented-only — the FUSE
  attempt's exact failure is in the transcripts. File access
  was proven through the CLI's supported WebDAV mode
  instead, on this guide's assigned loopback port 18091.
- **The desktop app's windows.** The AppImage starts and
  logs, then stops because the sandbox has no display
  (`Unable to open DISPLAY`). Creating a vault by clicking
  through the desktop app is described, not shown.
- **The recovery key flow** and other desktop-only extras
  are described from the project's documentation, not run.

None of that touches the encryption evidence: the vault
format, the on-disk layout and the cryptography were all
exercised directly.

## Chapters

1. [What Cryptomator Is](what-cryptomator-is.md) — the
   problem it solves and the shape of the answer.
2. [The Moving Parts](the-moving-parts.md) — the vault
   folder, the masterkey file, the signed config token, the
   `d/` tree and per-file encryption.
3. [Installing Cryptomator](installing-cryptomator.md) —
   desktop apps, the official CLI, and verifying downloads.
4. [Creating a Vault](creating-a-vault.md) — the desktop
   way, and the lab's library detour, honestly labelled.
5. [Unlocking and Mounting](unlocking-and-mounting.md) —
   the three doors into a vault, and which one this lab
   could open.
6. [The WebDAV Door](the-webdav-door.md) — adding, listing
   and reading files without a mount.
7. [Proving the Encryption](proving-the-encryption.md) —
   the grep evidence, the ciphertext sizes, and what an
   observer can still see.
8. [Passwords and Recovery](passwords-and-recovery.md) —
   wrong passwords, changing the password, the recovery
   key, and the lost-password truth.
9. [A Vault Is Just a Folder](a-vault-is-just-a-folder.md)
   — syncing, sharing and backing up the ciphertext.
10. [Recipes](recipes.md) — tested combinations.
11. [The Neighbours](the-neighbours.md) — VeraCrypt, LUKS,
    age, gocryptfs, rclone crypt and the rest.
12. [Limits](limits.md) — what Cryptomator will not do.
13. [Troubleshooting](troubleshooting.md) — the real
    errors from the lab, decoded.
14. [Glossary](glossary.md)

## What it doesn't cover

- **Cryptomator Hub and team features** — the commercial
  hosted key management; the lab used standalone vaults.
- **The mobile apps** beyond noting they exist and open the
  same vault format.
- **Windows and macOS specifics** beyond the download
  notes; the lab ran on Linux.
- **A security audit.** This guide demonstrates the design
  working as documented; it does not re-verify the
  cryptography. (The project's libraries have had
  independent audits; see the project's own pages.)

## How to use this guide

Terms are defined in the [Glossary](glossary.md) the first
time they matter. Full command transcripts from the lab
live in the guide's lab folder (`cryptomator-lab/`), named
`t00` onward in the order the chapters cite them, so any
claim here can be checked against what the machine
actually printed.

## Related guides in this series

- **[VeraCrypt for Mere Mortals](https://paulsullivan33-dev.github.io/veracrypt-for-mere-mortals/)**
  — one encrypted container that mounts as a whole drive;
  the other classic answer, with a different shape.
- **[age for Mere Mortals](https://paulsullivan33-dev.github.io/age-for-mere-mortals/)**
  — encrypting individual files for transfer, with keys
  instead of a live folder.
- **[rclone for Mere Mortals](https://paulsullivan33-dev.github.io/rclone-for-mere-mortals/)**
  — the sync tool people pair with vaults, and its own
  built-in encryption layer, `crypt`.

---

Part of **[For Mere Mortals](https://paulsullivan33-dev.github.io/mere-mortals/)**
— practical guides, no hype, no filler.
